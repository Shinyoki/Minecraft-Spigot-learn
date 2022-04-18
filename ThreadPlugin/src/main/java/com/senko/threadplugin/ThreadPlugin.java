package com.senko.threadplugin;


import com.senko.threadplugin.executor.ThreadCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;


public final class ThreadPlugin extends JavaPlugin {
    private static ThreadPlugin instance;

    public static ThreadPlugin getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;

        getCommand("stuck").setExecutor(new ThreadCommandExecutor());


    }

}


