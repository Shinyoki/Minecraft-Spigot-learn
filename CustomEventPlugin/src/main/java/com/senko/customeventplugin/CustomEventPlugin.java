package com.senko.customeventplugin;

import com.senko.customeventplugin.executor.GodBowCommandExecutor;
import com.senko.customeventplugin.listener.GodBowEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEventPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("godbow").setExecutor(new GodBowCommandExecutor());
        Bukkit.getPluginManager().registerEvents(new GodBowEventListener(),this);
    }

}
