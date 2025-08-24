package symbolics.division.occmy.client.ent;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import symbolics.division.occmy.OCCMY;

public class TurnkeyFeatureRenderer<M extends PlayerEntityModel> extends FeatureRenderer<PlayerEntityRenderState, M> {
    private static final Identifier TEXTURE = OCCMY.id("textures/entity/turnkey.png");
    private final BipedEntityModel<PlayerEntityRenderState> model;

    public TurnkeyFeatureRenderer(FeatureRendererContext<PlayerEntityRenderState, M> context, LoadedEntityModels modelLoader) {
        super(context);
        this.model = new TurnkeyModel(modelLoader.getModelPart(TurnkeyModel.TURNKEY_LAYER));
    }


    static final Vector3fc Z_AXIS = new Vector3f(0, 0, 1);

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, PlayerEntityRenderState state, float limbAngle, float limbDistance) {
        if (!((TurnkeyState) state).occmy$turnkey()) return;
        matrices.push();
        {
            matrices.translate(0, 0.3, -0.1);
            matrices.multiply(new Quaternionf().rotateAxis((float) state.age / 2, Z_AXIS));
            this.getContextModel().copyTransforms(this.model);
            this.model.setAngles(state);
            this.model.render(matrices, vertexConsumers.getBuffer(this.model.getLayer(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        }
        matrices.pop();
    }
}
