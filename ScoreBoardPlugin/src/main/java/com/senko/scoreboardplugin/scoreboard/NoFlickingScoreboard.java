package com.senko.scoreboardplugin.scoreboard;

import com.senko.scoreboardplugin.ScoreBoardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 无闪烁计分板
 * 
 * 本案例主要用于理解如何实现无闪烁的计分板，并不适用于组队系统。
 *
 * 不同计分板内的队伍不一致，也无法直接通过定义相同队伍名来获取相同Team对象，
 * 只能让开发者在更新玩家计分板时先unregister()Team再重新赋值，
 * 同时还要考虑每个玩家计分板内队伍的人员动态分配问题，这种具体的业务代码本教程不做演示。
 * @author senko
 * @date 2022/6/14 9:41
 */
public class NoFlickingScoreboard {


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
     * 需要被更新的玩家
     */
    private static Set<String> playerSet = new HashSet<>();;

    /**
     * 队伍名称
     * {
     *      red: ChatColor.RED,
     *      ...
     * }
     */
    private Map<String, ChatColor> teamColorMap;

    public NoFlickingScoreboard() {
        //初始化数据
        teamColorMap = new HashMap<>();
        isRunning = false;
        teamColorMap.put("green", ChatColor.GREEN);
        teamColorMap.put("red", ChatColor.RED);
        teamColorMap.put("blue", ChatColor.BLUE);
        teamColorMap.put("yellow", ChatColor.YELLOW);
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

    public static int sizeOfUpdatePlayerSet() {
        return playerSet.size();
    }

    public static HashSet<String> getUpdatePlayerSet() {
        return (HashSet<String>) playerSet;
    }

    /**
     * 查看该玩家名是否能匹配到一个真实玩家
     * @param playerName        玩家名
     * @return                  真实玩家
     */
    public static boolean validatePlayer(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (Objects.isNull(player) || !player.isOnline()) {
            //如果匹配不到玩家，或者玩家不在线，则返回false
            return false;
        }
        return true;
    }

    /**
     * 需要刷新玩家集合是否为空
     * @return  是否为空
     */
    public static boolean isEmpty() {
        return playerSet.isEmpty();
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
                    updatePlayerScoreboard(player);
                } else {
                    //玩家不存在，则从玩家集合中移除
                    playerSet.remove(playerName);
                }
            });
        }, 0, 20);          //每过1秒就给该玩家重新创建并设置计分板

        isRunning = true;
        return true;
    }

    /**
     * 更新玩家的计分板
     * @param player        玩家
     */
    private static void updatePlayerScoreboard(Player player) {
        //复用 计分板
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            //如果玩家没有计分板、或者是再用公共计分板 则新建
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        //复用 侧边计分项
        Objective sidebarObjective = scoreboard.getObjective("side-bar");
        if (Objects.isNull(sidebarObjective)) {
            //如果不存在则创建
            sidebarObjective = scoreboard.registerNewObjective("side-bar", "dummy", ChatColor.GOLD + "玩家信息");
            sidebarObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
        }
        /**
         * 无闪烁计分板的实现，信息并不依靠{@link Objective#unregister()} 先注销再赋值来实现，
         * 而是利用{@link Team#setSuffix(String)} 会 动态影响其他 同Entry计分项来实现。
         *
         * 注意要让这里getScore()传递的entry参数和下面给信息Team addEntry()传递的参数一致
         */
        //赋值 侧边计分项
        sidebarObjective.getScore(ChatColor.WHITE + "玩家名：").setScore(5);
        sidebarObjective.getScore(ChatColor.GOLD + "玩家权限：").setScore(4);
        sidebarObjective.getScore(ChatColor.YELLOW + "玩家等级：").setScore(3);
        sidebarObjective.getScore(ChatColor.RED + "玩家死亡次数：").setScore(1);
        sidebarObjective.getScore(ChatColor.AQUA + "玩家生存时间：").setScore(0);
        sidebarObjective.getScore(ChatColor.GREEN + "当前时间").setScore(-1);

        //复用 玩家名下面的计分项
        Objective nameBelowObjective = scoreboard.getObjective("name-below");
        if (Objects.isNull(nameBelowObjective)) {
            nameBelowObjective = scoreboard.registerNewObjective("name-below", Criterias.HEALTH, ChatColor.GREEN + "血量");
            nameBelowObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);
            nameBelowObjective.getScore(player.getName());
        }

        //复用 玩家列表计分项
        Objective tabListObjective = scoreboard.getObjective("tab-list");
        if (Objects.isNull(tabListObjective)) {
            tabListObjective = scoreboard.registerNewObjective("tab-list", Criterias.HEALTH, ChatColor.GREEN + "血量");
            tabListObjective.setRenderType(RenderType.HEARTS);
            tabListObjective.getScore(player.getName());
        }


        /**
         * 重点，这里注册的Team并不是为了组队，而是利用Team特性动态影响其他计分项
         * addEntry()传递的entry要和上面的侧边栏计分项一致。
         *
         * 一个Entry条目对应一个Team，如果一个Team里设了两个Entry，那么最后的效果是setSuffix()后这两个Entry就显示同样的后缀了。
         * 显然并不符合预期要求。
         * 由于我们在上面的侧边栏计分板里写了非常多的Entry条目，一个个拿来写就会造成代码体积疯狂膨胀，
         * 不仅麻烦，还影响可读性。因此建议根据业务场景抽象出合适的方法用于简化代码。
         */
        HashMap<String, String> infoMap = new HashMap<>();
        infoMap.put(ChatColor.WHITE + "玩家名：", ChatColor.YELLOW + player.getName());
        infoMap.put(ChatColor.GOLD + "玩家权限：", player.isOp() ? "管理员" : "玩家");
        infoMap.put(ChatColor.YELLOW + "玩家等级：", String.valueOf(player.getLevel()));                                         //用String.valueOf(int)
        infoMap.put(ChatColor.RED + "玩家死亡次数：", ChatColor.RED + String.valueOf(player.getStatistic(Statistic.DEATHS)));
        infoMap.put(ChatColor.AQUA + "玩家生存时间：", String.valueOf(player.getTicksLived() / 20) + "秒");
        infoMap.put(ChatColor.GREEN + "当前时间", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        //初始化或更新 信息队伍
        registerOrUpdateInfoTeam(scoreboard, infoMap, player);

        //设置 计分板
        player.setScoreboard(scoreboard);
    }

    /**
     * 初始化或更新 信息
     * @param scoreboard    计分板
     * @param infoMap       信息
     * <code>
     *         infoMap: {
     *              entry1: value1,
     *              entry2: value2,
     *              ...
     *         }
     * </code>
     * @param player        玩家
     */
    public static void registerOrUpdateInfoTeam(Scoreboard scoreboard, Map<String, String> infoMap, Player player) {
        if (infoMap.size() > 0) {

            infoMap.forEach((key, val) -> {
                Team infoTeam = scoreboard.getTeam(key + "-info-team");         //name: <entry>-info-team
                if (Objects.isNull(infoTeam)) {
                    //不存在 相应的 <entry>-info-team
                    infoTeam = scoreboard.registerNewTeam(key + "-info-team");      //创建相应的infoTeam
                    infoTeam.addEntry(key);                                               //添加需要动态绑定的Entry条目
                    infoTeam.setSuffix(val);                                              //将值作为后缀，并动态影响给其他计分项
                    return;
                } else {
                    //存在 相应的 <entry>-info-team
                    if (infoTeam.hasEntry(key)) {
                        //不存在 对应entry
                        infoTeam.addEntry(key);
                        infoTeam.setSuffix(val);
                        return;
                    } else {
                        //存在 对应entry
                        infoTeam.setSuffix(val);
                        return;
                    }
                }
            });
        }
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
