package lgbt.vaimok.neko.nekohax.modules.misc;

import com.mojang.authlib.GameProfile;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {
    
    public FakePlayer() {
        super(Category.misc);

		this.name        = "Fake Player";
		this.tag         = "FakePlayer";
		this.description = "hahahaaha what a noob its in beta ahahahahaha";
    }

    private EntityOtherPlayerMP fake_player;

    @Override
    protected void enable() {
        
        fake_player = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("0975093c-1eef-4e7a-b924-20af7172f022"), "Vaimok"));
        fake_player.copyLocationAndAnglesFrom(mc.player);
        fake_player.rotationYawHead = mc.player.rotationYawHead;
        mc.world.addEntityToWorld(-100, fake_player);

    }

    @Override
    protected void disable() {
        try {
            mc.world.removeEntity(fake_player);
        } catch (Exception ignored) {}
    }

}
