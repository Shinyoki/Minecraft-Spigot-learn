package com.senko.textcomponentplugin;

import com.senko.textcomponentplugin.executor.PrintCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class TextComponentPlugin extends JavaPlugin {
    //插件单例
    private static TextComponentPlugin instance;

    public static TextComponentPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        //注册指令  /print
        getCommand("print").setExecutor(new PrintCommand());
        instance = this;
    }


}
