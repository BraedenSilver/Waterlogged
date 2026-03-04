package net.braeden.waterlogged.criteria;

//?if fabric {
import net.minecraft.core.Registry;
//?}
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
//?if neoforge {
/*import net.neoforged.neoforge.registries.RegisterEvent;*/
//?}

import static net.braeden.waterlogged.WaterloggedMod.MOD_ID;

public class WaterloggedCriteria {
    public static final TradedWithPelicanCriterion TRADED_WITH_PELICAN =
//?if fabric {
            Registry.register(BuiltInRegistries.TRIGGER_TYPES,
                    Identifier.fromNamespaceAndPath(MOD_ID, "traded_with_pelican"),
                    new TradedWithPelicanCriterion());
//?} else {
/*            new TradedWithPelicanCriterion();*/
//?}

    public static final CaughtFishWithNetCriterion CAUGHT_FISH_WITH_NET =
//?if fabric {
            Registry.register(BuiltInRegistries.TRIGGER_TYPES,
                    Identifier.fromNamespaceAndPath(MOD_ID, "caught_fish_with_net"),
                    new CaughtFishWithNetCriterion());
//?} else {
/*            new CaughtFishWithNetCriterion();*/
//?}

    public static void init() {}

//?if neoforge {
/*    public static void registerAll(RegisterEvent event) {
        event.register(BuiltInRegistries.TRIGGER_TYPES.key(), helper -> {
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "traded_with_pelican"), TRADED_WITH_PELICAN);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "caught_fish_with_net"), CAUGHT_FISH_WITH_NET);
        });
    }*/
//?}
}
