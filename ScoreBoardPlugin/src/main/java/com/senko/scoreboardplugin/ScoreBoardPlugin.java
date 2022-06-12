package com.senko.scoreboardplugin;

import com.senko.scoreboardplugin.executor.TeamCommandExecutor;
import com.senko.scoreboardplugin.listener.ScoreBoardListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public final class ScoreBoardPlugin extends JavaPlugin {
    private static ScoreBoardPlugin instance;
    private Scoreboard myScoreboard;

    public Scoreboard getScoreboard() {
        return myScoreboard;
    }

    public static ScoreBoardPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getPluginManager().registerEvents(new ScoreBoardListener(), this);
        getCommand("add-team").setExecutor(new TeamCommandExecutor());
        getCommand("clear-score-board").setExecutor(this);

        //当插件被reload以及启动时触发，将所有当前在线玩家添加到scoreboard
        Bukkit.getOnlinePlayers().forEach(this::createScoreboardOnPluginEnabled);

    }



    /**
     *  服务器本身可以通过调用 /scoreboard 指令来创建一个计分板，这种计分板由指令创建，可以被所有的玩家所看到，具有全局Global效果，又可以叫做MainScoreBoard。<br> <br>
     * {@link Scoreboard} 和 {@link org.bukkit.event.Listener} 事件监听器差不多，需要通过XXXManager用于统一管理，只不过这里管理的分别是MainScoreBoard和插件自定义的Scoreboard。<br><br>
     * {@link ScoreboardManager#getNewScoreboard()}并不是简单的获取，其中还存在new创建的过程，每次调用都会创建出新的计分板。<br><br>
     * 注册完计分项后一定要记得{@link Objective#setDisplaySlot(DisplaySlot)} 指定显示位置，不然该计分项的内容不会被显示。<br>
     */
    public void createScoreboardOnPluginEnabled(Player player) {


        /** 创建计分板的过程很消耗资源，因此建议先获取判空，有就重复调用，无则创建/注册。 */
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();                    //获取计分板管理器
        Scoreboard scoreboard = player.getScoreboard();                                         //获取玩家的计分板
        if (Objects.isNull(scoreboard) || scoreboard.equals(scoreboardManager.getMainScoreboard())) {
            scoreboard = scoreboardManager.getNewScoreboard();                                  //如果玩家没有计分板、或该计分板是全局计分板，则生成新的计分板用于自定义
        }

        //侧边栏计分项
        Objective sideBarObjective = null;
        if (Objects.nonNull(scoreboard.getObjective("playerInfo"))) {
            //存在：则重新注册侧边栏计分项，否则如果getScore("经验" + exp)的entry因exp变动而发生变动，就会新生成一个Score，老的"经验xx"会继续存在
            sideBarObjective = scoreboard.getObjective("playerInfo");
            sideBarObjective.unregister();
        }



        /** 计分项是唯一的，它的name不可与其他计分项重名。*/
        //注册计分项(唯一名：playerInfo，修改准则：dummy也就是值随便写由开发者确定，计分项展示名：§6身份信息)
        sideBarObjective = scoreboard.registerNewObjective("playerInfo", "dummy", ChatColor.GOLD + "身份信息");
        sideBarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);                                     //设置该计分项显示在侧边栏

        /**
         * 计分项的内容是一个Score集合。
         * 这里Score构造函数所传入的参数entry，就是条目，也就每一行所显示的信息。
         * 计分项内的每一行并非由getScore()的代码的执行顺序决定，而是Score对象的分数值决定。
         * 最终会以倒序的形式将计分项内的Score集合依次展示。
         *
         * entry条目的字符长度存在限制，在1.13版本前是最大16个字符，在1.13版本后是最大64个字符。
         */
        //设置侧边栏内容
        Score score1 = sideBarObjective.getScore("年龄: " + ChatColor.BLUE + 21);                //虽然写的是get，但是如果没get到就会创建一个新的Score
        score1.setScore(3);                                                                     //设置分数为3，最大，因此该条目会最展示在侧边栏计分项的最上面
        sideBarObjective.getScore("性别: " + ChatColor.BLUE + "男").setScore(2);
        sideBarObjective.getScore("等级: " + ChatColor.BLUE + player.getLevel()).setScore(1);
        sideBarObjective.getScore("经验: " + ChatColor.BLUE + player.getExp()).setScore(0);

        /**
         * 玩家们计分项
         *
         * 也是个计分项，不过它是放在玩家们下的，且内容（条目)Score只能有一个会显示
         * 此处设置的计分项计分准则为 血量。
         * 因此在该计分项getScore()所生成的条目，其score值就是血量的值，且修改了没用。
         */
        //玩家名计分项
        Objective underPlayerNameObjective = null;
        if (Objects.nonNull(scoreboard.getObjective("serverInfo2"))) {
            underPlayerNameObjective = scoreboard.getObjective("serverInfo2");
        } else {
            underPlayerNameObjective = scoreboard.registerNewObjective("serverInfo2", Criterias.HEALTH, ChatColor.GOLD + "血量");
            underPlayerNameObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);                                 //设置该计分项显示在玩家名下方
        }
        underPlayerNameObjective.getScore(ChatColor.GREEN + "血量");
        underPlayerNameObjective.getScore(ChatColor.RED + "危险！").setScore(10);

        //最终将修改好的计分板赋值给玩家
        player.setScoreboard(scoreboard);

        //更新玩家的计分板
        this.myScoreboard = scoreboard;
    }


    /**
     * 初始化玩家的计分板为默认的全局计分板
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);


            return true;
        }
        return false;
    }
}
