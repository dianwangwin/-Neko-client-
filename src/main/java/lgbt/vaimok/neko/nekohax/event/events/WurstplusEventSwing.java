package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.WurstplusEventCancellable;
import net.minecraft.util.EnumHand;

public class WurstplusEventSwing extends WurstplusEventCancellable {
    
    public EnumHand hand;

    public WurstplusEventSwing(EnumHand hand) {
        super();
        this.hand = hand;
    }

}