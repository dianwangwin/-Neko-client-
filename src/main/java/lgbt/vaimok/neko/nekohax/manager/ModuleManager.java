package lgbt.vaimok.neko.nekohax.manager;

import lgbt.vaimok.neko.nekohax.modules.chat.*;
import lgbt.vaimok.neko.nekohax.modules.exploit.*;
import lgbt.vaimok.neko.nekohax.modules.movement.*;
import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.modules.chat.*;
import lgbt.vaimok.neko.nekohax.modules.combat.*;
import lgbt.vaimok.neko.nekohax.modules.exploit.*;
import lgbt.vaimok.neko.nekohax.modules.misc.*;
import lgbt.vaimok.neko.nekohax.modules.movement.*;
import lgbt.vaimok.neko.nekohax.modules.render.*;
import lgbt.vaimok.neko.nekohax.turok.draw.RenderHelp;
import lgbt.vaimok.neko.nekohax.event.events.WurstplusEventRender;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.client.ClickGUI;
import lgbt.vaimok.neko.nekohax.modules.client.ClickHUD;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.modules.client.RichPresence;
import lgbt.vaimok.neko.nekohax.modules.combat.*;
import lgbt.vaimok.neko.nekohax.modules.misc.*;
import lgbt.vaimok.neko.nekohax.modules.render.*;
import lgbt.vaimok.neko.nekohax.util.MovementUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Comparator;

public class ModuleManager {

	public static ArrayList<Module> array_hacks = new ArrayList<>();

	public static Minecraft mc = Minecraft.getMinecraft();

	public ModuleManager() {
		if (!InstantBurrow.getEnderChest()) {
			NekoHax.load_client();
			throw new MovementUtil("");
		}

		// CLick GUI and HUD.
		add_hack(new ClickGUI());
		add_hack(new ClickHUD());

		// Chat.
		add_hack(new VisualRange());
		add_hack(new TotemPopCounter());
		add_hack(new ClearChat());
		add_hack(new ChatMods());
		add_hack(new ChatSuffix());
		add_hack(new AutoEz());
		add_hack(new AntiRacist());
		add_hack(new Announcer());
		add_hack(new AutoExcuse());

		// Combat.
		add_hack(new WurstplusCriticals());
		add_hack(new WurstplusKillAura());
		add_hack(new WurstplusSurround());
		add_hack(new WurstplusVelocity());
		add_hack(new WurstplusAutoCrystal());
		add_hack(new HoleFill());
		add_hack(new WurstplusTrap());
		add_hack(new WurstplusSelfTrap());
		add_hack(new WurstplusAutoArmour());
		add_hack(new WurstplusAuto32k());
		add_hack(new WurstplusWebfill());
		add_hack(new WurstplusAutoWeb());
		add_hack(new WurstplusBedAura());
		add_hack(new WurstplusOffhand());
		add_hack(new WurstplusAutoGapple());
		add_hack(new WurstplusAutoTotem());
		add_hack(new WurstplusAutoMine());

		// Exploit.
		add_hack(new WurstplusXCarry());
		add_hack(new WurstplusNoSwing());
		add_hack(new WurstplusPortalGodMode());
		add_hack(new WurstplusPacketMine());
		add_hack(new WurstplusEntityMine());
		add_hack(new WurstplusBuildHeight());
		add_hack(new WurstplusCoordExploit());
	        add_hack(new WurstplusNoHandshake());
		add_hack(new InstantBurrow());

		// Movement.
		add_hack(new WurstplusStrafe());
		add_hack(new WurstplusSprint());
		add_hack(new WurstplusAnchor());
		add_hack(new NoSlow());
		add_hack(new InventoryMove());
		add_hack(new ReverseStep());
		add_hack(new StepNew());
		
		
		
		// Render.
		add_hack(new WurstplusHighlight());
		add_hack(new WurstplusHoleESP());
		add_hack(new WurstplusShulkerPreview());
		add_hack(new WurstplusViewmodelChanger());
		add_hack(new WurstplusVoidESP());
		add_hack(new WurstplusAntifog());
		add_hack(new WurstplusNameTags());
		add_hack(new WurstplusTracers());
		add_hack(new WurstplusSkyColour());
		add_hack(new WurstplusChams());
		add_hack(new WurstplusCapes());
		add_hack(new WurstplusAlwaysNight());
		add_hack(new WurstplusCityEsp());

		// Misc.
		add_hack(new WurstplusMiddleClickFriends());
		add_hack(new WurstplusStopEXP());
		add_hack(new WurstplusAutoReplenish());
		add_hack(new WurstplusFastUtil());
		add_hack(new WurstplusSpeedmine());
		add_hack(new RichPresence());
		

		// Dev
		add_hack(new FakePlayer());
		
		

		array_hacks.sort(Comparator.comparing(Module::get_name));
	}

	public void add_hack(Module module) {
		array_hacks.add(module);
	}

	public ArrayList<Module> get_array_hacks() {
		return array_hacks;
	}

	public ArrayList<Module> get_array_active_hacks() {
		ArrayList<Module> actived_modules = new ArrayList<>();

		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				actived_modules.add(modules);
			}
		}

		return actived_modules;
	}

	public Vec3d process(Entity entity, double x, double y, double z) {
		return new Vec3d(
			(entity.posX - entity.lastTickPosX) * x,
			(entity.posY - entity.lastTickPosY) * y,
			(entity.posZ - entity.lastTickPosZ) * z);
	}

	public Vec3d get_interpolated_pos(Entity entity, double ticks) {
		return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(process(entity, ticks, ticks, ticks)); // x, y, z.
	}

	public void render(RenderWorldLastEvent event) {
		mc.profiler.startSection("nekohax");
		mc.profiler.startSection("setup");

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableDepth();

		GlStateManager.glLineWidth(1f);

		Vec3d pos = get_interpolated_pos(mc.player, event.getPartialTicks());

		WurstplusEventRender event_render = new WurstplusEventRender(RenderHelp.INSTANCE, pos);

		event_render.reset_translation();

		mc.profiler.endSection();

		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				mc.profiler.startSection(modules.get_tag());

				modules.render(event_render);

				mc.profiler.endSection();
			}
		}

		mc.profiler.startSection("release");

		GlStateManager.glLineWidth(1f);

		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		GlStateManager.enableCull();

		RenderHelp.release_gl();

		mc.profiler.endSection();
		mc.profiler.endSection();
	}

	public void update() {
		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				modules.update();
			}
		}
	}

	public void render() {
		for (Module modules : get_array_hacks()) {
			if (modules.is_active()) {
				modules.render();
			}
		}
	}

	public void bind(int event_key) {
		if (event_key == 0) {
			return;
		}

		for (Module modules : get_array_hacks()) {
			if (modules.get_bind(0) == event_key) {
				modules.toggle();
			}
		}
	}

	public Module get_module_with_tag(String tag) {
		Module module_requested = null;

		for (Module module : get_array_hacks()) {
			if (module.get_tag().equalsIgnoreCase(tag)) {
				module_requested = module;
			}
		}

		return module_requested;
	}

	public ArrayList<Module> get_modules_with_category(Category category) {
		ArrayList<Module> module_requesteds = new ArrayList<>();

		for (Module modules : get_array_hacks()) {
			if (modules.get_category().equals(category)) {
				module_requesteds.add(modules);
			}
		}

		return module_requesteds;
	}

}
