package com.senko.command.commands.executors;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("god")) {
            //如果是玩家
            if (sender instanceof Player) {
                Player player = (Player) sender;
                //切换玩家的 无伤状态
                player.setInvulnerable(!player.isInvulnerable());
                String tempMessage = (player.isInvulnerable() ? ChatColor.GREEN + "是" : ChatColor.RED + "不是");
                player.sendMessage(ChatColor.WHITE + "现在你就" + tempMessage + ChatColor.WHITE + "上帝模式了。");

                return true;
            }else {
                sender.sendMessage(ChatColor.YELLOW + "只有玩家才能使用该指令！");
                return true;
            }
        }
        return false;
    }
}
