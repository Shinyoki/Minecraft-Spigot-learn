package com.senko.pluginwithdatabase.mapper;


import com.senko.pluginwithdatabase.entity.PlayerPO;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 插入玩家PO
     * @param player    玩家PO
     * @return          插入的行数
     */
    int insertPlayer(PlayerPO player);

    /**
     * 更新玩家PO
     * @param player        玩家PO
     * @return              更新的行数
     */
    int updatePlayerById(PlayerPO player);

    /**
     * 删除玩家PO
     * @param playerName    玩家名
     * @return              成功删除的数量
     */
    int deletePlayer(@Param("name") String playerName);
}
