package com.senko.maptutorialplugin;

import com.senko.maptutorialplugin.render.FirstRender;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.bukkit.plugin.java.JavaPlugin;



public final class MapTutorialPlugin extends JavaPlugin {

    private MapView view;

    {
        /**
         * 给Map添加View视图
         *
         * 通过Bukkit的createMap来生成一个MapView
         */
        view = Bukkit.createMap(Bukkit.getWorlds().get(0));

        // 修改视图的信息
        view.setScale(MapView.Scale.CLOSEST);
        view.setLocked(true);
        view.setTrackingPosition(true);         // 是否追踪玩家，并显示玩家的cursor          true: 显示Cursor，false: 不显示Cursor
        view.setUnlimitedTracking(false);       // 是否在玩家离开View时仍然标注玩家的cursor    true: 标注玩家的cursor，false: 不标注玩家的cursor

        // 删除原有用于渲染世界的Render
        view.getRenderers().forEach(view::removeRenderer);
        // 添加自己的render
        view.addRenderer(new FirstRender());
    }

    @Override
    public void onEnable() {
        getCommand("get").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            PlayerInventory inventory = player.getInventory();

            // 直接new出Map的物品槽
            ItemStack itemStack = new ItemStack(Material.FILLED_MAP, 1);
            // 既然是可以展示画面的特殊物品，相比也有个对应的Meta
            MapMeta mapMeta = (MapMeta) itemStack.getItemMeta();

            // 设置meta信息
            mapMeta.setLocationName("这是地点：" + player.getLocation().getBlockX() + "," + player.getLocation().getBlockY() + "," + player.getLocation().getBlockZ());
            mapMeta.setColor(Color.fromRGB(255, 0, 0));

            // 以后获取到的都是相同的View
            mapMeta.setMapView(view);

            // 查看默认生成的View信息
            int id = view.getId();
            int centerX = view.getCenterX();
            int centerZ = view.getCenterZ();
            MapView.Scale scale = view.getScale();
            sender.sendMessage("视图id：" + id);
            sender.sendMessage("视图中心点X坐标：" + centerX);
            sender.sendMessage("视图中心点Z坐标：" + centerZ);
            sender.sendMessage("视图缩放比例：" + scale);

            // 获取代码创建的Map信息
            String locationName = mapMeta.getLocationName();
            sender.sendMessage("locationName: " + locationName);

            Color color = mapMeta.getColor();
            sender.sendMessage("color: " + color);


            itemStack.setItemMeta(mapMeta);
            inventory.addItem(itemStack);
            return true;
        }
        sender.sendMessage("只有玩家才能使用此命令");
        return true;

    }

}