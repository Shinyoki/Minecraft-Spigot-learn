package com.senko.scoreboardplugin.utils;

import com.senko.scoreboardplugin.ScoreBoardPlugin;
import com.senko.scoreboardplugin.strategy.AbstractCreationStrategy;
import com.senko.scoreboardplugin.strategy.impl.SimpleScoreboardCreationStrategyImpl;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.util.*;

/**
 * 计分板工具类
 * 
 * 本案例主要用于理解如何实现动态无闪烁的计分板，并不适用于组队系统。
 *
 * 不同计分板内的队伍不一致，也无法直接通过定义相同队伍名来获取相同Team对象，
 * 只能让开发者在更新玩家计分板时先unregister()Team再重新赋值，
 * 同时还要考虑每个玩家计分板内队伍的人员动态分配问题，这种具体的业务代码本教程不做演示。
 * @author senko
 * @date 2022/6/14 9:41
 */
public class ScoreboardUtil {


    /**
     * 重复执行定时任务
     */
    private static BukkitTask task;

    /**
     * 定时任务运行状态
     * 防止重复注册定时任务
     */
    private static boolean isRunning;

    /**
     * 计分板的创建策略
     */
    private static AbstractCreationStrategy creationStrategy;

    /**
     * 需要被更新的玩家
     */
    private static Set<String> playerSet;

    static {
        //初始化静态变量
        creationStrategy = new SimpleScoreboardCreationStrategyImpl();      //默认采用简单的创建策略
        playerSet = new HashSet<>();
    }

    public ScoreboardUtil() {

    }

    public ScoreboardUtil(AbstractCreationStrategy creationStrategy) {
        if (Objects.isNull(creationStrategy)) {
            throw new IllegalArgumentException("creationStrategy is null");
        }
        this.creationStrategy = creationStrategy;
    }

    /**
     * 设置计分板的创建策略
     * @param creationStrategy {@link AbstractCreationStrategy} 计分板创建策略
     */
    public static boolean setScoreboardCreationStrategy(AbstractCreationStrategy creationStrategy) {
        if (Objects.isNull(creationStrategy)) {
            return false;   //创建策略不能为空
        }
        stopTaskTimer();
        resetUpdatePlayerMainScoreboard();                          //重置更新玩家的主计分板
        ScoreboardUtil.creationStrategy = creationStrategy;         //设置计分板的创建策略
        runTaskTimer();
        return true;
    }

