package lgbt.vaimok.neko.nekohax.modules.movement;

import lgbt.vaimok.neko.nekohax.event.events.EventGUIScreen;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.input.Keyboard;

public class InventoryMove
extends Module {
    private static final KeyBinding[] KEYS = new KeyBinding[]{InventoryMove.mc.gameSettings.keyBindForward, InventoryMove.mc.gameSettings.keyBindRight, InventoryMove.mc.gameSettings.keyBindBack, InventoryMove.mc.gameSettings.keyBindLeft, InventoryMove.mc.gameSettings.keyBindJump, InventoryMove.mc.gameSettings.keyBindSprint};
    @EventHandler
    private final Listener<EventGUIScreen> state_gui;

    public InventoryMove() {
        super(Category.movement);
        this.name = "Inventory Move";
        this.tag = "InventoryMove";
        this.description = "move in guis";
        this.state_gui = new Listener<>(event -> {
            if (InventoryMove.mc.player == null && InventoryMove.mc.world == null) {
                return;
            }
            if (event.get_guiscreen() instanceof GuiChat || event.get_guiscreen() == null) {
                return;
            }
            this.walk();
        });
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
 
