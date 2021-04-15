package me.neko.wurstplus.wurstplustwo.event.events;

import me.neko.wurstplus.wurstplustwo.event.WurstplusEventCancellable;

public class WurstplusEventMotionUpdate extends WurstplusEventCancellable {

    public int stage;

    public WurstplusEventMotionUpdate(int stage) {
        super();
        this.stage = stage;
    }
    
}