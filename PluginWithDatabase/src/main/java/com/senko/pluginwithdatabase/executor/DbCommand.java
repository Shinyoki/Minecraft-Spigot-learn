package com.senko.pluginwithdatabase.executor;

import com.senko.pluginwithdatabase.PluginWithDatabase;
import com.senko.pluginwithdatabase.entity.PlayerPO;
import com.senko.pluginwithdatabase.mapper.PlayerMapper;
import com.sun.istack.internal.NotNull;
import org.apache.ibatis.session.SqlSession;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

/**
 * 数据库交互指令
 * @author senko
 * @date 2022/6/20 19:30
 */
public class DbCommand implements CommandExecutor {

    private PlayerMapper playerMapper;

    private SqlSession sqlSession;

    private void setPlayerMapper() {
        sqlSession = PluginWithDatabase.getSqlSessionFactory().openSession();
        playerMapper = sqlSession.getMapper(PlayerMapper.class);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //获取链接
        setPlayerMapper();
        boolean flag = false;

        flag = execute(sender, args);

        //关闭链接
        sqlSession.close();
        return flag;
    }

    private boolean execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            PlayerPO playerPO = playerMapper.selectOneByName(args[0]);
            sender.sendMessage(args[0]);
            if (Objects.nonNull(playerPO)) {
                sender.sendMessage(playerPO.toString());
            } else {
                sender.sendMessage(args[0] + " is not exist");
                return false;
            }
        }
        return true;
    }


}
