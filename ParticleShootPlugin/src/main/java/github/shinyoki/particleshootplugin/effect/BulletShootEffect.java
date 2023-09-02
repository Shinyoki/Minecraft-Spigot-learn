package github.shinyoki.particleshootplugin.effect;

import github.shinyoki.particleshootplugin.ParticleShootPlugin;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.Objects;

public class BulletShootEffect {

    private int executeCount = 0;               // 当前执行次数
    private int maxExecuteCount = 50;          // 最大执行次数

    /**
     * 粒子必备信息
     */
    private Particle particle = Particle.REDSTONE;      // 红石
    Color startColor = Color.fromRGB(174, 214, 241);        // 起始颜色
    Color endColor = Color.fromRGB(108, 52, 131);           // 结束颜色
    int particleAmount = 1;                                 // 粒子数量
    double particleSpeed = 1;                               // 粒子速度
    double fliedDistance = 0;                             // 粒子飞行距离
    double maxDistance = 50;                               // 最大飞行距离

    /**
     * 初始化
     */
    Location startLocation;                                 // 起始位置
    Vector startDirection;                                  // 起始方向
    World world;                                            // 世界
    Player shooter;                                         // 发射者

    public BulletShootEffect(Player shooter) {
        this.shooter = shooter;
        this.world = shooter.getWorld();
        this.startLocation = shooter.getEyeLocation();
        this.startDirection = this.startLocation.getDirection();
    }

    public void start() {
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {

            @Override
            public void run() {

                // 计算当前的位置   开始位置 + 方向 * 飞行距离
                Location currentLocation = startLocation.clone().add(startDirection.clone().multiply(fliedDistance));
                // 适当位置结束
                if (executeCount >= maxExecuteCount || fliedDistance >= maxDistance) {
                    // 显示击中效果，兜底
                    showHitEffect(currentLocation, null);
                    return;
                }

                // 显示粒子
                Particle.DustOptions calculatedColor = calculateCurrentColor();
                world.spawnParticle(particle, currentLocation, particleAmount, 0, 0.1, 0.1, calculatedColor);

                // 受击判定 RayTraceEntitys
                RayTraceResult result = world.rayTraceEntities(currentLocation, startDirection, 1, 0.5, entity -> entity != shooter && entity instanceof LivingEntity);
                if (Objects.nonNull(result)) {
                    // 显示击中效果
                    showHitEffect(currentLocation, result.getHitEntity());
                    return;
                } else {
                    Block block = world.getBlockAt(currentLocation);
                    if (!block.isPassable()) {
                        // 显示击中效果
                        showHitEffect(currentLocation, null);
                        return;
                    }
                }

                executeCount++;
                fliedDistance += particleSpeed;
            }

            /**
             * 计算进度，通过RGB计算颜色
             */
            private Particle.DustOptions calculateCurrentColor() {
                // 进度百分比
                double progress = fliedDistance / maxDistance;
                int red = (int) (startColor.getRed() + (endColor.getRed() - startColor.getRed()) * progress);
                int green = (int) (startColor.getGreen() + (endColor.getGreen() - startColor.getGreen()) * progress);
                int blue = (int) (startColor.getBlue() + (endColor.getBlue() - startColor.getBlue()) * progress);

                return new Particle.DustOptions(Color.fromRGB(red, green, blue), 1);
            }

            /**
             * 显示击中效果
             */
            private void showHitEffect(Location currentLocation, Entity hitEntity) {
                if (Objects.isNull(hitEntity)) {
                    // 没有击中实体

                    Block block = world.getBlockAt(currentLocation);
                    if (block.getType() != Material.AIR) {
                        // 击中方块
                        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(block, shooter);
                        Bukkit.getPluginManager().callEvent(blockBreakEvent);
                        if (!blockBreakEvent.isCancelled()) {
                            // 没有被取消，打掉方块，播放方块破碎效果
                            world.playEffect(currentLocation, Effect.STEP_SOUND, block.getType());
                            // 移除方块
                            block.setType(Material.AIR);
                        }
                    }
                } else {
                    // 如果是LivingEntity
                    if (hitEntity instanceof LivingEntity) {
                        LivingEntity livingEntity = (LivingEntity) hitEntity;
                        // 造成伤害
                        livingEntity.damage(10, shooter);

                        // 播放击中效果，红石碎片模拟血液
                        world.spawnParticle(Particle.REDSTONE, currentLocation, 10, 0.5, 0.5, 0.5, 0, new Particle.DustOptions(Color.RED, 1));
                        shooter.sendMessage("击中" + livingEntity.getName() + "， 对方剩余血量" + livingEntity.getHealth());
                        livingEntity.sendMessage("你被" + shooter.getName() + "击中， 剩余血量" + livingEntity.getHealth());
                    }
                }

                // 取消任务
                cancel();
            }

        };
        // 每tick执行一次
        bukkitRunnable.runTaskTimer(ParticleShootPlugin.getInstance(), 0, 1);
    }

}
