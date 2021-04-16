package me.neko.wurstplus.wurstplustwo.event.rotateevents;

import me.neko.wurstplus.wurstplustwo.event.WurstplusEventCancellable;

public interface MultiPhase<T extends WurstplusEventCancellable> {
    Phase getPhase();

    T nextPhase();
}
