package me.travis.wurstplus;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRichPresence;
import club.minnced.discord.rpc.DiscordRPC;
import net.minecraft.client.Minecraft;

public class DiscordUtils
{
    private static final String ClientId = "827716700660826113";
    private static final Minecraft mc;
    private static final DiscordRPC rpc;
    public static DiscordRichPresence presence;
    private static String details;
    private static String state;
    
    public static void init() {
        final DiscordEventHandlers handlers = new DiscordEventHandlers();
        handlers.disconnected = ((var1, var2) -> System.out.println("Discord RPC disconnected, var1: " + String.valueOf(var1) + ", var2: " + var2));
        DiscordUtils.rpc.Discord_Initialize("827716700660826113", handlers, true, "");
        DiscordUtils.presence.startTimestamp = System.currentTimeMillis() / 1000L;
        DiscordUtils.presence.details = "nya~ im playing with " + mc.player.getName();
        DiscordUtils.presence.state = "Main Menu";
        DiscordUtils.presence.largeImageKey = "neko";
        DiscordUtils.presence.largeImageText = Wurstplus.WURSTPLUS_VERSION;
        DiscordUtils.rpc.Discord_UpdatePresence(DiscordUtils.presence);
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                	DiscordUtils.rpc.Discord_RunCallbacks();
                	DiscordUtils.details = "IGN: " + mc.player.getName();
                	DiscordUtils.state = "";
                    if (DiscordUtils.mc.isIntegratedServerRunning()) {
                    	DiscordUtils.state = "Playing on Singleplayer";
                    }
                    else if (DiscordUtils.mc.getCurrentServerData() != null) {
                        if (!DiscordUtils.mc.getCurrentServerData().serverIP.equals("")) {
                        	DiscordUtils.state = "Playing on " + DiscordUtils.mc.getCurrentServerData().serverIP;
                        }
                    }
                    else {
                    	DiscordUtils.state = "Main Menu";
                    }
                    if (!DiscordUtils.details.equals(DiscordUtils.presence.details) || !DiscordUtils.state.equals(DiscordUtils.presence.state)) {
                    	DiscordUtils.presence.startTimestamp = System.currentTimeMillis() / 1000L;
                    }
                    DiscordUtils.presence.details = DiscordUtils.details;
                    DiscordUtils.presence.state = DiscordUtils.state;
                    DiscordUtils.rpc.Discord_UpdatePresence(DiscordUtils.presence);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(5000L);
                }
                catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }, "Discord-RPC-Callback-Handler").start();
    }
    
    static {
        mc = Minecraft.getMinecraft();
        rpc = DiscordRPC.INSTANCE;
        DiscordUtils.presence = new DiscordRichPresence();
    }
    public static void shutdown() {
        rpc.Discord_Shutdown();
    }
}

