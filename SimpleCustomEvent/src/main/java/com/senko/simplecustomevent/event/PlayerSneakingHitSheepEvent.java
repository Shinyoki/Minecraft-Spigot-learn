package com.senko.simplecustomevent.event;

import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * <pre>
 * 自定义事件
 *
 * 当玩家潜行时伤害了一只羊事件。
 *
 * 自定义事件需要配合监听器或是在其他代码里，
 * 用{@link org.bukkit.plugin.PluginManager#callEvent(Event)}手动触发该事件
 * </pre>
 * @author senko
 * @date 2022/7/2 18:48
 */
public class PlayerSneakingHitSheepEvent extends Event implements Cancellable {

    /**
     * 服务器中该事件的处理器集合，
     * 我们只需要实现{@link Event#getHandlers()}然后返回这个对象就好，
     * 具体的赋值会被服务端处理。
     */
    private static final HandlerList handlers = new HandlerList();

    /**
     * 事件状态
     */
    private boolean isCancelled;

    /**
     * 玩家
     */
    private Player player;

    /**
     * 羊
     */
    private Sheep sheep;


    public PlayerSneakingHitSheepEvent(Player player, Sheep sheep) {
        this.player = player;
        this.sheep = sheep;
    }

    /**
     * 一个是需要实现的getHandlers
     */
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * 一个是约定的getHandlerList
     */
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    public boolean nonCancelled() {
        return isCancelled == false;
    }
    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    public Player getPlayer() {
        return player;
    }

    public Sheep getSheep() {
        return sheep;
    }


}
