package com.senko.blockplugin;

import com.senko.blockplugin.examples.block.BlockExample;
import com.senko.blockplugin.examples.blockdata.AnotherBlockDataExample;
import com.senko.blockplugin.examples.blockdata.BlockDataExample;
import com.senko.blockplugin.examples.blockstate.BlockStateExample;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Random;

public final class BlockPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onStickRightClick(PlayerInteractEvent event) {
        // 当玩家进行互动时，这个方法会被触发两次，因为是两只手都参与到了互动（
        if (event.getHand() == EquipmentSlot.HAND) {
            Block clickedBlock = event.getClickedBlock();
            ItemStack itemInMainHand = event.getPlayer().getInventory().getItemInMainHand();
            // 判断手里有没有物品
            if (Objects.isNull(itemInMainHand))
                return;

            // 根据手上木棍的名称来演示不同的案例
            if (itemInMainHand.getType() == Material.STICK) {
                // 和物品栏里的空物品一样，点击空气所返还的空气方块也是null值
                if (Objects.nonNull(clickedBlock)) {

                    String displayName = itemInMainHand.getItemMeta().getDisplayName();
                    switch (displayName) {
                        case "获取并修改BlockData":
                            // 演示BlockData的用法（序列化和反序列化）
                            new BlockDataExample().doGetAndSetBlockData(clickedBlock, event.getPlayer());
                            break;
                        case "关于BlockData的注意事项":
                            // 修改方块信息引发的 方块更新
                            new AnotherBlockDataExample().doSetBlockData(clickedBlock, event.getPlayer());
                            break;
                        case "获取方块状态":
                            new BlockStateExample().doGetBlockState(clickedBlock, event.getPlayer());
                            break;
                        case "改变方块生态":
                            new BlockExample().doBlockAPI(clickedBlock, event.getPlayer());
                            break;
                    }
                    event.getPlayer().sendMessage("boom");
                }
            }
        }
    }





//    /**
//     * 设置改变周围一圈草方块的生态， 以改变草地的颜色
//     */
//    private void doChangeBiome(Block centerBlock) {
//        Biome[] biomes = {Biome.THE_VOID, Biome.LUSH_CAVES, Biome.PLAINS, Biome.DRIPSTONE_CAVES, Biome.GROVE, Biome.BADLANDS, Biome.SWAMP, Biome.JUNGLE, Biome.MEADOW, Biome.SNOWY_BEACH};
//        Random random = new Random();
//        // 分别获取水平方向一圈的其他方块并设置随机的biomes
//        Location location = centerBlock.getLocation();
//        // 以center为中心, 设置半径为5，在圆的上下左右分别设置不同的生态
//        for (int i = -2; i <= 2; i++) {
//            for (int j = -2; j <= 2; j++) {
//                for (int k = -2; k <= 2; k++) {
//                    /**
//                     * 不要傻不愣登的直接修改原方块的location进行复用，
//                     * 因为location是一个地址引用，会影响到原方块的。
//                     *
//                     * 因此要先克隆出一个新的location，然后修改这个新的location再处理。
//                     */
//                    Block block = location.clone()
//                            .add(i, j, k).getBlock();
//                    if (block.getType() == Material.GRASS_BLOCK) {
//                        block.setBiome(biomes[random.nextInt(biomes.length)]);
//                    }
//                }
//            }
//        }
//
//    }


}
