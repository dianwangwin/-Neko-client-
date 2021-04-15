package me.neko.wurstplus.wurstplustwo.command.commands;

import me.neko.wurstplus.NekoHax;
import me.neko.wurstplus.wurstplustwo.command.WurstplusCommand;
import me.neko.wurstplus.wurstplustwo.modules.Module;
import me.neko.wurstplus.wurstplustwo.manager.CommandManager;
import me.neko.wurstplus.wurstplustwo.util.WurstplusMessageUtil;

public class WurstplusToggle extends WurstplusCommand {
	public WurstplusToggle() {
		super("t", "turn on and off stuffs");
	}

	public boolean get_message(String[] message) {
		String module = "null";

		if (message.length > 1) {
			module = message[1];
		}

		if (message.length > 2) {
			WurstplusMessageUtil.send_client_error_message(current_prefix() + "t <ModuleNameNoSpace>");

			return true;
		}

		if (module.equals("null")) {
			WurstplusMessageUtil.send_client_error_message(CommandManager.get_prefix() + "t <ModuleNameNoSpace>");

			return true;
		}

		Module module_requested = NekoHax.get_module_manager().get_module_with_tag(module);

		if (module_requested != null) {
			module_requested.toggle();

			WurstplusMessageUtil.send_client_message("[" + module_requested.get_tag() + "] - Toggled to " + module_requested.is_active() + ".");
		} else {
			WurstplusMessageUtil.send_client_error_message("Module does not exist.");
		}

		return true;
	}
}