package com.senko.customeventplugin.executor;

import com.senko.customeventplugin.event.GodBowEvent;
import com.senko.customeventplugin.pojo.GodBow;
import com.senko.customeventplugin.utils.InventoryUtils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;

/**
 * 给自己发一把类似于Hypixel的Modular Bow
 */
public class GodBowCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("godbow")) {
//            if (args != null && args.length == 1) {
//                sender.sendMessage("进入arg1模式");
//                String msg = ChatColor.GREEN + "当前模式：" + ChatColor.YELLOW + "击退" + ChatColor.GRAY + "[1]";
//                String str = ChatColor.translateAlternateColorCodes('\u00A7', msg);
//                System.out.println(str);
//                return true;
//            }
            if (sender instanceof Player && sender.hasPermission("senko.godbow")) {
                Player player = (Player) sender;
                if (!InventoryUtils.add(player.getInventory(), GodBow.createGodBow())) {
                    //背包满了
                    player.sendMessage(ChatColor.RED + "背包已满，请清理后再试");
                    return true;
                } else {
                    //背包未满
                    player.sendMessage(ChatColor.GREEN + "嘿！看看你的背包里多了个什么？");
                    return true;
                }
            }
        }
        return false;
    }




}
