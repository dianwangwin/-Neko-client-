package lgbt.vaimok.neko.nekohax.event;

import me.zero.alpine.fork.bus.EventBus;
import me.zero.alpine.fork.bus.EventManager;

public class EventBusTwo { //This class can't be called "EventBus"
	public static final EventBus EVENT_BUS = new EventManager();
}