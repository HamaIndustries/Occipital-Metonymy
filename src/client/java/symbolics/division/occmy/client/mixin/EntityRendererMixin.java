package symbolics.division.occmy.client.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import symbolics.division.occmy.client.view.CBestView;

import java.util.Optional;

@Mixin(EntityRenderer.class
)
public class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {
    private boolean occmy$checked = false;
    private boolean occmy$bad = false;

    @Inject(
            method = "shouldRender",
            at = @At("HEAD"),
            cancellable = true
    )
    private void butIfYouCloseYourEyes(T entity, Frustum frustum, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {
        if (!occmy$checked) {
            occmy$checked = true;
            Optional<RegistryKey<EntityType<?>>> key = entity.getType().getRegistryEntry().getKey();
            if (key.isPresent() && !key.get().getValue().getNamespace().equals("minecraft")) {
                occmy$bad = true;
            }
        }
        if (occmy$bad && CBestView.fixMinecraft()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
