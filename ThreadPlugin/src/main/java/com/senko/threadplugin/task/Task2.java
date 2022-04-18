package com.senko.threadplugin.task;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 请使用异步函数执行
 *
 * sleep 3s
 */
public class Task2 extends BukkitRunnable {
    private CommandSender sender;

    public Task2(CommandSender sender) {
        this.sender = sender;
    }

    @Override
    public void run() {
        sender.sendMessage("虽然这里Thread.sleep了3s，但是该任务被异步执行了，并不会影响主线程，此时你仍然能收到服务器的响应。");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sender.sendMessage("3s已到！");
        }
    }
}
