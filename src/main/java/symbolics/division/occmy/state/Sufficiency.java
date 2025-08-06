package symbolics.division.occmy.state;

public class Sufficiency extends Gestalt {
    private boolean complete = true;
    protected boolean check = true;

    @Override
    public void enableFor(int ticks) {
        super.enableFor(ticks);
        complete = false;
        onStart();
    }

    public boolean living() {
        return !complete;
    }

    public boolean complete() {
        if (ticksLeft() <= 0 && !complete) {
            complete = true;
            onEnd();
        }
        return complete;
    }

    @Override
    public void tick() {
        super.tick();
        if (ticksLeft == 0 && !check) complete();
    }

    protected void onStart() {
    }

    protected void onEnd() {
    }
}
