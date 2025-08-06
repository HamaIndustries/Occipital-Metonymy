package symbolics.division.occmy.client.mixin;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderLayer.MultiPhase.class)
public class MultiPhaseMixin {
//    @WrapOperation(
//            method = "draw",
//            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;getShaderTexture(I)Lcom/mojang/blaze3d/textures/GpuTextureView;")
//    )
//    public GpuTextureView a(int index, Operation<GpuTextureView> original) {
//        return ProjectionRenderer.textureView;
////        return null;
//    }
}
