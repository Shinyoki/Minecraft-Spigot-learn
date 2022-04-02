package com.senko.inventoryplugin.executor;

import com.senko.inventoryplugin.holder.GlobalInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;

public class RemoteInventoryCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("open")) {
            if (sender instanceof Player) {
                Player curPlayer = (Player) sender;
                Inventory inventory = null;
                if (!GlobalInventory.contains(curPlayer.getName())) {
                    inventory = Bukkit.createInventory(curPlayer, 9, curPlayer.getName() + "的箱子");

                    //一个槽 存羽毛
                    ItemStack itemStack = new ItemStack(Material.FEATHER, 1);

                    //获取羽毛的信息对象
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    //修改物品名称
                    itemMeta.setDisplayName(ChatColor.RED + "小鸡毛");

                    //List表，一个元素代表一行
                    LinkedList<String> lores = new LinkedList<>();
                    lores.add(ChatColor.YELLOW + "第一：绝不轻言放弃");
                    lores.add(ChatColor.GREEN + "第二：绝不高傲");
                    //修改物品的表示
                    itemMeta.setLore(lores);


                    itemStack.setItemMeta(itemMeta);
                    itemStack.addUnsafeEnchantment(Enchantment.LUCK, 666);
                    inventory.addItem(itemStack);

                    GlobalInventory.addToInventories(curPlayer.getName(), inventory);
                } else {
                    inventory = GlobalInventory.getInventory(curPlayer.getName());
                }

                curPlayer.openInventory(inventory);
            } else {
                sender.sendMessage("只有玩家才能使用该指令");
                return true;
            }
        }
        return false;
    }
}
