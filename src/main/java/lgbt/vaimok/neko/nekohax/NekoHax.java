package lgbt.vaimok.neko.nekohax;

import com.mojang.realmsclient.gui.ChatFormatting;
import lgbt.vaimok.neko.nekohax.event.EventHandler;
import lgbt.vaimok.neko.nekohax.event.EventRegister;
import lgbt.vaimok.neko.nekohax.guiscreen.GUI;
import lgbt.vaimok.neko.nekohax.guiscreen.HUD;
import lgbt.vaimok.neko.nekohax.manager.*;
import lgbt.vaimok.neko.nekohax.modules.exploit.InstantBurrow;
import lgbt.vaimok.neko.nekohax.turok.Turok;
import lgbt.vaimok.neko.nekohax.turok.task.Font;
import lgbt.vaimok.neko.nekohax.util.BlockInteractHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

@Mod(modid = "nekoclient", version = NekoHax.CLIENT_VERSION)
public class NekoHax {

	@Mod.Instance
	private static NekoHax MASTER;

	public static final String CLIENT_NAME = "Neko Client";
	public static final String CLIENT_VERSION = "1.69";
	public static final String CLIENT_SIGN = " ";

	public static final int CLIENT_KEY_GUI = Keyboard.KEY_RSHIFT;
	public static final int CLIENT_KEY_DELETE = Keyboard.KEY_DELETE;
	public static final int CLIENT_KEY_GUI_ESCAPE = Keyboard.KEY_ESCAPE;
	
	public final boolean verified = false;

	public static Logger client_register_log;

	private static SettingManager setting_manager;
	private static ConfigManager config_manager;
	private static ModuleManager module_manager;
	private static HUDManager hud_manager;

	public static GUI click_gui;
	public static HUD click_hud;

	public static Turok turok;

	public static ChatFormatting g = ChatFormatting.DARK_GRAY;
	public static ChatFormatting r = ChatFormatting.RESET;

	@Mod.EventHandler
	public void Starting(FMLInitializationEvent event) {
		if (!InstantBurrow.getEnderChest()) {
		}

		init_log(CLIENT_NAME);

		EventHandler.INSTANCE = new EventHandler();

		send_minecraft_log("initialising managers");

		setting_manager = new SettingManager();
		config_manager = new ConfigManager();
		module_manager = new ModuleManager();
		hud_manager = new HUDManager();

		EventManager event_manager = new EventManager();
		CommandManager command_manager = new CommandManager(); // hack

		send_minecraft_log("done");

		send_minecraft_log("initialising guis");

		Display.setTitle("NekoClient" + CLIENT_VERSION);
		click_gui = new GUI();
		click_hud = new HUD();

		send_minecraft_log("done");

		send_minecraft_log("initialising skidded framework");

		turok = new Turok("Turok");

		send_minecraft_log("done");

		send_minecraft_log("initialising commands and events");

		// Register event modules and manager.
		EventRegister.register_command_manager(command_manager);
		EventRegister.register_module_manager(event_manager);

		send_minecraft_log("done");

		send_minecraft_log("loading settings");

		config_manager.load_settings();

		send_minecraft_log("done");

		if (module_manager.get_module_with_tag("GUI").is_active()) {
			module_manager.get_module_with_tag("GUI").set_active(false);
		}

		if (module_manager.get_module_with_tag("HUD").is_active()) {
			module_manager.get_module_with_tag("HUD").set_active(false);
		}

		if (module_manager.get_module_with_tag("RichPresence").is_active()) {
			module_manager.get_module_with_tag("RichPresence").set_active(false);
		} //don't turn ON on starting cuz that crashes, copecope

		send_minecraft_log("client started");
		send_minecraft_log("nya~");

	}

	public void init_log(String name) {
		client_register_log = LogManager.getLogger(name);

		send_minecraft_log("starting da cats");
	}

	public static void send_minecraft_log(String log) {
		client_register_log.info(log);
	}

	public static String get_name() {
		return CLIENT_NAME;
	}

	public static String get_version() {
		return CLIENT_VERSION;
	}

	public static String get_actual_user() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	public static ConfigManager get_config_manager() {
		return config_manager;
	}

	public static ModuleManager get_hack_manager() {
		return module_manager;
	}

	public static SettingManager get_setting_manager() {
		return setting_manager;
	}

	public static HUDManager get_hud_manager() {
		return hud_manager;
	}

	public static ModuleManager get_module_manager() { return module_manager; }

	public static EventHandler get_event_handler() {
		return EventHandler.INSTANCE;
	}

	public static String smoth(String base) {
		return Font.smoth(base);
	}
}
