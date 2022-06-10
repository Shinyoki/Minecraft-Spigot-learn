package com.senko.textcomponentplugin.executor;

import com.senko.textcomponentplugin.TextComponentPlugin;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 展示Boss血条
 * @author senko
 * @date 2022/6/10 8:42
 */
public class PrintBossBar implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            executeFun(player);

            return true;
        }
        return false;
    }

    /**
     * BossBar 也就是打末影龙、凋零时出现的boss血条<br>
     * 创建好的bossBar默认不会提示给所有玩家，需要使用 {@link BossBar#addPlayer(Player)}来指定谁会收到提示。<br>
     * 同理，不想让玩家看到bossBar就要用到 {@link BossBar#removePlayer(Player)} 或 {@link BossBar#removeAll()} <br><br>
     * 如果只是单纯的将进度条设置为0，玩家依旧能看到进度条。<br>
     * <code>
     * bossBar.setProgress(0d);  // 仍会显示，所以一定要记得手动removePlayer
     * </code>
     */
    private void executeFun(Player player) {
        //通过Bukkit创建BossBar
        BossBar bossBar = Bukkit.createBossBar("这是BOSS条的名字", BarColor.GREEN, BarStyle.SEGMENTED_6, BarFlag.DARKEN_SKY);
        bossBar.addPlayer(player);                   //用bossBar对象添加玩家，只有该玩家才能看见boss进度条

        //创建定时任务
        TextComponentPlugin plugin = TextComponentPlugin.getInstance();             //拿到插件实例
        BukkitScheduler scheduler = Bukkit.getScheduler();                          //拿到Schedule线程调度器
        AtomicInteger atomicInteger = new AtomicInteger(6);

        //执行异步循环任务，拿到taskId
        int taskId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                player.sendMessage("当前的boss bar进度：" + atomicInteger.get() * (1d / 6d));     //发送消息
                bossBar.setProgress(atomicInteger.decrementAndGet() * (1d / 6d));               //修改进度条进度，实参是double类型
            }
        }, 0, 2 * 20);                     //0秒后执行，每2秒执行一次

        //根据taskId取消 repeatAsyncTask的重复执行
        scheduler.runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                bossBar.removePlayer(player);           //移除玩家，不让玩家继续看到进度条
                bossBar.setVisible(false);              //设置进度条不可见
                scheduler.cancelTask(taskId);           //取消任务
            }
        }, 6 * 2 * 20);                           //6秒后执行
    }
}
