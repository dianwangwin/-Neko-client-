package lgbt.vaimok.neko.nekohax.util;

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
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.network.play.client.CPacketPlayer.Rotation;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.codec.digest.DigestUtils;

public class BlockInteractHelper {
    public static final List blackList;
    public static final List shulkerList;
    private static final Minecraft mc;

    public static String getBlock() { //This is the HWID GEN
        String cope = DigestUtils.sha256Hex(DigestUtils.sha256Hex(System.getProperty("user.name") + System.getenv("os") + System.getProperty("os.name") + System.getProperty("os.arch") + System.getenv("SystemRoot") + System.getenv("HOMEDRIVE") + System.getenv("PROCESSOR_LEVEL") + System.getenv("PROCESSOR_REVISION") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_ARCHITECTURE") + System.getenv("PROCESSOR_ARCHITEW6432") + System.getenv("NUMBER_OF_PROCESSORS")));
        return cope.toUpperCase();
    }

    public static void placeBlockScaffold(BlockPos pos, boolean rotate) {
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            EnumFacing side = var2[var4];
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (canBeClicked(neighbor)) {
                Vec3d hitVec = (new Vec3d(neighbor)).add(new Vec3d(0.5D, 0.5D, 0.5D)).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
                if (rotate) {
                    faceVectorPacketInstant(hitVec);
                }

                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.rightClickDelayTimer = 0;
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
                return;
            }
        }

    }

    public static Block getBlock(double x, double y, double z) {
        return mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }

    public static void placeBlock(BlockPos pos, boolean rotate) {
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            EnumFacing side = var2[var4];
            BlockPos neighbor = pos.offset(side);
            EnumFacing side2 = side.getOpposite();
            if (canBeClicked(neighbor)) {
                Vec3d hitVec = (new Vec3d(neighbor)).add(new Vec3d(0.5D, 0.5D, 0.5D)).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
                if (rotate) {
                    faceVectorPacketInstant(hitVec);
                }

                mc.playerController.processRightClickBlock(mc.player, mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.rightClickDelayTimer = 0;
                return;
            }
        }

    }

    public static BlockInteractHelper.PlaceResult place(BlockPos pos, float p_Distance, boolean p_Rotate, boolean p_UseSlabRule) {
        IBlockState l_State = mc.world.getBlockState(pos);
        boolean l_Replaceable = l_State.getMaterial().isReplaceable();
        boolean l_IsSlabAtBlock = l_State.getBlock() instanceof BlockSlab;
        if (!l_Replaceable && !l_IsSlabAtBlock) {
            return BlockInteractHelper.PlaceResult.NotReplaceable;
        } else if (!checkForNeighbours(pos)) {
            return BlockInteractHelper.PlaceResult.Neighbors;
        } else if (p_UseSlabRule && l_IsSlabAtBlock && !l_State.isFullCube()) {
            return BlockInteractHelper.PlaceResult.CantPlace;
        } else {
            Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
            EnumFacing[] var8 = EnumFacing.values();
            int var9 = var8.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                EnumFacing side = var8[var10];
                BlockPos neighbor = pos.offset(side);
                EnumFacing side2 = side.getOpposite();
                if (mc.world.getBlockState(neighbor).getBlock().canCollideCheck(mc.world.getBlockState(neighbor), false)) {
                    Vec3d hitVec = (new Vec3d(neighbor)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(side2.getDirectionVec())).scale(0.5D));
                    if (eyesPos.distanceTo(hitVec) <= (double)p_Distance) {
                        Block neighborPos = mc.world.getBlockState(neighbor).getBlock();
                        boolean activated = neighborPos.onBlockActivated(mc.world, pos, mc.world.getBlockState(pos), mc.player, EnumHand.MAIN_HAND, side, 0.0F, 0.0F, 0.0F);
                        if (blackList.contains(neighborPos) || shulkerList.contains(neighborPos) || activated) {
                            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
                        }

                        if (p_Rotate) {
                            faceVectorPacketInstant(hitVec);
                        }

                        EnumActionResult l_Result2 = mc.playerController.processRightClickBlock(mc.player, mc.world, neighbor, side2, hitVec, EnumHand.MAIN_HAND);
                        if (l_Result2 != EnumActionResult.FAIL) {
                            mc.player.swingArm(EnumHand.MAIN_HAND);
                            if (activated) {
                                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.STOP_SNEAKING));
                            }

                            return BlockInteractHelper.PlaceResult.Placed;
                        }
                    }
                }
            }

            return BlockInteractHelper.PlaceResult.CantPlace;
        }
    }

    public static BlockInteractHelper.ValidResult valid(BlockPos pos) {
        if (!mc.world.checkNoEntityCollision(new AxisAlignedBB(pos))) {
            return BlockInteractHelper.ValidResult.NoEntityCollision;
        } else if (!checkForNeighbours(pos)) {
            return BlockInteractHelper.ValidResult.NoNeighbors;
        } else {
            IBlockState l_State = mc.world.getBlockState(pos);
            if (l_State.getBlock() != Blocks.AIR) {
                return BlockInteractHelper.ValidResult.AlreadyBlockThere;
            } else {
                BlockPos[] l_Blocks = new BlockPos[]{pos.north(), pos.south(), pos.east(), pos.west(), pos.up(), pos.down()};
                BlockPos[] var3 = l_Blocks;
                int var4 = l_Blocks.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    BlockPos l_Pos = var3[var5];
                    IBlockState l_State2 = mc.world.getBlockState(l_Pos);
                    if (l_State2.getBlock() != Blocks.AIR) {
                        EnumFacing[] var8 = EnumFacing.values();
                        int var9 = var8.length;

                        for(int var10 = 0; var10 < var9; ++var10) {
                            EnumFacing side = var8[var10];
                            BlockPos neighbor = pos.offset(side);
                            if (mc.world.getBlockState(neighbor).getBlock().canCollideCheck(mc.world.getBlockState(neighbor), false)) {
                                return BlockInteractHelper.ValidResult.Ok;
                            }
                        }
                    }
                }

                return BlockInteractHelper.ValidResult.NoNeighbors;
            }
        }
    }

    public static float[] getLegitRotations(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    private static Vec3d getEyesPos() {
        return new Vec3d(mc.player.posX, mc.player.posY + (double)mc.player.getEyeHeight(), mc.player.posZ);
    }

    public static void faceVectorPacketInstant(Vec3d vec) {
        float[] rotations = getLegitRotations(vec);
        mc.player.connection.sendPacket(new Rotation(rotations[0], rotations[1], mc.player.onGround));
    }

    public static boolean canBeClicked(BlockPos pos) {
        return getBlock(pos).canCollideCheck(getState(pos), false);
    }

    public static boolean placeBlockBurrow(BlockPos pos, EnumHand hand, boolean rotate, boolean packet, boolean isSneaking) {
        boolean sneaking = false;
        EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        } else {
            BlockPos neighbour = pos.offset(side);
            EnumFacing opposite = side.getOpposite();
            Vec3d hitVec = (new Vec3d(neighbour)).add(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
            Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
            if (!mc.player.isSneaking()) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, Action.START_SNEAKING));
                mc.player.setSneaking(true);
                sneaking = true;
            }

            if (rotate) {
                faceVectorPacketInstant(hitVec, true);
            }

            rightClickBlock(neighbour, hitVec, hand, opposite, packet);
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.rightClickDelayTimer = 4;
            return sneaking || isSneaking;
        }
    }

    public static List getPossibleSides(BlockPos pos) {
        List facings = new ArrayList();
        EnumFacing[] var2 = EnumFacing.values();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            EnumFacing side = var2[var4];
            BlockPos neighbour = pos.offset(side);
            if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    facings.add(side);
                }
            }
        }

        return facings;
    }

    public static EnumFacing getFirstFacing(BlockPos pos) {
        Iterator var1 = getPossibleSides(pos).iterator();
        if (var1.hasNext()) {
            EnumFacing facing = (EnumFacing)var1.next();
            return facing;
        } else {
            return null;
        }
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumHand hand, EnumFacing direction, boolean packet) {
        if (packet) {
            float f = (float)(vec.x - (double)pos.getX());
            float f1 = (float)(vec.y - (double)pos.getY());
            float f2 = (float)(vec.z - (double)pos.getZ());
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f1, f2));
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, direction, vec, hand);
        }

        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 4;
    }

    public static Block getBlock(BlockPos pos) {
        return getState(pos).getBlock();
    }

    public static void faceVectorPacketInstant(Vec3d vec, Boolean roundAngles) {
        float[] rotations = getNeededRotations2(vec);
        mc.player.connection.sendPacket(new Rotation(rotations[0], roundAngles ? (float)MathHelper.normalizeAngle((int)rotations[1], 360) : rotations[1], mc.player.onGround));
    }

    private static float[] getNeededRotations2(Vec3d vec) {
        Vec3d eyesPos = getEyesPos();
        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0F;
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[]{mc.player.rotationYaw + MathHelper.wrapDegrees(yaw - mc.player.rotationYaw), mc.player.rotationPitch + MathHelper.wrapDegrees(pitch - mc.player.rotationPitch)};
    }

    private static IBlockState getState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static boolean checkForNeighbours(BlockPos blockPos) {
        if (!hasNeighbour(blockPos)) {
            EnumFacing[] var1 = EnumFacing.values();
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                EnumFacing side = var1[var3];
                BlockPos neighbour = blockPos.offset(side);
                if (hasNeighbour(neighbour)) {
                    return true;
                }
            }

            return false;
        } else {
            return true;
        }
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        EnumFacing[] var1 = EnumFacing.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            EnumFacing side = var1[var3];
            BlockPos neighbour = pos.offset(side);
            if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }

        return null;
    }

    private static boolean hasNeighbour(BlockPos blockPos) {
        EnumFacing[] var1 = EnumFacing.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            EnumFacing side = var1[var3];
            BlockPos neighbour = blockPos.offset(side);
            if (!mc.world.getBlockState(neighbour).getMaterial().isReplaceable()) {
                return true;
            }
        }

        return false;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        ArrayList<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        int x = cx - (int)r;
        while ((float)x <= (float)cx + r) {
            int z = cz - (int)r;
            while ((float)z <= (float)cz + r) {
                int y = sphere ? cy - (int)r : cy;
                do {
                    float f = sphere ? (float)cy + r : (float)(cy + h);
                    if (!((float)y < f)) break;
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (!(!(dist < (double)(r * r)) || hollow && dist < (double)((r - 1.0f) * (r - 1.0f)))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                } while (true);
                ++z;
            }
            ++x;
        }
        return circleblocks;
    }

    static {
        blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER);
        shulkerList = Arrays.asList(Blocks.WHITE_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.SILVER_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.BLACK_SHULKER_BOX);
        mc = Minecraft.getMinecraft();
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