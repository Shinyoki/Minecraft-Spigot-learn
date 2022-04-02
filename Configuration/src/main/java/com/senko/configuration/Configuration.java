package com.senko.configuration;

import com.senko.configuration.executor.ChangeLocationExecutor;
import com.senko.configuration.listener.LoginEventListener;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
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

    private static Configuration instance;

    public Configuration() {
        this.instance = this;
    }

    public static Configuration getPlugin() {
        return instance;
    }

    /**
     *  Spigot配置文件的读取和存储。<br>
     *  Spigot自己设置了个专门用于处理Yaml配置文件的类，名为{@link YamlConfiguration}，它可以用来新建、加载、保存YML文件。<br>
     *  但是针对{@link YamlConfiguration}对象的set操作并不会实时改变本地文件中的内容，原因在于它只是存储于内存中的对象，A type of ConfigurationSection that is stored in memory.
     *  之后还需使用{@link YamlConfiguration#save(File)}来完成持久化更新，参考 <code>save(getDataFolder(),"custom-config.yml")</code><br>
     *  我们可以调用{@link JavaPlugin}对象的 {@link #getConfig()} 函数用来直接获插件的默认配置<i>config.yml</i>。<br><br>
     *  {@link #getDataFolder()} 函数会返回配置文件的所在文件夹。<br><br>
     *  使用{@link #saveConfig()} 会将{@link JavaPlugin#configFile}对象的信息持久化<br>
     *  使用{@link #saveDefaultConfig()} 会将jar包中的config.yml文件持久化到配置文件夹中，与{@link JavaPlugin#configFile}对象无关。
     */
    @Override
    public void onEnable() {
        /**
         * 默认插件配置文件的信息读取
         * 1. 先调用JavaPlugin父类的public函数getConfig得到config.yml中的信息。
         * 2. 使用YamlConfiguration对象的各种get方法，读取配置文件中的值。
         */
        //testGetConfigYaml();

        /**
         * config.yml文件的持久化。
         * 在当前案例中，虽然我们已经将config.yml事先定义在了resources文件夹中。
         * 但是当插件被加载完成，Spigot并不会在本地的为该插件创建config.yml文件。
         * 我们需要使用JavaPlugin的public函数 saveConfig()来实现完成持久化。
         */
        //testOnlySet();

        /**
         * saveConfig()与saveDefaultConfig()函数的区别。
         * saveConfig虽然会覆盖掉位于saveDefaultConfig()函数所生成的文件的值，
         * 但是之后我们仍然能从JavaPlugin所维护的config对象里得到源文件的信息。
         */
        //testSaveAndSaveDefaultConfig();

        /**
         * 自定义配置文件，活学活用{@link #getDataFolder()} 函数
         */
        //testCustomConfigYaml();

        //getCommand("change").setExecutor(new ChangeLocationExecutor());
        //getServer().getPluginManager().registerEvents(new LoginEventListener(), this);

    }

    /**
     * 使用{@link YamlConfiguration}对象的get函数来获取yaml属性。
     */
    private void testGetConfigYaml() {
        Logger logger = getLogger();

        //通过JavaPlugin#getConfig函数获取的配置文件只能是config.yml
        FileConfiguration config = getConfig();

        /**
         * getList("key")
         * 获取list数组
         */
        List<?> list = config.getList("senko.list");
        System.out.println("==========senko.list===========" + list.size());
        for (int i = 0; i < list.size(); i++) {
            logger.info("Result：" + list.get(i) + "| 类型为：" + list.get(i).getClass().getName());
        }

        /**
         * getMapList("key")
         * 获取Map集合
         */
        List<Map<?, ?>> mapList = config.getMapList("senko.map");
        System.out.println("==========senko.map===========" + mapList.size());
        for (int i = 0; i < mapList.size(); i++) {
            StringBuilder sb = new StringBuilder();

            Set<?> keys = mapList.get(i).keySet();
            Collection<?> values = mapList.get(i).values();
            sb.append("，map的keys：");

            keys.stream().forEach((e)->{
                sb.append(e + " ");
            });
            sb.append("，map的values：");

            values.stream().forEach(e->{
                sb.append(e + " ");
            });
            logger.info("当前map：" + i + sb.toString() );
        }

        /**
         * getInt("key")
         * 获取整型值
         */
        System.out.println("==========senko.age===========");
        System.out.println(config.getInt("senko.age"));

        /**
         * getBoolean("key")
         * 获取布尔值
         */
        System.out.println("==========senko.disabled===========");
        System.out.println(config.getBoolean("senko.disabled"));
        /**
         * getXXXX("key")
         */

        /**
         * 补充：
         * {@link YamlConfiguration#get()}虽然最终能获取到值的类型，但是在编译期还无法做到
         */


    }

    /**
     * 测试 {@link #saveConfig()} 与 {@link #saveDefaultConfig()} 的区别
     */
    private void testSaveAndSaveDefaultConfig() {
        //config.yml
        FileConfiguration config = getConfig();

        //更新JavaPlugin#newConfig对象的属性值
        config.set("senko.disabled", true);

        //覆盖保存
        //saveConfig();

        /**
         * 持久化后的config.yml文件，哪怕被覆盖了，JavaPlugin所维护的newConfig属性仍然保存着
         * 原config.yml文件的信息，重复的值会是持久化后文件的值。
         */
        /*
        FileConfiguration changedConfig = getConfig();
        System.out.println(ChatColor.RED +  "持久化后的Config.yml  age： " + changedConfig.get("senko.age"));
        System.out.println(ChatColor.RED +  "持久化后的Config.yml  disabled： " + changedConfig.get("senko.disabled"));
        */

        //非覆盖保存，但是不会同步JavaPlugin#newConfig对象的状态
        /**
         * 非覆盖保存并不会涉及到对JavaPlugin#newConfig对象的持久化，只会将jar包中的默认config.yml给持久化到配置文件夹中。
         */
        //saveDefaultConfig();


    }

    /**
     * 自定义的配置文件，活学活用{@link #getDataFolder()}函数
     */
    private void testCustomConfigYaml() {
        YamlConfiguration myConfig = new YamlConfiguration();
        myConfig.set("senko.age", 13);
        myConfig.set("senko.status","unknown");


        try {
            /**
             * 保存
             */
            myConfig.save(new File(getDataFolder(), "custom-config.yml"));
            /**
             * 读取
             */
            YamlConfiguration newConfig = new YamlConfiguration();
            newConfig.load(new File(getDataFolder(),"custom-config.yml"));
            System.out.println(ChatColor.GREEN + " " + newConfig.get("senko.age"));
            System.out.println(ChatColor.GREEN + " " + newConfig.get("senko.status"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 演示只set而不save的区别
     */
    private void testOnlySet() {
        /**
         * 由于指针引用的特性，这里get得到的config对象在被修改后会同步影响JavaPlugin所维护的config。
         */
        FileConfiguration config = this.getConfig();
        config.set("senko.disabled", true);

        /**
         * 如果不进行save操作，就不会在本地持久化刚刚更新好的config.yml文件。
         * 使用saveConfig或saveDefaultConfig都会造成config.yml文件的持久化。
         */
        this.saveConfig();

    }
}
