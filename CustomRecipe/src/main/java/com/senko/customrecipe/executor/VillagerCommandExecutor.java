package com.senko.customrecipe.executor;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * 生成一只特殊的村民
 */
public class VillagerCommandExecutor implements CommandExecutor {
    /**
     *  卧槽，idea可以在注解中插入图片, 真是惊了（看不了就对着函数名ctrl+q）<br>
     *  <img  data-src="https://i1.hdslb.com/bfs/face/792151598e6d1797c68709b97c6ec477cac042bd.jpg@240w_240h_1c_1s.webp" alt="" src="https://i1.hdslb.com/bfs/face/792151598e6d1797c68709b97c6ec477cac042bd.jpg@240w_240h_1c_1s.webp">
     * @param sender
     * @param command
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equalsIgnoreCase("villager")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                World world = player.getWorld();
                Location location = player.getLocation();

                Villager villager = ((Villager) world.spawnEntity(location, EntityType.VILLAGER));
                //修改村民的信息
                villager.setAI(false);
                villager.setCustomName("奸商YOHO");
                villager.setProfession(Villager.Profession.FARMER);


                //交易集合
                LinkedList<MerchantRecipe> recipes = new LinkedList<>();

                //结果1
                ItemStack result1 = new ItemStack(Material.DIAMOND_BLOCK, 2);
                //交易1
                MerchantRecipe recipe1 = new MerchantRecipe(result1, 3);
                //交易需要的材料 集合
                LinkedList<ItemStack> materials1 = new LinkedList<>();
                ItemStack itemStack = new ItemStack(Material.DIRT, 3);
                materials1.add(itemStack);
                recipe1.setIngredients(materials1);
                //加入配方1
                recipes.add(recipe1);

                //结果2
                ItemStack result2 = new ItemStack(Material.FEATHER, 2);
                MerchantRecipe recipe2 = new MerchantRecipe(result2, 1);
                LinkedList<ItemStack> materials2 = new LinkedList<>();
                ItemStack m1 = new ItemStack(Material.DIAMOND_BLOCK, 1);
                ItemStack m2 = new ItemStack(Material.DIRT, 2);
                materials2.add(m1);
                materials2.add(m2);
                recipe2.setIngredients(materials2);
                recipes.add(recipe2);

                villager.setRecipes(recipes);
                return true;
            }
        }
        return false;
    }
}
