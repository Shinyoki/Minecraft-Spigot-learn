package com.senko.customeventplugin.pojo;

import com.senko.customeventplugin.enums.EnumBowMode;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.LinkedList;
import java.util.List;

public class GodBow {
    private ItemStack bow;
    private EnumBowMode mode = EnumBowMode.PUNCH;

    public ItemStack getBow() {
        return bow;
    }

    public EnumBowMode getMode() {
        return mode;
    }

    public GodBow(ItemStack bow) {
        this.bow = bow;
        this.mode = updateBowInfo();
    }

    public GodBow(ItemStack bow, EnumBowMode mode) {
        this.bow = bow;
        this.mode = mode;
        this.mode = updateBowInfo();
    }

    protected EnumBowMode updateBowInfo() {
        return updateBowInfo(this.bow);
    }

    protected EnumBowMode updateBowInfo(ItemStack bow) {
        ItemMeta bowMeta = bow.getItemMeta();
        List<String> lore = bowMeta.getLore();

        StringBuilder sb = new StringBuilder();
        lore.stream().forEach(e->{
            int index = e.lastIndexOf(']');
            if(index != -1) {
                sb.append(e.substring(index - 1, index));
                System.out.println("得到的弓的类型：" + sb.toString());
                Integer i = Integer.parseInt(sb.toString());
                if (i == null) {
                    return;
                }
                mode = EnumBowMode.values()[i - 1];
            }
        });

        return mode;
    }

    public static EnumBowMode getBowInfo(ItemStack bow) {
        ItemMeta bowMeta = bow.getItemMeta();
        List<String> lore = bowMeta.getLore();
        StringBuilder sb = new StringBuilder();
        EnumBowMode res = null;
        for (String s : lore) {
            int index = s.lastIndexOf(']');
            if(index != -1) {
                sb.append(s.substring(index - 1, index));
                System.out.println("得到的弓的类型：" + sb.toString());
                Integer i = Integer.parseInt(sb.toString());
                if (i == null) {
                    return res;
                }
                res = EnumBowMode.values()[i - 1];
            }
        }

        return res;
    }

    public void changeBowMode() {
        System.out.println("初始mode：" + mode.getMode());
        Integer index = this.mode.getMode();
        //更新维护的属性
        mode = EnumBowMode.values()[(index + 1) % EnumBowMode.values().length];
        ItemMeta bowMeta = this.bow.getItemMeta();
        List<String> lore = bowMeta.getLore();
        for(int i = 0; i < lore.size(); i++) {
            if (lore.get(i).startsWith(ChatColor.GREEN + "当前模式：")) {
                String resultLore = ChatColor.GREEN + "当前模式：" + ChatColor.YELLOW + mode.getModeName() + ChatColor.GRAY + "[" + (mode.getMode() + 1) + "]";
                lore.set(i, resultLore);
            }
        }
        //更新弓的lore
        bowMeta.setLore(lore);

        //更新弓的附魔
        System.out.println("当前弓的模式：" + mode.getModeName() + "  " + mode.getMode());
        if (mode != EnumBowMode.PUNCH) {
            //不是击退模式
            System.out.println("不是击退模式，应该删除");
            bowMeta.removeEnchant(Enchantment.ARROW_KNOCKBACK);
        } else {
            //是击退模式
            if (bowMeta.getEnchantLevel(Enchantment.ARROW_KNOCKBACK) == 0) {
                //无附魔
                bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, false);
            }
        }

        bow.setItemMeta(bowMeta);
    }

    /**
     * 发一把神弓
     * @return
     */
    public static ItemStack createGodBow() {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta bowMeta = bow.getItemMeta();
        bowMeta.setDisplayName(ChatColor.GREEN + "神弓");
        bowMeta.addEnchant(Enchantment.DURABILITY, 1,false);
        bowMeta.addEnchant(Enchantment.ARROW_KNOCKBACK, 1, false);

        LinkedList<String> lores = new LinkedList<>();
        lores.add(ChatColor.AQUA + "模式1：击退");
        lores.add(ChatColor.AQUA + "模式2：雷击");
        lores.add("\n");
        lores.add(ChatColor.GREEN + "当前模式：" + ChatColor.YELLOW + "击退" + ChatColor.GRAY + "[1]");
        lores.add("\n");
        lores.add(ChatColor.GOLD + "左键切换模式!");
        bowMeta.setLore(lores);

        bow.setItemMeta(bowMeta);
        return bow;
    }

}
