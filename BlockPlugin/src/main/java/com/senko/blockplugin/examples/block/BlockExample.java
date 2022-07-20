package com.senko.blockplugin.examples.block;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 * 有关Block方块的示例
 * @author senko
 * @date 2022/7/15 10:38
 */
public class BlockExample {

    /**
     * {@link Block}的存在就跟{@link org.bukkit.inventory.ItemStack}类似，它不保存方块的内部信息，
     * 真正的方块内部信息分如箱子内容、告示牌内容则由{@link Block#getState()}所得到的对象存储。
     * 正因为有这么多复杂多变的属性，我们不可能通过简单的方法去new出一个方块，
     * 因此正常的做法是通过 事件系统或{@link org.bukkit.World#getBlockAt(Location)} 等方式
     * 来“获取” 方块，而非 ”new“ 出方块。
     *
     * Block方块主要代表了在当前世界。当前坐标下这个方块的状态信息，
     * 比如方块是什么、光照等级是多少、生态群系是什么、它的坐标是什么，它所在的世界是什么等等
     *
     * {@link org.bukkit.block.BlockState}里的内容看着好像和{@link Block}没什么不同，
     * 但它的作用并不是表面的这些，而是转型成相应的子类，得到特殊方块的内部信息。比如它的子类箱子{@link org.bukkit.block.Chest}
     * 命令方块{@link org.bukkit.block.CommandBlock}。
     */
    public void doBlockAPI(Block block, Player player) {

        Material type = block.getType();
        player.sendMessage("方块类型：" + type.toString());

        Block relativeEast = block.getRelative(BlockFace.EAST);
        player.sendMessage("与方块相邻的东边的方块：" + relativeEast.getType().toString());

        Location location = block.getLocation();
        player.sendMessage("方块的坐标：" + location.toString());

        Biome biome = block.getBiome();
        player.sendMessage("方块所在的生态群系：" + biome.toString());

        int blockPower = block.getBlockPower();
        player.sendMessage("方块的红石等级：" + blockPower);

        int blockLightLevel = block.getLightLevel();
        player.sendMessage("方块的光照等级：" + blockLightLevel);

        //block.getChunk();     // 获取方块所在的Chunk，Chunk API有关，本期视频不细讲

        byte lightLevel = block.getLightLevel();
        player.sendMessage("方块的光照等级：" + lightLevel);

        //...还有更多API，自己发掘吧

        /**
         * 真正负责存储方块内部数据的两个类
         */
//        block.getState();
//        block.getBlockData();

    }

}
