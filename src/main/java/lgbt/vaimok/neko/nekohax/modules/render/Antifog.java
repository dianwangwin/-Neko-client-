package lgbt.vaimok.neko.nekohax.modules.render;

import lgbt.vaimok.neko.nekohax.event.events.EventSetupFog;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;

public class Antifog extends Module {
    
    public Antifog() {
        super(Category.render);

        this.name = "Anti Fog";
        this.tag = "AntiFog";
        this.description = "see even more";
    }

    @EventHandler
    private Listener<EventSetupFog> setup_fog = new Listener<> (event -> {

        event.cancel();

        mc.entityRenderer.setupFogColor(false);

        GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.colorMaterial(1028, 4608);

    });

}