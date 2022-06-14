package com.senko.scoreboardplugin.listener;

import com.senko.scoreboardplugin.scoreboard.NoFlickingScoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * 玩家推出事件监听器
 * @author senko
 * @date 2022/6/14 9:46
 */
public class OnPlayerQuitHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        //将玩家从需要更新列表中删除
        NoFlickingScoreboard.removeUpdatePlayer(event.getPlayer());
    }

}
