package com.senko.textcomponentplugin.executor;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.Keybinds;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * 按键绑定信息
 * @author senko
 * @date 2022/6/10 9:04
 */
public class PrintKeyBind implements CommandExecutor {


    /**
     * 玩家绑定的按键                      <br>
     * Spigot可以获取到玩家的 攻击、丢弃物品等快捷键所绑定的案件是什么。<br>
     * 按键key的类型可以参考 <a href="https://minecraft.fandom.com/zh/wiki/%E6%8E%A7%E5%88%B6">MC wiki</a>   以及{@link Keybinds}  <br>
     * 服务器可以根据客户端的 ”语言和绑定的按键” 来决定应该输出哪种语言的按键名称。
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            executeFun(player);

            return true;
        }
        return false;
    }

    private void executeFun(Player player) {
        KeybindComponent key1 = new KeybindComponent();
        key1.setKeybind(Keybinds.ATTACK);          //攻击，默认左键
        key1.addExtra("：攻击键");

        KeybindComponent key2 = new KeybindComponent();
        key2.setKeybind("key.drop");            //丢弃物品，默认Q键------ 我自定义的是R键  英文：r  中文：r
        key2.addExtra("：丢弃物品");

        KeybindComponent key3 = new KeybindComponent("key.playerlist");
        key3.addExtra(":玩家列表");          //玩家列表，默认Tab键---- 我定义的是`键

        player.spigot().sendMessage(key1);
        player.spigot().sendMessage(key2);
        player.spigot().sendMessage(key3);
    }
}
