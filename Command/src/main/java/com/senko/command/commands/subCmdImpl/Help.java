package com.senko.command.commands.subCmdImpl;

import com.senko.command.commands.ICommand;
import com.senko.command.commands.handler.CommandHandler;
import org.bukkit.command.CommandSender;

public class Help extends ICommand {

    public Help() {
        super("help","","获取帮助信息");
    }

    @Override
    public boolean onCommand(CommandSender sender, java.lang.String[] args) {
        StringBuilder str = new StringBuilder();
        str.append("------- TestCommand Plugin Command List -------\n");

        /**
         * 从父类的属性 ComandHandler 中获取到所维护的commands列表，并根据权限筛选掉不符合的指令。
         */
        for (ICommand cmd : CommandHandler.getInstance().getCommands().values()) {
            if (sender.hasPermission(cmd.permission())) {
                //StringBuild使用append来实现字符串拼接可以提升效率
                str.append("/tc ").append(cmd.showUsage()).append('\n');
            }
        }

        sender.sendMessage(str.toString());
        return true;
    }

    @Override
    public String permission() {
        return "tc.help";
    }
}