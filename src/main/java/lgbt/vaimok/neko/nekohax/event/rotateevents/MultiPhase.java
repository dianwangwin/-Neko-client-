package lgbt.vaimok.neko.nekohax.event.rotateevents;


import lgbt.vaimok.neko.nekohax.event.EventCancellable;

public interface MultiPhase<T extends EventCancellable> {
    Phase getPhase();

    T nextPhase();
}
