package com.senko.scoreboardplugin;

import com.senko.scoreboardplugin.executor.SimpleScoreboardExecutor;
import com.senko.scoreboardplugin.listener.OnPlayerQuitHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreBoardPlugin extends JavaPlugin {
    private static ScoreBoardPlugin instance;

    public static ScoreBoardPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getCommand("simple-scoreboard").setExecutor(new SimpleScoreboardExecutor());
        Bukkit.getPluginManager().registerEvents(new OnPlayerQuitHandler(), this);
    }

    @Override
    public void onDisable() {
        //停止掉当前插件所生成的任务
        Bukkit.getScheduler().cancelTasks(this);
    }


}
