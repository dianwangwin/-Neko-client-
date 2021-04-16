package lgbt.vaimok.neko.nekohax.util.rotation;


import lgbt.vaimok.neko.nekohax.event.rotateevents.EntityRenderEvent;
import lgbt.vaimok.neko.nekohax.event.rotateevents.OnUpdateWalkingPlayerEvent;
import lgbt.vaimok.neko.nekohax.event.rotateevents.PacketEvent;
import lgbt.vaimok.neko.nekohax.event.rotateevents.Phase;
import lgbt.vaimok.neko.nekohax.util.CollectionUtil;
import me.zero.alpine.fork.event.EventPriority;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * FROM KAMI BLUE LOL - MOMIN5
 */
public class PlayerPacketManager {

    public static PlayerPacketManager INSTANCE;

    private final List<PlayerPacket> packets = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private Vec3d prevServerSidePosition = Vec3d.ZERO;
    private Vec3d serverSidePosition = Vec3d.ZERO;

    private Vec2f prevServerSideRotation = Vec2f.ZERO;
    private Vec2f serverSideRotation = Vec2f.ZERO;

    private Vec2f clientSidePitch = Vec2f.ZERO;

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<OnUpdateWalkingPlayerEvent> onUpdateWalkingPlayerEventListener = new Listener<>(event -> {
        if (event.getPhase() != Phase.BY || packets.isEmpty()) return;

        PlayerPacket packet = CollectionUtil.maxOrNull(packets, PlayerPacket::getPriority);

        if (packet != null) {
            event.cancel();
            event.apply(packet);
        }

        packets.clear();
    });

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<PacketEvent.PostSend> postSendListener = new Listener<>(event -> {
        if (event.isCancelled()) return;

        Packet<?> rawPacket = event.getPacket();
        EntityPlayerSP player = mc.player;

        if (player != null && rawPacket instanceof CPacketPlayer) {
            CPacketPlayer packet = (CPacketPlayer) rawPacket;

           // if (packet.moving) {
           //     serverSidePosition = new Vec3d(packet.x, packet.y, packet.z);
           // }

           // if (packet.rotate) {
            //    serverSideRotation = new Vec2f(packet.yaw, packet.pitch);
             //   player.rotationYawHead = packet.yaw;
            //}
        }
    }, EventPriority.LOWEST);

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<TickEvent.ClientTickEvent> tickEventListener = new Listener<>(event -> {
        if (event.phase != TickEvent.Phase.START) return;

        prevServerSidePosition = serverSidePosition;
        prevServerSideRotation = serverSideRotation;
    });

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<EntityRenderEvent.Head> renderEntityEventHeadListener = new Listener<>(event -> {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (player == null || player.isRiding() || event.getType() != EntityRenderEvent.Type.TEXTURE || event.getEntity() != player) return;

        clientSidePitch = new Vec2f(player.prevRotationPitch, player.rotationPitch);
        player.prevRotationPitch = prevServerSideRotation.y;
        player.rotationPitch = serverSideRotation.y;
    });

    @SuppressWarnings("unused")
    @EventHandler
    private final Listener<EntityRenderEvent.Return> renderEntityEventReturnListener = new Listener<>(event -> {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        if (player == null || player.isRiding() || event.getType() != EntityRenderEvent.Type.TEXTURE || event.getEntity() != player) return;

        player.prevRotationPitch = clientSidePitch.x;
        player.rotationPitch = clientSidePitch.y;
    });

    public void addPacket(PlayerPacket packet) {
        packets.add(packet);
    }

    public Vec3d getPrevServerSidePosition() {
        return prevServerSidePosition;
    }

    public Vec3d getServerSidePosition() {
        return serverSidePosition;
    }

    public Vec2f getPrevServerSideRotation() {
        return prevServerSideRotation;
    }

    public Vec2f getServerSideRotation() {
        return serverSideRotation;
    }

}
