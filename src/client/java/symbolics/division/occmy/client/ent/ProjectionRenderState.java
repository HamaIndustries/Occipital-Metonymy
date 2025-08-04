package symbolics.division.occmy.client.ent;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;


public class ProjectionRenderState extends EntityRenderState {

    public ProjectionRenderer.Image image;
    public Vec3d pos;
    public Vec3d eye;
    public Vec3d feet;
    public Quaternionf rot;
    public int age;

    public ProjectionRenderState() {
    }
}
