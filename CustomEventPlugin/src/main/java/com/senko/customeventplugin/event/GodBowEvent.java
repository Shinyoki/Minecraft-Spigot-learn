package com.senko.customeventplugin.event;

import com.senko.customeventplugin.enums.EnumBowMode;
import com.senko.customeventplugin.holder.GodBowHolder;
import com.senko.customeventplugin.pojo.GodBow;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GodBowEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private static boolean isCancelled = false;

    @NotNull
    private GodBow godBow;
    @Nullable
    private Entity holder;

    public GodBowEvent(@NotNull ItemStack bow, @Nullable Entity holder) {
        this.godBow = new GodBow(bow);
        GodBowHolder.add(bow, this.godBow);
        this.holder = holder;
    }

    public void changeBowMode() {
        godBow.changeBowMode();
        if (holder != null && holder instanceof Player) {
            Player player = (Player) this.holder;
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,.5f, .5f);
            holder.sendMessage(ChatColor.GREEN + "当前神弓的模式为：" + godBow.getMode().getModeName());
        }
        GodBowHolder.add(this.godBow.getBow(), this.godBow);
    }

    public EnumBowMode getMode() {
        return godBow.getMode();
    }

    public GodBow getGodBow() {
        return godBow;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
