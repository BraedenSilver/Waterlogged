package net.braeden.waterlogged.mixin;

import net.braeden.waterlogged.entity.CatfishEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Makes Creepers afraid of Catfish, since it's a cat.
 */
@Mixin(Creeper.class)
public abstract class CreeperCatfishMixin {

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void angling_addCatfishAvoidGoal(CallbackInfo ci) {
        var gs = ((MobGoalSelectorAccessor) this).angling_getGoalSelector();
        gs.addGoal(3, new AvoidEntityGoal<>((PathfinderMob) (Object) this, CatfishEntity.class, 6.0f, 1.0, 1.2));
    }
}
