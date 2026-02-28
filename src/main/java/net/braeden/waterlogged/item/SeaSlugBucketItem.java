package net.braeden.waterlogged.item;

import net.braeden.waterlogged.entity.util.SeaSlugColor;
import net.braeden.waterlogged.entity.util.SeaSlugPattern;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
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

public class SeaSlugBucketItem extends MobBucketItem {
    public SeaSlugBucketItem(EntityType<? extends Mob> type, Item.Properties props) {
        super(type, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, props);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, consumer, tooltipFlag);
        CustomData data = stack.get(DataComponents.BUCKET_ENTITY_DATA);
        if (data != null) {
            CompoundTag tag = data.copyTag();
            SeaSlugColor color = SeaSlugColor.byId(tag.getIntOr("Color", 0));
            SeaSlugPattern pattern = SeaSlugPattern.byId(tag.getIntOr("Pattern", 0));
            consumer.accept(Component.translatable("sea_slug_color.waterlogged." + color.getSerializedName())
                    .withStyle(ChatFormatting.GRAY));
            if (pattern != SeaSlugPattern.PLAIN) {
                SeaSlugColor patternColor = SeaSlugColor.byId(tag.getIntOr("PatternColor", 1));
                consumer.accept(Component.translatable("sea_slug_pattern.waterlogged." + pattern.getSerializedName())
                        .withStyle(ChatFormatting.GRAY));
                consumer.accept(Component.translatable("sea_slug_color.waterlogged." + patternColor.getSerializedName())
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
