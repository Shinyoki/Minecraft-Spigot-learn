package com.senko.pluginwithdatabase.executor;

import com.senko.pluginwithdatabase.PluginWithDatabase;
import com.senko.pluginwithdatabase.entity.PlayerPO;
import com.senko.pluginwithdatabase.mapper.PlayerMapper;
import org.apache.ibatis.session.SqlSession;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

/**
 * 数据库交互指令
 *
 * @author senko
 * @date 2022/6/20 19:30
 */
public class DbCommand implements CommandExecutor {

    private PlayerMapper playerMapper;

    /**
     * 建立一次数据库会话
     *
     * @return 数据库会话
     */
    private SqlSession getSqlSession() {
        SqlSession sqlSession = null;
        try {
            sqlSession = PluginWithDatabase.getSqlSessionFactory().openSession();
        } catch (Exception e) {
            PluginWithDatabase.getInstance().getLogger().warning("数据库连接失败！无法建立会话！");
            PluginWithDatabase.getInstance().getLogger().warning("错误信息：" + e.getMessage());
        }
        return sqlSession;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        //获取链接
        boolean flag = false;
        if (args.length >= 1) {
            switch (args[0]) {
                case "query":
                    // /db-mybatis query <playerName>
                    flag = executeQuery(sender, args);
                    break;
                case "insert":
                    // /db-mybatis insert <playerName>
                    flag = executeInsert(sender, args);
                    break;
                case "update":
                    // /db-mybatis update <playerName> <money>
                    flag = executeUpdate(sender, args);
                    break;
                case "delete":
                    flag = executeDelete(sender, args);
                    break;
            }
        }

        return flag;
    }

    /**
     * 查询指令
     * /db-mybatis query <playerName>
     */
    private boolean executeQuery(CommandSender sender, String... args) {
        // /db-mybatis query <playerName>
        if (args.length >= 2) {
            //当前会话
            SqlSession sqlSession = getSqlSession();
            PlayerMapper playerMapper = sqlSession.getMapper(PlayerMapper.class);

            /**
             * 异步调用数据库
             *
             * 如果有从数据库获取数据并处理的需求，
             * 需使用BukkitScheduler#runTask()回到主线程再执行
             */
            //查询
            Bukkit.getScheduler().runTaskAsynchronously(PluginWithDatabase.getInstance(), () -> {
                //异步查询
                PlayerPO playerPO = playerMapper.selectOneByName(args[1]);
                //查询完成，进入主线程调用Spigot API
                Bukkit.getScheduler().runTask(PluginWithDatabase.getInstance(), () -> {
                    if (Objects.nonNull(playerPO)) {
                        sender.sendMessage("查询结果：" + playerPO.toString());
                    } else {
                        sender.sendMessage("查询结果：查无此人！");
                    }

                    /**
                     * 再最后一步记得关闭会话
                     */
                    sqlSession.commit();
                    sqlSession.close();
                });
            });
        }
        return true;
    }

    /**
     * /db-mybatis insert <playerName>
     * 插入玩家数据
     */
    private boolean executeInsert(CommandSender sender, String... args) {
        // /db-mybatis insert <playerName>
        if (args.length >= 2) {
            //创建会话
            SqlSession sqlSession = getSqlSession();
            playerMapper = sqlSession.getMapper(PlayerMapper.class);

            //异步插入
            Bukkit.getScheduler().runTaskAsynchronously(PluginWithDatabase.getInstance(), () -> {
                PlayerPO oldPlayer = playerMapper.selectOneByName(args[1]);

                //调用API 回到主线程
                Bukkit.getScheduler().runTask(PluginWithDatabase.getInstance(), () -> {
                    if (Objects.nonNull(oldPlayer)) {
                        sender.sendMessage("插入失败：玩家已存在！");
                        return;
                    } else {
                        Player spigotPlayer = Bukkit.getPlayer(args[1]);
                        //创建玩家对象
                        if (Objects.isNull(spigotPlayer)) {
                            sender.sendMessage("插入失败：玩家不在线！");
                            return;
                        } else {
                            //插入玩家对象
                            //id主键自增、money有默认值0，因此可以传NULL值
                            PlayerPO playerPO = new PlayerPO(null, spigotPlayer.getName(), spigotPlayer.getUniqueId().toString(), null);
                            sender.sendMessage("插入玩家：" + playerPO.toString());
                            int i = playerMapper.insertPlayer(playerPO);
                            Bukkit.getScheduler().runTask(PluginWithDatabase.getInstance(), () -> {
                                if (i > 0) {
                                    sender.sendMessage("插入成功！" + i);
                                } else {
                                    sender.sendMessage("插入失败！");
                                }

                                /**
                                 * 再最后一步记得关闭会话
                                 */
                                sqlSession.commit();
                                sqlSession.close();
                            });
                        }
                    }
                });
            });
        }
        return true;
    }

    /**
     * /db-mybatis update <playerName> <money>
     * 修改玩家数据
     */
    private boolean executeUpdate(CommandSender sender, String... args) {
        // /db-mybatis update <playerName> <money>
        if (args.length >= 3) {
            //创建会话
            SqlSession sqlSession = getSqlSession();
            playerMapper = sqlSession.getMapper(PlayerMapper.class);
            Player spigotPlayer = Bukkit.getPlayer(args[1]);

            //校验参数
            if (Objects.isNull(spigotPlayer)) {
                sender.sendMessage("修改失败：玩家不在线！");
                return false;
            }
            try {
                Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage("修改失败：金额必须是数字！");
                return false;
            }

            //异步修改
            Bukkit.getScheduler().runTaskAsynchronously(PluginWithDatabase.getInstance(), () -> {
                //异步查询
                PlayerPO oldPlayerPO = playerMapper.selectOneByName(args[1]);
                //更新PO对象数据
                oldPlayerPO.setName(spigotPlayer.getName());
                oldPlayerPO.setUuid(spigotPlayer.getUniqueId().toString());
                oldPlayerPO.setMoney(Integer.parseInt(args[2]));
                //更新数据
                int i = playerMapper.updatePlayerById(oldPlayerPO);

                //回到主线程
                Bukkit.getScheduler().runTask(PluginWithDatabase.getInstance(), () -> {
                    if (i > 0) {
                        sender.sendMessage("修改成功！");
                    } else {
                        sender.sendMessage("修改失败！");
                    }

                    /**
                     * 再最后一步记得关闭会话
                     */
                    sqlSession.commit();
                    sqlSession.close();
                });
            });
        }
        return true;
    }

    /**
     * /db-mybatis delete <playerName>
     * 删除玩家数据
     */
    private boolean executeDelete(CommandSender sender, String... args) {
        // /db-mybatis delete <playerName>
        if (args.length >= 2) {
            //创建会话
            SqlSession sqlSession = getSqlSession();
            playerMapper = sqlSession.getMapper(PlayerMapper.class);

            //异步删除
            Bukkit.getScheduler().runTaskAsynchronously(PluginWithDatabase.getInstance(), () -> {
                int i = playerMapper.deletePlayer(args[1]);

                //回到主线程
                Bukkit.getScheduler().runTask(PluginWithDatabase.getInstance(), () -> {
                    if (i > 0) {
                        sender.sendMessage("删除成功！");
                    } else {
                        sender.sendMessage("删除失败！");
                    }

                    /**
                     * 再最后一步记得关闭会话
                     */
                    sqlSession.commit();
                    sqlSession.close();
                });
            });
        }
        return true;
    }
}