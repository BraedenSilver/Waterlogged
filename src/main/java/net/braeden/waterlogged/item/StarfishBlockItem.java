package net.braeden.waterlogged.item;

import net.braeden.waterlogged.entity.util.StarfishColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class StarfishBlockItem extends BlockItem {
    public StarfishBlockItem(Block block, Item.Properties props) {
        super(block, props);
    }

    @Override
    public Component getName(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null && data.copyTag().getIntOr("Color", -1) == StarfishColor.RAINBOW.getId()) {
            return rainbowName("Rainbow Starfish");
        }
        return super.getName(stack);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> consumer, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, display, consumer, tooltipFlag);
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data != null) {
            int colorId = data.copyTag().getIntOr("Color", -1);
            if (colorId < 0) return;
            StarfishColor color = StarfishColor.byId(colorId);
            if (color == StarfishColor.RAINBOW) {
                // Name already shows "Rainbow Starfish" — no extra tooltip needed
                return;
            }
            int rgb = color.getArgb() & 0x00FFFFFF;
            consumer.accept(Component.translatable("starfish_color.waterlogged." + color.getSerializedName())
                    .withStyle(style -> style.withColor(TextColor.fromRgb(rgb))));
        }
    }

    private static Component rainbowName(String text) {
        MutableComponent result = Component.empty();
        int len = text.length();
        for (int i = 0; i < len; i++) {
            float hue = (float) i / len;
            int rgb = Mth.hsvToRgb(hue, 1.0F, 1.0F);
            result.append(Component.literal(String.valueOf(text.charAt(i)))
                    .withStyle(style -> style.withColor(TextColor.fromRgb(rgb))));
        }
        return result;
    }
}
