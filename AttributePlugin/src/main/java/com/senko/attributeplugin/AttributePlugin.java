package com.senko.attributeplugin;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;


public final class AttributePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("boom").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length >= 1) {
                Player player = (Player) sender;
                switch (args[0].toLowerCase()) {
                    case "add":
                        // /boom add
                        addAttribute(player);
                        return true;
                    case "hide":
                        // /boom hide
                        hideAttribute(player);
                        return true;
                    case "show":
                        // /boom show
                        showAttribute(player);
                        return true;
                }
            }
            return false;
        }
        sender.sendMessage("必须是玩家才能使用该指令！");
        return true;
    }

    private void addAttribute(Player player) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        //主手上没拿物品时，ItemStack为NULL值
        if (Objects.nonNull(itemInMainHand)) {
            //获取物品信息
            ItemMeta itemMeta = itemInMainHand.getItemMeta();

            //添加些其他的东西
            itemMeta.setUnbreakable(true);
            itemMeta.addEnchant(Enchantment.LUCK, 100, true);

            //添加之前，先删除，否则会重复出现
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                //暴力全部删除：可以选择先把需要的保存，删除，再恢复需要的属性
                itemMeta.removeAttributeModifier(slot);
            }

            /**
             * 参数一：Attribute类型
             * 参数二：属性修饰符
             */
            //添加属性
            itemMeta.addAttributeModifier(Attribute.GENERIC_MOVEMENT_SPEED, new AttributeModifier(
                    /**
                     * 物品修饰符构造方法
                     * 参数一：UUID
                     * 参数二：修饰符名，随便起
                     * 参数三：修饰程度
                     * 参数四：数量的计算方式
                     * 参数五：属性生效时的物品位置
                     */
                    UUID.randomUUID(),
                    "增幅玩家速度",
                    0.5,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            ));
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                    UUID.randomUUID(),
                    "增幅攻击伤害",
                    19,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            ));
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                    UUID.randomUUID(),
                    "增幅攻击速度",
                    100,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.HAND
            ));

            itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(
                    UUID.randomUUID(),
                    "增幅最大血量",
                    40,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.OFF_HAND
            ));
            itemMeta.addAttributeModifier(Attribute.GENERIC_ATTACK_KNOCKBACK, new AttributeModifier(
                    UUID.randomUUID(),
                    "增幅攻击鸡腿",
                    2,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.OFF_HAND
            ));
            itemMeta.addAttributeModifier(Attribute.GENERIC_KNOCKBACK_RESISTANCE, new AttributeModifier(
                    UUID.randomUUID(),
                    "增幅自己的鸡腿抗性",
                    1,
                    AttributeModifier.Operation.ADD_NUMBER,
                    EquipmentSlot.OFF_HAND
            ));

            //获取的meta是副本，得再set回去
            itemInMainHand.setItemMeta(itemMeta);
        } else {
            player.sendMessage("好歹手上拿个物品吧:/");
        }
    }

    private void hideAttribute(Player player) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (Objects.nonNull(itemInMainHand)) {
            //依旧是获取meta物品信息
            ItemMeta itemMeta = itemInMainHand.getItemMeta();

            //添加物品标记
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS); //标记的重复添加是会被忽略的，因此不需要先删除再添加
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); //标记的重复添加是会被忽略的，因此不需要先删除再添加

            itemInMainHand.setItemMeta(itemMeta);
        } else {
            player.sendMessage("好歹手上拿个物品吧:/");

        }
    }

    private void showAttribute(Player player) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (Objects.nonNull(itemInMainHand)) {
            //依旧是获取meta物品信息
            ItemMeta itemMeta = itemInMainHand.getItemMeta();

            //删除物品标记
            itemMeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);   //不存在该标记则会被忽略
            itemMeta.removeItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            itemInMainHand.setItemMeta(itemMeta);
        } else {
            player.sendMessage("好歹手上拿个物品吧:/");

        }
    }

}
