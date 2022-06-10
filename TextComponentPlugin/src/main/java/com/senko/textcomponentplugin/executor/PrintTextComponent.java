package com.senko.textcomponentplugin.executor;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * @author senko
 * @date 2022/6/10 7:36
 */
public class PrintTextComponent implements CommandExecutor {

    /**
     * {@link TextComponent} 是 {@link BaseComponent}抽象类的一个子类，在继承信息组件所必有的 "文本"、"字体色彩"、"字体样式"<br>
     * 的同时，TextComponent还具备了 鼠标的点击以及悬浮事件。</br>
     * 最终信息组件需要使用 {@link Player#spigot()} 来发送，这是发送组件与发送字符串常量的区别所在。
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //步骤一：创建信息组件，一个用于显示在聊天框，另一个显示在物品栏上方
            TextComponent chatMessage = getTextComponent();
            BaseComponent[] actionBarMessage = getComponents(player);

            //步骤二：获取玩家的Spigot对象
            Player.Spigot spigot = player.spigot();

            //步骤三：通过Spigot对象将信息组件发送给玩家 [可选位置：聊天框、物品栏上方、系统级提示]
            //spigot.sendMessage(textComponent);                            //默认的发送位置为ChatMessageType.CHAT，也就是在聊天框里显示
            spigot.sendMessage(ChatMessageType.CHAT, chatMessage);        //还可以将位置改为ChatMessageType.ACTION_BAR，此时就能够显示在经验条、血量条上方的区域
            spigot.sendMessage(ChatMessageType.ACTION_BAR, actionBarMessage);  //还可以将位置改为ChatMessageType.SYSTEM，实际的效果与ChatMessageType.CHAT一样，区别在于这是系统级别的消息

            return true;
        }
        return false;
    }

    private TextComponent getTextComponent() {
        TextComponent text1 = new TextComponent("你好");
        text1.setColor(net.md_5.bungee.api.ChatColor.RED);              //设置颜色
        text1.setBold(true);                                            //设置粗体
        //text1.setItalic(true);                                        //设置斜体
        //text1.setObfuscated(true);                                    //设置模糊（效果是不停变换的乱码）
        //...

        //添加点击事件       其实本质上是让玩家说了一句话，不信你可以把这里"/help TextComponentPlugin"中的斜杠去掉
        text1.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help TextComponentPlugin"));
        //添加鼠标悬浮事件
        text1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("这是一个鼠标悬浮提示")));

        //拼接下一段信息
        TextComponent text2 = new TextComponent("世界");
        text2.setBold(false);
        text2.setObfuscated(true);
        text2.setColor(net.md_5.bungee.api.ChatColor.GREEN);

        //组件的拼接会自动继承上一个组件的颜色、粗体、斜体、鼠标事件等属性
        text1.addExtra(text2);                                         //拼接下一段信息

        //player.spigot().sendMessage(text1, text2); 不想拼接的话，可以这样写，记得不要用text1.addExtra(text2);
        return text1;
    }

    private BaseComponent[] getComponents(Player player) {
        //由于组件构造器的特殊性，必须.append()在前，然后在指定[颜色、字体、粗体、斜体、模糊]等样式
        return new ComponentBuilder()
                .append("当前所在位置：").color(ChatColor.RED).bold(true)
                .append(player.getLocation().getBlockX() + " ").color(ChatColor.GOLD).bold(false)
                .append(player.getLocation().getBlockY() + " ").color(ChatColor.YELLOW)
                .append(player.getLocation().getBlockZ() + " ").color(ChatColor.GREEN)
                .append("  ").color(ChatColor.BLUE)
                .append("延迟：" + player.getPing()).color(ChatColor.DARK_PURPLE).bold(true)
                .append("...").color(ChatColor.LIGHT_PURPLE).italic(true)
                .create();
    }
}
