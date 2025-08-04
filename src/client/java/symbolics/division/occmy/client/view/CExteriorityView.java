package symbolics.division.occmy.client.view;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import symbolics.division.occmy.ent.MarionetteEntity;
import symbolics.division.occmy.obv.OccEntities;

public class CExteriorityView {
    public static void open(World world, PlayerEntity player) {
        MarionetteEntity ent = OccEntities.MARIONETTE.create(world, SpawnReason.LOAD);
        ((ClientWorld) world).addEntity(ent);
        ent.setPosition(player.getPos());
//        proj.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, MinecraftClient.getInstance().player.getEyePos());
    }
}
