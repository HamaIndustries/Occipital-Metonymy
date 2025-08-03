package symbolics.division.occmy.state;

public class Necessity extends Gestalt {
    boolean fired = false;
    Runnable cb;

    public Necessity(Runnable cb, int ticks) {
        super(ticks);
        this.cb = cb;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksLeft() == 0 && !fired) {
            this.cb.run();
        }
    }
}
