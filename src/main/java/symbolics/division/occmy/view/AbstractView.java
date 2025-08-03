package symbolics.division.occmy.view;

import java.util.function.Consumer;

public abstract class AbstractView<T> implements View {
    protected Consumer<T> clientCallback = d -> {
    };

    public void setCallback(Consumer<T> cb) {
        this.clientCallback = cb;
    }

    protected Consumer<T> callback() {
        return clientCallback;
    }
}
