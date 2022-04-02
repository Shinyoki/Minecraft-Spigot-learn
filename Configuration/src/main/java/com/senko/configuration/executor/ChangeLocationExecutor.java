package com.senko.configuration.executor;

import com.senko.configuration.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ChangeLocationExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("change")) {
            if (args.length == 1) {
                if (sender instanceof Player) {
                    Player player = Bukkit.getPlayerExact(args[0]);
                    if (player != null) {
                        Location curLocation = player.getLocation();
                        FileConfiguration config = Configuration.getPlugin().getConfig();
                        config.set(player.getName()+".location",curLocation);
                        Configuration.getPlugin().saveConfig();
                        sender.sendMessage("标记成功！");
                    }
                    return true;
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("tp")) {
                if (sender instanceof Player) {
                    FileConfiguration config = Configuration.getPlugin().getConfig();
                    Location location = config.getLocation(args[0] + ".location");
                    if (location == null) {
                        sender.sendMessage("未找到标记地点！请检查Target名是否正确或重新标记！");
                        return false;
                    }
                    Player player = Bukkit.getPlayerExact(args[0]);
                    if (player != null) {
                        player.teleport(location);
                        return true;
                    }else {
                        return false;
                    }
                }
            }
        }
        return false;
    }
}
