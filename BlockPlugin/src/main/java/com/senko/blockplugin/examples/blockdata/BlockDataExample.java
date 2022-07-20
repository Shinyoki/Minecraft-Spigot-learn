package com.senko.blockplugin.examples.blockdata;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Chest;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

/**
 * 有关BlockData的基本使用（获取方块信息，以及序列化和反序列化）
 *
 * @author senko
 * @date 2022/7/15 9:49
 */
public class BlockDataExample {
    /**
     * BlockData在游戏中的体现就是F3键按下后，在屏幕中心右侧显示的Target Block下的那些内容
     * （#开头的是注解，因此不会被包含在BlockData里）
     *
     * 对于存储离散不连续的多个方块的需求，一般都是记录Location，BlockData#getAsString()序列化BlockData，然后通过某种方式存储起来。
     * 等需要的时候，再Bukkit#createBlockData(String)反序列化回BlockData，然后用{@link org.bukkit.World#setBlockData(Location, BlockData)}把方块设置在某处。
     * （但是BlockData并不是方块的全部，还有BlockState会记负责录方块的详细数据，比如箱子存储了什么、命令方块里的命令是什么等等，
     * 所以这种方法只能设置什么位置是什么方块，并不能连同详细数据一同序列化。
     *
     * 而且Spigot/Bukkit提供的这些修改BlockData的API都很笨重，如果要修改大批量的方块，
     * 将会严重增大服务器的性能消耗，除非你抛开Spigot/Bukkit繁重的代码，直接利用底层代码NMS来修改，
     * 又或者是使用WorldEdit API（推荐）。
     */
    public void doGetAndSetBlockData(Block block, Player player) {

        // TIP: 获取到的BlockData和Location一样，都是对象属性的内存地址，如果要进行修改，请尽量先clone()出副本再针对副本进行修改
        BlockData blockData = block.getBlockData();

        // 比如这里你点击了一个箱子
        if (block.getType() == Material.CHEST) {

            Chest chestData = ((Chest) blockData);                                              // 是箱子，就转换成BlockData的子类Chest
            player.sendMessage("点击箱子的方向是：" + chestData.getFacing().toString());            // 箱子BlockData所特有的信息
            player.sendMessage("点击箱子是否含水：" + (chestData.isWaterlogged() ? "是" : "否"));
            player.sendMessage("点击箱子的类型：" + chestData.getType().name());
            player.sendMessage("点击箱子的完整data：" + blockData.getAsString());                   // blockData的String形式
            //chestData....


//            BlockData blockData1 = Bukkit.createBlockData(blockData.getAsString());           // 还有其他方式可以用来（获取）BlockData
//            BlockData blockData1 = Bukkit.createBlockData(Material.CHEST);

            changeAnotherBlock(block, chestData);                                               // 利用BlockData去修改其他的方块

        } else {
            player.sendMessage("你点击的方块不是箱子，没有方向信息");
        }

    }

    private void changeAnotherBlock(Block curBlock, Chest chestData) {

        Block relative = curBlock.getRelative(chestData.getFacing());               // 获取chest对面的方块

        Chest randomChestData = getRandomChestData(chestData);                      // 随机修改chest的data。

        relative.setBlockData(randomChestData);                                     // 修改相对方块为随机掉data的箱子

    }

    private Chest getRandomChestData(Chest chestData) {

        // 这里先克隆再修改，是为了不在修改时影响到原方块
        Chest clonedChestData = (Chest) chestData.clone();

        Random random = new Random();

        int nextType = random.nextInt(Chest.Type.values().length);
        clonedChestData.setType(Chest.Type.values()[nextType]);               // 通过API修改方块Type

        clonedChestData.setWaterlogged(!clonedChestData.isWaterlogged());     // 修改方块是否含水

        return clonedChestData;                                               // 返回新的BlockData

    }

}
