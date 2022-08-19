package com.senko.featuretest.event;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.*;

/**
 * @author senko
 * @date 2022/8/8 14:54
 */
public class RecipeCraftEvent implements Listener {

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        // 通过事件获取配方
        Recipe recipe = event.getRecipe();
        if (recipe instanceof ShapedRecipe) {
            // 如果是ShapedRecipe，则一定是工作台里的配方
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;
            // 转型并获取命名空间
            NamespacedKey key = shapedRecipe.getKey();

            String namespace = key.getNamespace();      // 如果是原版配方，则为minecraft， 如果是自定义配方，则为自定义插件的名称
            String keyName = key.getKey();              // 如果是原版配方，则为物品的名字，如果是自定义配方，则为自定义配方的名字
            event.getWhoClicked().sendMessage("配方来源于：" + namespace);
            event.getWhoClicked().sendMessage("配方名称：" + keyName);

            if (namespace.equals("minecraft")) {
                CraftingInventory inventory = event.getInventory();
                event.setCancelled(true);
                inventory.setResult(null);
            }
        }
    }
}
