package symbolics.division.occmy.obv;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import symbolics.division.occmy.OCCMY;

public class OccSounds {
    public static void init() {
    }

    public static final SoundEvent SHUTTER = register("sfx.shutter");

    private static SoundEvent register(String id) {
        return Registry.register(Registries.SOUND_EVENT, OCCMY.id(id), SoundEvent.of(OCCMY.id(id)));
    }
}
