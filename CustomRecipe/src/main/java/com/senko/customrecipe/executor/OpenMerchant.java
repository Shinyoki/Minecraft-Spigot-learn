package com.senko.customrecipe.executor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class OpenMerchant implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("open_shop")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                List<MerchantRecipe> recipes = new ArrayList<>();
                ItemStack dia = new ItemStack(Material.DIAMOND_BLOCK);
                MerchantRecipe merchantRecipe = new MerchantRecipe(dia, 12);
                ItemStack ingredient = new ItemStack(Material.DIRT);
                merchantRecipe.addIngredient(ingredient);
                recipes.add(merchantRecipe);

                Merchant merchant = Bukkit.createMerchant("交易界面");
                merchant.setRecipes(recipes);

                Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
                for (Player onlinePlayer : onlinePlayers) {
                    if (onlinePlayer.getName().equalsIgnoreCase("senkosan")) {
                        onlinePlayer.openMerchant(merchant, true);
                    }
                }

            }
        }
        return false;
    }
}
