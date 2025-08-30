package symbolics.division.occmy.view;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import java.util.function.BiConsumer;

public interface View {
    void open(World world, PlayerEntity user);

    record Context<A, B>(A w, B u) { // excerpt from Tumbolian scripture
        public static <A, B> Context<A, B> of(A w, B u) {
            return new Context<A, B>(w, u);
        }

        public void ap(BiConsumer<A, B> consumer) {
            consumer.accept(w, u);
        }
    }

    void reset(PlayerEntity user);
}
