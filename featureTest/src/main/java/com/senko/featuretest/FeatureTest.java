package com.senko.featuretest;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class FeatureTest extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("testL").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /**
         * 对非克隆得到的Location副本进行修改，会影响到原Location对象的值
         */
        if (label.equalsIgnoreCase("testl") && args.length == 1 && args[0].equalsIgnoreCase("copy") && sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();

            //非Clone
            Location copy1 = location;
            copy1.add(0, 2, 0);
            player.teleport(location);
            return true;
        } else if (label.equalsIgnoreCase("testl") && args.length == 1 && args[0].equalsIgnoreCase("clone") && sender instanceof Player) {
            Player player = (Player) sender;
            Location location = player.getLocation();

            //clone
            Location clone1 = location.clone();
            player.teleport(location);
            return true;
        }
        return false;
    }
}
