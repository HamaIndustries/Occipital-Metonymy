package symbolics.division.occmy.client.view;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.obv.OccSounds;

public class CNullView {
    public static void open(World world, PlayerEntity player) {
        OCCMYClient.AFFAIRS.enableFor(Perspectives.OBSCURED, Integer.MAX_VALUE);
        MinecraftClient.getInstance().getSoundManager().pauseAllExcept(SoundCategory.UI);
        OCCMYClient.schedule(() -> MinecraftClient.getInstance().player.playSoundToPlayer(OccSounds.THOUSAND_EYES, SoundCategory.UI, 1, 1), 10);
        for (Entity e : ((ClientWorld) world).getEntities()) {
            if (e != null) ((ClientWorld) world).removeEntity(e.getId(), Entity.RemovalReason.DISCARDED);
        }
    }
}
