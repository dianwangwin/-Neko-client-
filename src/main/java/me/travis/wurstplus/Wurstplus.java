package me.travis.wurstplus;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.turok.Turok;
import me.travis.turok.task.Font;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventHandler;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventRegister;
import me.travis.wurstplus.wurstplustwo.guiscreen.WurstplusGUI;
import me.travis.wurstplus.wurstplustwo.guiscreen.WurstplusHUD;
import me.travis.wurstplus.wurstplustwo.manager.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(modid = "nekoclient", version = Wurstplus.WURSTPLUS_VERSION)
public class Wurstplus {

	@Mod.Instance
	private static Wurstplus MASTER;

	public static final String WURSTPLUS_NAME = "Neko Client";
	public static final String WURSTPLUS_VERSION = "0.9-beta";
	public static final String WURSTPLUS_SIGN = " ";

	public static final int WURSTPLUS_KEY_GUI = Keyboard.KEY_RSHIFT;
	public static final int WURSTPLUS_KEY_DELETE = Keyboard.KEY_DELETE;
	public static final int WURSTPLUS_KEY_GUI_ESCAPE = Keyboard.KEY_ESCAPE;
	
	public final boolean verified = false;

	public static Logger wurstplus_register_log;

	private static WurstplusSettingManager setting_manager;
	private static WurstplusConfigManager config_manager;
	private static WurstplusModuleManager module_manager;
	private static WurstplusHUDManager hud_manager;

	public static WurstplusGUI click_gui;
	public static WurstplusHUD click_hud;

	public static Turok turok;

	public static ChatFormatting g = ChatFormatting.DARK_GRAY;
	public static ChatFormatting r = ChatFormatting.RESET;

	@Mod.EventHandler
	public void WurstplusStarting(FMLInitializationEvent event) {
		
		 @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        final String var0 = String.valueOf(System.getenv("os")) + System.getProperty("os.name") + System.getProperty("os.arch") + System.getProperty("os.version") + System.getProperty("user.language") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS");
        final String sha512hex = DigestUtils.sha512Hex(var0);
        final String key = DigestUtils.sha512Hex(sha512hex);
        try {
            String list = "*******PASTEBIN URL HERE*****";
            URL pastebin = new URL(list.toString());
            BufferedReader in = new BufferedReader(new InputStreamReader(pastebin.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.equalsIgnoreCase(key))
                    verified = true;
            }
            if (!verified) {
                JOptionPane.showMessageDialog(null, "copied hwid");
                StringSelection stringSelection = new StringSelection(key);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        } catch (Exception exception) {}
        if (!verified) {
            Runtime.getRuntime().halt(0);
        }
    }

		init_log(WURSTPLUS_NAME);

		WurstplusEventHandler.INSTANCE = new WurstplusEventHandler();

		send_minecraft_log("initialising managers");

		setting_manager = new WurstplusSettingManager();
		config_manager = new WurstplusConfigManager();
		module_manager = new WurstplusModuleManager();
		hud_manager = new WurstplusHUDManager();

		WurstplusEventManager event_manager = new WurstplusEventManager();
		WurstplusCommandManager command_manager = new WurstplusCommandManager(); // hack

		send_minecraft_log("done");

		send_minecraft_log("initialising guis");

		Display.setTitle("NekoHax");
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

		send_minecraft_log("client started");
		send_minecraft_log("nya~");

	}

	public void init_log(String name) {
		wurstplus_register_log = LogManager.getLogger(name);

		send_minecraft_log("starting da cats");
	}

	public static void send_minecraft_log(String log) {
		wurstplus_register_log.info(log);
	}

	public static String get_name() {
		return  WURSTPLUS_NAME;
	}

	public static String get_version() {
		return WURSTPLUS_VERSION;
	}

	public static String get_actual_user() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	public static WurstplusConfigManager get_config_manager() {
		return config_manager;
	}

	public static WurstplusModuleManager get_hack_manager() {
		return module_manager;
	}

	public static WurstplusSettingManager get_setting_manager() {
		return setting_manager;
	}

	public static WurstplusHUDManager get_hud_manager() {
		return hud_manager;
	}

	public static WurstplusModuleManager get_module_manager() { return module_manager; }

	public static WurstplusEventHandler get_event_handler() {
		return WurstplusEventHandler.INSTANCE;
	}

	public static String smoth(String base) {
		return Font.smoth(base);
	}
}
