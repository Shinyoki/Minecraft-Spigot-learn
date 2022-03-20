# Command
- Command指令需要在`resources/plugin.yml`配置文件的`Commands属性`中声明，详情请看[这里](https://www.spigotmc.org/wiki/plugin-yml/#commands) 。只有在配置文件里声明过的指令才能通过 `plugin.getCommand("cmdName")`获取得到。
- 接着我们需要实现`CommandExecutor`接口，复写里面的`onCommand()`函数。该函数默认返回`false`，返回false后插件会调用`plugin.yml`文件的`commands.myCommand.usage`属性作为玩家的错误输入提示。
- 最后在`Plugin`里通过`getCommand("commandName").setExecutor(urCommandsExecutor))`来完成指令与执行器的绑定。
> `sender`可能是`Player`也可能是命令方块或控制台，请注意区分。


# CommandHandler
如果想要针对一个主指令下添加多个分指令的场景做优化，我们可以采用责任链Handler的设计模式，这样就可以省去重复 `实现CommandExecutor、写入plugin.yml声明、plugin.getCommand().setExecutor()` 的繁琐过程，直接再Executor的构造函数中写入需使用的对象，然后调用对象的统一方法。

 

步骤：
1. 在`plugin.yml`中声明主指令`tc`
2. 声明一个`ICommand`抽象类，内部维护了基本的 private**指令名称、参数、详情**属性，抽象方法则是`public abstract bool onCommand(CommandSender sende, String[] args)`和`public abstract String permission()`。设置全参的构造方法，以及名称参数详情的getter方法。
2. 设计子类继承ICommand抽象类，实现抽象接口，设定无参构造器，内部记得调用super的全参构造器，写好一个指令对象应该具备的`信息和函数`。
2. 设计`ComandHandler`实现`TabExecutor`接口。内部维护自己的单例，以及commands集合。在构造函数里初始化好需要维护的ICommand实现类，并根据传入的`args`来决定调用哪个`ICommand`实现类。

> 由于已经在Plugin中绑定了Command与CommandExecutor，因此我们就无需考虑label参数，这里类似于网页前端的路由，plugin先根据router path匹配到了对应的Component，自动渲染了Componnet的内容，我们还考虑Component的名字对不对干嘛。

