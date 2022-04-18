package com.senko.threadplugin.task;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.atomic.AtomicInteger;

public class Task4 extends BukkitRunnable {
    private AtomicInteger integer = new AtomicInteger(0);
    private CommandSender sender;

    public Task4(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        sender.sendMessage("这是第" + integer.getAndIncrement() + "次执行，当前时间戳：" + System.currentTimeMillis() + "是不是与上次发送间隔20ticks？(20ticks == 1s == 1000 Timestamp)");
    }
}
