package com.senko.blockplugin.examples.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

/**
 * 有关BlockData的基本使用（获取方块信息，以及序列化和反序列化）
 *
 * @author senko
 * @date 2022/7/15 9:49
 */
public class BlockDataExample {
    /**
     * BlockData在游戏中的提现就是F3键按下后，在屏幕中心右侧显示的Target Block下的那些内容
     * （#开头的是注解，因此不会被包含在BlockData里）
     *
     * 一般情况下，开发者会将这些方块信息通过{@link BlockData#getAsString()}序列化为字符串，然后通过其他方式存储
     * （但是BlockData并不是方块的全部，还有BlockState会记负责录方块的详细数据，比如箱子、命令方块、、、）
     * 等到需要的时候，再{@link Bukkit#createBlockData(String)}反序列化回BlockData来修其他方块的放开信息
     */
    public void doGetAndSetBlockData(Block clickedBlock, Player player) {
        BlockData blockData = clickedBlock.getBlockData();
        player.sendMessage("你点击的方块信息：" + blockData.getAsString());

        // 比如这里操作箱子，假设你点击了一个箱子
        if (clickedBlock.getType() == Material.CHEST) {
            // 通过序列化BlockData来提取信息
            String facing = getFacing(blockData);
            player.sendMessage("原先箱子的方向是：" + facing);

            // 修改朝向
            String anotherFacing = getAnotherFacing(facing);
            String blockDataInString = blockData.getAsString().replace(facing, anotherFacing);

            // 通过序列化后的字符串，反序列化回BlockData
            clickedBlock.setBlockData(Bukkit.createBlockData(blockDataInString));
            player.sendMessage("修改后箱子的方向是：" + anotherFacing);
        } else {
            player.sendMessage("你点击的方块不是箱子，没有方向信息");
        }

    }

    private String getFacing(BlockData blockData) {
        // 序列化BlockData为字符串
        String asString = blockData.getAsString();
        int first = asString.indexOf("facing=");
        // 从字符串中提取箱子的方向
        return asString.substring(first + 7, asString.substring(first).indexOf(",") + first);
    }

    private String getAnotherFacing(String facing) {
        switch (facing) {
            case "north":
                return "east";
            case "east":
                return "south";
            case "south":
                return "west";
            case "west":
                return "north";
            default:
                return "north";
        }
    }
}
