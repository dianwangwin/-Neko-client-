package me.neko.wurstplus.wurstplustwo.command.commands;

import me.neko.wurstplus.NekoHax;
import me.neko.wurstplus.wurstplustwo.command.WurstplusCommand;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import me.neko.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import org.lwjgl.input.Keyboard;

public class WurstplusBind extends WurstplusCommand {
	public WurstplusBind() {
		super("bind", "bind module to key");
	}

	public boolean get_message(String[] message) {
		String module = "null;";
		String key = "null";

		if (message.length == 3) {
			module = message[1].toUpperCase();
			key = message[2].toUpperCase();
		} else if (message.length > 1) {
			WurstplusMessageUtil.send_client_error_message(current_prefix() + "bind <ModuleTag> <key>");

			return true;
		}

		if (module.equals("null") || key.equals("null")) {
			WurstplusMessageUtil.send_client_error_message(current_prefix() + "bind <ModuleTag> <key>");

			return true;
		}

		Module module_requested = NekoHax.get_hack_manager().get_module_with_tag(module);

		if (module_requested == null) {
			WurstplusMessageUtil.send_client_error_message("Module does not exist.");

			return true;
		}

		if (key.equalsIgnoreCase("NONE")) {
			module_requested.set_bind(0);

			WurstplusMessageUtil.send_client_message(module_requested.get_tag() + " is bound to None.");

			return true;
		}

		int new_bind = Keyboard.getKeyIndex(key.toUpperCase());

		if (new_bind == 0) {
			WurstplusMessageUtil.send_client_error_message("Key does not exist.");

			return true;
		}

		if (new_bind == module_requested.get_bind(0)) {
			WurstplusMessageUtil.send_client_error_message(module_requested.get_tag() + " is already bound to this key");

			return true;
		}

		module_requested.set_bind(new_bind);

		WurstplusMessageUtil.send_client_message(module_requested.get_tag() +  " is bound to " + module_requested.get_bind(""));

		return true;
	}
}