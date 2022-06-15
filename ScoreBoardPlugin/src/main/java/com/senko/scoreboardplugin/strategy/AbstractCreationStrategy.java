package com.senko.scoreboardplugin.strategy;

import org.bukkit.entity.Player;

/**
 * 计分板生成策略
 * @author senko
 * @date 2022/6/15 13:10
 */
public interface AbstractCreationStrategy {

    /**
     * 设置玩家的计分板
     * @param player    玩家
     */
    void setScoreboard(Player player);
}
