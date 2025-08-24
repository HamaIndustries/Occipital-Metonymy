package symbolics.division.occmy.client.ent;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import symbolics.division.occmy.OCCMY;

// Made with Blockbench 4.12.5
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class TurnkeyModel extends BipedEntityModel<PlayerEntityRenderState> {
//    private final ModelPart bb_main;

    public static EntityModelLayer TURNKEY_LAYER = new EntityModelLayer(OCCMY.id("turnkey"), "turnkey");

    public TurnkeyModel(ModelPart root) {
        super(root, RenderLayer::getEntityCutout);
//        this.bb_main = root.getChild("bb_main");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = BipedEntityModel.getModelData(Dilation.NONE, 0);
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData head = modelPartData.addChild(EntityModelPartNames.HEAD);
        head.addChild(EntityModelPartNames.HAT);
        ModelPartData body = modelPartData.addChild(EntityModelPartNames.BODY);
        modelPartData.addChild(EntityModelPartNames.LEFT_ARM);
        modelPartData.addChild(EntityModelPartNames.RIGHT_ARM);
        modelPartData.addChild(EntityModelPartNames.LEFT_LEG);
        modelPartData.addChild(EntityModelPartNames.RIGHT_LEG);
        ModelPartData cube_r1 = body.addChild("cube_r1", ModelPartBuilder.create().uv(0, -16).cuboid(0.0F, -8.0F, 0.0F, 0.00001F, 16.0F, 16.0F, new Dilation(1.0F)), ModelTransform.of(0.0F, 1F, 20.0F, 0, (float) Math.PI, 0));
        return TexturedModelData.of(modelData, 32, 32);
    }
}