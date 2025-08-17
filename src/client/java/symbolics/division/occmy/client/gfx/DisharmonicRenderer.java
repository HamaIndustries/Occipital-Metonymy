package symbolics.division.occmy.client.gfx;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.Framebuffer;

import java.util.OptionalInt;

public class DisharmonicRenderer extends SFXPass {
    {
        this.purpose = "Disharmonic Render Pass";
    }

    @Override
    protected void applyPass(Framebuffer buffer, RenderSystem.ShapeIndexBuffer quadIndexBuffer, GpuBuffer quadBuffer) {
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> purpose, swapView, OptionalInt.empty())) {
            renderPass.setPipeline(OccPipelines.DISHARMONY);
            renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
            renderPass.setIndexBuffer(quadBuffer, quadIndexBuffer.getIndexType());
            renderPass.bindSampler("ColorSampler", buffer.getColorAttachmentView());
            renderPass.bindSampler("DepthSampler", buffer.getDepthAttachmentView());
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.drawIndexed(0, 0, 6, 1);
        }
    }
}
