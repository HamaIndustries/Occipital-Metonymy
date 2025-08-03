package symbolics.division.occmy.client.view;

import net.minecraft.sound.SoundEvents;
import symbolics.division.occmy.client.OCCMYClient;
import symbolics.division.occmy.state.Sufficiency;

public class Perspectives {

    private static void shutter() {
        OCCMYClient.player().playSound(SoundEvents.BLOCK_PISTON_CONTRACT, 1, 2);
    }

    public static final Obscured OBSCURED = new Obscured();

    public static final class Obscured extends Sufficiency {
        private Runnable trigger;

        @Override
        protected void onStart() {
            shutter();
        }

        @Override
        protected void onEnd() {
            shutter();
            if (trigger != null) {
                trigger.run();
                trigger = null;
            }
        }

        public void trigger(Runnable r) {
            this.trigger = r;
        }
    }
}
