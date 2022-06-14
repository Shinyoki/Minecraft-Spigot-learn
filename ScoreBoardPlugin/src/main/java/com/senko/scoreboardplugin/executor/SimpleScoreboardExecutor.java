package com.senko.scoreboardplugin.executor;

import com.senko.scoreboardplugin.ScoreBoardPlugin;
import com.senko.scoreboardplugin.scoreboard.NoFlickingScoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 简单的创建计分板
 *
 * 必须顺着某个过程执行，否则会出bug2333，毕竟越简单约好理解。
 * @author senko
 * @date 2022/6/14 8:15
 */
public class SimpleScoreboardExecutor implements TabExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 1) {
            if (sender instanceof Player && args[0].equalsIgnoreCase("simple")) {
                //      /simple-scoreboard simple
                createScoreboard((Player) sender);                                                      //创建简单计分板
            } else if (sender instanceof Player && args[0].equalsIgnoreCase("reset")) {
                //      /simple-scoreboard reset
                ((Player) sender).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());       //重置计分板
            }  else if (sender instanceof Player && args[0].equalsIgnoreCase("get")) {
                ScoreBoardPlugin.getInstance().getLogger().info("玩家计分板：" + ((Player) sender).getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()));
            } else if (args[0].equalsIgnoreCase("start")) {
                if (NoFlickingScoreboard.isEmpty()) {
                    sender.sendMessage("没有玩家需要显示计分板");
                    return true;
                }
                //     /simple-scoreboard start
                NoFlickingScoreboard.runTaskTimer();         //重复执行任务
            } else if (args[0].equalsIgnoreCase("stop")) {
                //     /simple-scoreboard stop
                NoFlickingScoreboard.stopTaskTimer();        //停止重复执行
            } else if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 2 && NoFlickingScoreboard.validatePlayer(args[1])) {
                    //  /simple-scoreboard add <player>
                    boolean flag = NoFlickingScoreboard.addUpdatePlayer(Bukkit.getPlayer(args[1]));    //添加一个玩家到需要更新的玩家列表
                    if (!flag) {
                        sender.sendMessage("添加玩家失败，请检查玩家是否存在且在线！");
                        return false;
                    }
                    sender.sendMessage("添加玩家 " + args[1] + " 到需要更新的玩家列表。当前维护的玩家列表：" + NoFlickingScoreboard.getUpdatePlayerSet().toString());
                } else
                    return false;
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 2 && NoFlickingScoreboard.validatePlayer(args[1])) {
                    //   /normal-scoreboard remove <player>
                    NoFlickingScoreboard.removeUpdatePlayer(Bukkit.getPlayer(args[1]));   //从需要更新的玩家列表中移除一个玩家
                } else
                    return false;
            } else {
                return false;
            }

            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            }
            return true;
        }
        return false;
    }


    /**
     *  服务器本身可以通过调用 /scoreboard 指令来创建一个计分板，这种计分板由指令创建，可以被所有的玩家所看到，具有全局Global效果，又可以叫做MainScoreBoard。<br> <br>
     * {@link Scoreboard} 和 {@link org.bukkit.event.Listener} 事件监听器差不多，需要通过XXXManager用于统一管理，只不过这里管理的分别是MainScoreBoard和插件自定义的Scoreboard。<br><br>
     * {@link ScoreboardManager#getNewScoreboard()}并不是简单的获取，其中还存在new创建的过程，每次调用都会创建出新的计分板。<br><br>
     * 注册完计分项后一定要记得{@link Objective#setDisplaySlot(DisplaySlot)} 指定显示位置，不然该计分项的内容不会被显示。<br><br>
     */
    private void createScoreboard(Player player) {

        //获取 计分板管理器并创建新的计分板
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = player.getScoreboard();
        //玩家默认使用的就是全局计分板
        if (player.getScoreboard().equals(manager.getMainScoreboard())) {         //如果玩家正在用全局计分板，则创建一个新的计分板
            scoreboard = manager.getNewScoreboard();                                                            //想要尽量避免计分板发生 闪烁 现象，就要尽可能的少创建，多复用
        }

        //设置 侧边计分项
        /**
         * 参数一：计分项唯一标识（每个计分项都有自己的名字，且相互独立）
         * 参数二：计分准则，当为dummy时意味着条目分数由开发者自己决定，当为health等准则时，则条目项所显示的分数由服务器自己设定 https://minecraft.fandom.com/zh/wiki/%E8%AE%B0%E5%88%86%E6%9D%BF?variant=zh
         * 参数三：计分项展示用名称
         */
        Objective sideBarObjective = scoreboard.getObjective("side-bar");
        if (Objects.isNull(sideBarObjective)) {
            //同样的道理，尽量少创建，多复用
            sideBarObjective = scoreboard.registerNewObjective("side-bar", "dummy", ChatColor.GOLD + "玩家信息");
            /**
             * 计分项的显示位置有三个：
             * {@link DisplaySlot#SIDEBAR}：侧边计分项
             * {@link DisplaySlot#PLAYER_LIST}：玩家列表
             * {@link DisplaySlot#BELOW_NAME}：玩家名下方
             */
            sideBarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);               //必要的一步，否则该计分项将不会被显示，展示位置没有默认值
        }

        /**
         * getScore(entry)会先找有没有这个Entry条目，如果没有，则会创建一个新的条目，并返回。
         *
         * 多个条目的显示顺序并不是由代码的创建顺序决定，而是由该条目的分数所决定，谁大最先显示谁。
         *
         * entry也是唯一值，不能重复。
         * 如果你想尝试让定时器重复执行sidebarObjective.getScore("当前时间" + 动态值)来更新计分板，对不起，旧的不被删，新的一直刷出来，
         * 计分板很快就会被挤爆了，因为每次getScore所接收的entry值不一样，他当然会一直新建。
         * 所以此时可以尝试通过sidebarObjective#unregister()来先删掉当前的计分项，然后再重新scoreboard.registerNewObjective()注册创建。
         * 但是计分板和计分项的创建过程都很消耗系统资源，所以不建议这样做。
         *
         * 这里的entry就是指Team队伍中的Entry玩家，如果你getScore的Entry正好是某个在线玩家的名子，
         * 又在Team中将这个Entry设置了前后缀，那么计分板中的该条目也会跟着发生变动，就不单单是只显示玩家名了。
         *
         * 我们实际上并不会依靠条目的setScore()来设置我们想要显示的数值，一般是
         * 利用Team的setPrefix()和setSuffix()会影响其他计分项中相同Entry显示效果的特性来展示动态内容。
         * 这也是解决计分板不会闪烁的一个方法。
         */
        Score score = sideBarObjective.getScore(ChatColor.GREEN + "玩家名称：" + ChatColor.WHITE + player.getName());                   //4的分数最大，这个entry会优先显示
        score.setScore(4);                                                                                                                  //这样写很麻烦，下面的写法就很简洁规整
        sideBarObjective.getScore(ChatColor.GOLD + "玩家权限：" + ChatColor.WHITE + (player.isOp() ? "OP" : "玩家")).setScore(3);
        sideBarObjective.getScore(ChatColor.YELLOW + "玩家等级：" + ChatColor.WHITE + player.getLevel()).setScore(2);
        /**
         * 统计信息，详情请看https://minecraft.fandom.com/zh/wiki/%E7%BB%9F%E8%AE%A1%E4%BF%A1%E6%81%AF?variant=zh-cn
         * 以及{@link Statistic}
         */
        sideBarObjective.getScore(ChatColor.RED + "玩家死亡次数：" + ChatColor.WHITE + player.getStatistic(Statistic.DEATHS)).setScore(1);
        sideBarObjective.getScore(ChatColor.AQUA + "玩家生存时间：" + ChatColor.WHITE + player.getStatistic(Statistic.TIME_SINCE_DEATH)).setScore(0);
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        sideBarObjective.getScore(ChatColor.GREEN + "当前时间" + ChatColor.WHITE + now).setScore(-1);
        sideBarObjective.getScore(player.getName()).setScore(233);

        /**
         * 这里第二个参数，也就是准则为 health （用的是普通类的静态常量，不是枚举值），此时getScore顶多可以修改entry条目怎么显示，但是不能修改entry的分数。
         * 因为health这个准则是不可 写 的。
         * 具体请看：https://minecraft.fandom.com/zh/wiki/%E8%AE%B0%E5%88%86%E6%9D%BF?variant=zh-cn
         */
        //设置 玩家名下的计分项
        Objective nameBelowObjective = scoreboard.getObjective("name-below");
        if (Objects.isNull(nameBelowObjective)) {
            nameBelowObjective = scoreboard.registerNewObjective("name-below", Criterias.HEALTH, ChatColor.GREEN + "玩家血量");
            nameBelowObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);      //不要忘记设置显示位置
        }
        nameBelowObjective.getScore("这个Entry就算是设置了也不会显示，因为health准则时显示的该计分项的displayName");

        //设置 玩家列表计分项
        Objective tabListObjective = scoreboard.getObjective("tab-list");
        if (Objects.isNull(tabListObjective)) {
            tabListObjective = scoreboard.registerNewObjective("tab-list","dummy", ChatColor.GREEN + "玩家血量");
            tabListObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);      //不要忘记设置显示位置
        }
        tabListObjective.getScore(player.getName()).setScore(666);          //让当前玩家的Tablist分数为666

        //创建 红Team
        Team redTeam = scoreboard.getTeam("红队");
        if (Objects.isNull(redTeam)) {
            redTeam = scoreboard.registerNewTeam("红队");
        }
        redTeam.setColor(ChatColor.RED);                                    //设置队伍颜色
        redTeam.setPrefix(ChatColor.RED + "[红队]");                         //设置队伍前缀 同时作用于所有的DisplaySlot，因此利用这个特性栏实现无闪计分板
        redTeam.addEntry("iLiveOnFaith");                                   //添加iLiveOnFaith到队伍
        redTeam.addEntry("Senkosan");                                       //添加Senkosan到队伍
        NoFlickingScoreboard.changeTeamOptions(redTeam);                                         //设置队伍的属性
        //创建 蓝Team
        Team blueTeam = scoreboard.getTeam("蓝队");
        if (Objects.isNull(blueTeam)) {
            blueTeam = scoreboard.registerNewTeam("蓝队");
        }
        blueTeam.setColor(ChatColor.BLUE);
        blueTeam.setPrefix(ChatColor.BLUE + "[蓝队]");
        /**
         * 多个队伍可以同时存在相同的Entry
         */
        blueTeam.addEntry("iLiveOnFaith");
        NoFlickingScoreboard.changeTeamOptions(blueTeam);

        //将设置好的计分板添加给玩家
        player.setScoreboard(scoreboard);
    }


    /**
     * 指令输入提示
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new LinkedList<>();
        String[] firstCommands = {"simple", "reset", "clear", "start", "stop", "add", "remove"};

        if (args.length == 1) {
            //输入第一个指令时
            if (args[0].isEmpty()) {
                //输入空格，准备写第一个参数时
                result.addAll(Arrays.asList(firstCommands));        //默认显示
                return result;
            }

            for (String string : firstCommands) {
                if (string.startsWith(args[0].toLowerCase())) {
                    result.add(string);
                }
            }
            return result;
        } else if (args.length == 2) {
            //输入第二个指令时
            if (args[0].toLowerCase().equals("add")) {
                //遍历所有玩家
                if (args[1].isEmpty()) {
                    //输入空格，准备写第二个参数时
                    for (Player player : Bukkit.getOnlinePlayers()) {   //默认显示
                        result.add(player.getName());
                    }
                    return result;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        result.add(player.getName());
                    }
                }
                return result;
            } else if (args[0].toLowerCase().equals("remove")) {
                //遍历所有玩家
                if (args[1].isEmpty()) {
                    //输入空格，准备写第二个参数时
                    for (Player player : Bukkit.getOnlinePlayers()) {   //默认显示
                        result.add(player.getName());
                    }
                    return result;
                }
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        result.add(player.getName());
                    }
                }
                return result;
            }
            return result;
        }
        return result;
    }
}
