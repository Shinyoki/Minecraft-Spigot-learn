package com.senko.scoreboardplugin;

import com.senko.scoreboardplugin.executor.SimpleScoreboardExecutor;
import com.senko.scoreboardplugin.listener.OnPlayerQuitHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class ScoreBoardPlugin extends JavaPlugin {
    private static ScoreBoardPlugin instance;

    /**
     * 获取插件实例
     * @return  插件实例
     */
    public static ScoreBoardPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;                                                                          //初始化插件实例
        getCommand("simple-scoreboard").setExecutor(new SimpleScoreboardExecutor());        //注册命令
    }

    @Override
    public void onDisable() {
        //停止掉当前插件所生成的任务
        Bukkit.getScheduler().cancelTasks(this);
    }

}
