package com.senko.pluginwithdatabase;

import com.senko.pluginwithdatabase.executor.DbCommand;
import com.senko.pluginwithdatabase.mapper.PlayerMapper;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PluginWithDatabase extends JavaPlugin {
    private static SqlSessionFactory sqlSessionFactory;

    private static PluginWithDatabase instance;

    public static PluginWithDatabase getInstance() {
        return instance;
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        if (Objects.isNull(sqlSessionFactory)) {
            synchronized (PluginWithDatabase.class) {
                if (Objects.isNull(sqlSessionFactory)) {
                    PluginWithDatabase plugin = (PluginWithDatabase)(Bukkit.getPluginManager().getPlugin("PluginWithDatabase"));
                    plugin.initSqlSessionFactory();
                }
            }
        }
        return sqlSessionFactory;
    }

    @Override
    public void onEnable() {
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
            Bukkit.broadcastMessage(plugin.getName());
        }
        instance = this;
        initSqlSessionFactory();
        getCommand("db").setExecutor(new DbCommand());
    }

    @Override
    public void onDisable() {

    }

    private void initSqlSessionFactory() {
        this.saveDefaultConfig();                               //本地配置文件不存在，则生成默认配置文件，如果存在，就啥也不干
        this.reloadConfig();                                    //重新从本地配置文件读取配置
        FileConfiguration pluginConfig = this.getConfig();      //得到配置对象
        //校验数据
        if (StringUtils.isEmpty(pluginConfig.getString("senko.datasource.host"))) {
            throw new Error("未配置数据库连接信息");
        }
        if (pluginConfig.getInt("senko.datasource.port") == 0) {
            throw new Error("未配置数据库端口信息");
        }
        if (pluginConfig.getString("senko.datasource.database") == null) {
            throw new Error("未配置数据库名称信息");
        }
        if (StringUtils.isEmpty(pluginConfig.getString("senko.datasource.username"))) {
            throw new Error("未配置数据库用户名");
        }
        if (StringUtils.isEmpty(pluginConfig.getString("senko.datasource.password"))) {
            throw new Error("未配置数据库密码");
        }
        //初始化数据库连接池
        initSqlSessionFactory(
                MySqlVersion.MySql8,
                pluginConfig.getString("senko.datasource.host"),
                pluginConfig.getInt("senko.datasource.port"),
                pluginConfig.getString("senko.datasource.database"),
                pluginConfig.getString("senko.datasource.username"),
                pluginConfig.getString("senko.datasource.password")
        );

    }

    /**
     * 初始化SqlSessionFactory
     * @param mySqlVersion  mysql版本 {@link MySqlVersion}
     * @param host          IP地址
     * @param port          端口
     * @param database      数据库名
     * @param dbUser        用户名
     * @param dbPassword    密码
     */
    private void initSqlSessionFactory(MySqlVersion mySqlVersion, String host, int port, String database, String dbUser, String dbPassword) {
        //数据源配置
        String dbUrl = new StringBuilder("jdbc:mysql://").append(host).append(":").append(port).append("/").append(database).append("?useUnicode=true&characterEncoding=utf8&useSSL=false").toString();
        String dbDriver = mySqlVersion.equals(MySqlVersion.MySql5) ? "com.mysql.jdbc.Driver" : "com.mysql.cj.jdbc.Driver";
        //创建数据源
        PooledDataSource dataSource = new PooledDataSource(dbDriver, dbUrl, dbUser, dbPassword);
        //配置Mybatis
        Environment mybatisEnv = new Environment("development", new JdbcTransactionFactory(), dataSource);
        Configuration mybatisConfig = new Configuration(mybatisEnv);
        //添加Mappers
        mybatisConfig.getMapperRegistry().addMapper(PlayerMapper.class);
        //添加Mappers.xml

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(mybatisConfig);
    }

    private enum MySqlVersion {
        MySql5,
        MySql8,
    }

}
