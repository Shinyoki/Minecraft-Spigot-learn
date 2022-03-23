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
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.plugin.RegisteredListener;

import javax.annotation.RegEx;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @Listener接口并没有要求任何方法实现，应该只是用于实现Java的动态代理
 * @EventHandler所允许传入的参数只有一个
 * Spigot所提供的Event事件并不全面，想要实现一连套的事件链发生方法，
 * 必不可少的要通过多个Handler在监听到对应事件后将信息存入一个共享变量，
 * 最终交由事件链的尾端函数来判断。
 */
public class MyEventHandler implements Listener {

    /**
     * 玩家登录事件   <br>
     * 【修改登录提示、加入登录音效】  <br>
     *  <i>若对玩家的坐标、背包等数据通过Bukkit API进行修改，需要稍微延迟一段时间。</i>
     *  <i>因为PlayerJoinEvent在触发时玩家还没有彻底载入完毕，可以用Bukkit提供的Scheduler延迟执行需要操作的代码</i><br>
     *  <i>摘取自：hamsteryds.github.io/noticements-in-bukkit </i>
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
     * 羊毛色彩变化 事件处理
     * 当玩家想要伤害羊羊君的时候羊毛色彩就会发生变化
     */
    @EventHandler
    public void onPlayerHitSheep(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager != null && damager instanceof Player) {
            //施害者是玩家类型
            if (event.getEntity() != null && event.getEntity() instanceof Sheep) {
                //被害者是羊羊君
                Entity entity = event.getEntity();
                Sheep sheep = (Sheep) entity;

                DyeColor[] colors = DyeColor.values();
                //防止发生数组越界，所以 length - 1
                int randomIndex = new Random().nextInt(colors.length - 1);

                //修改羊毛颜色：随机
                sheep.setColor(colors[randomIndex]);

                //取消伤害事件发生
                event.setCancelled(true);
            }
        }

        /**
         * 事件取消
         *
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
         */
    }
}
