package symbolics.division.occmy.state;


import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class Sachverhalten {
    private final Map<Identifier, ISachverhalt> states = new HashMap<>();
    private final List<ISachverhalt> anonymousStates = new ReferenceArrayList<>();

    public void enableFor(ISachverhalt state, int ticks) {
        enable(state);
        state.enableFor(ticks);
    }

    public void enable(Identifier id, ISachverhalt state) {
        states.put(id, state);
    }

    public void enable(ISachverhalt state) {
        anonymousStates.add(state);
    }

    public void tick() {
        for (Map.Entry<Identifier, ISachverhalt> state : states.entrySet()) {
            state.getValue().tick();
            if (!state.getValue().isActive()) {
                states.remove(state.getKey());
            }
        }
        List<ISachverhalt> toRemove = anonymousStates.stream()
                .peek(ISachverhalt::tick)
                .filter(s -> !s.isActive())
                .toList();
        anonymousStates.removeAll(toRemove);
    }

    public Optional<ISachverhalt> get(Identifier id) {
        return Optional.ofNullable(states.get(id));
    }

    public boolean isActive(Identifier id) {
        return states.containsKey(id);
    }
}