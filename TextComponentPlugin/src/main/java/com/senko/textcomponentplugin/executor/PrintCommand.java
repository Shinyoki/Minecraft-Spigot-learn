package com.senko.textcomponentplugin.executor;


import com.senko.textcomponentplugin.TextComponentPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;


/**
 * 打印文本
 */

public class PrintCommand implements CommandExecutor {


    /**
     * 文本样式：                                                                                    <br>
     * （一）：基于ChatColor实现的纯文本。<br>
     * ChatColor有两类，一类是Bukkit的 {@link ChatColor}，常用于拼接字符串常量和替换字符串中的字符为颜色代码§。<br>
     * 另一类是{@link net.md_5.bungee.api.ChatColor}，用作{@link BaseComponent}组件的属性，用于表示组件信息的样式。                       <br>
     * （二）：另一种是基于Bungeecord的{@link BaseComponent} ，他需要{@link net.md_5.bungee.api.ChatColor}等对象作为属性，用于确定当前组件的文本样式，需要注意的是{@link BaseComponent}并不是简单的String字符串，
     * 而是货真价实的类，最后要通过某种方法将他当做文本反馈给玩家。如player#sendMessage、player.spigot()#sendMessaege()                           <br><br>
     * 像 [可点击的文本ChatText，玩家列表Tablist...] 这些特殊的文本，都能在 {@link BaseComponent} 的子类里找到相应的类。 <br>
     * 这里推荐一个可以查看颜色代码的网站：<a href="https://htmlcolorcodes.com/zh/minecraft-yanse-daima/">点击此处</a>            <br>
     * 颜色代码wiki页面： <a href="https://minecraft.fandom.com/zh/wiki/%E6%A0%BC%E5%BC%8F%E5%8C%96%E4%BB%A3%E7%A0%81">点击此处</a><br>
     *<br><br>
     *
     * 小技巧：<br>
     * Bukkit的ChatColor提供了 {@link ChatColor#translateAlternateColorCodes(char, String)}函数，可以将String字符串中的特殊字符char替换为颜色代码§，<br>
     * 比如：<br>
     * <code>
     *     ChatColor.translateAlternateColorCodes('&', "&l&6你好啊&e我的朋友");
     * </code>
     * <br>
     * 结果是："§l§6你好啊§e我的朋友" 只有这种带有§字符的文本才能正确显示文本样式         <br><br>
     *
     * Bungeecord-ChatColor有个类函数{@link net.md_5.bungee.api.ChatColor#of(String)}，可以将HEX CODE利用起来，从而实现更加多样化的文本色彩样式。
     * @param sender    指令发送者
     * @param command   指令对象
     * @param label     指令名
     * @param args      指令附带参数
     * @return          执行正确与否
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("print") && sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                return false;
            } else {
                String arg = args[0];
                player.sendMessage("\n案例" + arg + "：\n");
                switch (arg) {

                    case "1":
                        /** ===============案例1 Bukkit ChatColor ====================== */
                        //详情情况函数注解
                        executeFun1(player);
                        break;

                    case "2":
                        /** ===============案例2 ChatColor的小特性======================== */
                        executeFun2(player);
                        break;

                    case "3":
                        /** ===============案例3 Bungeecord ComponentBuilder ============ */
                        executeFun3(player);
                        break;

                    case "4":
                        /** ===============案例4 Bungeecord TextComponent ===============*/
                        executeFun4(player);
                        break;

                    case "5":
                        /** ===============案例5 点击文本组件所发送的指令 ====================*/
                        String res = ChatColor.translateAlternateColorCodes('&', "&l&4被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！被发现了！");
                        player.sendMessage(res);
                        break;

                    case "6":
                        /** ===============案例6 Bungeecord KeybindComponent =============*/
                        executeFun6(player);
                        break;


                    case "7":
                        /** ==============案例7 Bungeecord ScoreComponent ================*/
                        player.sendMessage(ChatColor.RED + "本次视频暂不讲解，留给后续篇章！");
                        break;

                    case "8":
                        /** ==============案例8 Player SendTitle =========================*/
                        executeFun8(player);
                        break;

