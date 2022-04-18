package com.senko.threadplugin.executor;

import com.senko.threadplugin.ThreadPlugin;
import com.senko.threadplugin.task.Task2;
import com.senko.threadplugin.task.Task3;
import com.senko.threadplugin.task.Task4;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public class ThreadCommandExecutor implements CommandExecutor {
    private ThreadPlugin plugin;

    /**
     * 在Spigot插件里调用 同步/异步 线程有两种方式： <br>
     * 方式一：继承 {@link BukkitRunnable}抽象类，实现 {@link Runnable#run()}函数，然后直接调用抽象类的 {@link BukkitRunnable#runTask(Plugin)}等函数。 <br>
     * 方式二：通过 {@link Bukkit#getScheduler()} 得到 {@link org.bukkit.scheduler.BukkitScheduler}线程调度器，然后用 {@link org.bukkit.scheduler.BukkitScheduler#runTask(Plugin, Runnable)}等函数来
     * 执行 {@link Runnable}任务。<br><br>
     *
     * 补充：Spigot Api并没有提供线程调度器的实现类，所以我们无法在这里
     * 查看实现类的源码。但是据我在网上的搜索得知，该线程调度器本质上就是一种 ThreadPool，当然它没有直接实现或继承JDK的ExecutorService。
     * BukkitScheduler内部维护了 一个线程池 {@link java.util.concurrent.Executor} ，我们所传入的 Runnable 对象最终都会被线程池维护管理，因此无需手动的使用<code>new Thread().start();</code>创建新的线程，导致过于昂贵的资源开销。
     * 因此Bukkit也是十分建议开发者，先创建 {@link BukkitRunnable}的子类，然后调用 {@link BukkitRunnable#runTask(Plugin)}等函数来执行线程任务。
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {


        if (label.equalsIgnoreCase("stuck")) {
            if (args != null && args.length > 0 ) {
                plugin = ThreadPlugin.getInstance();
                sender.sendMessage("\n案例：" + args[0]);
                String arg = args[0];
                switch (arg) {
                        //BukkitRunnable
                    case "1":
                        /** =========== BukkitRunnable runTask 同步阻塞 线程调用 ========================== */
                        executeFun1(sender);
                        break;
                    case "2":
                        /** =========== BukkitRunnable runTaskAsynchronously 异步 执行 ===================*/
                        executeFun2(sender);
                        break;
                    case "3":
                        /** =========== BukkitRunnable runTaskLaterAsynchronously tisks时间后异步执行 =====*/
                        executeFun3(sender);
                        break;
                    case "4":
                        /** =========== BukkitRunnabe runTaskTimerAsynchronously 重复异步执行 =============*/
                        executeFun4(sender);
                        break;

                        //BukkitScheduler
                    case "5":
                        /** ==========  BukkitScheduler 线程调度器  =======================================*/
                        executeFun5(sender);
                        break;
                }
            }
            return true;
        }
        return false;
    }


    //**************************BukkitRunnable**************************************//
    /**
     *  Sleep 3s，<br>
     *  同步执行，会阻塞掉主线程，此时服务器不会响应玩家的任何操作，因为主线程被阻塞掉了。 <br><br>
     *
     *  使用异步执行可以给玩家带来更良好的游戏体验，但是异步执行也意味着更大的风险，比如共享资源的获取与修改问题。<br>
     *  因此Spigot规定不要在异步执行的任务里调用Bukkit的API(hummm，为什么不是Spigot API？因为Spigot Api底层就是在调用Bukkit Api)，除非开发者能够使用锁确保线程安全。<br>
     */
    private void executeFun1(CommandSender sender) {
        BukkitRunnable task1 = new BukkitRunnable() {
            @Override
            public void run() {
                sender.sendMessage("接下来主线程会卡死3s，你在服务器里的任何动作都不会得到响应。");
                try {
                    //卡死线程3s ，期间你放置生物等行为都无法得到响应，因为在这3s里，主线程被阻塞了
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    sender.sendMessage("现在主线程恢复运转！");
                }
            }
        };
        /**
         * 参数一：当前插件
         */
        task1.runTask(plugin);
    }

    /**
     * sleep 3s
     * 异步执行，虽然依旧是3s，但是BukkitScheduler会异步的调用该runnable任务,此时玩家仍然能得到服务器的响应。
     */
    private void executeFun2(CommandSender sender) {
        //自己创建的BukkitRunnable子类
        Task2 task2 = new Task2(sender);
        /**
         * 参数一：当前插件
         *
         * 返回值：BukkitTask
         */
        task2.runTaskAsynchronously(plugin);  //异步运行任务
    }

    /**
     * Run Task Later
     * 3s后 异步 地执行run()函数
     */
    private void executeFun3(CommandSender sender) {
        sender.sendMessage("当前时刻：" + System.currentTimeMillis() + "，你将会在3s后收到下一条提示。");
        Task3 task3 = new Task3(sender);
        /**
         * 参数一：当前插件
         * 参数二：几ticks后执行
         *
         * 返回值：BukkitTask
         */
        task3.runTaskLaterAsynchronously(plugin, 3 * 20);//稍后异步运行任务， 3 * 20 ticks
    }

    /**
     * 重复执行
     */
    private void executeFun4(CommandSender sender) {
        Task4 task4 = new Task4(sender);
        /**
         * 参数一：当前插件
         * 参数二：几ticks后执行
         * 参数三：几ticks后循环执行
         *
         * 返回值：BukkitTask
         */
        BukkitTask bukkitTask = task4.runTaskTimerAsynchronously(plugin, 0, 20);

        //这里必须用到BukkitTask返回值了，如果不主动改变isCancelled状态，该runnable会被一直重复调用下去
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            bukkitTask.cancel();
        }, 4 * 20);
    }

    //**************************BukkitScheduler**************************************//
    /**
     * 观察 {@link BukkitScheduler} 的函数会发现,他可以接收处理 jdk原生的{@link Runnable}以及 {@link Runnable}的子类 {@link BukkitRunnable}，<br>
     * 但是参数是{@link BukkitRunnable}类型的基本都被 {@link Deprecated} 弃用了。<br>
     * 这是因为{@link BukkitRunnable}的函数执行过程 就是1.先把自己转型为Runnable 2.调用Scheduler传入plugin以及转型后的runnable。<br>
     * 所以说BukkitRunnable和Runnable基本算是同一个东西，最后Scheduler处理的也不是BukkitRunnable。那么直接往Scheduler里传入Runnable就好了，再让Scheduler调用BukkitRunnable不就是套娃吗？
     */
    private void executeFun5(CommandSender sender) {
        /**
         * 本示例同效于 示例3
         */
        sender.sendMessage("当前时刻：" + System.currentTimeMillis() + "，你将会在3s后收到下一条提示。");
        //线程调度器
        BukkitScheduler scheduler = Bukkit.getScheduler();
        
        scheduler.runTaskLaterAsynchronously(plugin, new Runnable() {
            @Override
            public void run() {
                sender.sendMessage("这是3s后的消息，当前时间戳：" + System.currentTimeMillis());
            }
        },  3 * 20);
    }

}
