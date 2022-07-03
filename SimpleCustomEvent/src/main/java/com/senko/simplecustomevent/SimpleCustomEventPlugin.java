package com.senko.simplecustomevent;

import com.senko.simplecustomevent.event.listener.caller.PlayerSneakingHitSheepEventCaller;
import com.senko.simplecustomevent.event.listener.OnPlayerSneakingHitSheepEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleCustomEventPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        //关于事件的补充
//        Bukkit.getPluginManager().registerEvents(new TestEventListener(), this);
        //触发自定义事件的监听器
        Bukkit.getPluginManager().registerEvents(new PlayerSneakingHitSheepEventCaller(), this);
        //自定义事件监听器
        Bukkit.getPluginManager().registerEvents(new OnPlayerSneakingHitSheepEventListener(), this);
    }

}
