package com.senko.configuration;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public final class Configuration extends JavaPlugin {

    /**
     *  Spigot配置文件的读取和存储。<br>
     *  Spigot自己设置了个专门用于处理Yaml文件的类，名为{@link YamlConfiguration}，它可以用来新建、加载、保存YML文件。<br>
     *  但是针对{@link YamlConfiguration}对象的set操作并不会实时改变本地文件中的内容，A type of ConfigurationSection that is stored in memory.
     *  之后还需使用{@link YamlConfiguration#save(File)}来完成持久化更新，参考 <code>save(getDataFolder(),"custom-config.yml")</code><br>
     *  我们可以调用JavaPlugin的 {@link #getConfig()} 函数用来直接获取一个{@link YamlConfiguration}对象，
     *  通过该函数所获得的对象，内部存储着<i>config.yml</i>文件的属性。<br><br>
     *  {@link #getDataFolder()} 函数会返回配置文件的所在文件夹对象。<br><br>
     *  使用{@link #saveConfig()} 会覆盖原来的<i>config.yml</i>。<br>
     *  使用{@link #saveDefaultConfig()} 则不会覆盖替换掉原来的<i>config.yml</i>，从而保留并更新原先的内容。
     */
    @Override
    public void onEnable() {
        /**
         * config.yml: Spigot默认的插件配置文件
         */
//        testGetConfigYaml();
//        testSaveAndSaveDefaultConfig();

        /**
         * xxxx.yml：自定义的插件配置文件。
         * 我们可以先new出
         */
        testCustomConfigYaml();
    }

    /**
     * 使用{@link YamlConfiguration}对象的get函数来获取yaml属性。
     */
    private void testGetConfigYaml() {
        Logger logger = getLogger();

        //通过JavaPlugin#getConfig函数获取的配置文件只能是config.yml
        FileConfiguration config = getConfig();

        // list：
        List<?> list = config.getList("senko.list");
        System.out.println("==========senko.list===========" + list.size());
        for (int i = 0; i < list.size(); i++) {
            logger.info("Result：" + list.get(i) + "| 类型为：" + list.get(i).getClass().getName());
        }

        //map:
        List<Map<?, ?>> mapList = config.getMapList("senko.map");
        System.out.println("==========senko.map===========" + mapList.size());
        for (int i = 0; i < mapList.size(); i++) {
            StringBuilder sb = new StringBuilder();
            Set<?> keys = mapList.get(i).keySet();
            Collection<?> values = mapList.get(i).values();
            sb.append("，map的keys：");

            keys.stream().forEach((e)->{
                sb.append(e);
            });
            sb.append("，map的values：");

            values.stream().forEach(e->{
                sb.append(e);
            });
            logger.info("当前map：" + i + sb.toString() );
        }

        //Int
        System.out.println("==========senko.age===========");
        System.out.println(config.getInt("senko.age"));

        //Boolean
        System.out.println("==========senko.disabled===========");
        System.out.println(config.getBoolean("senko.disabled"));
        //...
    }

    /**
     * 测试 {@link #saveConfig()} 与 {@link #saveDefaultConfig()} 的区别
     */
    private void testSaveAndSaveDefaultConfig() {
        //config.yml
        FileConfiguration config = getConfig();

        //更新属性值
        config.set("senko.disabled", true);

        //覆盖保存
        //saveConfig();

        //非覆盖保存
        saveDefaultConfig();

        FileConfiguration config1 = getConfig();
        System.out.println("senko.age：" + config1.get("senko.age"));
        System.out.println("senko.disabled：" + config1.get("senko.disabled"));
    }

    /**
     * 自定义的配置文件
     */
    private void testCustomConfigYaml() {
        YamlConfiguration myConfig = new YamlConfiguration();
        myConfig.set("senko.age", 13);
        myConfig.set("senko.status","unknown");

        //存储
        try {
            myConfig.save(new File(getDataFolder(), "custom-config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
