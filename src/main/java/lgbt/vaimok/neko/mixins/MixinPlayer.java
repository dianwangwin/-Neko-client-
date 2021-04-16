package lgbt.vaimok.neko.mixins;

import lgbt.vaimok.neko.nekohax.event.events.EventPlayerTravel;
import lgbt.vaimok.neko.nekohax.event.EventBusTwo;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityPlayer.class)
public class MixinPlayer extends MixinEntity {
    
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	public void travel(float strafe, float vertical, float forward, CallbackInfo info) {
        EventPlayerTravel event_packet = new EventPlayerTravel(strafe, vertical, forward);

		EventBusTwo.EVENT_BUS.post(event_packet);

		if (event_packet.isCancelled()) {
			move(MoverType.SELF, motionX, motionY, motionZ);
			info.cancel();
		}
	}

}