package com.senko.simplecustomevent.event.emitter;

import com.senko.simplecustomevent.event.PlayerSneakingHitSheepEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * 玩家蹲下时伤害小样事件 触发器
 *
 * 如何知道 玩家在蹲下来的时候它是否有在击打某只羊？
 * 别无他法，我们只能通过已有的API Event创建一个 监听链，
 * 通过中间值来暂存 “状态” 信息，然后在需要的时候进行判断。
 * 此时要格外注意，如果是用容器这类的数据结构来存放这些中间值，就必须考虑在什么时候去清除被存放的元素，
 * 否则会存在内存泄漏的风险。
 * @author senko
 * @date 2022/7/2 19:06
 */
public class PlayerSneakingHitSheepEventEmitter implements Listener {

    /**
     * 正在蹲着的玩家
     *
     * 状态信息
     */
    private Set<UUID> playerSet = new HashSet<>();

    @EventHandler
    public void onPlayerSneakingEvent(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            //正在潜行时
            playerSet.add(event.getPlayer().getUniqueId());
        } else {
            //不在潜行就尝试删除中间状态
            playerSet.remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerDamageSheepEvent(EntityDamageByEntityEvent event) {
        //以前好像遇到过从事件中获取的对象为空的情况，于是我就先空判断了
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();

        //如果damager存在、damger是玩家对象，被害者是羊
        if (Objects.nonNull(damager) &&
                damager instanceof Player &&
                entity instanceof Sheep) {

            //取消事件，不造成伤害
            event.setCancelled(true);

            //通过中间值验证玩家是否在蹲着
            if (playerSet.contains(damager.getUniqueId())) {

                //触发我们的自定义事件，并清空中间状态信息
                Bukkit.getPluginManager().callEvent(new PlayerSneakingHitSheepEvent((Player) damager, (Sheep) entity));
                /**
                 * 一般情况下，都是在监听链的最后才remove | reset状态值，
                 * 我这里需要玩家在蹲着的时候能重复触发，因此选择在非蹲起的情况下remove | reset状态值。
                 * 还是那句话，
                 */
                //playerSet.remove(damager.getUniqueId());
            }
        }
    }
}
