package net.braeden.waterlogged.particle;

//?if fabric {
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
//?}
//?if fabric {
import net.minecraft.core.Registry;
//?}
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
//?if neoforge {
/*import net.neoforged.neoforge.registries.RegisterEvent;*/
//?}

import static net.braeden.waterlogged.WaterloggedMod.MOD_ID;

public class WaterloggedParticles {

    //?if fabric {
    public static SimpleParticleType ALGAE = Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "algae"), FabricParticleTypes.simple(true));
    public static SimpleParticleType WORM = Registry.register(BuiltInRegistries.PARTICLE_TYPE, Identifier.fromNamespaceAndPath(MOD_ID, "worm"), FabricParticleTypes.simple(true));
    //?} else {
    /*public static SimpleParticleType ALGAE = new SimpleParticleType(true);
    public static SimpleParticleType WORM = new SimpleParticleType(true);*/
    //?}

    public static void init() {

    }

//?if neoforge {
/*    public static void registerAll(RegisterEvent event) {
        event.register(BuiltInRegistries.PARTICLE_TYPE.key(), helper -> {
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "algae"), ALGAE);
            helper.register(Identifier.fromNamespaceAndPath(MOD_ID, "worm"), WORM);
        });
    }*/
//?}

}
