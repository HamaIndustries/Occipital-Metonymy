package symbolics.division.occmy.client.ent;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2ReferenceArrayMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LazyEntityReference;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import symbolics.division.occmy.client.gfx.OccPipelines;
import symbolics.division.occmy.ent.ProjectionEntity;

import java.util.ArrayList;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.OptionalInt;

public class ProjectionRenderer extends EntityRenderer<ProjectionEntity, ProjectionRenderState> {

    private static final Map<LazyEntityReference<ProjectionEntity>, Image> GALLERY = new Object2ReferenceArrayMap<>();

    public static class Image {
        public final GpuTexture texture;
        public final GpuTextureView textureView;
        public final int width;
        public final int height;
        public boolean baked = false;
        public float[] uvs = new float[8]; // 00 01 11 10

        public Image(int width, int height, GpuTextureView tex) {
            texture = RenderSystem.getDevice().createTexture(
                    "occmy projection",
                    8 | 2,
                    TextureFormat.RGBA8,
                    width,
                    height,
                    1, 1
            );
            textureView = RenderSystem.getDevice().createTextureView(texture);

            this.width = width;
            this.height = height;

            RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
            GpuBuffer gpuBuffer = shapeIndexBuffer.getIndexBuffer(6);

            try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "blit", textureView, OptionalInt.of(0))) {
                renderPass.setPipeline(RenderPipelines.TRACY_BLIT);
                renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
                renderPass.setIndexBuffer(gpuBuffer, shapeIndexBuffer.getIndexType());
                renderPass.bindSampler("InSampler", tex);
                renderPass.drawIndexed(0, 0, 6, 1);
            }
        }

        public void close() {
            texture.close();
            textureView.close();
        }
    }


    public static GpuTexture makeTexture(int i, int j) {
        width = i;
        height = j;
        return RenderSystem.getDevice().createTexture(
                "awawawa",
                8 | 2,
                TextureFormat.RGBA8,
                i,
                j,
                1, 1
        );
    }

    private static GpuTexture texture = makeTexture(100, 100);
    private static GpuTextureView textureView = RenderSystem.getDevice().createTextureView(texture);
    private static int width = 100;
    private static int height = 100;

    public ProjectionRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public ProjectionRenderState createRenderState() {
        return new ProjectionRenderState();
    }

    public static void capture(Framebuffer buffer) {
        if (width != buffer.textureWidth || height != buffer.textureHeight) {
            texture.close();
            texture = makeTexture(buffer.textureWidth, buffer.textureHeight);
            textureView.close();
            textureView = RenderSystem.getDevice().createTextureView(texture);
            width = buffer.textureWidth;
            height = buffer.textureHeight;
        }

        World world = MinecraftClient.getInstance().world;
        if (world != null && world.getTime() % 20 == 0) {
            var toRemove = new ArrayList<LazyEntityReference<ProjectionEntity>>();
            for (var ref : GALLERY.keySet()) {
                if (ref.resolve(world, ProjectionEntity.class) == null) {
                    toRemove.add(ref);
                    GALLERY.get(ref).close();
                }
            }
            for (var ref : toRemove) GALLERY.remove(ref);
        }

        // TODO probably should make this only run once rather than all the time
        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);
        GpuBuffer gpuBuffer = shapeIndexBuffer.getIndexBuffer(6);

        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "blit", textureView, OptionalInt.of(0))) {
            renderPass.setPipeline(RenderPipelines.TRACY_BLIT);
            renderPass.setVertexBuffer(0, RenderSystem.getQuadVertexBuffer());
            renderPass.setIndexBuffer(gpuBuffer, shapeIndexBuffer.getIndexType());
            renderPass.bindSampler("InSampler", buffer.getColorAttachmentView());
            renderPass.drawIndexed(0, 0, 6, 1);
        }
    }

    @Override
    public void updateRenderState(ProjectionEntity entity, ProjectionRenderState state, float tickProgress) {
        var key = new LazyEntityReference<>(entity);
        if (!GALLERY.containsKey(key)) {
            GALLERY.put(key, new Image(width, height, textureView));
        }

        state.image = GALLERY.get(key);
        float yaw = entity.getYaw();
        float pitch = entity.getPitch();
        state.rot = new Quaternionf().rotationYXZ((float) Math.PI - yaw * (float) (Math.PI / 180.0), -pitch * (float) (Math.PI / 180.0), 0.0F);
        state.pos = entity.getPos();
//        state.eye = MinecraftClient.getInstance().gameRenderer.getCamera().getCameraPos();
        state.eye = MinecraftClient.getInstance().cameraEntity.getClientCameraPosVec(tickProgress);
        state.feet = MinecraftClient.getInstance().cameraEntity.getPos();
        state.age = entity.age;
        super.updateRenderState(entity, state, tickProgress);
    }

    private static void transform(MatrixStack matrices, float x, float y, Vector3f in, ProjectionRenderState state) {
        in.set(0);
        matrices.peek().getPositionMatrix().transformPosition(x, y, 0f, in);
    }

    private static Vec3d textureProjection(Vector3f in) {
        return MinecraftClient.getInstance().gameRenderer.project(new Vec3d(in));
    }

    @Override
    public boolean shouldRender(ProjectionEntity entity, Frustum frustum, double x, double y, double z) {
        boolean should = super.shouldRender(entity, frustum, x, y, z);
        if (MinecraftClient.getInstance().player != null) {
            should &= MinecraftClient.getInstance().player.getRotationVecClient().dotProduct(entity.getRotationVecClient()) < 0;
        }
        if (!should) {
            entity.remove = true;
        }
        return should;
    }

    public static void drawVertex(BufferBuilder bb, ProjectionRenderState state, MatrixStack matrices, Vector3f posTransform, Vector3f texTransform, int uvOffset, int u, int v) {
        transform(matrices, u, v, posTransform, state);
        if (!state.image.baked) {
            Vec3d sourcePos = new Vec3d(posTransform);
            Vec3d tex = MinecraftClient.getInstance().gameRenderer.project(state.eye.add(sourcePos));
            state.image.uvs[uvOffset] = ((float) tex.x + 1) / 2;
            state.image.uvs[uvOffset + 1] = ((float) tex.y + 1) / 2;
        }
        bb.vertex(posTransform.x, posTransform.y, posTransform.z).texture(state.image.uvs[uvOffset], state.image.uvs[uvOffset + 1]);
    }

    @Override
    public void render(ProjectionRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        RenderSystem.ShapeIndexBuffer indices = RenderSystem.getSequentialBuffer(VertexFormat.DrawMode.QUADS);

        matrices.push();

        matrices.multiply(state.rot);
        matrices.translate(-1.5, -0.5, -1);

        matrices.scale(3, 3, 0);
        GpuBuffer vertexBuffer;
        Vector3f tempTex = new Vector3f();
        Vector3f tempPos = new Vector3f();

        boolean simple = true;
        VertexFormat format = simple ? VertexFormats.POSITION : VertexFormats.POSITION_TEXTURE;
        RenderPipeline pipeline = simple ? OccPipelines.PROJECTION_SIMPLE : OccPipelines.PROJECTION;

        try (BufferAllocator ba = BufferAllocator.method_72201(format.getVertexSize() * 4)) {
            BufferBuilder bufferBuilder = new BufferBuilder(ba, VertexFormat.DrawMode.QUADS, format);

            drawVertex(bufferBuilder, state, matrices, tempPos, tempTex, 0, 0, 0);
            drawVertex(bufferBuilder, state, matrices, tempPos, tempTex, 2, 0, 1);
            drawVertex(bufferBuilder, state, matrices, tempPos, tempTex, 4, 1, 1);
            drawVertex(bufferBuilder, state, matrices, tempPos, tempTex, 6, 1, 0);

            try (BuiltBuffer builtBuffer = bufferBuilder.end()) {
                vertexBuffer = RenderSystem.getDevice().createBuffer(() -> "Quad", 32, builtBuffer.getBuffer());
            }
        }
        state.image.baked = true;
        matrices.pop();

        GpuBuffer indexBuffer = indices.getIndexBuffer(6);


        try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(
                () -> "wtf", MinecraftClient.getInstance().getFramebuffer().getColorAttachmentView(), OptionalInt.empty(),
                MinecraftClient.getInstance().getFramebuffer().getDepthAttachmentView(), OptionalDouble.empty()
        )) {
            pass.setPipeline(pipeline);
            pass.setVertexBuffer(0, vertexBuffer);
            pass.setIndexBuffer(indexBuffer, indices.getIndexType());
            pass.bindSampler("Sampler0", state.image.textureView);
            RenderSystem.bindDefaultUniforms(pass);
            pass.drawIndexed(0, 0, 6, 1);
        }

        vertexBuffer.close();
    }
}
