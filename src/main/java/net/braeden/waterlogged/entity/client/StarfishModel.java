package net.braeden.waterlogged.entity.client;

import net.braeden.waterlogged.entity.client.blockbench.StarfishBlockbench;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;

public class StarfishModel extends Model<StarfishBlockEntityRenderer.StarfishRenderState> {

    public StarfishModel(ModelPart root) {
        super(root, RenderTypes::entityCutoutNoCull);
    }

    public static LayerDefinition getTexturedModelData() {
        return StarfishBlockbench.getTexturedModelData();
    }

    @Override
    public void setupAnim(StarfishBlockEntityRenderer.StarfishRenderState state) {
        this.resetPose();
    }
}
