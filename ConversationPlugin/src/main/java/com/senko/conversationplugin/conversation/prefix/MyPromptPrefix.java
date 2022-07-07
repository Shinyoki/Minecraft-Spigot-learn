package com.senko.conversationplugin.conversation.prefix;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NullConversationPrefix;

import java.util.Map;

/**
 * 自定义问答的 前缀
 * @author senko
 * @date 2022/7/7 10:39
 */
public class MyPromptPrefix extends NullConversationPrefix {
    @Override
    public String getPrefix(ConversationContext context) {
        //从上下文中获取信息集合
        Map<Object, Object> infoMap = context.getAllSessionData();

        if (!infoMap.containsKey("animal")) {
            //还不包含 animal时，肯定是第一个问题
            return ChatColor.GREEN +  "问题一：" + ChatColor.WHITE;
        }else if (!infoMap.containsKey("number")) {
            return ChatColor.GREEN +  "问题二：" + ChatColor.WHITE;
        } else if (!infoMap.containsKey("player")) {
            return ChatColor.GREEN +  "问题三：" + ChatColor.WHITE;
        }

        return "";
    }
}
