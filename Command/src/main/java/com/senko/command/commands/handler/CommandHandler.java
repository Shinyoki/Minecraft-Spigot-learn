package com.senko.command.commands.handler;

import com.senko.command.commands.GodMode;
import com.senko.command.commands.Help;
import com.senko.command.commands.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.*;

public class CommandHandler implements TabExecutor {
    private static CommandHandler instance;

    /**
     * 维护的指令集合
     */
    private Map<String, ICommand> commands = new HashMap<>();

    public Map<String, ICommand> getCommands() {
        return commands;
    }

    /**
     * handler初始化构造器
     */
    public CommandHandler() {
        instance = this;
        initHandler();
    }

    public static CommandHandler getInstance() {
        return instance;
    }

    /**
     * 初始化指令集
     * 注意要使用小写，与发送者的指令进行匹配
     */
    private void initHandler() {
        commands.put("help", new Help());
        commands.put("godmode", new GodMode());
    }

    /**
     * 手动注册指令
     * @param command
     */
    public void registerCommand(ICommand command) {
        //command.setHandler(this);
        commands.put(command.getCmdName(), command);
    }

    /**
     * 使用帮助指令
     * @param sender
     */
    public void showHelp(CommandSender sender) {
        commands.get("help").onCommand(sender, new String[0]);
    }

    /**
     * 统一返回true，使用自定义的showHelp()方法。
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /**
         *  args是 /command 之后的参数，并不是指令本身，指令本身是label。所以args存在length == 0 或 null的情况。
         *  假设输入的是=》 /tc hello senko ，那么得到的args=> [hello, senko]，而我们需要调用的command就是hello,参数为senko。
         *  判断< 1是防止 args.length > Integer.MAX_SIZE导致变成负数
         */
        if (args == null || args.length < 1) {
            //showHelp用于只输入/tc 而没有指明具体指令的情况
            showHelp(sender);
            return true;
        }

        //mc输入的文字区分大小写
        ICommand cmd = commands.get(args[0].toLowerCase());
        try {
            if (cmd != null && sender.hasPermission(cmd.permission())) {
                //指令参数
                String[] params = new String[0];
                if (args.length >= 2) {
                    //用链表的removeFirst，删掉第指令，得到参数
                    LinkedList<String> list = new LinkedList<>(Arrays.asList(args));
                    list.removeFirst();
                    params = list.toArray(new String[list.size()]);
                }
                boolean res = cmd.onCommand(sender, params);
                if (!res) {
                    //使用 cmd 自身的说明，而非调用 showHelp()
                    sender.sendMessage(cmd.showUsage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sender.sendMessage(ChatColor.RED + "发生了异常：" + e.getMessage());
            return true;
        }
        return true;
    }

    /**
     * 玩家每输入一个字母都会被服务器响应
     * @param sender
     * @param command
     * @param alias
     * @param args
     * @return
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args == null || args.length < 1) {
            showHelp(sender);
            return null;
        }

        List<String> result = new ArrayList<>();
        //正在输第一个指令，如 /tc god...
        if (args.length == 1) {
            String typingStr = args[0].toLowerCase();
            for (String cmdName : commands.keySet()) {
                /**
                 * 如果正在输入的字母是正确指令的前缀，且玩家拥有对应指令的权限，就将指令名称拼接到结果里去
                 * 注意：这里并不是检测到一个符合就立马返回，而是返回符合前缀的指令集合
                 */
                if (cmdName.startsWith(typingStr)) {
                    ICommand cmd = commands.get(cmdName);
                    if (sender.hasPermission(cmd.permission())) {
                        result.add(cmdName);
                    }
                }
            }
        } else if (args.length > 1) {
            //获取指令参数
            String typingStr = args[1].toLowerCase();
            //得到第一个指令，查看对应参数
            ICommand cmd = commands.get(args[0].toLowerCase());

            //玩家可能会输错，找不到指令，那就不管了
            if (cmd != null) {
                sender.sendMessage(cmd.showUsage());
            }else {
                showHelp(sender);
                return  null;
            }

        }
        return result;
    }
}
