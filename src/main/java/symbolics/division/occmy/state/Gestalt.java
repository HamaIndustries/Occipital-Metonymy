package symbolics.division.occmy.state;

public class Gestalt implements ISachverhalt {
    protected int ticksLeft = -1;

    public Gestalt(int ticks) {
        ticksLeft = ticks;
    }

    public Gestalt() {
    }

    @Override
    public void enable() {
        ticksLeft = -1;
    }

    @Override
    public void enableFor(int ticks) {
        ticksLeft = Math.max(ticks, ticksLeft);
    }

    @Override
    public void disable() {
        ticksLeft = 0;
    }

    @Override
    public boolean isActive() {
        return ticksLeft != 0;
    }

    @Override
    public void tick() {
        if (ticksLeft > 0) {
            ticksLeft--;
        }
    }

    @Override
    public int ticksLeft() {
        return ticksLeft;
    }
}
