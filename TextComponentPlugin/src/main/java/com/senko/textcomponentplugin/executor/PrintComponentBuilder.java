package com.senko.textcomponentplugin.executor;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * @author senko
 * @date 2022/6/10 10:45
 */
public class PrintComponentBuilder implements CommandExecutor {
    ChatColor color1 = ChatColor.of("#FCF3CF"); //十六进制的颜色码
    ChatColor color2 = ChatColor.of("#F9E79F"); //十六进制的颜色码
    ChatColor color3 = ChatColor.of("#F7DC6F"); //...
    ChatColor color4 = ChatColor.of("#F4D03F");
    ChatColor color5 = ChatColor.of("#F1C40F");
    ChatColor color6 = ChatColor.of("#D4AC0D");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            executeFun(player);

            return true;
        }

        return false;
    }

    private void executeFun(Player player) {
        BaseComponent[] components1 = new ComponentBuilder()
                .append("大").color(color1).bold(true)
                .append("家").color(color2).italic(true)
                .append("好").color(color3).obfuscated(true)
                .append("啊").color(color4).underlined(true)
                .create();

        //为什么需要单独再构建出一个components2？   是为了防止继承上一个组件的属性，下面的components3同理
        BaseComponent[] components2 = new ComponentBuilder()
                .append("欢").color(color5)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "虽然是发送指令，可实际上只是说了句话罢了"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("这个字可以点哦")))
                .create();

        BaseComponent[] components3 = new ComponentBuilder()
                .append("迎").color(color6)      //不单独构造，则会继承上一个组件的event事件等属性
                .create();

        TextComponent[] result = new TextComponent[]{new TextComponent(components1), new TextComponent(components2), new TextComponent(components3)};
        //发送 组件
        player.spigot().sendMessage(result);
    }

}
