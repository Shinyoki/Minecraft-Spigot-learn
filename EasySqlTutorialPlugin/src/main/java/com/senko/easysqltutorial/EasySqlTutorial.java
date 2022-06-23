package com.senko.easysqltutorial;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.api.action.query.QueryAction;
import cc.carm.lib.easysql.hikari.HikariDataSource;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public final class EasySqlTutorial extends JavaPlugin {

    private static SQLManager sqlManager;


    @Override
    public void onEnable() {
        initSQLManager();
        getCommand("db-easy-sql").setExecutor(this);
    }

    @Override
    public void onDisable() {

        getLogger().info("关闭插件中。。。。");
        //关闭数据源中的连接池
        if (Objects.nonNull(sqlManager)) {
            // 使用由EasySQL提供的静态方法直接关闭数据管理器。
            EasySQL.shutdownManager(sqlManager);
        }
    }

    /**
     * 初始化连接池
     */
    private void initSQLManager() {
        //加载config.yml必要的几个步骤
        this.saveDefaultConfig();                   //将位于jar包中的config.yml持久化到本地，如果本地已经存在配置文件，则啥也不干
        this.reloadConfig();                        //重新从本地的config.yml中读取配置，并赋值给config
        FileConfiguration config = this.getConfig();

        //读取配置信息
        String driver = config.getString("senko.datasource.driver");
        String url = config.getString("senko.datasource.url");
        String username = config.getString("senko.datasource.username");
        String password = config.getString("senko.datasource.password");

        //校验配置，校验失败则关闭插件
        if (StringUtils.isBlank(driver) || StringUtils.isBlank(url) || StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            Bukkit.getLogger().severe("[EasySQLTutorial] 缺少必备启动配置，将关闭插件!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        //初始化Manager
        sqlManager = EasySQL.createManager(driver, url, username, password);

        //检查并捕获
        try {

            if (!sqlManager.getConnection().isValid(5)) {
                getLogger().warning("[EasySQLTutorial] 数据库连接超时!");
            }
        } catch (SQLException e) {
            getLogger().severe("[EasySQLTutorial] 数据库连接失败，将关闭插件!");
            getLogger().warning("错误:" + e.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean flag = false;
        if (args.length >= 1) {
            switch (args[0]) {
                case "query":
                    // /db-easy-sql query <playerName>
                    flag = executeQuery(sender, args);
                    break;
                case "insert":
                    // /db-easy-sql insert <playerName>
                    flag = executeInsert(sender, args);
                    break;
                case "update":
                    // /db-easy-sql update <playerName> <money>
                    flag = executeUpdate(sender, args);
                    break;
                case "delete":
                    // /db-easy-sql delete <playerName>
                    flag = executeDelete(sender, args);
                    break;
            }

        }
        return flag;
    }

    private boolean executeQuery(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            sender.sendMessage("查询前线程：" + Thread.currentThread().getName());
            //得到查询构建器
            QueryAction queryAction = sqlManager.createQuery()
                    //指明需要操作的table表
                    .inTable("player")
                    //指明需要操作的column字段
                    .selectColumns("id", "name", "uuid", "money")
                    //添加where查询条件
                    .addCondition("name", args[1])
                    //构建查询
                    .build();

            //通过query对象执行里面的查询sql
            queryAction.executeAsync(successQuery -> {
                ResultSet resultSet1 = successQuery.getResultSet();
                //保存resultSet到
                ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap<>();
                if (resultSet1.next()) {
                    concurrentHashMap.put("id", resultSet1.getInt("id"));
                    concurrentHashMap.put("name", resultSet1.getString("name"));
                    concurrentHashMap.put("uuid", resultSet1.getString("uuid"));
                    concurrentHashMap.put("money", resultSet1.getInt("money"));
                }

                Bukkit.getScheduler().runTask(this, () -> {
                    sender.sendMessage("当前线程：" + Thread.currentThread().getName());
                    sender.sendMessage("查询成功！");
                    sender.sendMessage("当前线程：" + Thread.currentThread().getName());
                    concurrentHashMap.forEach((key, value) -> {
                        sender.sendMessage(key + ":" + value);
                    });
                    sender.sendMessage("查询结束！");

                });
            });


            return true;
        }
        return true;
    }

    private boolean executeInsert(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            Player player = Bukkit.getPlayer(args[1]);

            if (Objects.nonNull(player)) {
                sqlManager.createInsert("player")
                        .setColumnNames("name", "uuid")                 //需要插入的表名，顺序与下面的Params顺序一致
                        .setParams(args[1], player.getUniqueId())
                        .executeAsync();                                //非查询操作不需要build，直接execute | executeAsync

                sender.sendMessage("插入成功");
                return true;
            }

            sender.sendMessage("玩家不存在");
            return true;
        }
        return false;
    }

    private boolean executeUpdate(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            Player player = Bukkit.getPlayer(args[1]);
            if (Objects.nonNull(player)) {

                try {
                    //参数校验，不通过就直接捕获处理
                    Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("金额必须是数字");
                    return true;
                }

                // key: 字段名， value: 值
                LinkedHashMap<String, Object> infoMap = new LinkedHashMap<>();
                infoMap.put("name", player.getName());
                infoMap.put("uuid", player.getUniqueId());
                infoMap.put("money", Integer.parseInt(args[2]));

                sqlManager.createUpdate("player")
                        .setColumnValues(infoMap)                       //实现字段映射与数据
                        .setConditions("name = '" + args[1] + "'")      //where条件，String类型的数据记得像这样用 ' 单引号包裹住
                        .build()                                        //需要build得到SQLAction再execute | executeAsync
                        .executeAsync();

                sender.sendMessage("更新成功");
                return true;
            }
            sender.sendMessage("玩家不存在");
            return true;
        }
        return false;
    }

    private boolean executeDelete(CommandSender sender, String[] args) {
        if (args.length >= 2) {
            Player player = Bukkit.getPlayer(args[1]);
            sender.sendMessage("正在搜寻玩家：" + args[1]);
            if (Objects.nonNull(player)) {
                sqlManager.createDelete("player")
                        .setConditions("name = '" + args[1] + "'")
                        .build()
                        .executeAsync();
                sender.sendMessage("删除成功");
                return true;
            }
            sender.sendMessage("玩家不存在");
            return true;
        }
        return false;
    }
}
