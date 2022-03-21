package com.senko.configuration;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class Configuration extends JavaPlugin {

    @Override
    public void onEnable() {
        // TODO 配置文件读取与设置
        Logger logger = getLogger();

        FileConfiguration defaultConfig = getConfig();
        defaultConfig.set("senkoKey","senkoValue");

        logger.info(defaultConfig.getString("senkoKey"));
        try {
            YamlConfiguration yml = new YamlConfiguration();
            yml.set("key","value");
            File file = new File(getDataFolder(), "my-config.yml");
            yml.save(file);
            defaultConfig.save("config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
