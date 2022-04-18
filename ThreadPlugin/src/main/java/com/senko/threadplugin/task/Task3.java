package com.senko.threadplugin.task;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Task3 extends BukkitRunnable {
    private CommandSender sender;

    public Task3(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        sender.sendMessage("这是3s后的消息，当前时间戳：" + System.currentTimeMillis());
    }
}
