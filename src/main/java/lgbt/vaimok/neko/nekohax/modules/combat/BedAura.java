package lgbt.vaimok.neko.nekohax.modules.combat;

import lgbt.vaimok.neko.nekohax.event.events.EventRender;
import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.turok.draw.RenderHelp;
import lgbt.vaimok.neko.nekohax.util.BlockInteractHelper;
import lgbt.vaimok.neko.nekohax.util.BlockUtil;
import lgbt.vaimok.neko.nekohax.util.FriendUtil;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.math.BlockPos;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class BedAura extends Module {
    Setting delay = this.create("Delay", "BedAuraDelay", 6, 0, 20);
    Setting range = this.create("Range", "BedAuraRange", 5, 0, 6);
    Setting hard = this.create("Hard Rotate", "BedAuraRotate", false);
    Setting swing = this.create("Swing", "BedAuraSwing", "Mainhand", this.combobox(new String[]{"Mainhand", "Offhand", "Both", "None"}));
    private BlockPos render_pos;
    private int counter;
    private BedAura.spoof_face spoof_looking;

    public BedAura() {
        super(Category.combat);
        this.name = "Bed Aura";
        this.tag = "BedAura";
        this.description = "fucking endcrystal.me";
    }

    protected void enable() {
        this.render_pos = null;
        this.counter = 0;
    }

    protected void disable() {
        this.render_pos = null;
    }

    public void update() {
        if (mc.player != null) {
            if (this.counter > this.delay.get_value(1)) {
                this.counter = 0;
                this.place_bed();
                this.break_bed();
                this.refill_bed();
            }

            ++this.counter;
        }
    }

    public void refill_bed() {
        if (!(mc.currentScreen instanceof GuiContainer) && this.is_space()) {
            for(int i = 9; i < 35; ++i) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                    mc.playerController.windowClick(mc.player.inventoryContainer.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
                    break;
                }
            }
        }

    }

    private boolean is_space() {
        for(int i = 0; i < 9; ++i) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                return true;
            }
        }

        return false;
    }

    public void place_bed() {
        if (this.find_bed() != -1) {
            int bed_slot = this.find_bed();
            BlockPos best_pos = null;
            EntityPlayer best_target = null;
            float best_distance = (float)this.range.get_value(1);
            Iterator var5 = ((List)mc.world.playerEntities.stream().filter((entityPlayer) -> {
                return !FriendUtil.isFriend(entityPlayer.getName());
            }).collect(Collectors.toList())).iterator();

            while(var5.hasNext()) {
                EntityPlayer player = (EntityPlayer)var5.next();
                if (player != mc.player && !(best_distance < mc.player.getDistance(player))) {
                    boolean face_place = true;
                    BlockPos pos = get_pos_floor(player).down();
                    BlockPos pos2 = this.check_side_block(pos);
                    if (pos2 != null) {
                        best_pos = pos2.up();
                        best_target = player;
                        best_distance = mc.player.getDistance(player);
                        face_place = false;
                    }

                    if (face_place) {
                        BlockPos upos = get_pos_floor(player);
                        BlockPos upos2 = this.check_side_block(upos);
                        if (upos2 != null) {
                            best_pos = upos2.up();
                            best_target = player;
                            best_distance = mc.player.getDistance(player);
                        }
                    }
                }
            }

            if (best_target != null) {
                this.render_pos = best_pos;
                if (this.spoof_looking == BedAura.spoof_face.NORTH) {
                    if (this.hard.get_value(true)) {
                        mc.player.rotationYaw = 180.0F;
                    }

                    mc.player.connection.sendPacket(new Rotation(180.0F, 0.0F, mc.player.onGround));
                } else if (this.spoof_looking == BedAura.spoof_face.SOUTH) {
                    if (this.hard.get_value(true)) {
                        mc.player.rotationYaw = 0.0F;
                    }

                    mc.player.connection.sendPacket(new Rotation(0.0F, 0.0F, mc.player.onGround));
                } else if (this.spoof_looking == BedAura.spoof_face.WEST) {
                    if (this.hard.get_value(true)) {
                        mc.player.rotationYaw = 90.0F;
                    }

                    mc.player.connection.sendPacket(new Rotation(90.0F, 0.0F, mc.player.onGround));
                } else if (this.spoof_looking == BedAura.spoof_face.EAST) {
                    if (this.hard.get_value(true)) {
                        mc.player.rotationYaw = -90.0F;
                    }

                    mc.player.connection.sendPacket(new Rotation(-90.0F, 0.0F, mc.player.onGround));
                }

                BlockUtil.placeBlock(best_pos, bed_slot, false, false, this.swing);
            }
        }
    }

    public void break_bed() {
        BlockPos pos;
        for(Iterator var1 = ((List) BlockInteractHelper.getSphere(get_pos_floor(mc.player), (float)this.range.get_value(1), this.range.get_value(1), false, true, 0).stream().filter(BedAura::is_bed).collect(Collectors.toList())).iterator(); var1.hasNext(); BlockUtil.openBlock(pos)) {
            pos = (BlockPos)var1.next();
            if (mc.player.isSneaking()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
            }
        }

    }

    public int find_bed() {
        for(int i = 0; i < 9; ++i) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.BED) {
                return i;
            }
        }

        return -1;
    }

    public BlockPos check_side_block(BlockPos pos) {
        if (mc.world.getBlockState(pos.east()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.east().up()).getBlock() == Blocks.AIR) {
            this.spoof_looking = BedAura.spoof_face.WEST;
            return pos.east();
        } else if (mc.world.getBlockState(pos.north()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.north().up()).getBlock() == Blocks.AIR) {
            this.spoof_looking = BedAura.spoof_face.SOUTH;
            return pos.north();
        } else if (mc.world.getBlockState(pos.west()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.west().up()).getBlock() == Blocks.AIR) {
            this.spoof_looking = BedAura.spoof_face.EAST;
            return pos.west();
        } else if (mc.world.getBlockState(pos.south()).getBlock() != Blocks.AIR && mc.world.getBlockState(pos.south().up()).getBlock() == Blocks.AIR) {
            this.spoof_looking = BedAura.spoof_face.NORTH;
            return pos.south();
        } else {
            return null;
        }
    }

    public static BlockPos get_pos_floor(EntityPlayer player) {
        return new BlockPos(Math.floor(player.posX), Math.floor(player.posY), Math.floor(player.posZ));
    }

    public static boolean is_bed(BlockPos pos) {
        Block block = mc.world.getBlockState(pos).getBlock();
        return block == Blocks.BED;
    }

    public void render(EventRender event) {
        if (this.render_pos != null) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), (float)this.render_pos.getX(), (float)this.render_pos.getY(), (float)this.render_pos.getZ(), 1.0F, 0.2F, 1.0F, 255, 20, 20, 180, "all");
            RenderHelp.release();
        }
    }

    static enum spoof_face {
        EAST,
        WEST,
        NORTH,
        SOUTH;
    }
}
 
