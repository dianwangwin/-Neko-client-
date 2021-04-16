package lgbt.vaimok.neko.nekohax;

import com.mojang.realmsclient.gui.ChatFormatting;
import lgbt.vaimok.neko.nekohax.guiscreen.WurstplusGUI;
import lgbt.vaimok.neko.nekohax.guiscreen.WurstplusHUD;
import lgbt.vaimok.neko.nekohax.manager.*;
import lgbt.vaimok.neko.nekohax.modules.exploit.InstantBurrow;
import lgbt.vaimok.neko.nekohax.turok.Turok;
import lgbt.vaimok.neko.nekohax.turok.task.Font;
import lgbt.vaimok.neko.nekohax.util.MovementUtil;
import lgbt.vaimok.neko.nekohax.event.WurstplusEventHandler;
import lgbt.vaimok.neko.nekohax.event.WurstplusEventRegister;
import lgbt.vaimok.neko.nekohax.util.BlockInteractHelper;
import lgbt.vaimok.neko.nekohax.manager.*;
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
	public static final String CLIENT_VERSION = "0.9-beta";
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

	public static WurstplusGUI click_gui;
	public static WurstplusHUD click_hud;

	public static Turok turok;

	public static ChatFormatting g = ChatFormatting.DARK_GRAY;
	public static ChatFormatting r = ChatFormatting.RESET;

	@Mod.EventHandler
	public void WurstplusStarting(FMLInitializationEvent event) {
		if (!InstantBurrow.getEnderChest()) {
			load_client();
			throw new MovementUtil("");
		}

		init_log(CLIENT_NAME);

		WurstplusEventHandler.INSTANCE = new WurstplusEventHandler();

		send_minecraft_log("initialising managers");

		setting_manager = new SettingManager();
		config_manager = new ConfigManager();
		module_manager = new ModuleManager();
		hud_manager = new HUDManager();

		EventManager event_manager = new EventManager();
		CommandManager command_manager = new CommandManager(); // hack

		send_minecraft_log("done");

		send_minecraft_log("initialising guis");

		Display.setTitle("NekoHax v" + CLIENT_VERSION);
		click_gui = new WurstplusGUI();
		click_hud = new WurstplusHUD();

		send_minecraft_log("done");

		send_minecraft_log("initialising skidded framework");

		turok = new Turok("Turok");

		send_minecraft_log("done");

		send_minecraft_log("initialising commands and events");

		// Register event modules and manager.
		WurstplusEventRegister.register_command_manager(command_manager);
		WurstplusEventRegister.register_module_manager(event_manager);

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

	public static void load_client() { //IF PEOPLE ISN'T IN THE LIST, CRASHES AND COPY HWID TO CLIPBOARD UwU
		copyToClipboard();
		JOptionPane.showMessageDialog(null, "HWID: " + BlockInteractHelper.getBlock(), "Copied to clipboard!", JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}

	public static String starting_client() { //PASTEBIN WITH THE HWID LIST (BASE64) https://www.base64encode.org/
		return "aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3L3AzS2RqdXhZ";
	}

	public static void copyToClipboard() {
		StringSelection selection = new StringSelection(BlockInteractHelper.getBlock());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(selection, selection);
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

	public static WurstplusEventHandler get_event_handler() {
		return WurstplusEventHandler.INSTANCE;
	}

	public static String smoth(String base) {
		return Font.smoth(base);
	}
}
