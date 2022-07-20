package com.senko.blockplugin.examples.blockstate;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;

/**
 * 方块状态的示例
 * @author senko
 * @date 2022/7/15 10:27
 */
public class BlockStateExample {

    public void doGetBlockState(Block block, Player player) {

        // 如果被点击的方块是命令方块
        if (block.getType() == Material.COMMAND_BLOCK) {

            /**
             * BlockState是个基类，每个Block都具有，
             * 但是对于一些特殊的，可以存放信息的方块，比如命令方块，告示牌，箱子等，
             * 则有相应的BlockState的子类，比如{@link CommandBlock}，{@link org.bukkit.block.Sign}，{@link org.bukkit.block.Chest}等，
             * (注意，对于部分方块，他们存在同名的接口。比如{@link org.bukkit.block.Chest} 和 {@link org.bukkit.block.data.type.Chest},
             * 一个是BlockState的子类，一个是BlockData的子类
             *
             * 对于这些特殊方块，我们可以对BlockState其转型，然后获取其中的信息。
             *
             * API注解没提示到，这里返回的State对象是一个副本，你针对副本的修改不会立即生效。
             * 而且这意味着其它插件可能会更改方块的状态, 而你的插件不会知道其变更;
             * 或者其它插件可能会将方块更改为另一种类型, 从而导致你的BlockState失效.
             * 所以需要在最后通过 {@link BlockState#update()} 来更新方块的状态.
             */
            CommandBlock state = (CommandBlock) block.getState();                       // 转型为子类

            /**
             * 比如在这，我们就可以获取到命令方块的命令。
             * 这些方块内部的详细数据，都是由{@link org.bukkit.block.BlockState}来存储的。
             * 并不是{@link org.bukkit.block.data.BlockData}
             */
            String command = state.getCommand();                                        // 获取命令方块里的命令
            player.sendMessage("你点击的方块的命令是：" + command);

            /**
             * 同时我们还可以修改BlockState，但是任何有关BlockState的修改操作，
             * 都需要调用{@link BlockState#update()} 来完成对方块的更新。
             * 这也是为什么在第#14期视频里，我们用到了该方法。
             *
             * 这种做法也会导致触发方块更新，因此update方法也有个applyPhysics参数
             * {@link BlockState#update(boolean, boolean)}
             */
            state.setCommand("/say hello");                                             // 修改BlockState
            player.sendMessage("现在它的命令变成/say hello了，你看看是不是。");
            state.update();                                                             // 更新BlockState
            // BlockState的更新也可以触发方块更新，因此update也有个带有applyPhysic参数的同名方法
//            state.update(true, false);                                                // 强制更新方块状态，并不会引发周围方块的方块更新

        } else {
            player.sendMessage("你点击的方块不是命令方块");
        }

    }

}
