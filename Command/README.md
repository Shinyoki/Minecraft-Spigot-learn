# Command
- Command指令需要在`resources/config.yml`配置文件的`Commands属性`中声明，详情请看[这里](https://www.spigotmc.org/wiki/plugin-yml/#commands)。
- 接着我们需要实现`CommandExecutor`接口，复写里面的`onCommand()`函数。
- 最后在`Plugin`里通过`getCommand("commandName").setExecutor(urCommandsExecutor))`来完成指令绑定。
> `sender`可能是`Player`也可能是命令方块或控制台，请注意区分。
