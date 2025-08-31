package symbolics.division.occmy.client.view;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.state.Sufficiency;

public class Perspectives {
    private static void shutter() {
        PlayerEntity player = OCCMYClient.player();
        if (player == null) return;
        Vec3d p = player.getPos();
        World world = OCCMYClient.world();
        if (world == null) return;
        world.playSound(OCCMYClient.player(), p.x, 1000, p.z, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.UI, 10000, 2);
    }

    public static final Obscured OBSCURED = new Obscured();

    public static final class Obscured extends Sufficiency {
        private Runnable trigger;

        @Override
        protected void onStart() {
            MinecraftClient.getInstance().getSoundManager().pauseAllExcept();
            shutter();
        }

        @Override
        protected void onEnd() {
            shutter();
            MinecraftClient.getInstance().getSoundManager().resumeAll();
            if (trigger != null) {
                trigger.run();
                trigger = null;
            }
        }

        public void cancel() {
            this.complete = true;
            this.disable();
        }

        public void trigger(Runnable r) {
            this.trigger = r;
        }
    }

    public static void reset() {
        if (OBSCURED.isActive()) OBSCURED.cancel();
    }
}
