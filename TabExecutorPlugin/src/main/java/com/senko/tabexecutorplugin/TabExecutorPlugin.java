package com.senko.tabexecutorplugin;

import com.senko.tabexecutorplugin.executor.CommandHandler;
import com.senko.tabexecutorplugin.executor.CommandTipExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public final class TabExecutorPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // 视频教学案例
        getCommand("info").setExecutor(new CommandTipExecutor());

        //子指令执行器
        getCommand("sub").setExecutor(new CommandHandler());
    }

}
