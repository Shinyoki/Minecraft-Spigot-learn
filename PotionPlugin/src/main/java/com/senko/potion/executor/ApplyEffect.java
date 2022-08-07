package com.senko.potion.executor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

/**
 * @author senko
 * @date 2022/8/7 20:19
 */
public class ApplyEffect implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && args.length >= 1) {
            Player player = (Player) sender;

            PotionEffect potionEffect = null;
            switch (args[0].toLowerCase()) {
//                枚举
                case "enum":
                    potionEffect = getPotionEffectFromEnum();
                    break;
                case "new":
                    potionEffect = getPotionEffectFromNew();
                    break;
                default:
                    sender.sendMessage("未知的命令");
                    return true;
            }

            // 给实体赋予药水效果
//            potionEffect.apply(player);           // 两个是同一个方法
            player.addPotionEffect(potionEffect);
            return true;
        }
        sender.sendMessage("只有玩家才能使用当前指令！");
        return true;

    }

    private PotionEffect getPotionEffectFromEnum() {

        // 挑选一个枚举值，得到PotionEffectType
        PotionEffectType potionEffectType = PotionType.SPEED.getEffectType();
        // 利用PotionEffectType默认的参数new出一个PotionEffect
        return potionEffectType.createEffect(20 * 10, 5);

    }

    private PotionEffect getPotionEffectFromNew() {

        /**
         * 直接new出一个PotionEffect
         * 第一个参数：PotionEffectType           药水效果
         * 第二个参数：duration 20 * 10 ticks     持续时间
         * 第三个参数：amplifier [0~XXX]          药水等级，0：一级，1：二级...
         * 第四个参数：ambient [true|false]       true时，生物周围的药水粒子更少，颜色更透明
         * 第五个参数：particles [true|false]     true时，生物周围会有粒子效果
         * 第六个参数：showIcon [true|false]      false时，玩家的主界面不会出现药水图标
         */
        return new PotionEffect(PotionEffectType.SPEED, 20 * 10, 0, false, true, true);

    }

}
