package github.shinyoki.particleshootplugin;

import github.shinyoki.particleshootplugin.listener.cmd.GetBulletCmd;
import github.shinyoki.particleshootplugin.listener.event.BulletShootEventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;


public final class ParticleShootPlugin extends JavaPlugin {


    private static ParticleShootPlugin instance;
    public static ParticleShootPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        // 指令
        Optional.ofNullable(getCommand("getbullet"))
                .ifPresent(command -> command.setExecutor(new GetBulletCmd()));

        // 事件
        Bukkit.getPluginManager()
                .registerEvents(new BulletShootEventListener(), this);
    }

}
