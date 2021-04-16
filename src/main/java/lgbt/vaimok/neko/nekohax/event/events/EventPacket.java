package lgbt.vaimok.neko.nekohax.event.events;

import lgbt.vaimok.neko.nekohax.event.EventCancellable;
import net.minecraft.network.Packet;

// External.


public class EventPacket extends EventCancellable {
	private final Packet packet;

	public EventPacket(Packet packet) {
		super();

		this.packet = packet;
	}

	public Packet get_packet() {
		return this.packet;
	}

	public static class ReceivePacket extends EventPacket {
		public ReceivePacket(Packet packet) {
			super(packet);
		}
	}

	public static class SendPacket extends EventPacket {
		public SendPacket(Packet packet) {
			super(packet);
		}
	}
}