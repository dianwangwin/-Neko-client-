package me.neko.wurstplus.wurstplustwo.event.events;

import me.neko.wurstplus.wurstplustwo.event.WurstplusEventCancellable;
import net.minecraft.util.EnumHand;

public class WurstplusEventSwing extends WurstplusEventCancellable {
    
    public EnumHand hand;

    public WurstplusEventSwing(EnumHand hand) {
        super();
        this.hand = hand;
    }

}