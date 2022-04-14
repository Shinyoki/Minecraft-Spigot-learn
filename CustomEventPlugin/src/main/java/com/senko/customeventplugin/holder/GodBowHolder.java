package com.senko.customeventplugin.holder;

import com.senko.customeventplugin.pojo.GodBow;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GodBowHolder   {
    public static final Map<ItemStack, GodBow> holder = new HashMap<ItemStack, GodBow>();
    public static void add(ItemStack item, GodBow bow) {
        holder.put(item, bow);
    }
    public static void remove(ItemStack item) {
        holder.remove(item);
    }
    public static GodBow get(ItemStack item) {
        return holder.get(item);
    }

    public static boolean contains(ItemStack item) {
        return holder.containsKey(item);
    }
}
