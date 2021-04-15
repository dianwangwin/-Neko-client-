package me.neko.wurstplus.wurstplustwo.modules.combat;

import me.neko.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.neko.wurstplus.wurstplustwo.guiscreen.settings.Setting;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;


public class WurstplusCriticals extends Module {

	public WurstplusCriticals() {
		super(Category.combat);

		this.name        = "Criticals";
		this.tag         = "Criticals";
		this.description = "You can hit with criticals when attack.";
	}

	Setting mode = create("Mode", "CriticalsMode", "Packet", combobox("Packet", "Jump"));

	@EventHandler
	private Listener<WurstplusEventPacket.SendPacket> listener = new Listener<>(event -> {
		if (event.get_packet() instanceof CPacketUseEntity) {
			CPacketUseEntity event_entity = ((CPacketUseEntity) event.get_packet());

			if (event_entity.getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
				if (mode.in("Packet")) {
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.1f, mc.player.posZ, false));
					mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
				} else if (mode.in("Jump")) {
					mc.player.jump();
				}
			}
		}
	});

	@Override
	public String array_detail() {
		return mode.get_current_value();
	}
}