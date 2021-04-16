package lgbt.vaimok.neko.nekohax.modules.misc;

import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class AutoWither extends Module {

    public AutoWither() {
        super(Category.misc);

        this.name = "Auto Wither";
        this.tag = "AutoWither";
        this.description = "makes withers";
    }

    Setting range = create("Range", "WitherRange", 4, 0, 6);

    private int head_slot;
    private int sand_slot;

    @Override
    protected void enable() {

    }

    public boolean has_blocks() {

        int count = 0;

        for (int i = 0; i < 9; ++i) {

            final ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack != ItemStack.EMPTY && stack.getItem() instanceof ItemBlock) {

                final Block block = ((ItemBlock) stack.getItem()).getBlock();

                if (block instanceof BlockSoulSand) {
                    sand_slot = i;
                    count++;
                    break;
                }

            }
        }

        for (int i = 0; i < 9; ++i) {

            final ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack.getItem() == Items.SKULL && stack.getItemDamage() == 1) {
                head_slot = i;
                count++;
                break;
            }

        }

        if (count != 2) {
            return false;
        } return true;

    }

}
