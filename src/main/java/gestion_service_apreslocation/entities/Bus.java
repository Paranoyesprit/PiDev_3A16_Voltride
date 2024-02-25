package gestion_service_apreslocation.entities;



import java.util.ArrayList;
import java.util.List;

public class Bus {
    private static final Bus instance = new Bus();
    private final List<EventListener> listeners = new ArrayList<>();

    public static Bus getInstance() {
        return instance;
    }

    public void register(EventListener listener) {
        listeners.add(listener);
    }

    public void unregister(EventListener listener) {
        listeners.remove(listener);
    }

    public void notifyTableRefreshed() {
        for (EventListener listener : listeners) {
            listener.onTableRefreshed();
        }
    }

    public interface EventListener {
        void onTableRefreshed();
    }
}