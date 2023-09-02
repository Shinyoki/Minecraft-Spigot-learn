package github.shinyoki.particleshootplugin.listener.cmd;

import github.shinyoki.particleshootplugin.factory.BulletFactory;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GetBulletCmd implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // 添加物品
            PlayerInventory inventory = player.getInventory();
            if (inventory.firstEmpty() != -1) {
                ItemStack bullet = BulletFactory.getBullet();
                inventory.addItem(bullet);

                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);
                player.sendMessage("获得子弹" + bullet.getAmount() + "发");
            } else {
                player.sendMessage("背包已满");
            }
        } else {
            sender.sendMessage("该指令只能由玩家执行");
        }
        return true;
    }
}