                    case "9":
                        /** ==============案例9 Boss Bar ================================*/
                        executeFun9(player);
                        break;
                    case "10":
                        /** ==============案例10 Boss Bar ================================*/
                        executeFun10(player);
                        break;
                    default:
                        return true;
                }
            }
            return true;
        }
        return false;
    }


    /**
     * 单纯的向玩家发送转译好的彩色字符串<br><br>
     * <p>
     * {@link Player#sendMessage }所发送的 彩色字符串，他们的样式并不连续。<br>
     * "&2&l你好&a我的朋友"。<br>
     * 2对应绿色，a对应红色，l对应粗体。给“你好”设置粗体，当后面有新的颜色代码时，“我的朋友”就不会保持前者的粗体状态<br><br>
     * （我不会打§这个字符，所以。。。。有关彩色字符串的就全部用ChatColor#translateAlternateColorCodes()函数转译了）
     *
     * @param player
     * @return 指令执行正确与否（正确）
     */
    private boolean executeFun1(Player player) {
        String message = ChatColor.translateAlternateColorCodes('&', "&2&l你好&a我的朋友");

        TextComponentPlugin.getInstance().getLogger().info("翻译后的文本：" + message);
        player.sendMessage(message);
        return true;
    }


    /**
     * 虽然这里的文本与fun1案例非常相似，但是使用后你会发现<br>
     * 先有颜色再有粗体的字符串，并不能正确的显示粗体。<br>
     * 所以正确的做法是"先输出字体再输出色彩"
     */
    private void executeFun2(Player player) {
        String message = "大家好！" + ChatColor.RED + "这是红色！";
        player.sendMessage(message);

        String res = ChatColor.translateAlternateColorCodes('&', "&l&2你好&a我的朋友");
        player.sendMessage(res);
    }

    /**
     * {@link ComponentBuilder}组件构造器，内部维护了BaseComponent集合，并使用cursor作为下标来确定 #color() / #bold() / ...等函数所操作的 {@link BaseComponent} 对象 <br><br>
     * 每次设置颜色#color()、字体#font()、粗体#bold()、删除线等文本样式，需要先 #append()再使用那些函数，因为这些样式函数都是属于BaseComponent对象自己而非Builder。
     */
    private void executeFun3(Player player) {
        ComponentBuilder builder = new ComponentBuilder();
        /**
         * 组件构造器的使用比较特殊，要先append再指定文本样式
         */
        BaseComponent[] components = builder
                //链式编程
                .append("你好").color(net.md_5.bungee.api.ChatColor.GREEN).bold(true)  //component  cursor == 0
                .append("我的朋友").color(net.md_5.bungee.api.ChatColor.YELLOW)         //component  cursor == 1
                //构建 组件数组
                .create();
        /**
         * 向玩家输出组件信息
         * 需要用到player.spigot()#sendMessage(输出位置，组件)
         */
        Player.Spigot spigot = player.spigot();
        player.sendMessage("得到的Player.Spigot:" + spigot.getClass().getName());

        spigot.sendMessage(components);
    }

    /**
     * 不用组件构造器，直接new出BaseComponent抽象类的实现类。 <br><br>
     * <p>
     * 向玩家 {@link Player}等 {@link CommandSender}发送特殊组件
     */
    private void executeFun4(Player player) {
        TextComponent text = new TextComponent("你好");
        text.setBold(true);
        text.setColor(net.md_5.bungee.api.ChatColor.GREEN);

        //obfuscated混淆的文本，你无法看到“你好”二字，只能看到正在不停变化的看不懂的文字。
        text.setObfuscated(true);

        /**
         * 添加点击和悬停事件
         * 这些事件是自己new出来后自动触发的，不需要Bukkit.getPluginManager().callEvent()手动触发监听
         *
         * ClickEvent.Action.RUN_COMMAND：点击事件 发送指令，实际上还是发送文本，不信你把斜杠/去掉试试
         */
        //Click点击事件 Hover悬浮鼠标事件
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/print 5"));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("爱丽丝在哪里？")));

        //文本组件2
        TextComponent text2 = new TextComponent("我的朋友");
        //text.addExtra(text2); //不推荐
        /**
         * 以多参的形式发送，如果只是text1.addExtra(text2)，玩家得到的整段文字都是可点击的
         */
        player.spigot().sendMessage(text, text2);


        /**
         * 改正：以后的控制台打印尽量使用Logger日志输出，这样可以持久化输出过的信息，而不是打印后就找不到了。
         */
        TextComponentPlugin.getInstance().getLogger().info("plugin发送了一条消息");
    }

    /**
     * 玩家绑定的按键                      <br>
     * Spigot可以获取到玩家的 攻击、丢弃物品等快捷键所绑定的案件是什么。<br>
     * 按键key的类型可以参考 <a href="https://minecraft.fandom.com/zh/wiki/%E6%8E%A7%E5%88%B6">MC wiki</a>   以及{@link Keybinds}  <br>
     * 服务器可以根据客户端的 ”语言和绑定的按键” 来决定应该输出哪种语言的按键名称。
     *
     * @param player
     * @return
     */
    private void executeFun6(Player player) {

        KeybindComponent key1 = new KeybindComponent();
        //攻击，默认左键---------- 英文：Left Button 中文：左键
        key1.setKeybind("key.attack");
        key1.addExtra("：攻击键");

        KeybindComponent key2 = new KeybindComponent();
        //丢弃物品，默认Q键------ 我自定义的是R键  英文：r  中文：r
        key2.setKeybind("key.drop");
        key2.addExtra("：丢弃物品");

        //玩家列表，默认Tab键---- 我定义的是`键    英文：   中文：
        KeybindComponent key3 = new KeybindComponent("key.playerlist");
        key3.addExtra(":玩家列表");


        //聊天框位置显示
        player.spigot().sendMessage(key1);
        //行动条位置？总之是经验条上面的地方
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, key2);
        //居然还是聊天框位置显示
        player.spigot().sendMessage(ChatMessageType.SYSTEM, key3);
    }

    /**
     * 发送title和subTitle
     * 这是Spigot提供的功能，用的当然就是Spigot的ChatColor <br>
     * 不能传入 {@link BaseComponent}组件 :/
     *
     * @param player 玩家<br>
     *               实参：      <br>
     *               title – 标题<br>
     *               subtitle – 副标题<br>
     *               fadeIn – 渐变出现 所花费的时间(单位tick)，默认10<br>
     *               stay – 标题停留在屏幕上的时间(单位tick)，默认70<br>
     *               fadeOut – 渐变消失 所花费的时间(单位tick)，默认20<br>
     */
    private void executeFun8(Player player) {
        player.sendTitle(ChatColor.GREEN + "你好", ChatColor.YELLOW + "我的朋友", 10, 70, 20);
        //player.resetTitle();  //清理掉屏幕中正在显示的信息
    }

    /**
     * BossBar 也就是打末影龙、凋零时出现的boss血条<br>
     * 创建好的bossBar默认不会提示给所有玩家，需要使用 {@link BossBar#addPlayer(Player)}来指定谁会收到提示。<br>
     * 同理，不想让玩家看到bossBar就要用到 {@link BossBar#removePlayer(Player)} 或 {@link BossBar#removeAll()} <br><br>
     * 如果只是单纯的将进度条设置为0，玩家依旧能看到进度条。<br>
     * <code>
     * bossBar.setProgress(0d);  // 仍会显示
     * </code>
     *
     * @param player
     */
    private void executeFun9(Player player) {
        //通过Bukkit创建BossBar实例，指定bossBar的名称、颜色、会被分成多少片段 以及 BarFlag
        BossBar bossBar = Bukkit.createBossBar("Senkosan", BarColor.YELLOW, BarStyle.SEGMENTED_6, BarFlag.CREATE_FOG);
        //用bossBar对象添加玩家，只有该玩家才能看见boss进度条
        bossBar.addPlayer(player);

        //拿到插件实例
        TextComponentPlugin plugin = TextComponentPlugin.getInstance();
        //拿到Schedule线程调度器
        BukkitScheduler scheduler = Bukkit.getScheduler();
        //虽然在这个案例里不会发生共享资源争夺的情况，但还是建议尽量使用Atomic类型的元素
        AtomicInteger atomicInteger = new AtomicInteger(6);
        //执行异步循环任务，拿到taskId
        int taskId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                player.sendMessage("当前的boss bar进度：" + atomicInteger.get() * (1d / 6d));
                //修改进度条进度，实参是double类型
                bossBar.setProgress(atomicInteger.decrementAndGet() * (1d / 6d));
            }
        }, 0, 2 * 20); //为什么连Thread Task都要以tick为单位啊喂
        //根据taskId取消 repeatAsyncTask的重复执行
        scheduler.runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                //移除玩家，不让玩家继续看到进度条
                bossBar.removePlayer(player);
                //取消 repeatAsyncTask的重复执行
                scheduler.cancelTask(taskId);
            }
        }, 6 * 2 * 20);

    }

    //show text component on bottom of the player screen
    private void executeFun10(Player player) {

        TextComponentPlugin plugin = TextComponentPlugin.getInstance();
        BukkitScheduler scheduler = Bukkit.getScheduler();
        AtomicInteger atomicInteger = new AtomicInteger(6);
        int taskId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                player.sendTitle("倒计时 " + atomicInteger.getAndDecrement() + " 秒","", 10, 70, 20);

                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder()
                        .append("当前所在位置：").color(net.md_5.bungee.api.ChatColor.RED)
                        .append(player.getLocation().getBlockX()+" ").color(net.md_5.bungee.api.ChatColor.GOLD)
                        .append(player.getLocation().getBlockY()+" ").color(net.md_5.bungee.api.ChatColor.YELLOW)
                        .append(player.getLocation().getBlockZ()+" ").color(net.md_5.bungee.api.ChatColor.GREEN)
                        .append("  ").color(net.md_5.bungee.api.ChatColor.BLUE)
                        .append("延迟："+player.getPing()).color(net.md_5.bungee.api.ChatColor.DARK_PURPLE)
                        .append("...").color(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE)
                        .create());
            }
        }, 0, 20);
        scheduler.runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                scheduler.cancelTask(taskId);
                //清空正在显示的信息
                player.resetTitle();
            }
        }, 6 * 20);
    }

}



