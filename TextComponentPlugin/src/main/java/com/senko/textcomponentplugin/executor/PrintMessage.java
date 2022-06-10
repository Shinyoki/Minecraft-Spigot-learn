package com.senko.textcomponentplugin.executor;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * ChatColor的基础用法
 *
 * 与字符串常量拼接， 转换某个字符为颜色代码§
 *
 * @author senko
 * @date 2022/6/10 7:21
 */
public class PrintMessage implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //用法一：ChatColor拼接
            String message1 = ChatColor.GOLD + "你好 " + ChatColor.BOLD + ChatColor.RED + "世界";
            //等同于
            String message11 = ChatColor.GOLD.toString() + "你好 " + ChatColor.BOLD.toString() + ChatColor.RED.toString() + "世界";
            player.sendMessage(message1);

            //用法二：ChatColor.translateAlternateColorCodes(字符,字符串)转换颜色代码
            String message2 = ChatColor.translateAlternateColorCodes('&', "&6你好 &l&c世界");
            //转义后实际得到的文本    §6你好 §c世界
            player.sendMessage(message2);

            return true;
        }
        return false;
    }
}
