package com.senko.command.commands.subCmdImpl;

import com.senko.command.commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodMode extends ICommand {
    public GodMode() {
        super("godMode","","启用上帝模式");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.setInvulnerable(!player.isInvulnerable());
            player.sendMessage("Boom, 现在你" + (player.isInvulnerable() ? ChatColor.BLACK+"是" : ChatColor.YELLOW+"不是") + ChatColor.WHITE + "上帝模式了！");
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW+"只有玩家才能启用该功能");
        return false;
    }

    @Override
    public String permission() {
        return "tc.godMode";
    }
}