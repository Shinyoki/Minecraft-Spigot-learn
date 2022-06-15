package com.senko.scoreboardplugin.strategy.impl;

import com.senko.scoreboardplugin.strategy.AbstractCreationStrategy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 无闪烁计分板生成策略
 * @author senko
 * @date 2022/6/15 13:35
 */
public class NoFlickeringScoreboardCreationStrategyImpl implements AbstractCreationStrategy {

    @Override
    public void setScoreboard(Player player) {
        //复用 计分板
        Scoreboard scoreboard = player.getScoreboard();
        if (scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            //如果玩家没有计分板、或者是再用公共计分板 则新建
            scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        }

        //复用 侧边计分项
        Objective sidebarObjective = scoreboard.getObjective("side-bar");
        if (Objects.isNull(sidebarObjective)) {
            //没找到：初始化
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
            //没找到：初始化
            nameBelowObjective = scoreboard.registerNewObjective("name-below", "dummy", ChatColor.GREEN + "生涯击杀");
            nameBelowObjective.setDisplaySlot(DisplaySlot.BELOW_NAME);              //设置显示位置 重中之重，不要忘记
        }
        //赋值 遍历所有在线玩家，并将其加入到计分项中
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            nameBelowObjective.getScore(onlinePlayer.getName()).setScore(onlinePlayer.getStatistic(Statistic.PLAYER_KILLS));    //设置 对应entry玩家的分数为击杀数
        }

        //复用 玩家列表计分项
        Objective tabListObjective = scoreboard.getObjective("tab-list");
        if (Objects.isNull(tabListObjective)) {
            //没找到：初始化
            tabListObjective = scoreboard.registerNewObjective("tab-list", Criterias.HEALTH, ChatColor.GREEN + "血量");
            tabListObjective.setDisplaySlot(DisplaySlot.PLAYER_LIST);               //设置显示位置 重中之重，不要忘记
            tabListObjective.setRenderType(RenderType.HEARTS);                      //设置显示类型 显示为心形
        }
        //赋值 由于准则是 health，所以这里的分数就会由服务器自动更新，无需我们手动设置getScore()

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
                    if (!infoTeam.hasEntry(key)) {
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

}
