package com.senko.conversationplugin.conversation.promts;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.EntityType;

/**
 * 第一个问题：选择需要生成的生物[COW, PIG, CAT, NONE]
 * @author senko
 * @date 2022/7/7 10:14
 */
public class FirstPrompt extends FixedSetPrompt {
    /**
     * 指出需要的 答案
     */
    public FirstPrompt() {
        //传入Fixed固定的答案
        super(EntityType.COW.toString(), EntityType.PIG.toString(), EntityType.CAT.toString(), "NONE");
    }


    /**
     * 输入正确回复 的后处理
     * @param context   存储 全局信息（当前的会话对象，插件，自定义信息）的上下文
     * @param input     与super()里添加的默认文本相同的input，可惜这里的input是匹配后的，如果想自己来验证，可以Override 父类的isInputValid方法
     * @return          下一个问题，如果return null则相当于关闭了会话
     */
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        /**
         * 这里的input只会是 构造方法里传入的那些String，
         * 如果想自己来验证input是否匹配构造方法里传入的默认参数，可以Override 父类的isInputValid方法
         */
        if (input.equalsIgnoreCase("none")) {
            //同等与 return null
            return Prompt.END_OF_CONVERSATION;
        }
        //通过context保存自定义中间信息，方便在以后的问答会话中获取
        context.setSessionData("animal", input);
        return new SecondPrompt();
    }

    /**
     * 一开始给出的提示
     * @param context   存储 全局信息（当前的会话对象，插件，自定义信息）的上下文
     * @return          当前问题的提示
     */
    @Override
    public String getPromptText(ConversationContext context) {
        //由于是第一个问题：我们暂时用不到context来获取什么信息
        return "请选择需要生成的动物：" + ChatColor.WHITE +  this.formatFixedSet();
    }
}
