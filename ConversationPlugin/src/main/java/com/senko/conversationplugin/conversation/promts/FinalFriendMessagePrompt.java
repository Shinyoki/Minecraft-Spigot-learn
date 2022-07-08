package com.senko.conversationplugin.conversation.promts;

import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

/**
 * 友好信息提示
 * @author senko
 * @date 2022/7/7 11:09
 */
public class FinalFriendMessagePrompt extends MessagePrompt {
    @Override
    protected Prompt getNextPrompt(ConversationContext context) {
        //不需要继续提出问题了
        return null;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        String animal = (String) context.getSessionData("animal");
        Integer number = (Integer) context.getSessionData("number");
        Player targetPlayer = (Player) context.getSessionData("player");

        //给对应玩家生成实体
        for (Integer i = 0; i < number; i++) {
            targetPlayer.getWorld().spawnEntity(targetPlayer.getLocation(), EntityType.valueOf(animal));
        }

        //获取问题的回复者
        Player forWhom = (Player) context.getForWhom();
        forWhom.playSound(forWhom.getLocation(), Sound.BLOCK_ANVIL_BREAK, 1, 1);

        return "成功生成了 " + number + "只" + animal + "给" + targetPlayer.getName() + "！";
    }
}
