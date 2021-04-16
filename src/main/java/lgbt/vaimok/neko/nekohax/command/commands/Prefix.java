package lgbt.vaimok.neko.nekohax.command.commands;


import lgbt.vaimok.neko.nekohax.command.Command;
import lgbt.vaimok.neko.nekohax.manager.CommandManager;
import lgbt.vaimok.neko.nekohax.util.MessageUtil;


public class Prefix extends Command {
	public Prefix() {
		super("prefix", "Change prefix.");
	}

	public boolean get_message(String[] message) {
		String prefix = "null";

		if (message.length > 1) {
			prefix = message[1];
		}

		if (message.length > 2) {
			MessageUtil.send_client_error_message(current_prefix() + "prefix <character>");

			return true;
		}

		if (prefix.equals("null")) {
			MessageUtil.send_client_error_message(current_prefix() + "prefix <character>");

			return true;
		}

		CommandManager.set_prefix(prefix);

		MessageUtil.send_client_message("The new prefix is " + prefix);

		return true;
	}
}