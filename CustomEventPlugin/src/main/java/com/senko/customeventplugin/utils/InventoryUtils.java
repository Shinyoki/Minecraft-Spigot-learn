package com.senko.customeventplugin.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * 管理玩家背包的工具类
 */
public class InventoryUtils {
    /**
     * 向容器发送物品
     * @param inventory 容器
     * @return          是否成功
     */
    public static boolean add(Inventory inventory, ItemStack itemStack) {
        if (inventory == null) {
            //容器为空
            return false;
        } else {
            //容器不为空
            if (inventory.firstEmpty() == -1) {
                //容器满了
                return false;
            } else {
                //容器未满
                inventory.setItem(inventory.firstEmpty(), itemStack);
            }
            return true;
        }
    }

}
