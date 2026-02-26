package net.braeden.waterlogged.entity.ai;

import net.braeden.waterlogged.item.WaterloggedItems;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.EnumSet;

/**
 * Makes the mob follow a player who is holding a worm.
 * Equivalent to TemptGoal but does not require the minecraft:tempt_range
 * attribute, which vanilla fish do not have.
 */
public class WormTemptGoal extends Goal {

    private static final double RANGE = 10.0;

    private final PathfinderMob mob;
    private final double speedModifier;
    private Player player;

    public WormTemptGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.player = mob.level().getNearestPlayer(mob, RANGE);
        if (this.player == null) return false;
        return isHoldingWorm(this.player);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.player == null || !this.player.isAlive()) return false;
        if (!isHoldingWorm(this.player)) return false;
        return mob.distanceToSqr(this.player) < RANGE * RANGE;
    }

    @Override
    public void stop() {
        this.player = null;
        mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (this.player == null) return;
        mob.getLookControl().setLookAt(this.player, 30.0f, mob.getMaxHeadXRot());
        if (mob.distanceToSqr(this.player) > 2.25) {
            mob.getNavigation().moveTo(this.player, speedModifier);
        } else {
            mob.getNavigation().stop();
        }
    }

    private static boolean isHoldingWorm(Player player) {
        return isWorm(player.getMainHandItem()) || isWorm(player.getOffhandItem());
    }

    private static boolean isWorm(ItemStack stack) {
        return stack.getItem() == WaterloggedItems.WORM;
    }
}
