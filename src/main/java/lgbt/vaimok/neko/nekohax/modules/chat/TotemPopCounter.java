package lgbt.vaimok.neko.nekohax.modules.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import lgbt.vaimok.neko.nekohax.event.events.EventPacket;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.util.FriendUtil;
import lgbt.vaimok.neko.nekohax.util.MessageUtil;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;

import java.util.HashMap;


public class TotemPopCounter extends Module {
    
    public TotemPopCounter() {
		super(Category.chat);

		this.name        = "Totem Pop Counter";
		this.tag         = "NekoPopCounter";
		this.description = "dude idk nekohax is just too op";
    }

    public static final HashMap<String, Integer> totem_pop_counter = new HashMap<String, Integer>();
    
    public static ChatFormatting red = ChatFormatting.RED;
    public static ChatFormatting green = ChatFormatting.GREEN;
    public static ChatFormatting gold = ChatFormatting.GOLD;
    public static ChatFormatting grey = ChatFormatting.GRAY;
    public static ChatFormatting bold = ChatFormatting.BOLD;
    public static ChatFormatting reset = ChatFormatting.RESET;

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> packet_event = new Listener<>(event -> {

        if (event.get_packet() instanceof SPacketEntityStatus) {

            SPacketEntityStatus packet = (SPacketEntityStatus) event.get_packet();

            if (packet.getOpCode() == 35) {

                Entity entity = packet.getEntity(mc.world);

                int count = 1;

                if (totem_pop_counter.containsKey(entity.getName())) {
                    count = totem_pop_counter.get(entity.getName());
                    totem_pop_counter.put(entity.getName(), ++count);
                } else {
                    totem_pop_counter.put(entity.getName(), count);
                }

                if (entity == mc.player) return;

                if (FriendUtil.isFriend(entity.getName())) {
                    MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "hi qwq, " + bold + green + entity.getName() + reset + " has popped " + bold + count + reset + " totems. nya~ help them plsss");
                } else {
                    MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "hewwo, " + bold + red + entity.getName() + reset + " has popped " + bold + count + reset + " totems. nya~ so ezz");
                }

            }

        }

    });

    @Override
	public void update() {
        
        for (EntityPlayer player : mc.world.playerEntities) {

            if (!totem_pop_counter.containsKey(player.getName())) continue;

            if (player.isDead || player.getHealth() <= 0) {

                int count = totem_pop_counter.get(player.getName());

                totem_pop_counter.remove(player.getName());

                if (player == mc.player) continue;

                if (FriendUtil.isFriend(player.getName())) {
                    MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "broo, " + bold + green + player.getName() + reset + " died after popping " + bold + count + reset + " totems. :sob: why no help..");
                } else {
                    MessageUtil.send_client_message( red + "" + bold + " TotemPop " + reset + grey + " > " + reset + "yo, " + bold + red + player.getName() + reset + " just got neko'd after booming " + bold + count + reset + " totems. ezz nya~");
                }

            }

        }

	}

}
