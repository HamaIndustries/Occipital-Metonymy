package symbolics.division.occmy.client.gfx;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.render.VertexFormats;
import symbolics.division.occmy.OCCMY;

public class OccPipelines {
    public static final RenderPipeline PROJECTION = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
                    .withLocation(OCCMY.id("pipeline/projection"))
                    .withVertexShader("core/position_tex")
                    .withFragmentShader(OCCMY.id("core/position_tex_but_good"))
                    .withVertexFormat(VertexFormats.POSITION_TEXTURE, VertexFormat.DrawMode.QUADS)
                    .withSampler("Sampler0")
                    .withDepthWrite(true)
                    .build()
    );

    public static final RenderPipeline PROJECTION_SIMPLE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
                    .withLocation(OCCMY.id("pipeline/projection_simple"))
                    .withVertexShader("core/position")
                    .withFragmentShader(OCCMY.id("core/projection"))
                    .withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS)
                    .withSampler("Sampler0")
                    .withDepthWrite(true)
                    .build()
    );

    public static final RenderPipeline DEPTH = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.TRANSFORMS_PROJECTION_FOG_LIGHTING_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
                    .withLocation(OCCMY.id("pipeline/depth"))
                    .withVertexShader(OCCMY.id("core/blit_screen"))
                    .withFragmentShader(OCCMY.id("core/depth"))
                    .withSampler("DepthSampler")
                    .withSampler("D2Sampler")
                    .withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS)
                    .withDepthWrite(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .build()
    );

    public static final RenderPipeline PLEASE_BLIT = RenderPipelines.register(
            RenderPipeline.builder()
                    .withLocation(OCCMY.id("pipeline/please_blit"))
                    .withVertexShader(OCCMY.id("core/blit_screen"))
                    .withFragmentShader(OCCMY.id("core/blit_screen"))
                    .withSampler("BlitSampler")
                    .withDepthWrite(false)
                    .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
                    .withVertexFormat(VertexFormats.POSITION, VertexFormat.DrawMode.QUADS)
                    .build()
    );
}
