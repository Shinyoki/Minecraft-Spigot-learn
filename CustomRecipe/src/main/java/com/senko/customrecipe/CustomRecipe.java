package com.senko.customrecipe;

import com.senko.customeventplugin.pojo.GodBow;


import com.senko.customrecipe.executor.VillagerCommandExecutor;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.font.TextHitInfo;
import java.util.*;

public final class CustomRecipe extends JavaPlugin {

    /**
     * 命名空间 {@link NamespacedKey}不可重复使用在不同配方里，
     * 不可重名，否则会抛出
     * {@link IllegalArgumentException}异常
     */
    @Override
    public void onEnable() {
        /**
         * 熔炉自定义
         */
        //命名空间
        NamespacedKey key1 = new NamespacedKey(this, "furnace_recipe");
        //煅烧结果
        ItemStack godBow = GodBow.createGodBow();
        //熔炉配方
        FurnaceRecipe furnaceRecipe = new FurnaceRecipe(key1,godBow,Material.DIRT,0f,3 * 20);
        //加入到服务器中
        Bukkit.addRecipe(furnaceRecipe);

        /**
         * 自定义工作台物品合成
         * Shaped       定形
         * Shapeless    不定形
         * rows
         * ["  P", "  S", "   "]
         *
         * "  P"
         * "  S"
         * "   "
         *
         * "P  "
         * "S  "
         * "   "
         */
        //命名空间
        NamespacedKey craftKey = new NamespacedKey(this, "craft_table");
        //结果
        ShapedRecipe shapedRecipe = new ShapedRecipe(craftKey, godBow);
        //原材料的摆放方式
        shapedRecipe.shape(
                "  P",
                "  S",
                "   "
        );
        //指定摆放方式中的字符所代表的原材料
        shapedRecipe.setIngredient('P', Material.BLAZE_POWDER);
        shapedRecipe.setIngredient('S', Material.SLIME_BALL);
        //加入到服务器中
        Bukkit.addRecipe(shapedRecipe);

        /**
         * 村民的自定义交易
         */
        getCommand("villager").setExecutor(new VillagerCommandExecutor());

    }
}
