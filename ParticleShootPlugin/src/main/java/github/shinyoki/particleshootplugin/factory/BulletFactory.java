package github.shinyoki.particleshootplugin.factory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BulletFactory {
    /**
     * 获取子弹
     */
    public static ItemStack getBullet() {
        ItemStack itemStack = new ItemStack(Material.ARROW, 16);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("子弹");
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * 检查是否是子弹
     */
    public static boolean isBullet(ItemStack itemHold) {
        return itemHold != null &&
                itemHold.getType() == Material.ARROW &&
                itemHold.getItemMeta() != null &&
                itemHold.getItemMeta().getDisplayName().equals("子弹");
    }
}
