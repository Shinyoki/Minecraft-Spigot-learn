package com.senko.featuretest.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredListener;

import java.util.ArrayList;

/**
 * 吃完食物后会触发coolDown冷却时间
 */
public class OnEatEventListener implements Listener {
    @EventHandler
    public void onEat(PlayerItemConsumeEvent event) {
        ItemStack item = event.getItem();
        Material type = item.getType();
        if (type.equals(Material.GOLDEN_APPLE)) {
            //如果是吃到的是金苹果，则触发coolDown
            event.getPlayer().setCooldown(Material.GOLDEN_APPLE, 20 * 5);
            ArrayList<HandlerList> handlerLists = HandlerList.getHandlerLists();
            Player player = event.getPlayer();
            player.sendMessage("得到的HandlerList数组：" + handlerLists.size());
            int i = 0;
            for (HandlerList handlerList : handlerLists) {

                player.sendMessage("得到的第"+i+"个HandlerList：===========================");
                RegisteredListener[] registeredListeners = handlerList.getRegisteredListeners();
                for (RegisteredListener registeredListener : registeredListeners) {
                    player.sendMessage("所属插件：" + registeredListener.getPlugin().getName());
                    player.sendMessage("所属优先级：" + registeredListener.getPriority().getSlot());
                    player.sendMessage("监听器的类名："+registeredListener.getListener().getClass().getName());
                }
                i++;
            }
        }
    }
}
