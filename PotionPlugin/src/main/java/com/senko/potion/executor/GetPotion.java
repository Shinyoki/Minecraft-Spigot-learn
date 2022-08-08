package com.senko.potion.executor;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author senko
 * @date 2022/8/7 20:28
 */
public class GetPotion implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (-1 != player.getInventory().firstEmpty()) {
                PlayerInventory inventory = player.getInventory();

                ItemStack potionStack = new ItemStack(Material.POTION);             // 创建物品槽，用于存放药水

                PotionMeta potionMeta = (PotionMeta) potionStack.getItemMeta();     // 得到药水的meta
                addAllPotionEffects(potionMeta);                                    // 添加所有药水效果
                potionMeta.setColor(Color.fromRGB(178,176,170));        // 修改药水的颜色
//                potionMeta.setColor(Color.GREEN);        // 修改药水的颜色

                potionStack.setItemMeta(potionMeta);
                inventory.addItem(potionStack);
            } else {
                sender.sendMessage("背包已满！");
            }

            return true;
        }
        sender.sendMessage("只有玩家才能使用当前指令！");
        return true;

    }

    private void addAllPotionEffects(PotionMeta potionMeta) {
        Stream.of(PotionEffectType.values())
                // 遍历Type
                .forEach(potionEffectType -> {
                    // 给meta添加一个药水效果
                    potionMeta.addCustomEffect(
                            // 10秒、一级、粒子效果更明显，ui界面带有图标的药水效果
                            new PotionEffect(potionEffectType, 20 * 10, 0, false, true, true),
                            // 如果玩家存在该药水效果，则强制替换
                            true
                    );
                });
    }

}
