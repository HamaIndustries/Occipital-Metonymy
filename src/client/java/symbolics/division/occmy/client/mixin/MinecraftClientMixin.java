package symbolics.division.occmy.client.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import symbolics.division.occmy.client.view.Perspectives;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Shadow
    @Final
    public GameRenderer gameRenderer;

    @Shadow
    public boolean skipGameRender;

    @Inject(
            method = "render",
            at = @At("HEAD")
    )
    private void tick(boolean tick, CallbackInfo ci) {
        if (Perspectives.OBSCURED.living()) {
            this.skipGameRender = !Perspectives.OBSCURED.complete();
        }
    }
}
