package com.senko.command;

import com.senko.command.commands.MyCommand2;
import org.bukkit.plugin.java.JavaPlugin;

public final class Command extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getCommand("suicide").setExecutor(new MyCommand2());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
