package com.senko.simplecustomevent.event.listener;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.RegisteredListener;

/**
 * 有关EventPriority优先级、ignoreCancelled以及HandlerList的小研究
 */
public class TestEventListener implements Listener {


    /**
     * EventPriority的优先级越高，改Handler越往后处理，
     * 因此EventPriority.LOWEST标注的最先执行。
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerSneakEvent(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            event.getPlayer().sendMessage("这是优先级最高的事件监听，将会在LOWEST优先级的监听器执行之后出现！");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerSneakEvent2(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            event.getPlayer().sendMessage("这是优先级最低的事件监听，将会最先出现！");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerSneakEvent3(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            RegisteredListener[] registeredListeners = event.getHandlers().getRegisteredListeners();
            Player player = event.getPlayer();

            player.sendMessage("负责监听PlayerToggleSneakEvent的监听器：");
            int i = 1;
            for (RegisteredListener registeredListener : registeredListeners) {
                player.sendMessage("监听器" + i++ + "号所在插件：" + registeredListener.getPlugin().getName());
                event.getPlayer().sendMessage("所在类：" + registeredListener.getListener().getClass().getSimpleName());
            }

            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerSneakEvent4(PlayerToggleSneakEvent event) {
        if (event.isSneaking()) {
            event.getPlayer().sendMessage("这段代码执行了吗？");
        }
    }


}
