package symbolics.division.occmy.client.view;

import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.state.Sufficiency;

public class Perspectives {

    private static void shutter() {
        Vec3d p = OCCMYClient.player().getPos();
        OCCMYClient.world().playSound(OCCMYClient.player(), p.x, 1000, p.z, SoundEvents.BLOCK_PISTON_CONTRACT, SoundCategory.UI, 10000, 2);
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

        public void trigger(Runnable r) {
            this.trigger = r;
        }
    }

    public static void reset() {
        OBSCURED.disable();
    }
}
