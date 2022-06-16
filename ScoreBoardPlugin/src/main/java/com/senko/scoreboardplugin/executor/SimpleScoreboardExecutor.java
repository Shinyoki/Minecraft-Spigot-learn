package com.senko.scoreboardplugin.executor;


import com.senko.scoreboardplugin.ScoreBoardPlugin;
import com.senko.scoreboardplugin.strategy.impl.NoFlickeringScoreboardCreationStrategyImpl;
import com.senko.scoreboardplugin.strategy.impl.SimpleScoreboardCreationStrategyImpl;
import com.senko.scoreboardplugin.utils.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
            if (args[0].equalsIgnoreCase("size")) {                                     //查看更新队列大小
                //  /simple-scoreboard size
                sender.sendMessage(ScoreboardUtil.sizeOfUpdatePlayerSet() + " 名玩家仍在更新队列中！");
            } else if (args[0].equalsIgnoreCase("switch")) {                             //更换生成策略
                if (args.length >= 2 && args[1].equalsIgnoreCase("simple")) {
                    //   /simple-scoreboard switch simple
                    ScoreboardUtil.setScoreboardCreationStrategy(new SimpleScoreboardCreationStrategyImpl());       //更换为简单的生成策略
                } else if (args.length >= 2 && args[1].equalsIgnoreCase("no-flickering")) {
                    //   /simple-scoreboard switch no-flickering
                    ScoreboardUtil.setScoreboardCreationStrategy(new NoFlickeringScoreboardCreationStrategyImpl()); //更换为不闪烁计分板的生成策略
                } else {
                    sender.sendMessage(ChatColor.RED + "参数错误，请检查参数！[simple | no-flickering]");
                    return false;
                }
            }else if (sender instanceof Player && args[0].equalsIgnoreCase("reset")) {
                //      /simple-scoreboard reset [all]
                if (args.length >= 2 && args[1].equalsIgnoreCase("all")) {
                    ScoreboardUtil.resetUpdateAll();                                                //重置所有计分板
                } else {
                    ScoreboardUtil.resetUpdatePlayer(sender.getName());                              //重置玩家计分板
                }
            } else if (sender instanceof Player && args[0].equalsIgnoreCase("get")) {
                ScoreBoardPlugin.getInstance().getLogger().info("玩家计分板：" + ((Player) sender).getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard()));
            } else if (args[0].equalsIgnoreCase("start")) {
                if (ScoreboardUtil.isEmpty()) {
                    sender.sendMessage("没有玩家需要显示计分板，请先使用/simple-scoreboard add <玩家名> 添加玩家！");
                    return true;
                }
                //     /simple-scoreboard start
                ScoreboardUtil.runTaskTimer();                                                       //重复执行任务
            } else if (args[0].equalsIgnoreCase("stop")) {
                //     /simple-scoreboard stop
                ScoreboardUtil.stopTaskTimer();                                                      //停止重复执行
            } else if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 2 && ScoreboardUtil.validatePlayer(args[1])) {
                    //  /simple-scoreboard add <player>
                    boolean flag = ScoreboardUtil.addUpdatePlayer(Bukkit.getPlayer(args[1]));         //添加一个玩家到需要更新的玩家列表
                    if (!flag) {
                        sender.sendMessage("添加玩家失败，请检查玩家是否存在且在线！");
                        return false;
                    }
                    sender.sendMessage("添加玩家 " + args[1] + " 到需要更新的玩家列表。当前维护的玩家列表：" + ScoreboardUtil.getUpdatePlayersToString());
                } else {
                    sender.sendMessage("参数错误，请检查参数！[在线玩家名]");
                    return false;
                }
            } else if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 2 && ScoreboardUtil.validatePlayer(args[1])) {
                    //   /normal-scoreboard remove <player>
                    ScoreboardUtil.removeUpdatePlayer(Bukkit.getPlayer(args[1]));                     //从需要更新的玩家列表中移除一个玩家
                    sender.sendMessage("删除玩家 " + args[1] + " 成功。当前维护的玩家列表：" + ScoreboardUtil.getUpdatePlayersToString());
                } else {
                    sender.sendMessage("参数错误，请检查参数！[在线玩家名]");
                    return false;
                }
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
     * 指令输入提示
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new LinkedList<>();
        String[] firstCommands = {"size", "switch", "reset", "start", "stop", "add", "remove"};

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
            } else if (args[0].toLowerCase().equals("reset")) {
                if (args[1].isEmpty()) {
                    //输入空格，准备写第二个参数时
                    result.add("all");
                    return result;
                } else if ("all".startsWith(args[1].toLowerCase())) {
                    result.add("all");
                    return result;
                }
            } else if (args[0].toLowerCase().equals("switch")) {
                List<String> argList = Arrays.asList("simple", "no-flickering");
                if (args[1].isEmpty()) {
                    //输入空格，准备写第二个参数时
                    result.addAll(argList);
                    return result;
                } else {
                    for (String string : argList) {
                        if (string.startsWith(args[1].toLowerCase())) {
                            result.add(string);
                        }
                    }
                    return result;
                }
            }
            return result;
        }
        return result;
    }

}
