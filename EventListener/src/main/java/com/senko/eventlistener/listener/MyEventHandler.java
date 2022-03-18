package com.senko.eventlistener.listener;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.plugin.RegisteredListener;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Listener接口并没有要求任何方法实现，
 * 应该只是用于实现Java的动态代理
 */
public class MyEventHandler implements Listener {

    /**
     * 玩家登录事件
     * 【修改登录提示、加入登录音效】
     *
     * @EventHadnler，用在@Listener实现类的方法上，参数是Event子类。
     * @param event
     * @return 不需要返回值
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage(player.getDisplayName()+ ChatColor.BLUE + "加入了服务器");
        player.chat("我是"+player.getPlayerListName()+"，我喂自己袋盐~");
        System.out.println("当前玩家时间："+new Date(player.getPlayerTime()));
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.5f,0.5f);
    }

    /**
     * 事件取消
     */
    @EventHandler
    public void onShearSheep(PlayerShearEntityEvent event) {
        Entity entity = event.getEntity();
        Player player1 = event.getPlayer();
        //经过验证，entity并不是指所有有效实体
        if (entity == null) {
            player1.sendMessage("Entity null");
            return;
        }
        event.getPlayer().chat("我剪了"+event.getEntity().getName());
        if (entity.getType() == EntityType.SHEEP) {
            Sheep sheep = (Sheep) entity;
            List<DyeColor> collect = Arrays.stream(DyeColor.values()).collect(Collectors.toList());
            int next = new Random().nextInt(collect.size());
            sheep.setColor(collect.get(next));
            event.setCancelled(true);
        } else {
            Player player = event.getPlayer();
            player.chat("我没事剪"+entity.getName()+"干啥啊");
        }
    }
//    @EventHandler
//    public void expBottleBreaker(ExpBottleEvent event) {
//        Block hitBlock = event.getHitBlock();
//        event.setShowEffect(false);
//        event.setExperience(0);
//        if (hitBlock != null) {
//            for (RegisteredListener registeredListener : event.getHandlers().getRegisteredListeners()) {
//                System.out.println(registeredListener.getListener());
//            }
//            if (hitBlock.getType().equals(Material.STONE) || hitBlock.getType().equals(Material.DIRT) || hitBlock.getType().equals(Material.GRASS_BLOCK)) {
//                hitBlock.breakNaturally();
//            }
//            System.out.println(hitBlock.getType().name());
//        }else {
//            System.out.println("HITBOX IS NULL");
//        }
//    }
}
