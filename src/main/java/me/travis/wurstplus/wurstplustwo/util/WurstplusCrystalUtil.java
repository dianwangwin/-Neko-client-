package me.travis.wurstplus.wurstplustwo.util;

import net.minecraft.init.MobEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.util.CombatRules;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.Explosion;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.block.Block;
import java.util.Iterator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.client.Minecraft;

public class WurstplusCrystalUtil
{
    static final Minecraft mc;
    
    public WurstplusCrystalUtil() {
        super();
    }
    
    public static List<BlockPos> possiblePlacePositions(final float n, final boolean b) {
        final NonNullList create = NonNullList.create();
        create.addAll(getSphere(getPlayerPos(WurstplusCrystalUtil.mc.player), n, (int)n, false, true, 0).stream().filter(blockPos -> canPlaceCrystal(blockPos, b)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (List<BlockPos>)create;
    }
    
    public static BlockPos getPlayerPos(final EntityPlayer entityPlayer) {
        return new BlockPos(Math.floor(entityPlayer.posX), Math.floor(entityPlayer.posY), Math.floor(entityPlayer.posZ));
    }
    
    public static List<BlockPos> getSphere(final BlockPos blockPos, final float n, final int n2, final boolean b, final boolean b2, final int n3) {
        final ArrayList<BlockPos> list = new ArrayList<BlockPos>();
        final int x = blockPos.getX();
        final int y = blockPos.getY();
        final int z = blockPos.getZ();
        for (int n4 = x - (int)n; n4 <= x + n; ++n4) {
            for (int n5 = z - (int)n; n5 <= z + n; ++n5) {
                for (int n6 = b2 ? (y - (int)n) : y; n6 < (b2 ? (y + n) : ((float)(y + n2))); ++n6) {
                    final double n7 = (x - n4) * (x - n4) + (z - n5) * (z - n5) + (b2 ? ((y - n6) * (y - n6)) : 0);
                    if (n7 < n * n && (!b || n7 >= (n - 1.0f) * (n - 1.0f))) {
                        list.add(new BlockPos(n4, n6 + n3, n5));
                    }
                }
            }
        }
        return list;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos, final boolean b) {
        final BlockPos add = blockPos.add(0, 1, 0);
        final BlockPos add2 = blockPos.add(0, 2, 0);
        final BlockPos add3 = blockPos.add(0, 3, 0);
        try {
            if (WurstplusCrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && WurstplusCrystalUtil.mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN) {
                return false;
            }
            if (WurstplusCrystalUtil.mc.world.getBlockState(add).getBlock() != Blocks.AIR || (WurstplusCrystalUtil.mc.world.getBlockState(add2).getBlock() != Blocks.AIR && !b)) {
                return false;
            }
            final Iterator iterator = WurstplusCrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(add)).iterator();
            while (iterator.hasNext()) {
                if (!(iterator.next() instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            final Iterator iterator2 = WurstplusCrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(add2)).iterator();
            while (iterator2.hasNext()) {
                if (!(iterator2.next() instanceof EntityEnderCrystal)) {
                    return false;
                }
            }
            final Iterator iterator3 = WurstplusCrystalUtil.mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(add3)).iterator();
            while (iterator3.hasNext()) {
                if (iterator3.next() instanceof EntityEnderCrystal) {
                    return false;
                }
            }
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos) {
        final Block block = WurstplusCrystalUtil.mc.world.getBlockState(blockPos).getBlock();
        if (block == Blocks.OBSIDIAN || block == Blocks.BEDROCK) {
            final Block block2 = WurstplusCrystalUtil.mc.world.getBlockState(blockPos.add(0, 1, 0)).getBlock();
            final Block block3 = WurstplusCrystalUtil.mc.world.getBlockState(blockPos.add(0, 2, 0)).getBlock();
            if (block2 == Blocks.AIR && block3 == Blocks.AIR && WurstplusCrystalUtil.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos.add(0, 1, 0))).isEmpty() && WurstplusCrystalUtil.mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(blockPos.add(0, 2, 0))).isEmpty()) {
                return true;
            }
        }
        return false;
    }
    
    public static float calculateDamage(final double n, final double n2, final double n3, final Entity entity) {
        if (entity == WurstplusCrystalUtil.mc.player && WurstplusCrystalUtil.mc.player.capabilities.isCreativeMode) {
            return 0.0f;
        }
        final double n4 = entity.getDistance(n, n2, n3) / 12.0;
        final Vec3d vec3d = new Vec3d(n, n2, n3);
        double n5 = 0.0;
        try {
            n5 = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        }
        catch (Exception ex) {}
        final double n6 = (1.0 - n4) * n5;
        final float n7 = (float)(int)((n6 * n6 + n6) / 2.0 * 7.0 * 12.0 + 1.0);
        double n8 = 1.0;
        if (entity instanceof EntityLivingBase) {
            n8 = getBlastReduction((EntityLivingBase)entity, getDamageMultiplied(n7), new Explosion(WurstplusCrystalUtil.mc.world, null, n, n2, n3, 6.0f, false, true));
        }
        return (float)n8;
    }
    
    public static float getBlastReduction(final EntityLivingBase entityLivingBase, final float n, final Explosion explosion) {
        if (entityLivingBase instanceof EntityPlayer) {
            final EntityPlayer entityPlayer = (EntityPlayer)entityLivingBase;
            final DamageSource causeExplosionDamage = DamageSource.causeExplosionDamage(explosion);
            final float damageAfterAbsorb = CombatRules.getDamageAfterAbsorb(n, (float)entityPlayer.getTotalArmorValue(), (float)entityPlayer.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
            int enchantmentModifierDamage = 0;
            try {
                enchantmentModifierDamage = EnchantmentHelper.getEnchantmentModifierDamage(entityPlayer.getArmorInventoryList(), causeExplosionDamage);
            }
            catch (Exception ex) {}
            float n2 = damageAfterAbsorb * (1.0f - MathHelper.clamp((float)enchantmentModifierDamage, 0.0f, 20.0f) / 25.0f);
            if (entityLivingBase.isPotionActive(MobEffects.RESISTANCE)) {
                n2 -= n2 / 4.0f;
            }
            return Math.max(n2, 0.0f);
        }
        return CombatRules.getDamageAfterAbsorb(n, (float)entityLivingBase.getTotalArmorValue(), (float)entityLivingBase.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
    }
    
    public static float getDamageMultiplied(final float n) {
        final int id = WurstplusCrystalUtil.mc.world.getDifficulty().getId();
        return n * ((id == 0) ? 0.0f : ((id == 2) ? 1.0f : ((id == 1) ? 0.5f : 1.5f)));
    }
    
    public static float calculateDamage(final EntityEnderCrystal entityEnderCrystal, final Entity entity) {
        return calculateDamage(entityEnderCrystal.posX, entityEnderCrystal.posY, entityEnderCrystal.posZ, entity);
    }
    
    private static /* synthetic */ boolean lambda$possiblePlacePositions$0(final boolean b, final BlockPos blockPos) {
        return canPlaceCrystal(blockPos, b);
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
