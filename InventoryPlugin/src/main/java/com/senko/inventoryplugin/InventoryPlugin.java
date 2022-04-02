package com.senko.inventoryplugin;

import com.senko.inventoryplugin.executor.RemoteInventoryCommandExecutor;
import com.senko.inventoryplugin.listener.CantGetFromChestListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoryPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("open").setExecutor(new RemoteInventoryCommandExecutor());
        Bukkit.getServer().getPluginManager().registerEvents(new CantGetFromChestListener(),this);
    }
}
