package me.travis.wurstplus.wurstplustwo.hacks.combat;

import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.Wurstplus;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventEntityRemoved;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;
import me.travis.wurstplus.wurstplustwo.hacks.chat.WurstplusAutoEz;
import me.travis.wurstplus.wurstplustwo.util.WurstplusBlockUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMathUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusPosManager;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRenderUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRotationUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity$Action;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WurstplusAutoCrystal
extends WurstplusHack {
    WurstplusSetting debug = this.create("Debug", "CaDebug", false);
    WurstplusSetting place_crystal = this.create("Place", "CaPlace", true);
    WurstplusSetting break_crystal = this.create("Break", "CaBreak", true);
    WurstplusSetting break_trys = this.create("Break Attempts", "CaBreakAttempts", 2, 1, 6);
    WurstplusSetting anti_weakness = this.create("Anti-Weakness", "CaAntiWeakness", true);
    WurstplusSetting enemyRange = this.create("Enemy Range", "CaEnemyRange", 9.0, 5.0, 15.0);
    WurstplusSetting hit_range = this.create("Hit Range", "CaHitRange", 5.2f, 1.0, 6.0);
    WurstplusSetting place_range = this.create("Place Range", "CaPlaceRange", 5.2f, 1.0, 6.0);
    WurstplusSetting hit_range_wall = this.create("Range Wall", "CaRangeWall", 4.0, 1.0, 6.0);
    WurstplusSetting wallPlaceRange = this.create("Place Wall Range", "CaPlaceWallRange", 4.0, 0.0, 6.0);
    WurstplusSetting place_delay = this.create("Place Delay", "CaPlaceDelay", 0, 0, 10);
    WurstplusSetting break_delay = this.create("Break Delay", "CaBreakDelay", 2, 0, 10);
    WurstplusSetting min_player_place = this.create("Min Enemy Place", "CaMinEnemyPlace", 8, 0, 20);
    WurstplusSetting min_player_break = this.create("Min Enemy Break", "CaMinEnemyBreak", 6, 0, 20);
    WurstplusSetting max_self_damage = this.create("Max Self Damage", "CaMaxSelfDamage", 6, 0, 20);
    WurstplusSetting rotate_mode = this.create("Rotate", "CaRotateMode", "Good", this.combobox("Off", "Old", "Const", "Good"));
    WurstplusSetting raytrace = this.create("Raytrace", "CaRaytrace", false);
    WurstplusSetting auto_switch = this.create("Auto Switch", "CaAutoSwitch", true);
    WurstplusSetting anti_suicide = this.create("Anti Suicide", "CaAntiSuicide", true);
    WurstplusSetting client_side = this.create("Client Side", "CaClientSide", false);
    WurstplusSetting jumpy_mode = this.create("Jumpy Mode", "CaJumpyMode", false);
    WurstplusSetting anti_stuck = this.create("Anti Stuck", "CaAntiStuck", false);
    WurstplusSetting antiStuckTries = this.create("Anti Stuck Tries", "CaAntiStuckTries", 5, 1, 15);
    WurstplusSetting endcrystal = this.create("1.13 Mode", "CaThirteen", false);
    WurstplusSetting faceplace_mode = this.create("Tabbott Mode", "CaTabbottMode", true);
    WurstplusSetting faceplace_mode_damage = this.create("T Health", "CaTabbottModeHealth", 8, 0, 36);
    WurstplusSetting fuck_armor_mode = this.create("Armor Destroy", "CaArmorDestory", true);
    WurstplusSetting fuck_armor_mode_precent = this.create("Armor %", "CaArmorPercent", 25, 0, 100);
    WurstplusSetting stop_while_mining = this.create("Stop While Mining", "CaStopWhileMining", false);
    WurstplusSetting faceplace_check = this.create("No Sword FP", "CaJumpyFaceMode", false);
    WurstplusSetting swing = this.create("Swing", "CaSwing", "Mainhand", this.combobox("Mainhand", "Offhand", "Both", "None"));
    WurstplusSetting render_mode = this.create("Render", "CaRenderMode", "Pretty", this.combobox("Pretty", "Solid", "Outline", "None"));
    WurstplusSetting old_render = this.create("Old Render", "CaOldRender", false);
    WurstplusSetting future_render = this.create("Future Render", "CaFutureRender", false);
    WurstplusSetting top_block = this.create("Top Block", "CaTopBlock", false);
    WurstplusSetting r = this.create("R", "CaR", 255, 0, 255);
    WurstplusSetting g = this.create("G", "CaG", 255, 0, 255);
    WurstplusSetting b = this.create("B", "CaB", 255, 0, 255);
    WurstplusSetting a = this.create("A", "CaA", 100, 0, 255);
    WurstplusSetting a_out = this.create("Outline A", "CaOutlineA", 255, 0, 255);
    WurstplusSetting rainbow_mode = this.create("Rainbow", "CaRainbow", false);
    WurstplusSetting sat = this.create("Satiation", "CaSatiation", 0.8, 0.0, 1.0);
    WurstplusSetting brightness = this.create("Brightness", "CaBrightness", 0.8, 0.0, 1.0);
    WurstplusSetting height = this.create("Height", "CaHeight", 1.0, 0.0, 1.0);
    WurstplusSetting render_damage = this.create("Render Damage", "RenderDamage", true);
    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attacked_crystals = new ConcurrentHashMap();
    private final List<BlockPos> placePosList = new CopyOnWriteArrayList<BlockPos>();
    private final WurstplusTimer remove_visual_timer = new WurstplusTimer();
    private EntityPlayer autoez_target = null;
    private String detail_name = null;
    private int detail_hp = 0;
    private BlockPos render_block_init;
    private BlockPos render_block_old;
    private double render_damage_value;
    private float yaw;
    private float pitch;
    private boolean already_attacking = false;
    private boolean is_rotating;
    private boolean did_anything;
    private boolean outline;
    private boolean solid;
    private int place_timeout;
    private int break_timeout;
    private int break_delay_counter;
    private int place_delay_counter;
    @EventHandler
    private Listener<WurstplusEventEntityRemoved> on_entity_removed = new Listener<WurstplusEventEntityRemoved>(wurstplusEventEntityRemoved -> {
        if (wurstplusEventEntityRemoved.get_entity() instanceof EntityEnderCrystal) {
            this.attacked_crystals.remove(wurstplusEventEntityRemoved.get_entity());
        }
    }, new Predicate[0]);
    @EventHandler
    private Listener<WurstplusEventPacket.SendPacket> send_listener = new Listener<WurstplusEventPacket.SendPacket>(sendPacket -> {
        Object object;
        if (sendPacket.get_packet() instanceof CPacketPlayer && this.is_rotating && this.rotate_mode.in("Old")) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Rotating");
            }
            object = (CPacketPlayer)((Object)sendPacket.get_packet());
            ((CPacketPlayer)object).yaw = this.yaw;
            ((CPacketPlayer)object).pitch = this.pitch;
            this.is_rotating = false;
        }
        if (sendPacket.get_packet() instanceof CPacketPlayerTryUseItemOnBlock && this.is_rotating && this.rotate_mode.in("Old")) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Rotating");
            }
            object = (CPacketPlayerTryUseItemOnBlock)sendPacket.get_packet();
            ((CPacketPlayerTryUseItemOnBlock)object).facingX = this.render_block_init.getX();
            ((CPacketPlayerTryUseItemOnBlock)object).facingY = this.render_block_init.getY();
            ((CPacketPlayerTryUseItemOnBlock)object).facingZ = this.render_block_init.getZ();
            this.is_rotating = false;
        }
    }, new Predicate[0]);
    @EventHandler
    private Listener<WurstplusEventMotionUpdate> on_movement = new Listener<WurstplusEventMotionUpdate>(wurstplusEventMotionUpdate -> {
        if (wurstplusEventMotionUpdate.stage == 0 && (this.rotate_mode.in("Good") || this.rotate_mode.in("Const"))) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("updating rotation");
            }
            WurstplusPosManager.updatePosition();
            WurstplusRotationUtil.updateRotations();
            this.do_ca();
        }
        if (wurstplusEventMotionUpdate.stage == 1 && (this.rotate_mode.in("Good") || this.rotate_mode.in("Const"))) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("resetting rotation");
            }
            WurstplusPosManager.restorePosition();
            WurstplusRotationUtil.restoreRotations();
        }
    }, new Predicate[0]);
    @EventHandler
    private final Listener<WurstplusEventPacket.ReceivePacket> receive_listener = new Listener<WurstplusEventPacket.ReceivePacket>(receivePacket -> {
        Object object;
        if (receivePacket.get_packet() instanceof SPacketSoundEffect && ((SPacketSoundEffect)(object = (SPacketSoundEffect)((Object)receivePacket.get_packet()))).getCategory() == SoundCategory.BLOCKS && ((SPacketSoundEffect)object).getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
            for (Entity entity : WurstplusAutoCrystal.mc.world.loadedEntityList) {
                if (!(entity instanceof EntityEnderCrystal) || !(entity.getDistance(((SPacketSoundEffect)object).getX(), ((SPacketSoundEffect)object).getY(), ((SPacketSoundEffect)object).getZ()) <= 6.0)) continue;
                WurstplusAutoCrystal.mc.world.removeEntityFromWorld(entity.getEntityId());
            }
        }
        if (receivePacket.get_packet() instanceof SPacketSpawnObject && ((SPacketSpawnObject)(object = (SPacketSpawnObject)((Object)receivePacket.get_packet()))).getType() == 51 && this.placePosList.contains(new BlockPos(((SPacketSpawnObject)object).getX(), ((SPacketSpawnObject)object).getY(), ((SPacketSpawnObject)object).getZ()).down())) {
            CPacketUseEntity cPacketUseEntity = new CPacketUseEntity();
            cPacketUseEntity.action = CPacketUseEntity$Action.ATTACK;
            cPacketUseEntity.entityId = ((SPacketSpawnObject)object).getEntityID();
            mc.getConnection().sendPacket(cPacketUseEntity);
        }
    }, new Predicate[0]);

    public WurstplusAutoCrystal() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.name = "Auto Crystal";
        this.tag = "AutoCrystal";
        this.description = "kills people (if ur good)";
    }

    public void do_ca() {
        this.did_anything = false;
        if (WurstplusAutoCrystal.mc.player == null || WurstplusAutoCrystal.mc.world == null) {
            return;
        }
        if (this.rainbow_mode.get_value(true)) {
            this.cycle_rainbow();
        }
        if (this.remove_visual_timer.passed(1000L)) {
            this.remove_visual_timer.reset();
            this.attacked_crystals.clear();
        }
        if (this.check_pause()) {
            return;
        }
        if (this.place_crystal.get_value(true) && this.place_delay_counter > this.place_timeout) {
            this.place_crystal();
        }
        if (this.break_crystal.get_value(true) && this.break_delay_counter > this.break_timeout) {
            this.break_crystal();
        }
        if (!this.did_anything) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            this.autoez_target = null;
            this.is_rotating = false;
        }
        if (this.autoez_target != null) {
            WurstplusAutoEz.add_target(this.autoez_target.getName());
            this.detail_name = this.autoez_target.getName();
            this.detail_hp = Math.round(this.autoez_target.getHealth() + this.autoez_target.getAbsorptionAmount());
        }
        this.render_block_old = this.render_block_init;
        ++this.break_delay_counter;
        ++this.place_delay_counter;
    }

    @Override
    public void update() {
        if (this.rotate_mode.in("Off") || this.rotate_mode.in("Old")) {
            this.do_ca();
        }
    }

    public void cycle_rainbow() {
        float[] fArray = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0f};
        int n = Color.HSBtoRGB(fArray[0], this.sat.get_value(1), this.brightness.get_value(1));
        this.r.set_value(n >> 16 & 0xFF);
        this.g.set_value(n >> 8 & 0xFF);
        this.b.set_value(n & 0xFF);
    }

    public EntityEnderCrystal get_best_crystal() {
        double d = 0.0;
        double d2 = this.max_self_damage.get_value(1);
        double d3 = 0.0;
        EntityEnderCrystal entityEnderCrystal = null;
        for (Entity entity : WurstplusAutoCrystal.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) continue;
            EntityEnderCrystal entityEnderCrystal2 = (EntityEnderCrystal)entity;
            if (WurstplusAutoCrystal.mc.player.getDistance(entityEnderCrystal2) > (float)(!WurstplusAutoCrystal.mc.player.canEntityBeSeen(entityEnderCrystal2) ? this.hit_range_wall.get_value(1) : this.hit_range.get_value(1)) || !WurstplusAutoCrystal.mc.player.canEntityBeSeen(entityEnderCrystal2) && this.raytrace.get_value(true) || this.attacked_crystals.containsKey(entityEnderCrystal2) && this.attacked_crystals.get(entityEnderCrystal2) > this.antiStuckTries.get_value(1) && this.anti_stuck.get_value(true)) continue;
            for (EntityPlayer entityPlayer : WurstplusAutoCrystal.mc.world.playerEntities) {
                double d4;
                if (entityPlayer == WurstplusAutoCrystal.mc.player || WurstplusFriendUtil.isFriend(entityPlayer.getName()) || entityPlayer.getDistance(WurstplusAutoCrystal.mc.player) >= (float)this.enemyRange.get_value(1) || entityPlayer.isDead || entityPlayer.getHealth() <= 0.0f) continue;
                boolean bl = this.faceplace_check.get_value(true) && WurstplusAutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double d5 = entityPlayer.getHealth() < (float)this.faceplace_mode_damage.get_value(1) && this.faceplace_mode.get_value(true) && !bl || this.get_armor_fucker(entityPlayer) && !bl ? 2.0 : (double)this.min_player_break.get_value(1);
                double d6 = WurstplusCrystalUtil.calculateDamage(entityEnderCrystal2, entityPlayer);
                if (d6 < d5 || (d4 = (double)WurstplusCrystalUtil.calculateDamage(entityEnderCrystal2, WurstplusAutoCrystal.mc.player)) > d2 || this.anti_suicide.get_value(true) && (double)(WurstplusAutoCrystal.mc.player.getHealth() + WurstplusAutoCrystal.mc.player.getAbsorptionAmount()) - d4 <= 0.5 || !(d6 > d) || this.jumpy_mode.get_value(true)) continue;
                d = d6;
                entityEnderCrystal = entityEnderCrystal2;
            }
            if (!this.jumpy_mode.get_value(true) || !(WurstplusAutoCrystal.mc.player.getDistanceSq(entityEnderCrystal2) > d3)) continue;
            d3 = WurstplusAutoCrystal.mc.player.getDistanceSq(entityEnderCrystal2);
            entityEnderCrystal = entityEnderCrystal2;
        }
        return entityEnderCrystal;
    }

    public BlockPos get_best_block() {
        double d = 0.0;
        double d2 = this.max_self_damage.get_value(1);
        BlockPos blockPos = null;
        List<BlockPos> list = WurstplusCrystalUtil.possiblePlacePositions(this.place_range.get_value(1), this.endcrystal.get_value(true));
        for (EntityPlayer entityPlayer : WurstplusAutoCrystal.mc.world.playerEntities) {
            if (WurstplusFriendUtil.isFriend(entityPlayer.getName())) continue;
            for (BlockPos blockPos2 : list) {
                double d3;
                if (entityPlayer == WurstplusAutoCrystal.mc.player || entityPlayer.getDistance(WurstplusAutoCrystal.mc.player) >= (float)this.enemyRange.get_value(1) || !WurstplusBlockUtil.rayTracePlaceCheck(blockPos2, this.raytrace.get_value(true)) || !WurstplusBlockUtil.canSeeBlock(blockPos2) && WurstplusAutoCrystal.mc.player.getDistance(blockPos2.getX(), blockPos2.getY(), blockPos2.getZ()) > (double)this.wallPlaceRange.get_value(1) || entityPlayer.isDead || entityPlayer.getHealth() <= 0.0f) continue;
                boolean bl = this.faceplace_check.get_value(true) && WurstplusAutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double d4 = entityPlayer.getHealth() < (float)this.faceplace_mode_damage.get_value(1) && this.faceplace_mode.get_value(true) && !bl || this.get_armor_fucker(entityPlayer) && !bl ? 2.0 : (double)this.min_player_place.get_value(1);
                double d5 = WurstplusCrystalUtil.calculateDamage((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 1.0, (double)blockPos2.getZ() + 0.5, entityPlayer);
                if (d5 < d4 || (d3 = (double)WurstplusCrystalUtil.calculateDamage((double)blockPos2.getX() + 0.5, (double)blockPos2.getY() + 1.0, (double)blockPos2.getZ() + 0.5, WurstplusAutoCrystal.mc.player)) > d2 || this.anti_suicide.get_value(true) && (double)(WurstplusAutoCrystal.mc.player.getHealth() + WurstplusAutoCrystal.mc.player.getAbsorptionAmount()) - d3 <= 0.5 || !(d5 > d)) continue;
                d = d5;
                blockPos = blockPos2;
                this.autoez_target = entityPlayer;
            }
        }
        list.clear();
        this.render_damage_value = d;
        this.render_block_init = blockPos;
        return blockPos;
    }

    public void place_crystal() {
        BlockPos blockPos = this.get_best_block();
        if (blockPos == null) {
            return;
        }
        this.place_delay_counter = 0;
        this.already_attacking = false;
        boolean bl = false;
        if (WurstplusAutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (WurstplusAutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && this.auto_switch.get_value(true)) {
                if (this.find_crystals_hotbar() == -1) {
                    return;
                }
                WurstplusAutoCrystal.mc.player.inventory.currentItem = this.find_crystals_hotbar();
                return;
            }
        } else {
            bl = true;
        }
        this.did_anything = true;
        this.rotate_to_pos(blockPos);
        WurstplusBlockUtil.placeCrystalOnBlock(blockPos, bl ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        this.placePosList.add(blockPos);
    }

    public boolean get_armor_fucker(EntityPlayer entityPlayer) {
        for (ItemStack itemStack : entityPlayer.getArmorInventoryList()) {
            if (itemStack == null || itemStack.getItem() == Items.AIR) {
                return true;
            }
            float f = (float)(itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float)itemStack.getMaxDamage() * 100.0f;
            if (!this.fuck_armor_mode.get_value(true) || !((float)this.fuck_armor_mode_precent.get_value(1) >= f)) continue;
            return true;
        }
        return false;
    }

    public void break_crystal() {
        int n;
        EntityEnderCrystal entityEnderCrystal = this.get_best_crystal();
        if (entityEnderCrystal == null) {
            return;
        }
        if (this.anti_weakness.get_value(true) && WurstplusAutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            n = 1;
            if (WurstplusAutoCrystal.mc.player.isPotionActive(MobEffects.STRENGTH) && Objects.requireNonNull(WurstplusAutoCrystal.mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                n = 0;
            }
            if (n != 0) {
                if (!this.already_attacking) {
                    this.already_attacking = true;
                }
                int n2 = -1;
                for (int i = 0; i < 9; ++i) {
                    ItemStack itemStack = WurstplusAutoCrystal.mc.player.inventory.getStackInSlot(i);
                    if (!(itemStack.getItem() instanceof ItemSword) && !(itemStack.getItem() instanceof ItemTool)) continue;
                    n2 = i;
                    WurstplusAutoCrystal.mc.playerController.updateController();
                    break;
                }
                if (n2 != -1) {
                    WurstplusAutoCrystal.mc.player.inventory.currentItem = n2;
                }
            }
        }
        this.did_anything = true;
        this.rotate_to(entityEnderCrystal);
        for (n = 0; n < this.break_trys.get_value(1); ++n) {
            WurstplusEntityUtil.attackEntity(entityEnderCrystal, true, this.swing);
        }
        this.add_attacked_crystal(entityEnderCrystal);
        if (this.client_side.get_value(true)) {
            WurstplusAutoCrystal.mc.world.removeEntityFromWorld(entityEnderCrystal.getEntityId());
        }
        this.break_delay_counter = 0;
    }

    public boolean check_pause() {
        if (this.find_crystals_hotbar() == -1 && WurstplusAutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            return true;
        }
        if (this.stop_while_mining.get_value(true) && WurstplusAutoCrystal.mc.gameSettings.keyBindAttack.isKeyDown() && WurstplusAutoCrystal.mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        if (Wurstplus.get_hack_manager().get_module_with_tag("Surround").is_active()) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        if (Wurstplus.get_hack_manager().get_module_with_tag("HoleFill").is_active()) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        if (Wurstplus.get_hack_manager().get_module_with_tag("Trap").is_active()) {
            if (this.old_render.get_value(true)) {
                this.render_block_init = null;
            }
            return true;
        }
        return false;
    }

    private int find_crystals_hotbar() {
        for (int i = 0; i < 9; ++i) {
            if (WurstplusAutoCrystal.mc.player.inventory.getStackInSlot(i).getItem() != Items.END_CRYSTAL) continue;
            return i;
        }
        return -1;
    }

    private void add_attacked_crystal(EntityEnderCrystal entityEnderCrystal) {
        if (this.attacked_crystals.containsKey(entityEnderCrystal)) {
            int n = this.attacked_crystals.get(entityEnderCrystal);
            this.attacked_crystals.put(entityEnderCrystal, n + 1);
        } else {
            this.attacked_crystals.put(entityEnderCrystal, 1);
        }
    }

    public void rotate_to_pos(BlockPos blockPos) {
        float[] fArray = this.rotate_mode.in("Const") ? WurstplusMathUtil.calcAngle(WurstplusAutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float)blockPos.getX() + 0.5f, (float)blockPos.getY() + 0.5f, (float)blockPos.getZ() + 0.5f)) : WurstplusMathUtil.calcAngle(WurstplusAutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((float)blockPos.getX() + 0.5f, (float)blockPos.getY() - 0.5f, (float)blockPos.getZ() + 0.5f));
        if (this.rotate_mode.in("Off")) {
            this.is_rotating = false;
        }
        if (this.rotate_mode.in("Good") || this.rotate_mode.in("Const")) {
            WurstplusRotationUtil.setPlayerRotations(fArray[0], fArray[1]);
        }
        if (this.rotate_mode.in("Old")) {
            this.yaw = fArray[0];
            this.pitch = fArray[1];
            this.is_rotating = true;
        }
    }

    public void rotate_to(Entity entity) {
        float[] fArray = WurstplusMathUtil.calcAngle(WurstplusAutoCrystal.mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
        if (this.rotate_mode.in("Off")) {
            this.is_rotating = false;
        }
        if (this.rotate_mode.in("Good")) {
            WurstplusRotationUtil.setPlayerRotations(fArray[0], fArray[1]);
        }
        if (this.rotate_mode.in("Old") || this.rotate_mode.in("Cont")) {
            this.yaw = fArray[0];
            this.pitch = fArray[1];
            this.is_rotating = true;
        }
    }

    @Override
    public void render(WurstplusEventRender wurstplusEventRender) {
        if (this.render_block_init == null) {
            return;
        }
        if (this.render_mode.in("None")) {
            return;
        }
        if (this.render_mode.in("Pretty")) {
            this.outline = true;
            this.solid = true;
        }
        if (this.render_mode.in("Solid")) {
            this.outline = false;
            this.solid = true;
        }
        if (this.render_mode.in("Outline")) {
            this.outline = true;
            this.solid = false;
        }
        this.render_block(this.render_block_init);
        if (this.future_render.get_value(true) && this.render_block_old != null) {
            this.render_block(this.render_block_old);
        }
        if (this.render_damage.get_value(true)) {
            WurstplusRenderUtil.drawText(this.render_block_init, (Math.floor(this.render_damage_value) == this.render_damage_value ? Integer.valueOf((int)this.render_damage_value) : String.format("%.1f", this.render_damage_value)) + "");
        }
    }

    public void render_block(BlockPos blockPos) {
        BlockPos blockPos2 = this.top_block.get_value(true) ? blockPos.up() : blockPos;
        float f = (float)this.height.get_value(1.0);
        if (this.solid) {
            RenderHelp.prepare("quads");
            RenderHelp.draw_cube(RenderHelp.get_buffer_build(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), 1.0f, f, 1.0f, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a.get_value(1), "all");
            RenderHelp.release();
        }
        if (this.outline) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), 1.0f, f, 1.0f, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a_out.get_value(1), "all");
            RenderHelp.release();
        }
    }

    @Override
    public void enable() {
        this.place_timeout = this.place_delay.get_value(1);
        this.break_timeout = this.break_delay.get_value(1);
        this.is_rotating = false;
        this.autoez_target = null;
        this.remove_visual_timer.reset();
        this.detail_name = null;
        this.detail_hp = 20;
    }

    @Override
    public void disable() {
        this.render_block_init = null;
        this.autoez_target = null;
    }

    @Override
    public String array_detail() {
        return this.detail_name != null ? this.detail_name + " | " + this.detail_hp : "None";
    }
}
