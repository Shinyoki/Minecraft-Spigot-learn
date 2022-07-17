package com.senko.invokethirdpluginapi;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

/**
 * Vault API官方教程
 * https://github.com/MilkBowl/VaultAPI
 *
 * 调用第三方插件API的步骤：
 * （一）: 给当前项目导入对应插件的jar包作为依赖
 *      这是为了能够在编写代码时能够正常调用第三方插件的API接口。
 * （二）：在plugin.yml中声明 softdepend 或者 depend
 *      这里的depend就是所谓的“前置插件”。
 *      softdepend所声明的前置插件，仅仅是起到提示作用，如果服务端没有检测到对应的前置插件，也不会对当前的插件产生任何影响。
 *      depend所声明的前置插件，如果服务端没有检测到对应的前置插件，则会关闭当前插件。
 * （三）：调用第三方插件API接口
 *      每个插件的API都不一样，得自行去相应的官网寻找教程，
 *      （又或者是自行阅读源码理解如何使用。
 * （三）：打包依赖
 *      就像是打包SpigotAPI依赖一样，
 *      SpigotAPI和我们要用到的第三方API插件都已经被服务端加载好了，
 *      等我们的插件被加载进服务器时，就可以直接调用第三方插件的API接口，
 *      因此我们无需将第三方插件的jar包一同打包进我们的插件中。
 */
public final class InvokeThirdPluginAPI extends JavaPlugin {

    /**
     * Vault API提供的经济服务接口
     */
    public static Economy econ = null;

    @Override
    public void onEnable() {

        getCommand("senko").setExecutor(this);

        // 判断Vault前置插件是否存在（如果plugin.yml中已经声明了 soft: [Vault] 则不需要有这一步）
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().info("没有发现Vault前置，本插件无法继续使用！");
            // 禁用插件
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // 初始化经济服务
        if (false == setupEconomy()) {
            // VaultAPI没有发现其他 经济插件
            getLogger().severe("我这个插件就是要调用经济API的，没了Eco服务我还运行干嘛！");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

    }

    private boolean setupEconomy() {
        // 获取Economy服务（说简单点，就是看看有没有EssentialX等经济插件被加载了，如果有，VaultAPI就能够提供对应的经济服务）
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager()
                .getRegistration(Economy.class);
        econ = rsp.getProvider();
        return Objects.nonNull(econ);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            getLogger().info("只有玩家才能这么做");
            return true;
        }

        Player player = (Player) sender;
        if (args.length > 0) {
            switch (args[0].toLowerCase()) {
                case "add":
                    //   /senko add <金额>      添加金额到玩家的钱包
                    return doAdd(player, args);
                case "remove":
                    //   /senko remove <金额>   从玩家的钱包中移除金额
                    return doRemove(player, args);
                case "get":
                    //   /senko get             获取玩家的钱包金额
                    return doGet(player, args);
            }
        }
        return false;
    }

    private boolean doAdd(Player player, String... args) {
        if (args.length < 2) {
            player.sendMessage("你忘了在后面输入金额!");
            return false;
        }
        try {
            int amount = Integer.parseInt(args[1]);
            player.sendMessage("将要给你加" + amount + "元");

            // Economy#depositePlayer(Player, int) 方法用于将金额添加到玩家的钱包中
            EconomyResponse response = econ.depositPlayer(player, amount);

            if (response.transactionSuccess()) {
                // Economy#getBalance(Player) 方法用于获取玩家的钱包金额
                player.sendMessage("你现在有" + econ.getBalance(player) + "元");
                return true;
            } else {
                player.sendMessage(response.errorMessage);
                return false;
            }
        } catch (NumberFormatException e) {
            player.sendMessage("你输入的金额不是数字!");
            return false;
        }
    }

    private boolean doRemove(Player player, String... args) {
        if (args.length < 2) {
            player.sendMessage("你忘了在后面输入金额!");
            return false;
        }
        try {
            int amount = Integer.parseInt(args[1]);
            player.sendMessage("将要从你扣除" + amount + "元");

            // Economy#withdrawPlayer(String, int) 方法用于从玩家的钱包中移除金额
            EconomyResponse response = econ.withdrawPlayer(player, amount);

            if (response.transactionSuccess()) {
                player.sendMessage("你现在有" + econ.getBalance(player) + "元");
                return true;
            } else {
                player.sendMessage(response.errorMessage);
                return false;
            }
        } catch (NumberFormatException e) {
            player.sendMessage("你输入的金额不是数字!");
            return false;
        }
    }

    private boolean doGet(Player player, String... args) {
        player.sendMessage("你现在有" + econ.getBalance(player) + "元");
        return true;
    }
}
