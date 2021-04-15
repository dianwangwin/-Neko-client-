package me.neko.wurstplus.wurstplustwo.modules.render;

import me.neko.wurstplus.wurstplustwo.event.events.WurstplusEventSetupFog;
import me.neko.wurstplus.wurstplustwo.modules.Category;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;

public class WurstplusAntifog extends Module {
    
    public WurstplusAntifog() {
        super(Category.render);

        this.name = "Anti Fog";
        this.tag = "AntiFog";
        this.description = "see even more";
    }

    @EventHandler
    private Listener<WurstplusEventSetupFog> setup_fog = new Listener<> (event -> {

        event.cancel();

        mc.entityRenderer.setupFogColor(false);

        GlStateManager.glNormal3f(0.0F, -1.0F, 0.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.colorMaterial(1028, 4608);

    });

}