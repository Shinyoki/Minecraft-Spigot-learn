package com.senko.configuration.listener;

import com.senko.configuration.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginEventListener implements Listener {
    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = Configuration.getPlugin().getConfig();

        Location location = config.getLocation(player.getName() + ".location");
        if (location != null) {
            Bukkit.getServer().broadcastMessage("找到了");
            player.teleport(location);
        }else {
            Bukkit.getServer().broadcastMessage("没找到");
        }
    }
}
