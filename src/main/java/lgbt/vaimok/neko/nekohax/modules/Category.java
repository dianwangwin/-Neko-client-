package lgbt.vaimok.neko.nekohax.modules;

public enum Category {
	chat("Chat", "Chat", false),
	combat("Combat", "Combat", false),
	movement("Movement", "Movement", false),
	render("Render", "Render", false),
	exploit("Exploit", "Exploit", false),
	misc("Misc", "Misc", false),
	client("Client", "Client", false),
	beta("Beta", "Beta", false),
	hidden("Hidden", "Hidden", true);

	String name;
	String tag;
	boolean hiddencategory;

	Category(String name, String tag, boolean hiddencategory) {
		this.name   = name;
		this.tag    = tag;
		this.hiddencategory = hiddencategory;
	}

	public boolean is_hidden() {
		return this.hiddencategory;
	}

	public String get_name() {
		return this.name;
	}

	public String get_tag() {
		return this.tag;
	}
}