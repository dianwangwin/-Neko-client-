package lgbt.vaimok.neko.nekohax.modules.movement;


import lgbt.vaimok.neko.nekohax.event.EventCancellable;
import lgbt.vaimok.neko.nekohax.event.events.EventEntity;
import lgbt.vaimok.neko.nekohax.event.events.EventPacket;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;


public class Velocity extends Module {
	public Velocity() {
		super(Category.movement);

		this.name        = "Velocity";
		this.tag         = "Velocity";
		this.description = "No kockback";
	}

	@EventHandler
	private Listener<EventPacket.ReceivePacket> damage = new Listener<>(event -> {
		if (event.get_era() == EventCancellable.Era.EVENT_PRE) {
			if (event.get_packet() instanceof SPacketEntityVelocity) {
				SPacketEntityVelocity knockback = (SPacketEntityVelocity) event.get_packet();

				if (knockback.getEntityID() == mc.player.getEntityId()) {
					event.cancel();

					knockback.motionX *= 0.0f;
					knockback.motionY *= 0.0f;
					knockback.motionZ *= 0.0f;
				}
			} else if (event.get_packet() instanceof SPacketExplosion) {
				event.cancel();

				SPacketExplosion knockback = (SPacketExplosion) event.get_packet();

				knockback.motionX *= 0.0f;
				knockback.motionY *= 0.0f;
				knockback.motionZ *= 0.0f;
			}
		}
	});

	@EventHandler
	private Listener<EventEntity.EventColision> explosion = new Listener<>(event -> {
		if (event.get_entity() == mc.player) {
			event.cancel();

		}
	});
}