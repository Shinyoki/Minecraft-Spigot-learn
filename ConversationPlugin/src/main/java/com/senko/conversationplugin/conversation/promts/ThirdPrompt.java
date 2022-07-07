package com.senko.conversationplugin.conversation.promts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.PlayerNamePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

/**
 * @author senko
 * @date 2022/7/7 10:55
 */
public class ThirdPrompt extends PlayerNamePrompt {

    /**
     * 由于需要判断玩家输入的名字能否找到对应的玩家，需要插件对象
     * @param plugin    插件对象
     */
    public ThirdPrompt(Plugin plugin) {
        super(plugin);
    }

    /**
     * 同样的，我又对父类的isInputValid感到不满意了，于是手动@Overrider这个方法，自己对input进行判断
     * @param context 存储 全局信息（当前的会话对象，插件，自定义信息）的上下文
     * @param input   玩家输入的内容
     * @return        校验结果
     */
    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        Player player = context.getPlugin().getServer().getPlayer(input);
        //如果玩家不存在，或玩家不在线，则校验失败
        if (Objects.isNull(player) || !player.isOnline()) {
            context.getForWhom().sendRawMessage("玩家不存在或不在线！");
            return false;
        }
        return true;
    }

    /**
     * 校验成功，开始处理最终结果，并通过返回null值来结束会话
     * @param context 存储 全局信息（当前的会话对象，插件，自定义信息）的上下文
     * @param input
     * @return
     */
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Player input) {
        /**
         * 可以在这里直接根据context存储的中间信息处理，然后return null结束会话
         *
         * 也可以跟我一样再返回一个MessagePrompt，提示个问题（友好信息）后返回null结束会话
         */
        context.setSessionData("player", input);
        return new FinalFriendMessagePrompt();
//        return Prompt.END_OF_CONVERSATION;
    }

    /**
     * 问题提示
     */
    @Override
    public String getPromptText(ConversationContext context) {
        return "请输入玩家的名字来给它生成" + context.getSessionData("number") + "只" + context.getSessionData("animal") + "：";
    }
}
