package net.braeden.waterlogged.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class RoeBlockItem extends BlockItem {
    public RoeBlockItem(Block block, Item.Properties props) {
        super(block, props);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, consumer, tooltipFlag);
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            String parentType = data.copyTag().getStringOr("ParentType", "");
            if (!parentType.isEmpty()) {
                // "minecraft:salmon" → "entity.minecraft.salmon" → "Salmon"
                consumer.accept(Component.translatable("entity." + parentType.replace(":", "."))
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }
}