    /**
     * 将更新队列中的玩家计分板改为主计分板，
     * 避免在定时任务执行时因切换生成策略导致面板显示不正常
     */
    private static void resetUpdatePlayerMainScoreboard() {
        playerSet.forEach(playerName -> {
            if (validatePlayer(playerName)) {
                Player player = Bukkit.getPlayer(playerName);
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());        //将更新队列中的玩家计分板改为主计分板
            }
        });
    }

    /**
     * 设置玩家的计分板
     * @param player    玩家
     * @return          计分板
     */
    public static boolean setScoreboard(Player player) {
        if (Objects.isNull(player)) {
            return false;
        }
        creationStrategy.setScoreboard(player);
        return true;
    }

    /**
     * 将玩家从需要更新列表中删除
     * @param player    玩家
     */
    public static boolean removeUpdatePlayer(Player player) {
        if (Objects.nonNull(player) && playerSet.contains(player.getName())) {
            playerSet.remove(player.getName());
            return true;
        }
        return false;
    }

    /**
     * 添加一个玩家到需要更新的玩家列表
     * @param player    玩家
     */
    public static boolean addUpdatePlayer(Player player) {
        if (Objects.nonNull(player)) {
            playerSet.add(player.getName());
            return true;
        }
        return false;
    }

    /**
     * 返回更新队列中的玩家数量
     * @return  更新队列中的玩家数量
     */
    public static int sizeOfUpdatePlayerSet() {
        return playerSet.size();
    }

    /**
     * 返回玩家更新队列
     * @return  玩家更新队列
     */
    public static HashSet<String> getUpdatePlayerSet() {
        return (HashSet<String>) playerSet;
    }

    /**
     * 显示更新队列中的玩家
     * @return  更新队列中的玩家
     */
    public static String getUpdatePlayersToString() {
        return ((HashSet<String>) playerSet).toString();
    }

    /**
     * 验证该玩家名是否能匹配到一个真实玩家
     * @param playerName        玩家名
     * @return                  真实玩家
     */
    public static boolean validatePlayer(String playerName) {
        assert  Objects.nonNull(playerName) && !playerName.isEmpty();
        Player player = Bukkit.getPlayer(playerName);
        if (Objects.isNull(player) || !player.isOnline()) {
            //如果匹配不到玩家，或者玩家不在线，则返回false
            return false;
        }
        return true;
    }


    /**
     * 玩家更新队列是否为空
     * @return  是否为空
     */
    public static boolean isEmpty() {
        return playerSet.isEmpty();
    }

    /**
     * 清空队列并、停止定时任务、恢复玩家的计分板为全局计分板
     */
    public static void resetUpdateAll() {
        stopTaskTimer();
        playerSet.forEach(playerName -> {
            if (!validatePlayer(playerName)) {
                return;
            }
            Player player = Bukkit.getPlayer(playerName);
            if (Objects.nonNull(player)) {
                player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard()); //重置计分板
            }
        });
        playerSet.clear();
    }

    /**
     * 将玩家从定时任务中删除  并恢复回全局计分板
     * @param playerName    玩家名
     * @return              是否清空成功
     */
    public static boolean resetUpdatePlayer(String playerName) {
        if (!validatePlayer(playerName)) {
            return false;                       //玩家不合规则，则返回false
        }

        Bukkit.getPlayer(playerName).setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());  //将玩家的计分板重置为主计分板

        if (playerSet.isEmpty()) {
            stopTaskTimer();                    //如果此时玩家名集合为空，则停止定时任务
            return true;
        }
        if (playerSet.contains(playerName)) {
            playerSet.remove(playerName);       //如果玩家名存在，则删除
        }
        return true;
    }

    /**
     * 停止重复执行任务
     */
    public static void stopTaskTimer() {
        if (Objects.nonNull(task)) {
            task.cancel();
            task = null;
        }
        isRunning = false;
    }

    /**
     * 每过1秒就给该玩家重新创建并设置计分板
     */
    public static boolean runTaskTimer() {
        if (isRunning) {
            //正在运行，则退出
            return false;
        }
        if (playerSet.size() == 0) {
            //没有玩家，则退出
            return false;
        }

        //创建重复执行任务，并将返回的任务赋值给task
        task = Bukkit.getScheduler().runTaskTimer(ScoreBoardPlugin.getInstance(), () -> {
            //遍历玩家集合，在更新计分板的同时让玩家使用同一个计分板
            playerSet.forEach(playerName -> {
                Player player = Bukkit.getPlayer(playerName);
                if (Objects.nonNull(player) && player.isOnline()) {
                    //玩家存在，则更新计分板
                    creationStrategy.setScoreboard(player);
                } else {
                    //玩家不存在，则从玩家集合中移除
                    if (playerSet.contains(playerName)) {
                        playerSet.remove(playerName);
                    }
                }
            });
        }, 0, 20);          //每过1秒就给该玩家重新创建并设置计分板

        isRunning = true;
        return true;
    }


    /**
     * 修改小队特性
     */
    public static void changeTeamOptions(Team team) {
        /**
         * Team还有一个特性比较好用，就是可以很方便的设置一些小队功能
         * 比如关闭友伤、关闭玩家名称的可见性，队友在隐身时也可以看到队友若隐若现的样子等，
         * 当然这些功能可以通过事件监听器来实现，但是使用Team提供的API直接调用更加方便
         *
         * 小队特性的效果是根据当前计分板内的队伍来进行区分的，
         * 比如senkosan用的计分板内，有个蓝队，蓝队的小队特性用的就是下面的代码，蓝队里有senkosan和iLiveOnFaith两名玩家。
         * 而iLiveOnFatih所用的计分板和senkosan用的不是同一个scoreboard对象，并且iLiveOnFatih的计分板内senkosan和iLiveOnFaith并不在同一个队伍里。
         * 那么senkosan就无法伤害到iLiveOnFaith，而iLiveOnFaith可以伤害Senkosan，
         */
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);            //设置队伍的碰撞规则，这里是不会碰撞的
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);      //设置队伍的名字标签可见性，这里是可见的
        team.setAllowFriendlyFire(false);                                               //关闭 友伤
        team.setCanSeeFriendlyInvisibles(true);                                         //设置可以看到队友隐身的样子
    }

}
