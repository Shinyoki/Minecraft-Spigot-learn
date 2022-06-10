package com.senko.textcomponentplugin.executor;

import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.Objects;

/**
 * ChatColor的基础用法
 * <p>
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

            if (Objects.nonNull(args) && args.length > 0) {
                String arg = args[0];
                switch (arg) {
                    case "1":
                        fun1(player);
                        break;
                    case "2":
                        fun2(player);
                        break;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private void fun1(Player player) {
        //用法一：ChatColor拼接
        String message1 = ChatColor.GOLD + "你好 " + ChatColor.BOLD + ChatColor.RED + "世界";                        //转义后实际得到的文本    §6你好 §c世界
        //等同于
        String message11 = ChatColor.GOLD.toString() + "你好 " + ChatColor.BOLD.toString() + ChatColor.RED.toString() + "世界";
        player.sendMessage(message1);

        //用法二：ChatColor.translateAlternateColorCodes(字符,字符串)转换颜色代码
        String message2 = ChatColor.translateAlternateColorCodes('&', "&6你好 &l&c世界");   //转义后实际得到的文本    §6你好 §c世界
        player.sendMessage(message2);
    }

    /**
     * 完全自定义的色彩 <br><br>
     *
     * {@link ChatColor}枚举所提供的色彩存在一定的限制<br>
     * 如果需要自定义色彩，我们可以通过{@link net.md_5.bungee.api.ChatColor#of(String)}传入十六进制颜色码来细分色彩
     */
    private void fun2(Player player) {
        net.md_5.bungee.api.ChatColor color1 = net.md_5.bungee.api.ChatColor.of("#FCF3CF"); //十六进制的颜色码
        net.md_5.bungee.api.ChatColor color2 = net.md_5.bungee.api.ChatColor.of("#F9E79F"); //十六进制的颜色码
        net.md_5.bungee.api.ChatColor color3 = net.md_5.bungee.api.ChatColor.of("#F7DC6F"); //...
        net.md_5.bungee.api.ChatColor color4 = net.md_5.bungee.api.ChatColor.of("#F4D03F");
        net.md_5.bungee.api.ChatColor color5 = net.md_5.bungee.api.ChatColor.of("#F1C40F");
        net.md_5.bungee.api.ChatColor color6 = net.md_5.bungee.api.ChatColor.of("#D4AC0D");

        //发送 纯文本            这里也是用了toString()特性
        String pureText = MessageFormat.format("{0}大{1}家{2}好{3}啊{4}欢{5}迎", color1, color2, color3, color4, color5, color6);
        player.sendMessage(pureText + ChatColor.translateAlternateColorCodes('&', "       &f sent in pure text form!"));

    }
}
