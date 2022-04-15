package com.senko.customrecipe;

import com.senko.customeventplugin.pojo.GodBow;
import com.senko.customrecipe.executor.OpenMerchant;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.font.TextHitInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public final class CustomRecipe extends JavaPlugin {

    @Override
    public void onEnable() {
        /**
         * 物品合成
         * Shaped
         */
        ItemStack godBow = GodBow.createGodBow();
        NamespacedKey nsKey = new NamespacedKey(this, "god_bow");
        ShapedRecipe shapedRecipe = new ShapedRecipe(nsKey, godBow);
        shapedRecipe.shape(
                "B",
                "S"
        );
        shapedRecipe.setIngredient('B', Material.BLAZE_POWDER);
        shapedRecipe.setIngredient('S', Material.SLIME_BALL);
        System.out.println("得到的神弓配方group："+shapedRecipe.getGroup());
        Bukkit.addRecipe(shapedRecipe);

        /**
         * 物品合成
         * Shapeless
         */
        ItemStack dia = new ItemStack(Material.DIAMOND_BLOCK);
        NamespacedKey nsKey2 = new NamespacedKey(this, "dia_block");

        ShapelessRecipe diaRecipe = new ShapelessRecipe(nsKey2, dia);
        diaRecipe.addIngredient(2, Material.BLAZE_POWDER);
        diaRecipe.addIngredient(2, Material.DIRT);

        Bukkit.addRecipe(diaRecipe);

        getCommand("open_shop").setExecutor(new OpenMerchant());

        /**
         * Villager MerchantRecipe
         */
        //spawn a farmer villager fast
        Villager farmer = (Villager) getServer().getWorld("world").spawnEntity(getServer().getWorld("world").getSpawnLocation(), EntityType.VILLAGER);
        farmer.setProfession(Villager.Profession.FARMER);
        farmer.setAI(false);

        MerchantRecipe recipe = null;
        // create list of merchant recipes:
        List<MerchantRecipe> merchantRecipes = new ArrayList<MerchantRecipe>();

        recipe = new MerchantRecipe(new ItemStack(Material.FEATHER), 13); // high max purchase limit
        recipe.addIngredient(new ItemStack(Material.DIRT)); // what's required to buy it
        merchantRecipes.add(recipe);

        farmer.setRecipes(merchantRecipes);

        /**
         * FurnaceRecipe
         * 不要重复声明和使用同名的namespaceKey，否则会报出 {@link IllegalArgumentException} 异常
         */
        FurnaceRecipe dirtRecipe = new FurnaceRecipe(new NamespacedKey(this, "god_bow_furnace"), godBow, Material.DIRT, 0f, 3 * 20);
//        FurnaceRecipe dirtRecipe = new FurnaceRecipe(nsKey, godBow, Material.DIRT, 0f, 3 * 20);

        Bukkit.addRecipe(dirtRecipe);

    }

}
