package me.travis.wurstplus.wurstplustwo.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction$Action;
import net.minecraft.network.play.client.CPacketPlayer$Rotation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BlockInteractionHelper {
    public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
    public static final List<Block> shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void placeBlockScaffold(BlockPos blockPos, boolean bl) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            EnumFacing enumFacing2 = enumFacing.getOpposite();
            if (!BlockInteractionHelper.canBeClicked(blockPos2)) continue;
            Vec3d vec3d = new Vec3d(blockPos2).add(new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
            if (bl) {
                BlockInteractionHelper.faceVectorPacketInstant(vec3d);
            }
            BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketEntityAction(BlockInteractionHelper.mc.player, CPacketEntityAction$Action.START_SNEAKING));
            BlockInteractionHelper.mc.playerController.processRightClickBlock(BlockInteractionHelper.mc.player, BlockInteractionHelper.mc.world, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND);
            BlockInteractionHelper.mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockInteractionHelper.mc.rightClickDelayTimer = 0;
            BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketEntityAction(BlockInteractionHelper.mc.player, CPacketEntityAction$Action.STOP_SNEAKING));
            return;
        }
    }

    public static Block getBlock(double d, double d2, double d3) {
        return BlockInteractionHelper.mc.world.getBlockState(new BlockPos(d, d2, d3)).getBlock();
    }

    public static void placeBlock(BlockPos blockPos, boolean bl) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            EnumFacing enumFacing2 = enumFacing.getOpposite();
            if (!BlockInteractionHelper.canBeClicked(blockPos2)) continue;
            Vec3d vec3d = new Vec3d(blockPos2).add(new Vec3d(0.5, 0.5, 0.5)).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
            if (bl) {
                BlockInteractionHelper.faceVectorPacketInstant(vec3d);
            }
            BlockInteractionHelper.mc.playerController.processRightClickBlock(BlockInteractionHelper.mc.player, BlockInteractionHelper.mc.world, blockPos, enumFacing, vec3d, EnumHand.MAIN_HAND);
            BlockInteractionHelper.mc.player.swingArm(EnumHand.MAIN_HAND);
            BlockInteractionHelper.mc.rightClickDelayTimer = 0;
            return;
        }
    }

    public static PlaceResult place(BlockPos blockPos, float f, boolean bl, boolean bl2) {
        IBlockState iBlockState = BlockInteractionHelper.mc.world.getBlockState(blockPos);
        boolean bl3 = iBlockState.getMaterial().isReplaceable();
        boolean bl4 = iBlockState.getBlock() instanceof BlockSlab;
        if (!bl3 && !bl4) {
            return PlaceResult.NotReplaceable;
        }
        if (!BlockInteractionHelper.checkForNeighbours(blockPos)) {
            return PlaceResult.Neighbors;
        }
        if (bl2 && bl4 && !iBlockState.isFullCube()) {
            return PlaceResult.CantPlace;
        }
        Vec3d vec3d = new Vec3d(BlockInteractionHelper.mc.player.posX, BlockInteractionHelper.mc.player.posY + (double)BlockInteractionHelper.mc.player.getEyeHeight(), BlockInteractionHelper.mc.player.posZ);
        for (EnumFacing enumFacing : EnumFacing.values()) {
            EnumActionResult enumActionResult;
            Vec3d vec3d2;
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            EnumFacing enumFacing2 = enumFacing.getOpposite();
            if (!BlockInteractionHelper.mc.world.getBlockState(blockPos2).getBlock().canCollideCheck(BlockInteractionHelper.mc.world.getBlockState(blockPos2), false) || !(vec3d.distanceTo(vec3d2 = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5))) <= (double)f)) continue;
            Block block = BlockInteractionHelper.mc.world.getBlockState(blockPos2).getBlock();
            boolean bl5 = block.onBlockActivated(BlockInteractionHelper.mc.world, blockPos, BlockInteractionHelper.mc.world.getBlockState(blockPos), BlockInteractionHelper.mc.player, EnumHand.MAIN_HAND, enumFacing, 0.0f, 0.0f, 0.0f);
            if (blackList.contains(block) || shulkerList.contains(block) || bl5) {
                BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketEntityAction(BlockInteractionHelper.mc.player, CPacketEntityAction$Action.START_SNEAKING));
            }
            if (bl) {
                BlockInteractionHelper.faceVectorPacketInstant(vec3d2);
            }
            if ((enumActionResult = BlockInteractionHelper.mc.playerController.processRightClickBlock(BlockInteractionHelper.mc.player, BlockInteractionHelper.mc.world, blockPos2, enumFacing2, vec3d2, EnumHand.MAIN_HAND)) == EnumActionResult.FAIL) continue;
            BlockInteractionHelper.mc.player.swingArm(EnumHand.MAIN_HAND);
            if (bl5) {
                BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketEntityAction(BlockInteractionHelper.mc.player, CPacketEntityAction$Action.STOP_SNEAKING));
            }
            return PlaceResult.Placed;
        }
        return PlaceResult.CantPlace;
    }

    public static ValidResult valid(BlockPos blockPos) {
        if (!BlockInteractionHelper.mc.world.checkNoEntityCollision(new AxisAlignedBB(blockPos))) {
            return ValidResult.NoEntityCollision;
        }
        if (!BlockInteractionHelper.checkForNeighbours(blockPos)) {
            return ValidResult.NoNeighbors;
        }
        IBlockState iBlockState = BlockInteractionHelper.mc.world.getBlockState(blockPos);
        if (iBlockState.getBlock() == Blocks.AIR) {
            BlockPos[] blockPosArray;
            for (BlockPos blockPos2 : blockPosArray = new BlockPos[]{blockPos.north(), blockPos.south(), blockPos.east(), blockPos.west(), blockPos.up(), blockPos.down()}) {
                IBlockState iBlockState2 = BlockInteractionHelper.mc.world.getBlockState(blockPos2);
                if (iBlockState2.getBlock() == Blocks.AIR) continue;
                for (EnumFacing enumFacing : EnumFacing.values()) {
                    BlockPos blockPos3 = blockPos.offset(enumFacing);
                    if (!BlockInteractionHelper.mc.world.getBlockState(blockPos3).getBlock().canCollideCheck(BlockInteractionHelper.mc.world.getBlockState(blockPos3), false)) continue;
                    return ValidResult.Ok;
                }
            }
            return ValidResult.NoNeighbors;
        }
        return ValidResult.AlreadyBlockThere;
    }

    public static float[] getLegitRotations(Vec3d vec3d) {
        Vec3d vec3d2 = BlockInteractionHelper.getEyesPos();
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
        return new float[]{BlockInteractionHelper.mc.player.rotationYaw + MathHelper.wrapDegrees(f - BlockInteractionHelper.mc.player.rotationYaw), BlockInteractionHelper.mc.player.rotationPitch + MathHelper.wrapDegrees(f2 - BlockInteractionHelper.mc.player.rotationPitch)};
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(BlockInteractionHelper.mc.player.posX, BlockInteractionHelper.mc.player.posY + (double)BlockInteractionHelper.mc.player.getEyeHeight(), BlockInteractionHelper.mc.player.posZ);
    }

    public static void faceVectorPacketInstant(Vec3d vec3d) {
        float[] fArray = BlockInteractionHelper.getLegitRotations(vec3d);
        BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketPlayer$Rotation(fArray[0], fArray[1], BlockInteractionHelper.mc.player.onGround));
    }

    public static boolean canBeClicked(BlockPos blockPos) {
        return BlockInteractionHelper.getBlock(blockPos).canCollideCheck(BlockInteractionHelper.getState(blockPos), false);
    }

    public static boolean placeBlockBurrow(BlockPos blockPos, EnumHand enumHand, boolean bl, boolean bl2, boolean bl3) {
        boolean bl4 = false;
        EnumFacing enumFacing = BlockInteractionHelper.getFirstFacing(blockPos);
        if (enumFacing == null) {
            return bl3;
        }
        BlockPos blockPos2 = blockPos.offset(enumFacing);
        EnumFacing enumFacing2 = enumFacing.getOpposite();
        Vec3d vec3d = new Vec3d(blockPos2).add(0.5, 0.5, 0.5).add(new Vec3d(enumFacing2.getDirectionVec()).scale(0.5));
        Block block = BlockInteractionHelper.mc.world.getBlockState(blockPos2).getBlock();
        if (!BlockInteractionHelper.mc.player.isSneaking()) {
            BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketEntityAction(BlockInteractionHelper.mc.player, CPacketEntityAction$Action.START_SNEAKING));
            BlockInteractionHelper.mc.player.setSneaking(true);
            bl4 = true;
        }
        if (bl) {
            BlockInteractionHelper.faceVectorPacketInstant(vec3d, true);
        }
        BlockInteractionHelper.rightClickBlock(blockPos2, vec3d, enumHand, enumFacing2, bl2);
        BlockInteractionHelper.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockInteractionHelper.mc.rightClickDelayTimer = 4;
        return bl4 || bl3;
    }

    public static List<EnumFacing> getPossibleSides(BlockPos blockPos) {
        ArrayList<EnumFacing> arrayList = new ArrayList<EnumFacing>();
        for (EnumFacing enumFacing : EnumFacing.values()) {
            IBlockState iBlockState;
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (!BlockInteractionHelper.mc.world.getBlockState(blockPos2).getBlock().canCollideCheck(BlockInteractionHelper.mc.world.getBlockState(blockPos2), false) || (iBlockState = BlockInteractionHelper.mc.world.getBlockState(blockPos2)).getMaterial().isReplaceable()) continue;
            arrayList.add(enumFacing);
        }
        return arrayList;
    }

    public static EnumFacing getFirstFacing(BlockPos blockPos) {
        Iterator<EnumFacing> iterator = BlockInteractionHelper.getPossibleSides(blockPos).iterator();
        if (iterator.hasNext()) {
            EnumFacing enumFacing = iterator.next();
            return enumFacing;
        }
        return null;
    }

    public static void rightClickBlock(BlockPos blockPos, Vec3d vec3d, EnumHand enumHand, EnumFacing enumFacing, boolean bl) {
        if (bl) {
            float f = (float)(vec3d.x - (double)blockPos.getX());
            float f2 = (float)(vec3d.y - (double)blockPos.getY());
            float f3 = (float)(vec3d.z - (double)blockPos.getZ());
            BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(blockPos, enumFacing, enumHand, f, f2, f3));
        } else {
            BlockInteractionHelper.mc.playerController.processRightClickBlock(BlockInteractionHelper.mc.player, BlockInteractionHelper.mc.world, blockPos, enumFacing, vec3d, enumHand);
        }
        BlockInteractionHelper.mc.player.swingArm(EnumHand.MAIN_HAND);
        BlockInteractionHelper.mc.rightClickDelayTimer = 4;
    }

    public static Block getBlock(BlockPos blockPos) {
        return BlockInteractionHelper.getState(blockPos).getBlock();
    }

    public static void faceVectorPacketInstant(Vec3d vec3d, Boolean bl) {
        float[] fArray = BlockInteractionHelper.getNeededRotations2(vec3d);
        BlockInteractionHelper.mc.player.connection.sendPacket(new CPacketPlayer$Rotation(fArray[0], bl != false ? (float)MathHelper.normalizeAngle((int)fArray[1], 360) : fArray[1], BlockInteractionHelper.mc.player.onGround));
    }

    private static float[] getNeededRotations2(Vec3d vec3d) {
        Vec3d vec3d2 = BlockInteractionHelper.getEyesPos();
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(d3, d)) - 90.0f;
        float f2 = (float)(-Math.toDegrees(Math.atan2(d2, d4)));
        return new float[]{BlockInteractionHelper.mc.player.rotationYaw + MathHelper.wrapDegrees(f - BlockInteractionHelper.mc.player.rotationYaw), BlockInteractionHelper.mc.player.rotationPitch + MathHelper.wrapDegrees(f2 - BlockInteractionHelper.mc.player.rotationPitch)};
    }

    private static IBlockState getState(BlockPos blockPos) {
        return BlockInteractionHelper.mc.world.getBlockState(blockPos);
    }

    public static boolean checkForNeighbours(BlockPos blockPos) {
        if (!BlockInteractionHelper.hasNeighbour(blockPos)) {
            for (EnumFacing enumFacing : EnumFacing.values()) {
                BlockPos blockPos2 = blockPos.offset(enumFacing);
                if (!BlockInteractionHelper.hasNeighbour(blockPos2)) continue;
                return true;
            }
            return false;
        }
        return true;
    }

    public static EnumFacing getPlaceableSide(BlockPos blockPos) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            IBlockState iBlockState;
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (!BlockInteractionHelper.mc.world.getBlockState(blockPos2).getBlock().canCollideCheck(BlockInteractionHelper.mc.world.getBlockState(blockPos2), false) || (iBlockState = BlockInteractionHelper.mc.world.getBlockState(blockPos2)).getMaterial().isReplaceable()) continue;
            return enumFacing;
        }
        return null;
    }

    private static boolean hasNeighbour(BlockPos blockPos) {
        for (EnumFacing enumFacing : EnumFacing.values()) {
            BlockPos blockPos2 = blockPos.offset(enumFacing);
            if (BlockInteractionHelper.mc.world.getBlockState(blockPos2).getMaterial().isReplaceable()) continue;
            return true;
        }
        return false;
    }

    public static List<BlockPos> getSphere(BlockPos blockPos, float f, int n, boolean bl, boolean bl2, int n2) {
        ArrayList<BlockPos> arrayList = new ArrayList<BlockPos>();
        int n3 = blockPos.getX();
        int n4 = blockPos.getY();
        int n5 = blockPos.getZ();
        int n6 = n3 - (int)f;
        while ((float)n6 <= (float)n3 + f) {
            int n7 = n5 - (int)f;
            while ((float)n7 <= (float)n5 + f) {
                int n8 = bl2 ? n4 - (int)f : n4;
                while (true) {
                    float f2;
                    float f3 = f2 = bl2 ? (float)n4 + f : (float)(n4 + n);
                    if (!((float)n8 < f2)) break;
                    double d = (n3 - n6) * (n3 - n6) + (n5 - n7) * (n5 - n7) + (bl2 ? (n4 - n8) * (n4 - n8) : 0);
                    if (!(!(d < (double)(f * f)) || bl && d < (double)((f - 1.0f) * (f - 1.0f)))) {
                        BlockPos blockPos2 = new BlockPos(n6, n8 + n2, n7);
                        arrayList.add(blockPos2);
                    }
                    ++n8;
                }
                ++n7;
            }
            ++n6;
        }
        return arrayList;
    }

    public static enum PlaceResult {
        NotReplaceable,
        Neighbors,
        CantPlace,
        Placed;

    }

    public static enum ValidResult {
        NoEntityCollision,
        AlreadyBlockThere,
        NoNeighbors,
        Ok;

    }
}
