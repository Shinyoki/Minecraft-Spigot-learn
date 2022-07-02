package com.senko.simplecustomevent.event.listener;

import com.senko.simplecustomevent.event.PlayerSneakingHitSheepEvent;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * 事件监听器
 *
 * 当玩家在潜行时击打小羊，小样就会垂直起飞、变换羊毛色彩、并给玩家一个音效提示.
 * 确实是在模仿Hyoixel，但是我这没有给羊监听摔落伤害事件（
 * @author senko
 * @date 2022/7/2 19:54
 */
public class OnPlayerSneakingHitSheepEventListener implements Listener {

    @EventHandler
    public void onPlayerSneakingHitSheepEventListener(PlayerSneakingHitSheepEvent event) {
        Player player = event.getPlayer();
        Sheep sheep = event.getSheep();
        Random random = new Random();
        int next = random.nextInt(DyeColor.values().length + 1);    // [0, length + 1)  左闭右开的int

        //随机颜色
        sheep.setColor(DyeColor.values()[next]);
        //垂直起飞：施加Velocity速度，速度矢量为y轴方向一个单位
        sheep.setVelocity(new Vector(0, 1, 0));
        //音效：玩家#playSound和word#playSound是有区别的，一个是只给该玩家发送音效封包，另一个是给全体玩家发送音效封包
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 1F);

    }
}
