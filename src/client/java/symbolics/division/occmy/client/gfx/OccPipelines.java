package symbolics.division.occmy.client.gfx;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;
import symbolics.division.occmy.OCCMY;

public class OccPipelines {
    public static final RenderPipeline PROJECTION = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
                    .withLocation(OCCMY.id("pipeline/projection"))
                    .withVertexShader("core/position")
                    .withFragmentShader(OCCMY.id("core/projection"))
                    .withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS)
                    .withSampler("Sampler0")
                    .withDepthWrite(true)
                    .build()
    );
}
