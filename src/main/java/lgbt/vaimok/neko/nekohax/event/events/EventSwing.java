package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.EventCancellable;
import net.minecraft.util.EnumHand;

public class EventSwing extends EventCancellable {
    
    public EnumHand hand;

    public EventSwing(EnumHand hand) {
        super();
        this.hand = hand;
    }

}