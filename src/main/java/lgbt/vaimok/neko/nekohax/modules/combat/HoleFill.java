package lgbt.vaimok.neko.nekohax.modules.combat;

import lgbt.vaimok.neko.nekohax.event.events.EventRender;
import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.turok.draw.RenderHelp;
import lgbt.vaimok.neko.nekohax.util.BlockInteractHelper;
import lgbt.vaimok.neko.nekohax.util.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class HoleFill extends Module {
    public HoleFill() {
		super(Category.combat);

		this.name        = "Hole Fill"; 
		this.tag         = "HoleFill";
		this.description = "Turn holes into floors";
    }

    /**
     * @author ObsidianBreaker
     * @since 15/04/2021
     * LeuxBackdoor 0.7 HoleFill :3
     */

    Setting hole_toggle = create("Toggle", "HoleFillToggle", false);
    Setting hole_rotate = create("Rotate", "HoleFillRotate", false);
    Setting hole_range = create("Range", "HoleFillRange", 4, 1, 6);
    Setting render = create("Render", "HoleFillRender", true);
    Setting r = create("Red", "HoleFillRed", 0, 0, 255);
    Setting g = create("Green", "HoleFillGreen", 100, 0, 255);
    Setting b = create("Blue", "HoleFillBlue", 100, 0, 255);
    Setting a = create("Alpha", "HoleFillAlpha", 30, 0, 255);
    Setting renderMode = create("Render Mode", "HoleFillMode", "Both", combobox("Quads", "Lines", "Both"));
    private final ArrayList<BlockPos> holes = new ArrayList<>();
    public BlockPos pos_to_fill;

    @Override
    public void enable() {
        find_new_holes();
    }

    @Override
    public void update() {
        if (find_in_hotbar() == -1) {
            disable();
        } else {
            if (holes.isEmpty()) {
                if (!hole_toggle.get_value(true)) {
                    set_disable();
                    return;
                }
                find_new_holes();
            }
            pos_to_fill = null;
            for (Object o : new ArrayList<>(holes)) {
                BlockPos pos = (BlockPos)o;
                if (pos == null) continue;
               BlockInteractHelper.ValidResult result = BlockInteractHelper.valid(pos);
                if (result == BlockInteractHelper.ValidResult.Ok) {
                    pos_to_fill = pos;
                    break;
                }
                holes.remove(pos);
            }
            int obi_slot = find_in_hotbar();
            if (find_in_hotbar() == -1) {
                disable();
            } else if (pos_to_fill != null) {
                int last_slot = mc.player.inventory.currentItem;
                mc.player.inventory.currentItem = obi_slot;
                mc.playerController.updateController();
                if (place_blocks(pos_to_fill)) {
                    holes.remove(pos_to_fill);
                }
                mc.player.inventory.currentItem = last_slot;
            }
        }
    }

    @Override
    public void disable() {
        holes.clear();
    }

    @Override
    public void render(EventRender event) {
        if (render.get_value(true) && pos_to_fill != null) {
            if (renderMode.in("Quads")) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            } else if (renderMode.in("Lines")) {
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            } else if (renderMode.in("Both")) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), pos_to_fill.getX(), pos_to_fill.getY(), pos_to_fill.getZ(), 1.0f, 1.0f, 1.0f, r.get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");
                RenderHelp.release();
            }
        }
    }

    public void find_new_holes() {
        holes.clear();
        for (BlockPos blockPos : BlockInteractHelper.getSphere(PlayerUtil.GetLocalPlayerPosFloored(), hole_range.get_value(1), hole_range.get_value(1), false, true, 0)) {
            if (!mc.world.getBlockState(blockPos).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock().equals(Blocks.AIR) || !mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock().equals(Blocks.AIR))
                continue;
            boolean possible = true;
            for (BlockPos seems_blocks : new BlockPos[]{new BlockPos(0, -1, 0), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(-1, 0, 0)}) {
                Block block = mc.world.getBlockState(blockPos.add(seems_blocks)).getBlock();
                if (block == Blocks.BEDROCK || block == Blocks.OBSIDIAN || block == Blocks.ENDER_CHEST || block == Blocks.ANVIL)
                    continue;
                possible = false;
                break;
            }
            if (!possible) continue;
            holes.add(blockPos);
        }
    }

    private int find_in_hotbar() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) continue;
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            if (block instanceof BlockEnderChest) {
                return i;
            }
            if (!(block instanceof BlockObsidian)) continue;
            return i;
        }
        return -1;
    }

    private boolean place_blocks(BlockPos pos) {
        if (!mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
            return false;
        }
        if (!BlockInteractHelper.checkForNeighbours(pos)) {
            return false;
        }
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (!BlockInteractHelper.canBeClicked(neighbor)) continue;
            if (BlockInteractHelper.blackList.contains(mc.world.getBlockState(neighbor).getBlock())) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            }
            Vec3d hitVec = new Vec3d(neighbor).add(0.5, 0.5, 0.5).add(new Vec3d(side2.getDirectionVec()).scale(0.5));
            if (hole_rotate.get_value(true)) {
               BlockInteractHelper.faceVectorPacketInstant(hitVec);
            }
            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            return true;
        }
        return false;
    }
}