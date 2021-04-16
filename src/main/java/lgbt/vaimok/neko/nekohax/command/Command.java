package lgbt.vaimok.neko.nekohax.command;


import lgbt.vaimok.neko.nekohax.manager.CommandManager;


public class Command {
	String name;
	String description;

	public Command(String name, String description) {
		this.name        = name;
		this.description = description;
	}

	public boolean get_message(String[] message) {
		return false;
	}

	public String get_name() {
		return this.name;
	}

	public String get_description() {
		return this.description;
	}

	public String current_prefix() {
		return CommandManager.get_prefix();
	}
}