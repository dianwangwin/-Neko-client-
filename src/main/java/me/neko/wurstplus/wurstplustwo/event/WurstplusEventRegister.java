package me.neko.wurstplus.wurstplustwo.event;

import me.neko.wurstplus.wurstplustwo.manager.CommandManager;
import me.neko.wurstplus.wurstplustwo.manager.EventManager;
import net.minecraftforge.common.MinecraftForge;


public class WurstplusEventRegister {
	public static void register_command_manager(CommandManager manager) {
		MinecraftForge.EVENT_BUS.register(manager);
	}

	public static void register_module_manager(EventManager manager) {
		MinecraftForge.EVENT_BUS.register(manager);
	}
}