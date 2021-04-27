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
   public static final Minecraft mc = Minecraft.func_71410_x();

   public static int getHotbarItemSlot(Item item) {
      int slot = -1;

      for(int i = 0; i < 9; ++i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
            slot = i;
            break;
         }
      }

      return slot == -1 ? mc.field_71439_g.field_71071_by.field_70461_c : slot;
   }

   public static void switchToSlot(int slot) {
      mc.field_71439_g.field_71071_by.field_70461_c = slot;
   }

   public static void switchToSlot(Item item) {
      mc.field_71439_g.field_71071_by.field_70461_c = getHotbarItemSlot(item);
   }

   public static void switchToSlot(Block block) {
      if (getBlockInHotbar(block) != -1 && mc.field_71439_g.field_71071_by.field_70461_c != getBlockInHotbar(block)) {
         mc.field_71439_g.field_71071_by.field_70461_c = getBlockInHotbar(block);
      }

   }

   public static void switchToSlotGhost(int slot) {
      mc.field_71439_g.field_71174_a.func_147297_a(new CPacketHeldItemChange(slot));
   }

   public static void switchToSlotGhost(Item item) {
      switchToSlotGhost(getHotbarItemSlot(item));
   }

   public static int getInventoryItemSlot(Item item) {
      int slot = -1;

      for(int i = 45; i > 0; --i) {
         if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
            slot = i;
            break;
         }
      }

      return slot;
   }

   public static void moveItemToOffhand(int slot) {
      int returnSlot = 0;
      if (slot != -1) {
         mc.field_71442_b.func_187098_a(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.field_71439_g);
         mc.field_71442_b.func_187098_a(0, 45, 0, ClickType.PICKUP, mc.field_71439_g);

         for(int i = 0; i < 45; ++i) {
            if (mc.field_71439_g.field_71071_by.func_70301_a(i).func_190926_b()) {
               returnSlot = i;
               break;
            }
         }

         mc.field_71442_b.func_187098_a(0, returnSlot < 9 ? returnSlot + 36 : returnSlot, 0, ClickType.PICKUP, mc.field_71439_g);
      }

   }

   public static void moveItemToOffhand(Item item) {
      int slot = getInventoryItemSlot(item);
      if (slot != -1) {
         moveItemToOffhand(slot);
      }

   }

   public static void moveItem(int slot, int slotOut) {
      mc.field_71442_b.func_187098_a(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.field_71439_g);
      mc.field_71442_b.func_187098_a(0, slotOut < 9 ? slotOut + 36 : slotOut, 0, ClickType.PICKUP, mc.field_71439_g);
      mc.field_71442_b.func_187098_a(0, slot < 9 ? slot + 36 : slot, 0, ClickType.PICKUP, mc.field_71439_g);
   }

   public static int getBlockInHotbar(Block block) {
      for(int i = 0; i < 9; ++i) {
         Item item = mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
         if (item instanceof ItemBlock && ((ItemBlock)item).func_179223_d().equals(block)) {
            return i;
         }
      }

      return -1;
   }

   public static void moveItem(Item item, int slot) {
      moveItem(getInventoryItemSlot(item), slot);
   }

   public static boolean isNull(ItemStack stack) {
      return stack == null || stack.func_77973_b() instanceof ItemAir;
   }
}
