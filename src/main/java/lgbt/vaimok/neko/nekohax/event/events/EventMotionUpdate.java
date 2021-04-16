package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.EventCancellable;

public class EventMotionUpdate extends EventCancellable {

    public int stage;

    public EventMotionUpdate(int stage) {
        super();
        this.stage = stage;
    }
    
}