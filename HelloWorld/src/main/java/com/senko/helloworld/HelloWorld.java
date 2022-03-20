package com.senko.helloworld;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class HelloWorld extends JavaPlugin {

    @Override
    public void onEnable() {

        System.out.println("Hello World Console");

        Logger logger = this.getLogger();
        logger.info("Hello World Logger Info");
        logger.warning("Hello World Logger Warning");
        logger.log(Level.ALL,"Hello World Logger All");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
