package com.senko.potion;

import com.senko.potion.executor.ApplyEffect;
import com.senko.potion.executor.GetPotion;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


public final class PotionPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        setCommand("potion", new ApplyEffect());
        setCommand("get-potion", new GetPotion());
    }

    protected void setCommand(String cmdName, CommandExecutor executor) {
        Optional.ofNullable(getCommand(cmdName))
                .orElseThrow(() -> {
                    getLogger().severe("指令" + cmdName + "不存在！");
                    return new RuntimeException("指令" + cmdName + "不存在！");
                })
                .setExecutor(executor);
    }

}
