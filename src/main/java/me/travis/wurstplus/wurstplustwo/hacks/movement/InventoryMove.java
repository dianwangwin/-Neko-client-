package me.travis.wurstplus.wurstplustwo.hacks.movement;

import java.util.function.Predicate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventGUIScreen;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.input.Keyboard;

public class InventoryMove
extends WurstplusHack {
    private static final KeyBinding[] KEYS = new KeyBinding[]{InventoryMove.mc.gameSettings.keyBindForward, InventoryMove.mc.gameSettings.keyBindRight, InventoryMove.mc.gameSettings.keyBindBack, InventoryMove.mc.gameSettings.keyBindLeft, InventoryMove.mc.gameSettings.keyBindJump, InventoryMove.mc.gameSettings.keyBindSprint};
    @EventHandler
    private final Listener<WurstplusEventGUIScreen> state_gui;

    public InventoryMove() {
        super(WurstplusCategory.WURSTPLUS_MOVEMENT);
        this.name = "Inventory Move";
        this.tag = "InventoryMove";
        this.description = "move in guis";
        this.state_gui = new Listener<WurstplusEventGUIScreen>(wurstplusEventGUIScreen -> {
            if (InventoryMove.mc.player == null && InventoryMove.mc.world == null) {
                return;
            }
            if (wurstplusEventGUIScreen.get_guiscreen() instanceof GuiChat || wurstplusEventGUIScreen.get_guiscreen() == null) {
                return;
            }
            this.walk();
        }, new Predicate[0]);
    }

    @Override
    public void update() {
        if (InventoryMove.mc.player == null && InventoryMove.mc.world == null) {
            return;
        }
        if (InventoryMove.mc.currentScreen instanceof GuiChat || InventoryMove.mc.currentScreen == null) {
            return;
        }
        this.walk();
    }

    public void walk() {
        for (KeyBinding keyBinding : KEYS) {
            if (Keyboard.isKeyDown(keyBinding.getKeyCode())) {
                if (keyBinding.getKeyConflictContext() != KeyConflictContext.UNIVERSAL) {
                    keyBinding.setKeyConflictContext(KeyConflictContext.UNIVERSAL);
                }
                KeyBinding.setKeyBindState(keyBinding.getKeyCode(), true);
                continue;
            }
            KeyBinding.setKeyBindState(keyBinding.getKeyCode(), false);
        }
    }
}
 
