package com.senko.eventlistener;

import com.senko.eventlistener.listener.MyEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class EventListener extends JavaPlugin {

    @Override
    public void onEnable() {
        // 需要在Server的插件管理器中注册事件监听器。
        this.getServer().getPluginManager().registerEvents(new MyEventHandler(),this);

        //Bukkit.getPluginManager().registerEvents(new MyEventHandler(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
