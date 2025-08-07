package symbolics.division.occmy.client.mixin;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RenderLayer.MultiPhase.class)
public class MultiPhaseMixin {

//    @WrapOperation(
//            method = "draw",
//            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/CommandEncoder;createRenderPass(Ljava/util/function/Supplier;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalInt;Lcom/mojang/blaze3d/textures/GpuTextureView;Ljava/util/OptionalDouble;)Lcom/mojang/blaze3d/systems/RenderPass;")
//    )
//    public RenderPass a(CommandEncoder instance, Supplier<String> stringSupplier, GpuTextureView colorTextureView, OptionalInt optionalInt, @Nullable GpuTextureView depthTextureView, OptionalDouble optionalDouble, Operation<RenderPass> original) {
//        if (CInversionView.active()) {
//            return original.call(instance, stringSupplier, colorTextureView, optionalInt, colorTextureView, optionalDouble);
//        }
//        return original.call(instance, stringSupplier, colorTextureView, optionalInt, depthTextureView, optionalDouble);
//    }
}
