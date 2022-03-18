package com.senko.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
@Deprecated
public class MyCommand {}
/*
public class MyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (command.getName().equals("shoot")) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.BLACK+"===== Label：" + label + " =====");
                StringBuilder str = new StringBuilder();
                for (String arg : args) {
                    str.append(arg + "，");
                }
                player.sendMessage("Args: " + args.length + str);
                player.sendMessage("Command Info: " + command.getName());
            }
        }else {
            sender.sendMessage("只有玩家才能使用该指令,"+sender.getName()+"你看看你自己是什么先~");
        }
        return false;
    }
}
*/