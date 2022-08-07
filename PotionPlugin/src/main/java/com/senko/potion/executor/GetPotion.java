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
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

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

                ItemStack potionStack = new ItemStack(Material.POTION);
                PotionMeta potionMeta = (PotionMeta) potionStack.getItemMeta();
                addAllPotionEffects(potionMeta);
                potionMeta.setColor(Color.fromRGB(225,6,2));

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
        Arrays.stream(PotionEffectType.values())
                .forEach(potionEffectType -> {
                    potionMeta.addCustomEffect(
                            potionEffectType.createEffect(20 * 10, 0),
                            true
                    );
                });
    }

}
