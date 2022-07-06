package com.senko.persistentplugin;

import com.senko.persistentplugin.data.LocalDateTimeDataTypeImpl;
import org.apache.commons.lang.StringUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@SuppressWarnings("all")
public final class PersistentPlugin extends JavaPlugin {

    /**
     * 设置几个会重复用到的NamespaceKey
     */
    private NamespacedKey playerName = new NamespacedKey(this, "username");
    private NamespacedKey playerUuid = new NamespacedKey(this, "playeruuid");
    private NamespacedKey curDateTime = new NamespacedKey(this, "curdatetime");

    /**
     * 不需要重复new出LocalDateTimeDataType
     */
    private PersistentDataType localDateTimeDataType = new LocalDateTimeDataTypeImpl();



    @Override
    public void onEnable() {
        getCommand("meta").setExecutor(this::onCommand);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length > 0) {
                Player player = (Player) sender;

                switch (args[0].toLowerCase()) {
                    case "add":
                        // /meta add
                        executeAdd(player);
                        return true;
                    case "get":
                        // /meta get
                        executeGet(player);
                        return true;
                    case "mark":
                        // /meta mark
                        executeMark(player);
                        return true;
                    case "query":
                        // /meta query
                        executeQuery(player);
                        return true;
                }

            }
            return false;
        }
        sender.sendMessage("必须是玩家才能使用该指令！");
        return true;
    }

    /**
     * 向手中的物品添加自定义数据
     */
    private void executeAdd(Player player) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (Objects.nonNull(itemInMainHand)) {
            ItemMeta itemMeta = itemInMainHand.getItemMeta();

            //有了meta，就可以调用#getPersistentDataContainer()方法得到持久层容器
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();

            /**
             * 涉及到NamespaceKey的，就一律不能重复
             */
            //在添加前，先删除
            if (dataContainer.has(playerName, PersistentDataType.STRING)) {
                //存在
                dataContainer.remove(playerName);
            }
            //添加playername
            dataContainer.set(playerName, PersistentDataType.STRING, player.getName());

            //玩家UUID
            if (dataContainer.has(playerUuid, PersistentDataType.STRING)) {
                dataContainer.remove(playerUuid);
            }
            dataContainer.set(playerUuid, PersistentDataType.STRING, player.getUniqueId().toString());

            /**
             * 如果我想要存储其他类型的数据？
             * 就得自己实现一个PersistentDataType
             */
            if (dataContainer.has(curDateTime, localDateTimeDataType)) {
                dataContainer.remove(curDateTime);
            }
            dataContainer.set(curDateTime, localDateTimeDataType, LocalDateTime.now(ZoneId.of("Asia/Shanghai")));


            //重新setMeta
            itemInMainHand.setItemMeta(itemMeta);

            player.sendMessage("向物品添加数据成功");
        } else {
            player.sendMessage("你的手里好歹拿个物品吧！");
        }
    }

    /**
     * 从玩家手中的物品里读取自定义数据
     * @param player
     */
    private void executeGet(Player player) {
        ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
        if (Objects.nonNull(itemInMainHand)) {
            ItemMeta itemMeta = itemInMainHand.getItemMeta();

            //有了meta，就可以调用#getPersistentDataContainer()方法得到持久层容器
            PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();

            String name = dataContainer.get(this.playerName, PersistentDataType.STRING);
            String uuid = dataContainer.get(this.playerUuid, PersistentDataType.STRING);
            LocalDateTime curTime = (LocalDateTime) (dataContainer.get(this.curDateTime, localDateTimeDataType));

            if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(uuid) && Objects.nonNull(curTime)) {
                player.sendMessage("读取到物品中的自定义信息！");
                player.sendMessage("所属玩家：" + name);
                player.sendMessage("UUID：" + uuid);
                player.sendMessage("标记时间：" + curTime);
            } else {
                player.sendMessage("该物品中不含自定义信息！");
            }

        } else {
            player.sendMessage("你的手里好歹拿个物品吧！");
        }
    }

    private void executeMark(Player player) {
        //先获取玩家视角准星对其的方块 (10个方块的距离)
        Block targetBlockExact = player.getTargetBlockExact(10);
        //空判断
        if (Objects.nonNull(targetBlockExact)) {
            //判断方块是不是可以存储信息的类型
            BlockState state = targetBlockExact.getState();
            if (state instanceof TileState) {
                //是
                TileState tileState = (TileState) state;
                //转型并获取dataContainer
                PersistentDataContainer dataContainer = tileState.getPersistentDataContainer();

                //=========================================
                //在添加前，先删除
                if (dataContainer.has(playerName, PersistentDataType.STRING)) {
                    //存在
                    dataContainer.remove(playerName);
                }
                //添加playername
                dataContainer.set(playerName, PersistentDataType.STRING, player.getName());

                //玩家UUID
                if (dataContainer.has(playerUuid, PersistentDataType.STRING)) {
                    dataContainer.remove(playerUuid);
                }
                dataContainer.set(playerUuid, PersistentDataType.STRING, player.getUniqueId().toString());

                if (dataContainer.has(curDateTime, localDateTimeDataType)) {
                    dataContainer.remove(curDateTime);
                }
                dataContainer.set(curDateTime, localDateTimeDataType, LocalDateTime.now(ZoneId.of("Asia/Shanghai")));
                //================================

                /**
                 * 方块的TileState和ItemMeta有点相似，设置完container后，也需要手动#update()更新
                 */
                tileState.update();

                player.sendMessage("向方块设置信息完成！");
            } else {
                //不是
                player.sendMessage("这个方块并不能存放信息捏~");
            }
        } else {
            player.sendMessage("你的视角内没有搜到方块捏，要不再靠近一点看看~");
        }

    }

    private void executeQuery(Player player) {
        //先获取玩家视角准星对其的方块 (10个方块的距离)
        Block targetBlockExact = player.getTargetBlockExact(10);
        //空判断
        if (Objects.nonNull(targetBlockExact)) {
            //判断方块是不是可以存储信息的类型
            BlockState state = targetBlockExact.getState();
            if (state instanceof TileState) {
                //是
                TileState tileState = (TileState) state;
                //转型并获取dataContainer
                PersistentDataContainer dataContainer = tileState.getPersistentDataContainer();

                //==============================
                String name = dataContainer.get(this.playerName, PersistentDataType.STRING);
                String uuid = dataContainer.get(this.playerUuid, PersistentDataType.STRING);
                LocalDateTime curTime = (LocalDateTime) (dataContainer.get(this.curDateTime, localDateTimeDataType));

                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(uuid) && Objects.nonNull(curTime)) {
                    player.sendMessage("读取到方块中的自定义信息！");
                    player.sendMessage("所属玩家：" + name);
                    player.sendMessage("UUID：" + uuid);
                    player.sendMessage("标记时间：" + curTime);
                } else {
                    player.sendMessage("该方块中不含自定义信息！");
                }
                //==============================

            } else {
                //不是
                player.sendMessage("这个方块并不能存放信息捏~");
            }
        } else {
            player.sendMessage("你的视角内没有搜到方块捏，要不再靠近一点看看~");
        }

    }
}
