package symbolics.division.occmy.client.gfx;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.Framebuffer;
import symbolics.division.occmy.client.ent.ProjectionRenderer;

import java.util.OptionalInt;

public class DepthRenderer {
    private static GpuTexture swap = ProjectionRenderer.makeTexture(100, 100);
    private static GpuTextureView swapView = RenderSystem.getDevice().createTextureView(swap);

    private static GpuTexture depth = RenderSystem.getDevice().createTexture(() -> " thingy/ Depth", 15, TextureFormat.DEPTH32, 100, 100, 1, 1);
    private static GpuTextureView depthView = RenderSystem.getDevice().createTextureView(depth);

    public static void apply(Framebuffer buffer) {
        if (swap.getWidth(1) != buffer.textureWidth || swap.getHeight(1) != buffer.textureHeight) {
            swap.close();
            swap = ProjectionRenderer.makeTexture(buffer.textureWidth, buffer.textureHeight);
            swapView.close();
            swapView = RenderSystem.getDevice().createTextureView(swap);
            depth.close();
            depth = RenderSystem.getDevice().createTexture(() -> " thingy/ Depth", 15, TextureFormat.DEPTH32, buffer.textureWidth, buffer.textureHeight, 1, 1);
            depthView.close();
            depthView = RenderSystem.getDevice().createTextureView(swap);
        }

        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer gpuBuffer = shapeIndexBuffer.getIndexBuffer(6);

        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "blittttt", swapView, OptionalInt.empty())) {
            renderPass.setPipeline(OccPipelines.DEPTH);
            renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
            renderPass.setIndexBuffer(gpuBuffer, shapeIndexBuffer.getIndexType());
            renderPass.bindSampler("DepthSampler", buffer.getColorAttachmentView());
            renderPass.bindSampler("D2Sampler", buffer.getDepthAttachmentView());
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.drawIndexed(0, 0, 6, 1);
        }

        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "blit", buffer.getColorAttachmentView(), OptionalInt.empty())) {
            renderPass.setPipeline(OccPipelines.PLEASE_BLIT);
            renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
            renderPass.setIndexBuffer(gpuBuffer, shapeIndexBuffer.getIndexType());
            renderPass.bindSampler("BlitSampler", swapView);
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.drawIndexed(0, 0, 6, 1);
        }
    }

}
