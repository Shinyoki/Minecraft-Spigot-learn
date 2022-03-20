package com.senko.command.commands.executors;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class MyCommand2 implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("shoot")) {
                Player player = (Player) sender;
                player.sendMessage("自杀神教万岁！（仅限UHC模式）");
                player.setHealth(0);
                player.sendMessage(ChatColor.BLACK+"===== Label：" + label + " =====");
                StringBuilder str = new StringBuilder();
                for (String arg : args) {
                    str.append(arg + "，");
                }
                player.sendMessage("Args: " + args.length + str);
                player.sendMessage("Command Info: " + command.getName());

                World world = player.getWorld();

                return true;
            }
        }else {
            sender.sendMessage("只有玩家才能使用该指令,"+sender.getName()+"你看看你自己是什么先~");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        LinkedList<String> list = new LinkedList<>();
        StringBuilder str = new StringBuilder();
        if (sender instanceof Player) {
            list.add(ChatColor.BLACK+"===== onTabComplete：" + args.length + " =====\n");
            for (String arg : args) {
                str.append(arg + "，");
            }
            list.add("\nArgs: " + args.length + str + "\n");
            list.add("\nCommand Info: " + command.getName() + "\n");
        }else {
            sender.sendMessage("只有玩家才能使用该指令,"+sender.getName()+"你看看你自己是什么先~");
        }
        return list;
    }
}
