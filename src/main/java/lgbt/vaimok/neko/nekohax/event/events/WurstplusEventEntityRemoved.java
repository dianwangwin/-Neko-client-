package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.WurstplusEventCancellable;
import net.minecraft.entity.Entity;

public class WurstplusEventEntityRemoved extends WurstplusEventCancellable {
    
    private final Entity entity;

    public WurstplusEventEntityRemoved(Entity entity) {
        this.entity = entity;
    }

    public Entity get_entity() {
        return this.entity;
    }

}