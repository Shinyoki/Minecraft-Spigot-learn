package com.senko.inventoryplugin.listener;


import com.senko.inventoryplugin.holder.GlobalInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CantGetFromChestListener implements Listener {
    @EventHandler
    public void onGet(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        HumanEntity whoClicked = event.getWhoClicked();
        if (GlobalInventory.contains(whoClicked.getName())) {
            Inventory remoteChest = GlobalInventory.getInventory(whoClicked.getName());
            if (remoteChest.equals(inventory)) {

                ItemStack itemStack = new ItemStack(Material.FEATHER, 1);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.RED+"小鸡毛");
                if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.RED + "小鸡毛")) {
                    whoClicked.sendMessage(ChatColor.RED + "哎，不让你拿~");
                    event.setCancelled(true);
                }
            }
        }
    }
}
