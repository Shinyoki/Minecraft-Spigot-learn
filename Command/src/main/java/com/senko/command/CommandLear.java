package com.senko.command;

import com.senko.command.commands.MyCommand2;
import com.senko.command.commands.handler.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CommandLear extends JavaPlugin {

    @Override
    public void onEnable() {
        /**
         * 根指令绑定 sucide -> MyCommand2.class
         */
        this.getCommand("suicide").setExecutor(new MyCommand2());
        /**
         * tc子指令执行器，在CommandExecutor里维护多个自定义的指令，通过args[index]进行匹配调用。
         */
        this.getCommand("tc").setExecutor(new CommandHandler());
    }

    @Override
    public void onDisable() {

    }

}
