package lgbt.vaimok.neko.nekohax.event.rotateevents;

import lgbt.vaimok.neko.nekohax.event.EventCancellable;
import net.minecraft.entity.Entity;

public class EntityRenderEvent extends EventCancellable {
    public final Entity entity;
    public final Type type;

    public EntityRenderEvent(Entity entity, Type type) {
        this.entity = entity;
        this.type = type;
    }

    public enum Type {
        TEXTURE,
        COLOR
    }

    public Entity getEntity() {
        return this.entity;
    }

    public Type getType() {
        return this.type;
    }

    public static class Head extends EntityRenderEvent {
        public Head(Entity entity, Type type) {
            super(entity, type);
        }
    }

    public static class Return extends EntityRenderEvent {
        public Return(Entity entity, Type type) {
            super(entity, type);
        }
    }
}
