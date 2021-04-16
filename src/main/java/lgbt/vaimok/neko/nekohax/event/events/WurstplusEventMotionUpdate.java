package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.WurstplusEventCancellable;

public class WurstplusEventMotionUpdate extends WurstplusEventCancellable {

    public int stage;

    public WurstplusEventMotionUpdate(int stage) {
        super();
        this.stage = stage;
    }
    
}