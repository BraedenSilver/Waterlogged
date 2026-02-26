package net.braeden.waterlogged.mixin;

import net.braeden.waterlogged.entity.util.StarfishColor;
import net.braeden.waterlogged.item.WaterloggedItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.CustomModelDataSource;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Makes the rainbow starfish item cycle colors in the inventory/hand
 * by overriding the tint calculation at render time.
 */
@Mixin(CustomModelDataSource.class)
public class RainbowStarfishItemColorMixin {

    @Inject(method = "calculate", at = @At("HEAD"), cancellable = true)
    private void waterlogged_rainbowStarfishColor(
            ItemStack stack, ClientLevel level, LivingEntity entity,
            CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() != WaterloggedItems.STARFISH) return;
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return;
        if (data.copyTag().getIntOr("Color", -1) != StarfishColor.RAINBOW.getId()) return;

        Minecraft mc = Minecraft.getInstance();
        long gameTicks = level != null ? level.getGameTime()
                : (mc.level != null ? mc.level.getGameTime() : 0);
        cir.setReturnValue(StarfishColor.RAINBOW.getArgbForTicks(gameTicks, 0F));
    }
}
