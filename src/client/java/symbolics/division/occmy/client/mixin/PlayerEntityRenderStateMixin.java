package symbolics.division.occmy.client.mixin;

import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import symbolics.division.occmy.client.ent.TurnkeyState;

@Mixin(PlayerEntityRenderState.class)
public class PlayerEntityRenderStateMixin implements TurnkeyState {
    private boolean occmy$turnkey = false;

    @Override
    public void occmy$setTurnkey(boolean v) {
        occmy$turnkey = v;
    }

    @Override
    public boolean occmy$turnkey() {
        return occmy$turnkey;
    }
}
