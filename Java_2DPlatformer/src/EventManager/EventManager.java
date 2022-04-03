package EventManager;

import java.util.LinkedList;
import java.util.List;

public class EventManager implements EventPublisher {
    private List<EventObserver> observers;

    public EventManager(){
        observers = new LinkedList<>();
    }

    @Override
    public void registerObserver(EventObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(EventObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (EventObserver observer : observers)
            observer.update();
    }

}

