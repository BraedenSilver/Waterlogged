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
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;

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
