package lgbt.vaimok.neko.nekohax.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAir;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

public class InventoryUtil {
    public static final Minecraft mc = Minecraft.getMinecraft();

    public static int getHotbarItemSlot(Item item) {
        int slot = -1;

        for(int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }

        return slot == -1 ? mc.player.inventory.currentItem : slot;
    }

    public static void switchToSlot(int slot) {
        mc.player.inventory.currentItem = slot;
    }

    public static void switchToSlot(Item item) {
        mc.player.inventory.currentItem = getHotbarItemSlot(item);
    }

    public static void switchToSlot(Block block) {
        if (getBlockInHotbar(block) != -1 && mc.player.inventory.currentItem != getBlockInHotbar(block)) {
            mc.player.inventory.currentItem = getBlockInHotbar(block);
        }

    }

    public static void switchToSlotGhost(int slot) {
        mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
    }

    public static void switchToSlotGhost(Item item) {
        switchToSlotGhost(getHotbarItemSlot(item));
    }

    public static int getInventoryItemSlot(Item item) {
        int slot = -1;

        for(int i = 45; i > 0; --i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == item) {
                slot = i;
                break;
            }
        }

        return slot;
    }

    public static void moveItemToOffhand(int slot) {
        int returnSlot = 0;
        if (slot != -1) {
            mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
            mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);

            for(int i = 0; i < 45; ++i) {
                if (mc.player.inventory.getStackInSlot(i).isEmpty()) {
                    returnSlot = i;
                    break;
                }
            }

            mc.playerController.windowClick(0, returnSlot < 9 ? returnSlot + 36 : returnSlot, 0, ClickType.PICKUP, mc.player);
        }

    }

    public static void moveItemToOffhand(Item item) {
        int slot = getInventoryItemSlot(item);
        if (slot != -1) {
            moveItemToOffhand(slot);
        }

    }

    public static void moveItem(int slot, int slotOut) {
        mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slotOut < 9 ? slotOut + 36 : slotOut, 0, ClickType.PICKUP, mc.player);
        mc.playerController.windowClick(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.player);
    }

    public static int getBlockInHotbar(Block block) {
        for(int i = 0; i < 9; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item instanceof ItemBlock && ((ItemBlock)item).getBlock().equals(block)) {
                return i;
            }
        }

        return -1;
    }

    public static void moveItem(Item item, int slot) {
        moveItem(getInventoryItemSlot(item), slot);
    }

    public static boolean isNull(ItemStack stack) {
        return stack == null || stack.getItem() instanceof ItemAir;
    }
}