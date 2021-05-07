package lgbt.vaimok.neko.nekohax.manager;

import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.event.events.EventRender;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.modules.chat.*;
import lgbt.vaimok.neko.nekohax.modules.client.ClickGUI;
import lgbt.vaimok.neko.nekohax.modules.client.ClickHUD;
import lgbt.vaimok.neko.nekohax.modules.client.RichPresence;
import lgbt.vaimok.neko.nekohax.modules.combat.*;
import lgbt.vaimok.neko.nekohax.modules.exploit.*;
import lgbt.vaimok.neko.nekohax.modules.misc.*;
import lgbt.vaimok.neko.nekohax.modules.movement.*;
import lgbt.vaimok.neko.nekohax.modules.render.*;
import lgbt.vaimok.neko.nekohax.turok.draw.RenderHelp;
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
		{
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
		add_hack(new Criticals());
		add_hack(new KillAura());
		add_hack(new Surround());
		add_hack(new Velocity());
		add_hack(new AutoCrystal());
		add_hack(new HoleFill());
		add_hack(new AutoTrap());
		add_hack(new SelfTrap());
		add_hack(new AutoArmour());
		add_hack(new Auto32k());
		add_hack(new Webfill());
		add_hack(new AutoWeb());
		add_hack(new BedAura());
		add_hack(new Offhand());
		add_hack(new AutoGapple());
		add_hack(new AutoTotem());
		add_hack(new AutoMine());

		// Exploit.
		add_hack(new XCarry());
		add_hack(new NoSwing());
		add_hack(new PortalGodMode());
		add_hack(new PacketMine());
		add_hack(new EntityMine());
		add_hack(new BuildHeight());
		add_hack(new CoordExploit());
	        add_hack(new NoHandshake());
		add_hack(new InstantBurrow());
		add_hack(new Lolipics());

		// Movement.
		add_hack(new Strafe());
		add_hack(new Sprint());
		add_hack(new Anchor());
		add_hack(new NoSlow());
		add_hack(new InventoryMove());
		add_hack(new ReverseStep());
		add_hack(new StepNew());
		
		
		
		// Render.
		add_hack(new Highlight());
		add_hack(new HoleESP());
		add_hack(new ShulkerPreview());
		add_hack(new ViewmodelChanger());
		add_hack(new VoidESP());
		add_hack(new Antifog());
		add_hack(new NameTags());
		add_hack(new Tracers());
		add_hack(new SkyColour());
		add_hack(new Chams());
		add_hack(new Capes());
		add_hack(new AlwaysNight());
		add_hack(new CityEsp());

		// Misc.
		add_hack(new MiddleClickFriends());
		add_hack(new StopEXP());
		add_hack(new AutoReplenish());
		add_hack(new FastUtil());
		add_hack(new Speedmine());
		add_hack(new RichPresence());
		add_hack(new MiddleClickXP());

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

		EventRender event_render = new EventRender(RenderHelp.INSTANCE, pos);

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
