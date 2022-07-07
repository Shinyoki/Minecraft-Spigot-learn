package com.senko.conversationplugin;

import com.senko.conversationplugin.conversation.MyQueryFactory;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class ConversationPlugin extends JavaPlugin {

    private static ConversationPlugin plugin;

    public static ConversationPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        getCommand("start").setExecutor(this);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {

            Player me = (Player) sender;
            MyQueryFactory.build().start(me);
            return true;
        }
        sender.sendMessage("只有玩家才嫩使用该指令！");
        return true;
    }

}
