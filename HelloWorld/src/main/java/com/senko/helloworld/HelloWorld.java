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
        System.out.println(ChatColor.GREEN + "==========HelloWorld2============");
        System.out.println(ChatColor.GREEN + "HelloWorld2");
        getLogger().info(ChatColor.GREEN + "HelloWorld2");
    }

}
