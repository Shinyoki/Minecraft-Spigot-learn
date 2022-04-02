package com.senko.inventoryplugin.holder;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class GlobalInventory {
    private static Map<String , Inventory> inventories = new HashMap<>();

    public static boolean contains(String playerName) {
        return inventories.containsKey(playerName);
    }

    public static Inventory getInventory(String playerName) {

        if (!contains(playerName)) {
            //没有注册过
            return null;
        } else {
            //存在
            return inventories.get(playerName);
        }
    }

    public static void addToInventories(String playerName, Inventory inventory) {
        inventories.put(playerName, inventory);
    }

    public static Inventory inventoryInit(int capacity) {
        return Bukkit.createInventory(null, capacity);
    }
}
