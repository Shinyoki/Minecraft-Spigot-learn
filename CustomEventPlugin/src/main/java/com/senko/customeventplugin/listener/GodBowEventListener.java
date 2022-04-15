package com.senko.customeventplugin.listener;

import com.senko.customeventplugin.enums.EnumBowMode;
import com.senko.customeventplugin.event.GodBowEvent;
import com.senko.customeventplugin.holder.GodBowHolder;
import com.senko.customeventplugin.pojo.GodBow;
import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * 神弓的事件监听器
 * [模式切换]
 */
public class GodBowEventListener implements Listener {
    //key是箭，value是弓
    private Map<Entity,ItemStack> godBowShotArrows = new HashMap<>();

    public boolean isGodBow(ItemStack item) {
        return item != null &&
                item.getType() == Material.BOW &&
                item.getItemMeta().getDisplayName().equals(ChatColor.GREEN + "神弓");
    }

    /**
     * 当玩家持有godbow时，左键可以切换模式
     * @param event
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onHoldingGodBow(org.bukkit.event.player.PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            //玩家在左键
            System.out.println("玩家左键了");
            if (event.hasItem()) {
                //玩家有物品
                System.out.println("玩家拥有物品");
                ItemStack item = event.getItem();
                //是神弓
                if (isGodBow(item)) {
                    System.out.println("是神弓");
                    Bukkit.getPluginManager().callEvent(new GodBowEvent(item, event.getPlayer()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onLeftClickGodBow(GodBowEvent event) {
        GodBow bow = event.getGodBow();
        if (bow != null) {
            event.changeBowMode();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onGodBowHit(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        //施害者是弓箭
        if (damager instanceof Arrow) {
            //判断这根箭是不是来自于神弓
            if (godBowShotArrows.containsKey(damager)) {
                //如果是，就根据弓的mode锁定效果，并且移除
                Entity entity = event.getEntity();

                execute(entity, godBowShotArrows.get(damager));

                System.out.println("箭是来自于神弓");
                godBowShotArrows.remove(damager);
            } else {
                System.out.println("不是神弓的箭");
            }

        }
    }


    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        ItemStack bow = event.getBow();
        if (isGodBow(bow)) {
            //如果是神弓就添加到map中
            godBowShotArrows.put(event.getProjectile(),bow);
        }
    }

    /**
     * 处理神弓的击中效果
     * @param entity    被击中者
     * @param bow       神弓
     */
    private void execute(Entity entity, ItemStack bow) {
        System.out.println("准备执行");
        if (entity != null && bow != null) {
            //如果被击中者是玩家
            //提取GodBow对象
            if (GodBow.getBowInfo(bow) != null)
                System.out.println("当前模式是" + GodBow.getBowInfo(bow).getModeName());
                if (GodBow.getBowInfo(bow) == EnumBowMode.LIGHTING) {
                    Location location = entity.getLocation();
                    World world = location.getWorld();
                    world.strikeLightning(location);
                }
            }
    }


}
