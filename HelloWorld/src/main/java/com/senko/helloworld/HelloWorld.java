package com.senko.helloworld;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class HelloWorld extends JavaPlugin {

    @Override
    public void onEnable() {

        System.out.println("Hello World Console");

        Logger logger = this.getLogger();
        logger.info("Hello World Logger");
        logger.warning("这是一条警告");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
