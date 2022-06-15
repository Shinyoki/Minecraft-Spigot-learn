package com.senko.scoreboardplugin.strategy.impl;

import com.senko.scoreboardplugin.utils.ScoreboardUtil;
import com.senko.scoreboardplugin.strategy.AbstractCreationStrategy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * 简单计分板生成策略
 * @author senko
 * @date 2022/6/15 13:14
 */
public class SimpleScoreboardCreationStrategyImpl implements AbstractCreationStrategy {

    /**
     *  服务器本身可以通过调用 /scoreboard 指令来创建一个计分板，这种计分板由指令创建，可以被所有的玩家所看到，具有全局Global效果，又可以叫做全局计分板。<br>
     *  玩家在进入服务器时，会默认使用全局计分板，无论他上一次登录时是否使用自定义计分板，只要重新登陆，就会使用全局计分板。因此全局计分板很适合用在大厅服务器里，用于展示通用数据信息<br>
     * {@link Scoreboard} 和 {@link org.bukkit.event.Listener} 事件监听器差不多，需要通过XXXManager用于统一管理，只不过这里管理的分别是获取全局计分板和创建自定义的coreboard。<br><br>
     * {@link ScoreboardManager#getNewScoreboard()}并不是简单的获取，每次调用都会创建出新的计分板。<br><br>
     * 注册完计分项后一定要记得{@link Objective#setDisplaySlot(DisplaySlot)} 指定显示位置，不然该计分项的内容不会被显示。<br><br>
     */
    @Override
    public void setScoreboard(Player player) {

        //获取 计分板管理器并创建新的计分板
        ScoreboardManager manager = Bukkit.getScoreboardManager();                //获取计分板管理器
        Scoreboard playerScoreboard = player.getScoreboard();                     //获取玩家的计分板
        //玩家默认使用的就是全局计分板 则替换成新的计分板
        if (playerScoreboard.equals(manager.getMainScoreboard())) {              //如果玩家正在用全局计分板，则创建一个新的计分板
            playerScoreboard = manager.getNewScoreboard();                       //想要尽量避免计分板发生 闪烁 现象，就要尽可能的少创建，多复用，当前计分板的生成策略是会造成闪烁的
        }

        //设置 侧边计分项
        Objective sideBarObjective = playerScoreboard.getObjective("side-bar");
        if (Objects.nonNull(sideBarObjective)) {
            /**
             * 在这里，我们将把信息存放到Entry中。
             * 如果信息entry发生变化，就会导致getScore(entry)后会生成一个新的条目，于是要重新注册该计分项。
             */
            sideBarObjective.unregister();                  //注销掉原有的计分项
        }
        /**
         * 参数一：计分项唯一标识（每个计分项都有自己的名字，且相互独立）
         * 参数二：计分准则，当为dummy时意味着条目分数由开发者自己决定，当为health等准则时，则条目项所显示的分数由服务器自己设定 https://minecraft.fandom.com/zh/wiki/%E8%AE%B0%E5%88%86%E6%9D%BF?variant=zh
         * 参数三：计分项展示用名称
         */
        //注销后重新注册
        sideBarObjective = playerScoreboard.registerNewObjective("side-bar", "dummy", ChatColor.GOLD +  "玩家信息");
        sideBarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);

        /**
         * getScore(entry)会先找有没有这个Entry条目，如果没有，则会创建一个新的条目，并返回。
         *
         * 多个条目的显示顺序并不是由代码的创建顺序决定，而是由该条目的分数所决定，谁大最先显示谁。
         *
         * entry也是唯一值，不能重复。
         * 如果你想尝试让定时器重复执行sidebarObjective.getScore("当前时间" + 动态值)来更新计分板，对不起，旧的不被删，新的一直刷出来，
         * 计分板很快就会被挤爆了，因为每次getScore所接收的entry值不一样，他当然会一直新建。
         * 所以此时可以尝试通过sidebarObjective#unregister()来先删掉当前的计分项，然后再重新scoreboard.registerNewObjective()注册创建。
         * 但是计分板和计分项的创建过程都很消耗系统资源，不建议这样做。
         *
         * 计分项 getScore(entry)里的entry就是指Team队伍中的Entry玩家，如果你getScore的Entry正好是某个在线玩家的名子，
         * 又在Team中将这个Entry设置了前后缀，那么这种后缀就会同步影响到同一计分板中所有计分项相同entry的显示效果，就不单单是只显示玩家名了。
         *
         * 我们实际上并不会依靠entry条目的setScore()来设置我们想要显示的数值，一般是
         * 利用Team的setPrefix()和setSuffix()会影响其他计分项中相同Entry显示效果的特性来展示动态信息。
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
        Objective nameBelowObjective = playerScoreboard.getObjective("name-below");
        if (Objects.isNull(nameBelowObjective)) {
            //未找到：初始化
            nameBelowObjective = playerScoreboard.registerNewObjective("name-below", Criterias.HEALTH, ChatColor.GREEN + "玩家血量");
            nameBelowObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);      //不要忘记设置显示位置
            nameBelowObjective.setRenderType(RenderType.HEARTS);
        }
        //赋值 health准则时设置的entry都是无效的
        nameBelowObjective.getScore("这个Entry就算是设置了也不会显示，因为health准则时显示的该计分项的displayName").setScore(233);

        //设置 玩家列表计分项
        Objective tabListObjective = playerScoreboard.getObjective("tab-list");
        if (Objects.isNull(tabListObjective)) {
            //未找到：初始化
            tabListObjective = playerScoreboard.registerNewObjective("tab-list","dummy", ChatColor.GREEN + "玩家击杀数");
            tabListObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);      //不要忘记设置显示位置
        }
        //赋值 将玩家列表，又或者说是服务器上的所有玩家都设置分数，entry就是玩家名
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            tabListObjective.getScore(onlinePlayer.getName()).setScore(onlinePlayer.getStatistic(Statistic.PLAYER_KILLS)); //设置显示的数值是 玩家的击杀数
        }

        //创建 红Team
        Team redTeam = playerScoreboard.getTeam("红队");
        if (Objects.isNull(redTeam)) {
            //未找到：初始化
            redTeam = playerScoreboard.registerNewTeam("红队");
        }
        //赋值
        redTeam.setColor(ChatColor.RED);                                    //设置队伍颜色
        redTeam.setPrefix(ChatColor.RED + "[红队]");                         //设置队伍前缀 同时作用于所有的DisplaySlot，因此利用这个特性栏实现无闪计分板
        redTeam.addEntry("iLiveOnFaith");                                   //添加iLiveOnFaith到队伍
        redTeam.addEntry("Senkosan");                                       //添加Senkosan到队伍
        ScoreboardUtil.changeTeamOptions(redTeam);                                         //设置队伍的属性
        //创建 蓝Team
        Team blueTeam = playerScoreboard.getTeam("蓝队");
        if (Objects.isNull(blueTeam)) {
            //未找到：初始化
            blueTeam = playerScoreboard.registerNewTeam("蓝队");
        }
        blueTeam.setColor(ChatColor.BLUE);
        blueTeam.setPrefix(ChatColor.BLUE + "[蓝队]");
        /**
         * 多个队伍可以同时存在相同的Entry
         */
        blueTeam.addEntry("iLiveOnFaith");
        ScoreboardUtil.changeTeamOptions(blueTeam);

        //将设置好的计分板添加给玩家
        player.setScoreboard(playerScoreboard);

    }

}
