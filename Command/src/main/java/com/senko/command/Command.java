package com.senko.command;

import com.senko.command.commands.executors.MyCommand2;
import com.senko.command.commands.handler.CommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Command extends JavaPlugin {

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
