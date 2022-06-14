package com.senko.tabexecutorplugin.executor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 命令提示执行器
 * @author senko
 * @date 2022/6/14 14:15
 */
public class CommandTipExecutor implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return false;
    }

    /**
     * 玩家在聊天框中的输入行为会被服务器监听到，
     * 输入框中的光标每发生一次位置变动，就会触发一次指令补全提示。
     *
     * 指令是一种树形结构，每个指令都可以有子指令，
     * 暴力的if else语句逐个查询会非常破坏代码的可读性、可维护性，增加耦合度，
     * 因此建议采用合理的设计模式或数据结构来解决代码膨胀的问题。
     *
     *
     * /info
     *    - add [player] [apple | orange | banana]]
     *    - locate [x | y | z]
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        LinkedList<String> tips = new LinkedList<>();
        //处理第一个参数
        if (args.length == 1) {
            //如果只输入一个空格时
            List<String> firstArgList = Arrays.asList("add", "locate");
            if (args[0].isEmpty()) {
                //添加所有信息
                tips.addAll(firstArgList);
                return tips;
            } else {
                //已经开始输入字符了，则遍历已有信息，并将 信息的小写toLowerCase()通过startWith()检查该arg[0]的小写是否匹配
                for (String firstArg : firstArgList) {
                    if (firstArg.toLowerCase().startsWith(args[0].toLowerCase())) {
                        tips.add(firstArg);
                    }
                }
                return tips;
            }
        }
        //处理第二个参数
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add".toLowerCase())) {
                //如果第一个参数是add，则添加玩家名称
                if (args[1].isEmpty()) {
                    //正打算输入第二个参数时，显示所有玩家
                    Bukkit.getOnlinePlayers().forEach(player -> tips.add(player.getName()));
                    return tips;
                } else {
                    //将匹配的玩家名称添加到tips中
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                            tips.add(player.getName());
                        }
                    });
                    return tips;
                }
            }
            if (args[0].equalsIgnoreCase("locate".toLowerCase())) {
                //如果第一个参数是locate，则添加x y z
                List<String> secondArgList = Arrays.asList("x", "y", "z");
                if (args[1].isEmpty()) {
                    //正打算输入第二个参数时，显示所有x y z
                    tips.addAll(secondArgList);
                    return tips;
                } else {
                    //将匹配的x y z添加到tips中
                   secondArgList.forEach(secondArg -> {
                       if (secondArg.toLowerCase().startsWith(args[1].toLowerCase())) {
                           tips.add(secondArg);
                       }
                   });
                    return tips;
                }
            }
        }
        //处理第三个参数
        if (args.length == 3) {
            //这里当然也要分别检查 args[0] args[1]
            if (args[0].equalsIgnoreCase("add") && !args[1].isEmpty()) {
                //如果第一个参数是add，并且玩家名不为空
                List<String> thirdArgList = Arrays.asList("apple", "orange", "banana");
                if (args[2].isEmpty()) {
                    //正打算输入第三个参数时，显示所有水果
                    tips.addAll(thirdArgList);
                    return tips;
                } else {
                    //将匹配的水果添加到tips中
                    thirdArgList.forEach(thirdArg -> {
                        if (thirdArg.toLowerCase().startsWith(args[2].toLowerCase())) {
                            tips.add(thirdArg);
                        }
                    });
                    return tips;
                }
            }
        }
        return tips;
    }
}

