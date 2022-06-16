package com.senko.command;

import com.senko.command.commands.executors.GodCommandExecutor;
import com.senko.command.commands.handler.CommandHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Command extends JavaPlugin {
    private String name = "abc";
    @Override
    public void onEnable() {

        /**
         * 1. 在插件的描述plugin.yml文件中声明Command指令
         * 2. 编写指令的CommandExecutor执行器
         * 3. 在当前插件获取指令，并绑定CommandExecutor执行器。
         */

        this.getCommand("suicide").setExecutor(this);
        this.getCommand("god").setExecutor(new GodCommandExecutor());


        /**
         * tc子指令执行器，在CommandExecutor里维护多个自定义的指令，通过args[index]进行匹配调用。
         */
        this.getCommand("tc").setExecutor(new CommandHandler());

    }

    /**
     * /suicide 指令执行器
     * @param sender    指令的发送者：可以是玩家、远程控制台、本地控制台、命令方块、其他实现了Permissible接口的实现类
     * @param command   指令：就是this.getCommand("suicide")的返回值，存储着在plugin.yml中声明的各种指令信息
     * @param label     指令的名称：如/suicide的指令label就是 "suicide"
     * @param args      指令的参数：加入输入的完整指令是/suicide 1 2 3，那么该args数组就是 [1, 2, 3]
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        /**
         * 自杀指令：只有玩家能使用
         */
        if (label.equalsIgnoreCase("suicide")) {
            if (sender instanceof Player) {
                //输入指令的对象是Player玩家
                Player player = (Player) sender;
                //向全服玩家广播信息
                player.getServer().broadcastMessage(ChatColor.YELLOW + "再见了，世界！" +
                        "\n" + ChatColor.BLUE + player.getDisplayName() + ChatColor.YELLOW + "趋势了：/");
                player.setHealth(0);

                //return true是为了防止服务器给sender返回 指令的usage信息
                return true;
            }else {
                sender.sendMessage(ChatColor.YELLOW + "只有玩家能使用");
                return true;
            }
        }

        //返回false：自动调用plugin.yml配置的commands.suicide.usage值并返回给CommandSender指令发送者
        return false;
    }
}
