package com.senko.conversationplugin.conversation.promts;

import com.senko.conversationplugin.ConversationPlugin;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

/**
 * 第二个问题：需要生成 X 只 XX 生物？
 * @author senko
 * @date 2022/7/7 10:31
 */
public class SecondPrompt extends NumericPrompt {

    /**
     * 自己重载了这个方法，用来手动校验数字是否正确
     *
     * 不是所有的方法都满足我们的需求，有需求就自己Override或写个自己的方法
     * @param context   存储 全局信息（当前的会话对象，插件，自定义信息）的上下文
     * @param input     玩家给出的输入
     * @return          校验结果，true表示校验通过，false表示校验不通过
     */
    @Override
    protected boolean isNumberValid(ConversationContext context, Number input) {
        //如果玩家输入的数字小于等于0，则认为输入错误
        return input.intValue() >= 1 && input.intValue() <= 10;
    }

    /**
     * 输入校验成功 的后处理
     * @param context 存储 全局信息（当前的会话对象，插件，自定义信息）的上下文
     * @param input   校验成功的数字
     * @return        下一个提示对象，返回null则关闭会话
     */
    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        //存储数字到上下文中
        context.setSessionData("number", input.intValue());
        return new ThirdPrompt(ConversationPlugin.getPlugin());
    }

    /**
     * 提示信息
     * @param context 存储 全局信息（当前的会话对象，插件，自定义信息）的上下文
     * @return        提示信息
     */
    @Override
    public String getPromptText(ConversationContext context) {
        return "你要生成几只" + context.getSessionData("animal") + "呢？(允许数量：[1 ~ 10])";
    }
}
