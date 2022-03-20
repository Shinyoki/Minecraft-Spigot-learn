package com.senko.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public abstract class ICommand {

    //private CommandHandler handler;

    private String cmdName;
    private String params;
    private String info;

    public ICommand(String cmdName) {
        this.cmdName = cmdName;
    }

    public ICommand(String cmdName, String params,String usage) {
        this.cmdName = cmdName;
        this.info = usage;
        this.params = params;
    }

    public String getCmdName() {
        return cmdName;
    }

    /*
    public void setHandler(CommandHandler handler) {
        this.handler = handler;
    }
     */

    public String showUsage() {
        return ChatColor.AQUA + getCmdName() + " §r" + cmdName + " "+ params + " -- " + info;
    }
    /**
     * 指令内容
     * @param sender
     * @param args
     * @return
     */
    public abstract boolean onCommand(CommandSender sender, String[] args);

    /**
     * 指令权限
     * @return
     */
    public abstract String permission();
}