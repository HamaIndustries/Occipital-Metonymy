package symbolics.division.occmy.client.ent;

import com.mojang.blaze3d.textures.GpuTextureView;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;

public class ProjectionRenderState extends EntityRenderState {

    public GpuTextureView textureView;
    public Vec3d pos;
    public Vec3d eye;
    public Quaternionf rot;

    public ProjectionRenderState() {
    }
}
