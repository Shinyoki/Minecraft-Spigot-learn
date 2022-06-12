package com.senko.scoreboardplugin.executor;

import com.senko.scoreboardplugin.ScoreBoardPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 计分板Team指令执行器
 * @author senko
 * @date 2022/6/12 18:58
 */
public class TeamCommandExecutor implements CommandExecutor {

    private static Map<String, ChatColor> teamNameMap = new HashMap<>();
    static {
        teamNameMap.put("red", ChatColor.RED);
        teamNameMap.put("blue", ChatColor.BLUE);
        teamNameMap.put("green", ChatColor.GREEN);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Scoreboard scoreboard = ScoreBoardPlugin.getInstance().getScoreboard();

        if (Objects.nonNull(args) && args.length >= 2) {
            if (Bukkit.getPlayer(args[0]) != null) {
                Player player = Bukkit.getPlayer(args[0]);

                if (player.isOnline() && Objects.nonNull(scoreboard)) {
                    if (Arrays.asList(teamNameMap.keySet().toArray()).contains(args[1].toLowerCase())) {
                        addPlayerToTeam(scoreboard, args[1].toLowerCase(), player);

                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        return true;
                    }
                }
            }
        }
        sender.sendMessage("参数错误！请检查格式是否正确：/add-team 玩家名 " + Arrays.toString(teamNameMap.keySet().toArray()));
        return true;
    }

    private void addPlayerToTeam(Scoreboard scoreboard, String teamName, Player player) {
        /**
         * 玩家列表，又称TabList。在Spigot所提供的API里需要用Team进行编辑。
         *
         * 请注意，一个玩家只能有一个计分板。
         * 因此请确保不同队伍的玩家都被赋值为同一个计分板，否则将不会正常显示。
         */
        //玩家列表（不是计分项，列表是通过Team编辑的）
        Team team = scoreboard.getTeam(teamName);
        if (Objects.isNull(team)) {
            team = scoreboard.registerNewTeam(teamName);                                   //队伍名也不能重复
        }
        team.setPrefix(ChatColor.GREEN + "[" + teamName + "] " + ChatColor.WHITE);         //设置玩家列表中位于该队伍的玩家的名称前缀
        team.setColor(teamNameMap.get(teamName));                                          //设置玩家列表中位于该队伍的玩家的名称颜色
        team.addPlayer(player);                                                            //将玩家加入到该队伍

        //组队效果
        team.setAllowFriendlyFire(false);                                                  //关闭友伤
        team.setCanSeeFriendlyInvisibles(true);                                            //队友隐身时也可以看到

        player.setScoreboard(scoreboard);
    }
}
