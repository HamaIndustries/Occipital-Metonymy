package symbolics.division.occmy.client.gfx;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import symbolics.division.occmy.client.view.CTreacherousView;

import java.util.OptionalInt;

public class RenderBlueSkyRed extends SFXPass {
    {
        this.purpose = "to forgive";
    }

    @Override
    protected void applyPass(Framebuffer buffer, RenderSystem.ShapeIndexBuffer quadIndexBuffer, GpuBuffer quadBuffer) {
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> purpose, swapView, OptionalInt.empty())) {
            renderPass.setPipeline(OccPipelines.disquiet);
            renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
            renderPass.setIndexBuffer(quadBuffer, quadIndexBuffer.getIndexType());
            renderPass.bindSampler("ColorSampler", buffer.getColorAttachmentView());
            renderPass.bindSampler("DepthSampler", buffer.getDepthAttachmentView());
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.drawIndexed(0, 0, 6, 1);
        }
    }

    public static Vector4f paintTheBlueSkyRed(FogRenderer instance, Camera camera, int viewDistance, boolean thick, RenderTickCounter tickCounter, float skyDarkness, ClientWorld world, Operation<Vector4f> original) {
        if (CTreacherousView.active()) {
            return new Vector4f(0.4f, 0f, 0, 0);
        }
        return original.call(instance, camera, viewDistance, thick, tickCounter, skyDarkness, world);
    }
}
