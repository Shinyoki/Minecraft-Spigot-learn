package com.senko.tabexecutorplugin.commands.impl;


import com.senko.tabexecutorplugin.commands.ICommand;
import com.senko.tabexecutorplugin.executor.CommandHandler;
import org.bukkit.command.CommandSender;

public class HelpCmd extends ICommand {

    public HelpCmd() {
        super("help","","获取帮助信息");
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        StringBuilder str = new StringBuilder();
        str.append("------- TestCommand Plugin Command List -------\n");

        /**
         * 从父类的属性 ComandHandler 中获取到所维护的commands列表，并根据权限筛选掉不符合的指令。
         */
        for (ICommand cmd : CommandHandler.getInstance().getCommands().values()) {
            if (sender.hasPermission(cmd.permission())) {
                //StringBuild使用append来实现字符串拼接可以提升效率
                str.append("/sub ").append(cmd.showUsage()).append('\n');
            }
        }

        sender.sendMessage(str.toString());
        return true;
    }

    @Override
    public String permission() {
        return "senko.help";
    }
}