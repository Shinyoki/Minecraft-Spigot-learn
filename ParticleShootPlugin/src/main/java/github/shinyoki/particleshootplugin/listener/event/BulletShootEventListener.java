package github.shinyoki.particleshootplugin.listener.event;

import github.shinyoki.particleshootplugin.effect.BulletShootEffect;
import github.shinyoki.particleshootplugin.factory.BulletFactory;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class BulletShootEventListener implements Listener {

    @EventHandler
    public void onBulletShoot(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        EquipmentSlot hand = event.getHand();
        if (checkMainHandAndRightClick(action, hand)) {

            // 检查背包里是否存在子弹
            ItemStack itemHold = event.getItem();
            if (BulletFactory.isBullet(itemHold)) {
                // 扣除数量
                itemHold.setAmount(itemHold.getAmount() - 1);
                player.sendMessage("剩余子弹" + ChatColor.RED + itemHold.getAmount() + ChatColor.WHITE + "发");

                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1, 1);

                // 发射子弹
                new BulletShootEffect(player)
                        .start();
            }
        }

    }

    /**
     * 检查是 主手和右键
     */
    private boolean checkMainHandAndRightClick(Action action, EquipmentSlot hand) {
        return hand == EquipmentSlot.HAND &&
                (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK);
    }
}
