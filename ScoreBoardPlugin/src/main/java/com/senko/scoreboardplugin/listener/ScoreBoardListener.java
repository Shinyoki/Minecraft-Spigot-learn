package com.senko.scoreboardplugin.listener;

import com.senko.scoreboardplugin.ScoreBoardPlugin;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Date;

/**
 * 计分板监听器
 * @author senko
 * @date 2022/6/12 18:34
 */
public class ScoreBoardListener implements Listener {


    /**
     * 重新登陆以及新加入的玩家，会丢失上次登录时被赋予的scoreboard，因此需要重新绑定。
     * 好像如果用/scoreboard指令事先创建了全局计分板，之后新加入的玩家会自动被赋值为全局计分板。
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage("§a欢迎回来，" + event.getPlayer().getName());
        ScoreBoardPlugin.getInstance().createScoreboardOnPluginEnabled(event.getPlayer());
    }
}
