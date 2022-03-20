# Command
- Command指令需要在`resources/config.yml`配置文件的`Commands属性`中声明，详情请看[这里](https://www.spigotmc.org/wiki/plugin-yml/#commands)。
- 接着我们需要实现`CommandExecutor`接口，复写里面的`onCommand()`函数。
- 最后在`Plugin`里通过`getCommand("commandName").setExecutor(urCommandsExecutor))`来完成指令绑定。
> `sender`可能是`Player`也可能是命令方块或控制台，请注意区分。

# CommandHandler

假设我定义了一个指令，`label`为 senko，我想以后设计很多子指令例如 help，GodMode，那么只需要输入 `/senko [help | GodMode]`即可，这时我们当然可以直接定义多个`CommandExecutor`实现类，然后到Plugin里与`plugin.yml`中声明的指令挨个绑定，但这样非常麻烦。

有没有一种方法，只需要定义一个label为senko，然后脱离配置文件来进行声明呢？



这里我们可以使用Handler设计模式。

> - `ICommand`抽象类
>
>     - 属性：
>         - CmdName：指令名称
>         - Params：指令参数
>         - Usage：指令介绍/教程
>
>     - 函数：
>         - `onCommand()`：被Handler统一调用
>         - `permission()`：指令权限
>
> - `ICommand`的实现类
>
>     - 函数：
>         - `constructor()`：通过super()将指令信息初始化。
>         - `onCommand()`实现
>         - `permission()`实现
>
> - `CommandHandler`：本身就是`TabExecutor`的实现类
>
>     - 属性
>         - 自己的单例
>         - 维护的ICommand指令
>     - 函数
>         - `showHelp()`
>         - `onCommand()`：根据args判断应调用哪条指令
>         - `onTabComplete()`：根据args判断应回应哪条指令作为返回值
>         - `init()`：初始化所维护的ICommand指令



现在我们只需要在`plugin.yml`中声明一个senko指令，然后将写好的ICommand子类，初始化到CommandHandler所维护的map中，在Plugin里通过`getCommand("senko").setExecutor(new CommandHandler())`。即可实现，多条子指令的一键声明绑定了。
