package com.senko.textcomponentplugin;

import com.senko.textcomponentplugin.executor.*;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class TextComponentPlugin extends JavaPlugin {
    //插件单例
    private static TextComponentPlugin instance;

    public static TextComponentPlugin getInstance() {
        return instance;
    }

    /**
     * 文本样式：                                                                                    <br>
     * （一）：基于ChatColor实现的纯文本。<br>
     * ChatColor有两类，一类是Bukkit的 {@link ChatColor}，常用于拼接字符串常量和替换字符串中的字符为颜色代码§。<br>
     * 另一类是{@link net.md_5.bungee.api.ChatColor}，用作{@link BaseComponent}组件的属性，用于表示组件信息的样式。                       <br>
     * （二）：另一种是基于Bungeecord的{@link BaseComponent} ，他需要{@link net.md_5.bungee.api.ChatColor}等对象作为属性，用于确定当前组件的文本样式，需要注意的是{@link BaseComponent}并不是简单的String字符串，
     * 而是货真价实的类，最后要通过某种方法将他当做文本反馈给玩家。如player#sendMessage、player.spigot()#sendMessaege()                           <br><br>
     * 像 [可点击的文本ChatText，玩家列表Tablist...] 这些特殊的文本，都能在 {@link BaseComponent} 的子类里找到相应的类。 <br>
     * 这里推荐一个可以查看颜色代码的网站：<a href="https://htmlcolorcodes.com/zh/minecraft-yanse-daima/">点击此处</a>            <br>
     * 颜色代码wiki页面： <a href="https://minecraft.fandom.com/zh/wiki/%E6%A0%BC%E5%BC%8F%E5%8C%96%E4%BB%A3%E7%A0%81">点击此处</a><br>
     *<br><br>
     *
     * 小技巧：<br>
     * Bukkit的ChatColor提供了 {@link ChatColor#translateAlternateColorCodes(char, String)}函数，可以将String字符串中的特殊字符char替换为颜色代码§，<br>
     * 比如：<br>
     * <code>
     *     ChatColor.translateAlternateColorCodes('&', "&l&6你好啊&e我的朋友");
     * </code>
     * <br>
     * 结果是："§l§6你好啊§e我的朋友" 只有这种带有§字符的文本才能正确显示文本样式         <br><br>
     *
     * Bungeecord-ChatColor有个类函数{@link net.md_5.bungee.api.ChatColor#of(String)}，可以将HEX CODE利用起来，从而实现更加多样化的文本色彩样式。
     */
    @Override
    public void onEnable() {
        //注册指令  /help TextComponentPlugin
        getCommand("print").setExecutor(new PrintCommand());
        getCommand("print-message").setExecutor(new PrintMessage());                //展示ChatColor的两种用法
        getCommand("print-title").setExecutor(new PrintTitle());                    //向玩家的 屏幕中心 展示标题
        getCommand("print-text-component").setExecutor(new PrintTextComponent());   //向玩家的 聊天框、物品栏上方 展示信息
        getCommand("print-boss-bar").setExecutor(new PrintBossBar());
        getCommand("print-key-bind").setExecutor(new PrintKeyBind());
        instance = this;
    }


}
