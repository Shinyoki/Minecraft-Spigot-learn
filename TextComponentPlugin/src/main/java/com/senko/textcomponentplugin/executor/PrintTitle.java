package com.senko.textcomponentplugin.executor;

import com.senko.textcomponentplugin.TextComponentPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 在玩家的屏幕中心展示一个标题和一个副标题
 *
 * @author senko
 * @date 2022/6/10 7:58
 */
public class PrintTitle implements CommandExecutor {

    /**
     *  屏幕中心区域的标题与副标题
     *  需要直接通过{@link org.bukkit.entity.Player#sendTitle(String, String, int, int, int)}来发送，
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;


            //标题： §l§6Area Discovered
            StringBuilder titleBuilder = new StringBuilder()
                    .append(ChatColor.BOLD).append(ChatColor.GOLD).append("Area Discovered");
            //副标题： §eRagni
            StringBuilder subTitle = new StringBuilder()
                    .append(ChatColor.YELLOW).append("Ragni");

            /**
             * 发送标题和副标题
             * 参数一：标题
             * 参数二：副标题
             * 参数三：渐入花费的时间（tick单位）
             * 参数四：标题停留的时间（tick单位）
             * 参数五：渐出花费的时间（tick单位）
             */
            player.sendTitle(titleBuilder.toString(), subTitle.toString(), 24, 60, 24);
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1,1);           //模仿wynncraft的区域发现音效

            return true;
        }
        return false;
    }
}
