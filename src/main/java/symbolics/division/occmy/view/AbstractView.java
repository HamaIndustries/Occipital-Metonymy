package symbolics.division.occmy.view;

import java.util.function.Consumer;

public abstract class AbstractView<T> implements View {
    // to be honest I didn't expect most of these to be entirely on the client
    // so pretty much every view is just a client callback wrapper whoops

    protected Consumer<T> clientCallback = d -> {
    };

    public void setCallback(Consumer<T> cb) {
        this.clientCallback = cb;
    }

    protected Consumer<T> callback() {
        return clientCallback;
    }
}
