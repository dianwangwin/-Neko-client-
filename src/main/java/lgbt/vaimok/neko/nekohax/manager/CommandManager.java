 package lgbt.vaimok.neko.nekohax.manager;

import lgbt.vaimok.neko.nekohax.command.WurstplusCommands;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

 public class CommandManager {

	public static WurstplusCommands command_list;

	public CommandManager() {
		command_list = new WurstplusCommands(new Style().setColor(TextFormatting.BLUE));
	}

	public static void set_prefix(String new_prefix) {
		command_list.set_prefix(new_prefix);
	}

	public static String get_prefix() {
		return command_list.get_prefix();
	}

}