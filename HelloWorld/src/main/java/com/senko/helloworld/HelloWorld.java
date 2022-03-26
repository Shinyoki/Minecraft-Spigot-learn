package com.senko.helloworld;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class HelloWorld extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("==========HelloWorld============");
        System.out.println("HelloWorld");
        getLogger().info(ChatColor.GREEN + "HelloWorld");

        FileConfiguration config = getConfig();

        Object o = config.get("senko.age");

        config.set("senko.age",21);

        try {
            config.save(new File(getDataFolder(), "config.yml"));
            reloadConfig();
            System.out.println(getConfig().get("senko.age"));
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
