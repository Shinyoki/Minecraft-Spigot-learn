package com.senko.blockplugin.examples.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;

/**
 *
 * @author senko
 * @date 2022/7/15 10:20
 */
public class ApplyPhysicsExample {
    public void doSetBlockData(Block clickedBlock, Player player) {

        // 如果是箱子就将箱子删除
        if (clickedBlock.getType() == Material.CHEST){

            BlockData airBlockData = Bukkit.createBlockData(Material.AIR);
            player.sendMessage("现在我要删除该箱子，且不会引发周围的方块更新！");

            /**
             * 第二个参数applyPhysics = false，意味着修改方块信息不会引发周围的方块更新。
             * 因为有些情况，插件需要大面积的修改方块，这时候如果选择触发方块更新，可能会出现意想不到的效果
             * (腐竹，你也不想让服务器因为巨浪的方块更新计算导致暴毙吧)。
             * 比如著名的/co i指令，如果管理员选择对玩家进行事务回滚操作。最后回滚完的区域可能留下了
             * 浮空火把，又或者是we里对地形的改造。都选择了不触发方块更新。
             *
             * {@link Block#setType(Material, boolean)} 也可以修改方块的类型，也可以设置是否触发方块更新。
             * 但是原有的方块状态BlockState和方块信息BlockData会被覆盖为默认的，和这里的Bukkit.createBlockData(Material.AIR)有点类似。
             */
            clickedBlock.setBlockData(airBlockData, true);         // 关闭方块更新 的修改
//            clickedBlock.setBlockData(airBlockData);                      // 默认启用方块更新 的修改

        }

    }
}
