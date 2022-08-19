package com.senko.maptutorialplugin.render;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

public class FirstRender extends MapRenderer {

    public FirstRender() {
        /**
         * 不分开渲染
         */
        super(false);

        try {
            image = ImageIO.read(new URL("https://www.baidu.com/img/bd_logo1.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Image image;

    private MapCursorCollection cursors = new MapCursorCollection();

    {
        MapCursor cursor1 = new MapCursor((byte) 0, (byte) 0, (byte) 4, MapCursor.Type.MANSION, true);
        MapCursor cursor2 = new MapCursor((byte) 0, (byte) 0, (byte) 0, MapCursor.Type.BANNER_PURPLE, true);
        cursors.addCursor(cursor1);
        cursors.addCursor(cursor2);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * 高频率调用，所以请务必不要在这里进行很耗时的操作
     */
    @Override
    public void render(MapView map, MapCanvas canvas, Player player) {
//        if (null != player) {
//            Bukkit.broadcastMessage(player.getName() + "玩家，你好！现在是：" + System.currentTimeMillis());
//        } else {
//            Bukkit.broadcastMessage("游客，你好！现在是：" + System.currentTimeMillis());
//        }

        /**
         * 需改为128 X 128 像素大小的图片
         */
        canvas.drawImage(0, 0, image);
        /**
         * 在View中渲染英文字符（不要带有MinecraftFont.Font中不存在的字符）
         */
        canvas.drawText((int) player.getLocation().getX(), (int) player.getLocation().getZ(), MinecraftFont.Font, "Hello World!");

        MapCursor cursor1 = cursors.getCursor(0);   // 宅邸
        MapCursor cursor2 = cursors.getCursor(1);   // 旗帜

        Location senkosan = Bukkit.getPlayer("Senkosan").getLocation();
        byte x = (byte) senkosan.getX();
        byte y = (byte) senkosan.getZ();

        Location iLiveOnFaith = Bukkit.getPlayer("iLiveOnFaith").getLocation();
        byte x2 = (byte) iLiveOnFaith.getX();
        byte y2 = (byte) iLiveOnFaith.getZ();

        cursor1.setX(x);
        cursor1.setY(y);

        cursor2.setX(x2);
        cursor2.setY(y2);

        canvas.setCursors(cursors);
    }

}
