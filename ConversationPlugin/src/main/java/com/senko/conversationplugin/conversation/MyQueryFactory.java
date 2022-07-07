package com.senko.conversationplugin.conversation;

import com.senko.conversationplugin.ConversationPlugin;
import com.senko.conversationplugin.conversation.prefix.MyPromptPrefix;
import com.senko.conversationplugin.conversation.promts.FirstPrompt;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

/**
 * 创建并开启自定义会话
 *
 * @author senko
 * @date 2022/7/7 9:49
 */
public class MyQueryFactory {
    private static MyQueryFactory instance;

    /**
     * 问答会话 工厂
     */
    private static ConversationFactory conversationFactory;

    /**
     * 初始化问答会话工厂
     */
    private MyQueryFactory() {
        conversationFactory = new ConversationFactory(ConversationPlugin.getPlugin())
                //factory对象的playerOnlyMessage属性如果非空， 则会阻止控制台发起Conversation会话
                .thatExcludesNonPlayersWithMessage("控制台你这次别参与了捏~")
                //会话途中退出 用的语句
                .withEscapeSequence("quit")
                //第一次显示给（玩家|控制台）的问题（Prompt的翻译是提示，我感觉叫成问题更为合适）
                .withFirstPrompt(new FirstPrompt())
                //设置 每个Prompt问题的前缀
                .withPrefix(new MyPromptPrefix())
                //10秒后没有得到 （玩家|控制台）的回复，则关闭此次会话
                .withTimeout(10)
                //是否显示（玩家|控制台）的回复内容，默认为true
                .withLocalEcho(false)
                //是否阻塞非问答会话信息的展示(比如其他玩家的聊天、服务器公告等等)，默认为true
                .withModality(false);
    }

    /**
     * 获取单例
     */
    public static MyQueryFactory build() {
        if (instance == null) {
            instance = new MyQueryFactory();
        }
        return instance;
    }

    /**
     * 给玩家开启一次问答会话
     * @param player    玩家
     */
    public void start(Player player) {
        if (instance == null) {
            instance = new MyQueryFactory();
        }
        //Conversation对象创建后就直接使用，不需要实例化后再维护abandoned。服务端会根据超时时间自动关闭会话。
        conversationFactory.buildConversation(player).begin();
    }

/**
 * 如果没有给 factor添加这一条配置      .thatExcludesNonPlayersWithMessage("控制台你这次别参与了捏~")，
 * 这时候控制台也可以参与问答会话
 */
//    public void start(Conversable playerOrConsole) {
//        if (instance == null) {
//            instance = new MyQueryFactory();
//        }
//        conversationFactory.buildConversation(playerOrConsole).begin();
//    }

}
