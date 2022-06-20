package com.senko.pluginwithdatabase.mapper;


import com.senko.pluginwithdatabase.entity.PlayerPO;

/**
 * 玩家Mapper
 * @author senko
 * @date 2022/6/20 19:05
 */
public interface PlayerMapper {

    /**
     * 获取玩家PO
     * @param playerName 玩家名
     */
    PlayerPO selectOneByName(String playerName);
}
