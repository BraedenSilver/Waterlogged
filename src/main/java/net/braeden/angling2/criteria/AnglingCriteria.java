package net.braeden.angling2.criteria;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;

import static net.braeden.angling2.AnglingMod.MOD_ID;

public class AnglingCriteria {
    public static final TradedWithPelicanCriterion TRADED_WITH_PELICAN = Registry.register(
            BuiltInRegistries.TRIGGER_TYPES,
            Identifier.fromNamespaceAndPath(MOD_ID, "traded_with_pelican"),
            new TradedWithPelicanCriterion()
    );

    public static void init() {}
}
