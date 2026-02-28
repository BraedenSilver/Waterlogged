package net.braeden.waterlogged.item;

import net.braeden.waterlogged.entity.util.SunfishVariant;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Consumer;

public class SunfishBucketItem extends MobBucketItem {
    public SunfishBucketItem(EntityType<? extends Mob> type, Item.Properties props) {
        super(type, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, consumer, tooltipFlag);
        CustomData data = stack.get(DataComponents.BUCKET_ENTITY_DATA);
        if (data != null) {
            SunfishVariant variant = SunfishVariant.byId(data.copyTag().getIntOr("Variant", 0));
            consumer.accept(Component.translatable("sunfish_variant.waterlogged." + variant.getSerializedName())
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
