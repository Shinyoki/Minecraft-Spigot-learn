package com.senko.scoreboardplugin.listener;

import com.senko.scoreboardplugin.utils.ScoreboardUtil;
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
        ScoreboardUtil.removeUpdatePlayer(event.getPlayer());

        //如果更新玩家列表为空，则停止定时任务
        if (ScoreboardUtil.sizeOfUpdatePlayerSet() < 1) {
            ScoreboardUtil.stopTaskTimer();
        }
    }

}
