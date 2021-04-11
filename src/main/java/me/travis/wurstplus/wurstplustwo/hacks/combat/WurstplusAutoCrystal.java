package me.travis.wurstplus.wurstplustwo.hacks.combat;

import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRenderUtil;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventRender;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMathUtil;
import net.minecraft.util.math.Vec3d;
import me.travis.wurstplus.Wurstplus;
import net.minecraft.item.ItemPickaxe;
import me.travis.wurstplus.wurstplustwo.util.WurstplusEntityUtil;
import net.minecraft.item.ItemTool;
import net.minecraft.item.ItemSword;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import me.travis.wurstplus.wurstplustwo.util.WurstplusBlockUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusCrystalUtil;
import net.minecraft.init.Items;
import me.travis.wurstplus.wurstplustwo.util.WurstplusFriendUtil;
import java.awt.Color;
import me.travis.wurstplus.wurstplustwo.hacks.chat.WurstplusAutoEz;
import java.util.Iterator;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity$Action;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.network.play.server.SPacketSoundEffect;
import me.travis.wurstplus.wurstplustwo.util.WurstplusRotationUtil;
import me.travis.wurstplus.wurstplustwo.util.WurstplusPosManager;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import me.travis.wurstplus.wurstplustwo.util.WurstplusMessageUtil;
import net.minecraft.network.play.client.CPacketPlayer;
import java.util.function.Predicate;
import java.util.concurrent.CopyOnWriteArrayList;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusCategory;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventMotionUpdate;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventPacket;
import me.zero.alpine.fork.listener.EventHandler;
import me.travis.wurstplus.wurstplustwo.event.events.WurstplusEventEntityRemoved;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.player.EntityPlayer;
import me.travis.wurstplus.wurstplustwo.util.WurstplusTimer;
import net.minecraft.util.math.BlockPos;
import java.util.List;
import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.concurrent.ConcurrentHashMap;
import me.travis.wurstplus.wurstplustwo.guiscreen.settings.WurstplusSetting;
import me.travis.wurstplus.wurstplustwo.hacks.WurstplusHack;

public class WurstplusAutoCrystal extends WurstplusHack
{
    WurstplusSetting debug;
    WurstplusSetting place_crystal;
    WurstplusSetting break_crystal;
    WurstplusSetting break_trys;
    WurstplusSetting anti_weakness;
    WurstplusSetting enemyRange;
    WurstplusSetting hit_range;
    WurstplusSetting place_range;
    WurstplusSetting hit_range_wall;
    WurstplusSetting wallPlaceRange;
    WurstplusSetting place_delay;
    WurstplusSetting break_delay;
    WurstplusSetting min_player_place;
    WurstplusSetting min_player_break;
    WurstplusSetting max_self_damage;
    WurstplusSetting rotate_mode;
    WurstplusSetting raytrace;
    WurstplusSetting auto_switch;
    WurstplusSetting anti_suicide;
    WurstplusSetting client_side;
    WurstplusSetting jumpy_mode;
    WurstplusSetting anti_stuck;
    WurstplusSetting antiStuckTries;
    WurstplusSetting endcrystal;
    WurstplusSetting faceplace_mode;
    WurstplusSetting faceplace_mode_damage;
    WurstplusSetting fuck_armor_mode;
    WurstplusSetting fuck_armor_mode_precent;
    WurstplusSetting stop_while_mining;
    WurstplusSetting faceplace_check;
    WurstplusSetting swing;
    WurstplusSetting render_mode;
    WurstplusSetting old_render;
    WurstplusSetting future_render;
    WurstplusSetting top_block;
    WurstplusSetting r;
    WurstplusSetting g;
    WurstplusSetting b;
    WurstplusSetting a;
    WurstplusSetting a_out;
    WurstplusSetting rainbow_mode;
    WurstplusSetting sat;
    WurstplusSetting brightness;
    WurstplusSetting height;
    WurstplusSetting render_damage;
    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attacked_crystals;
    private final List<BlockPos> placePosList;
    private final WurstplusTimer remove_visual_timer;
    private EntityPlayer autoez_target;
    private String detail_name;
    private int detail_hp;
    private BlockPos render_block_init;
    private BlockPos render_block_old;
    private double render_damage_value;
    private float yaw;
    private float pitch;
    private boolean already_attacking;
    private boolean is_rotating;
    private boolean did_anything;
    private boolean outline;
    private boolean solid;
    private int place_timeout;
    private int break_timeout;
    private int break_delay_counter;
    private int place_delay_counter;
    @EventHandler
    private Listener<WurstplusEventEntityRemoved> on_entity_removed;
    @EventHandler
    private Listener<WurstplusEventPacket.SendPacket> send_listener;
    @EventHandler
    private Listener<WurstplusEventMotionUpdate> on_movement;
    @EventHandler
    private final Listener<WurstplusEventPacket.ReceivePacket> receive_listener;
    
    public WurstplusAutoCrystal() {
        super(WurstplusCategory.WURSTPLUS_COMBAT);
        this.debug = this.create("Debug", "CaDebug", false);
        this.place_crystal = this.create("Place", "CaPlace", true);
        this.break_crystal = this.create("Break", "CaBreak", true);
        this.break_trys = this.create("Break Attempts", "CaBreakAttempts", 2, 1, 6);
        this.anti_weakness = this.create("Anti-Weakness", "CaAntiWeakness", true);
        this.enemyRange = this.create("Enemy Range", "CaEnemyRange", 9.0, 5.0, 15.0);
        this.hit_range = this.create("Hit Range", "CaHitRange", 5.199999809265137, 1.0, 6.0);
        this.place_range = this.create("Place Range", "CaPlaceRange", 5.199999809265137, 1.0, 6.0);
        this.hit_range_wall = this.create("Range Wall", "CaRangeWall", 4.0, 1.0, 6.0);
        this.wallPlaceRange = this.create("Place Wall Range", "CaPlaceWallRange", 4.0, 0.0, 6.0);
        this.place_delay = this.create("Place Delay", "CaPlaceDelay", 0, 0, 10);
        this.break_delay = this.create("Break Delay", "CaBreakDelay", 2, 0, 10);
        this.min_player_place = this.create("Min Enemy Place", "CaMinEnemyPlace", 8, 0, 20);
        this.min_player_break = this.create("Min Enemy Break", "CaMinEnemyBreak", 6, 0, 20);
        this.max_self_damage = this.create("Max Self Damage", "CaMaxSelfDamage", 6, 0, 20);
        this.rotate_mode = this.create("Rotate", "CaRotateMode", "Good", this.combobox("Off", "Old", "Const", "Good"));
        this.raytrace = this.create("Raytrace", "CaRaytrace", false);
        this.auto_switch = this.create("Auto Switch", "CaAutoSwitch", true);
        this.anti_suicide = this.create("Anti Suicide", "CaAntiSuicide", true);
        this.client_side = this.create("Client Side", "CaClientSide", false);
        this.jumpy_mode = this.create("Jumpy Mode", "CaJumpyMode", false);
        this.anti_stuck = this.create("Anti Stuck", "CaAntiStuck", false);
        this.antiStuckTries = this.create("Anti Stuck Tries", "CaAntiStuckTries", 5, 1, 15);
        this.endcrystal = this.create("1.13 Mode", "CaThirteen", false);
        this.faceplace_mode = this.create("Tabbott Mode", "CaTabbottMode", true);
        this.faceplace_mode_damage = this.create("T Health", "CaTabbottModeHealth", 8, 0, 36);
        this.fuck_armor_mode = this.create("Armor Destroy", "CaArmorDestory", true);
        this.fuck_armor_mode_precent = this.create("Armor %", "CaArmorPercent", 25, 0, 100);
        this.stop_while_mining = this.create("Stop While Mining", "CaStopWhileMining", false);
        this.faceplace_check = this.create("No Sword FP", "CaJumpyFaceMode", false);
        this.swing = this.create("Swing", "CaSwing", "Mainhand", this.combobox("Mainhand", "Offhand", "Both", "None"));
        this.render_mode = this.create("Render", "CaRenderMode", "Pretty", this.combobox("Pretty", "Solid", "Outline", "None"));
        this.old_render = this.create("Old Render", "CaOldRender", false);
        this.future_render = this.create("Future Render", "CaFutureRender", false);
        this.top_block = this.create("Top Block", "CaTopBlock", false);
        this.r = this.create("R", "CaR", 255, 0, 255);
        this.g = this.create("G", "CaG", 255, 0, 255);
        this.b = this.create("B", "CaB", 255, 0, 255);
        this.a = this.create("A", "CaA", 100, 0, 255);
        this.a_out = this.create("Outline A", "CaOutlineA", 255, 0, 255);
        this.rainbow_mode = this.create("Rainbow", "CaRainbow", false);
        this.sat = this.create("Satiation", "CaSatiation", 0.8, 0.0, 1.0);
        this.brightness = this.create("Brightness", "CaBrightness", 0.8, 0.0, 1.0);
        this.height = this.create("Height", "CaHeight", 1.0, 0.0, 1.0);
        this.render_damage = this.create("Render Damage", "RenderDamage", true);
        this.attacked_crystals = new ConcurrentHashMap<EntityEnderCrystal, Integer>();
        this.placePosList = new CopyOnWriteArrayList<BlockPos>();
        this.remove_visual_timer = new WurstplusTimer();
        this.autoez_target = null;
        this.detail_name = null;
        this.detail_hp = 0;
        this.already_attacking = false;
        this.on_entity_removed = new Listener<WurstplusEventEntityRemoved>(wurstplusEventEntityRemoved -> {
            if (wurstplusEventEntityRemoved.get_entity() instanceof EntityEnderCrystal) {
                this.attacked_crystals.remove(wurstplusEventEntityRemoved.get_entity());
            }
            return;
        }, (Predicate<WurstplusEventEntityRemoved>[])new Predicate[0]);
        CPacketPlayer cPacketPlayer;
        CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock;
        this.send_listener = new Listener<WurstplusEventPacket.SendPacket>(sendPacket -> {
            if (sendPacket.get_packet() instanceof CPacketPlayer && this.is_rotating && this.rotate_mode.in("Old")) {
                if (this.debug.get_value(true)) {
                    WurstplusMessageUtil.send_client_message("Rotating");
                }
                cPacketPlayer = (CPacketPlayer)sendPacket.get_packet();
                cPacketPlayer.yaw = this.yaw;
                cPacketPlayer.pitch = this.pitch;
                this.is_rotating = false;
            }
            if (sendPacket.get_packet() instanceof CPacketPlayerTryUseItemOnBlock && this.is_rotating && this.rotate_mode.in("Old")) {
                if (this.debug.get_value(true)) {
                    WurstplusMessageUtil.send_client_message("Rotating");
                }
                cPacketPlayerTryUseItemOnBlock = (CPacketPlayerTryUseItemOnBlock)sendPacket.get_packet();
                cPacketPlayerTryUseItemOnBlock.facingX = (float)this.render_block_init.getX();
                cPacketPlayerTryUseItemOnBlock.facingY = (float)this.render_block_init.getY();
                cPacketPlayerTryUseItemOnBlock.facingZ = (float)this.render_block_init.getZ();
                this.is_rotating = false;
            }
            return;
        }, (Predicate<WurstplusEventPacket.SendPacket>[])new Predicate[0]);
        this.on_movement = new Listener<WurstplusEventMotionUpdate>(wurstplusEventMotionUpdate -> {
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
            return;
        }, (Predicate<WurstplusEventMotionUpdate>[])new Predicate[0]);
        SPacketSoundEffect sPacketSoundEffect;
        final Iterator<Entity> iterator;
        Entity entity;
        SPacketSpawnObject sPacketSpawnObject;
        CPacketUseEntity cPacketUseEntity;
        this.receive_listener = new Listener<WurstplusEventPacket.ReceivePacket>(receivePacket -> {
            if (receivePacket.get_packet() instanceof SPacketSoundEffect) {
                sPacketSoundEffect = (SPacketSoundEffect)receivePacket.get_packet();
                if (sPacketSoundEffect.getCategory() == SoundCategory.BLOCKS && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                    WurstplusAutoCrystal.mc.world.loadedEntityList.iterator();
                    while (iterator.hasNext()) {
                        entity = iterator.next();
                        if (entity instanceof EntityEnderCrystal && entity.getDistance(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) <= 6.0) {
                            WurstplusAutoCrystal.mc.world.removeEntityFromWorld(entity.getEntityId());
                        }
                    }
                }
            }
            if (receivePacket.get_packet() instanceof SPacketSpawnObject) {
                sPacketSpawnObject = (SPacketSpawnObject)receivePacket.get_packet();
                if (sPacketSpawnObject.getType() == 51) {
                    if (this.placePosList.contains(new BlockPos(sPacketSpawnObject.getX(), sPacketSpawnObject.getY(), sPacketSpawnObject.getZ()).down())) {
                        cPacketUseEntity = new CPacketUseEntity();
                        cPacketUseEntity.action = CPacketUseEntity$Action.ATTACK;
                        cPacketUseEntity.entityId = sPacketSpawnObject.getEntityID();
                        WurstplusAutoCrystal.mc.getConnection().sendPacket(cPacketUseEntity);
                    }
                }
            }
            return;
        }, (Predicate<WurstplusEventPacket.ReceivePacket>[])new Predicate[0]);
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
        final int hsBtoRGB = Color.HSBtoRGB((new float[] { System.currentTimeMillis() % 11520L / 11520.0f })[0], (float)this.sat.get_value(1), (float)this.brightness.get_value(1));
        this.r.set_value(hsBtoRGB >> 16 & 0xFF);
        this.g.set_value(hsBtoRGB >> 8 & 0xFF);
        this.b.set_value(hsBtoRGB & 0xFF);
    }
    
    public EntityEnderCrystal get_best_crystal() {
        double n = 0.0;
        final double n2 = this.max_self_damage.get_value(1);
        double distanceSq = 0.0;
        EntityEnderCrystal entityEnderCrystal = null;
        for (final Entity entity : WurstplusAutoCrystal.mc.world.loadedEntityList) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            final EntityEnderCrystal entityEnderCrystal2 = (EntityEnderCrystal)entity;
            if (WurstplusAutoCrystal.mc.player.getDistance(entityEnderCrystal2) > (WurstplusAutoCrystal.mc.player.canEntityBeSeen(entityEnderCrystal2) ? this.hit_range.get_value(1) : this.hit_range_wall.get_value(1))) {
                continue;
            }
            if (!WurstplusAutoCrystal.mc.player.canEntityBeSeen(entityEnderCrystal2) && this.raytrace.get_value(true)) {
                continue;
            }
            if (this.attacked_crystals.containsKey(entityEnderCrystal2) && this.attacked_crystals.get(entityEnderCrystal2) > this.antiStuckTries.get_value(1) && this.anti_stuck.get_value(true)) {
                continue;
            }
            for (final EntityPlayer entityPlayer : WurstplusAutoCrystal.mc.world.playerEntities) {
                if (entityPlayer == WurstplusAutoCrystal.mc.player) {
                    continue;
                }
                if (WurstplusFriendUtil.isFriend(entityPlayer.getName())) {
                    continue;
                }
                if (entityPlayer.getDistance(WurstplusAutoCrystal.mc.player) >= this.enemyRange.get_value(1)) {
                    continue;
                }
                if (entityPlayer.isDead) {
                    continue;
                }
                if (entityPlayer.getHealth() <= 0.0f) {
                    continue;
                }
                final boolean b = this.faceplace_check.get_value(true) && WurstplusAutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double n3;
                if ((entityPlayer.getHealth() < this.faceplace_mode_damage.get_value(1) && this.faceplace_mode.get_value(true) && !b) || (this.get_armor_fucker(entityPlayer) && !b)) {
                    n3 = 2.0;
                }
                else {
                    n3 = this.min_player_break.get_value(1);
                }
                final double n4 = WurstplusCrystalUtil.calculateDamage(entityEnderCrystal2, entityPlayer);
                if (n4 < n3) {
                    continue;
                }
                final double n5 = WurstplusCrystalUtil.calculateDamage(entityEnderCrystal2, WurstplusAutoCrystal.mc.player);
                if (n5 > n2) {
                    continue;
                }
                if (this.anti_suicide.get_value(true) && WurstplusAutoCrystal.mc.player.getHealth() + WurstplusAutoCrystal.mc.player.getAbsorptionAmount() - n5 <= 0.5) {
                    continue;
                }
                if (n4 <= n || this.jumpy_mode.get_value(true)) {
                    continue;
                }
                n = n4;
                entityEnderCrystal = entityEnderCrystal2;
            }
            if (!this.jumpy_mode.get_value(true) || WurstplusAutoCrystal.mc.player.getDistanceSq(entityEnderCrystal2) <= distanceSq) {
                continue;
            }
            distanceSq = WurstplusAutoCrystal.mc.player.getDistanceSq(entityEnderCrystal2);
            entityEnderCrystal = entityEnderCrystal2;
        }
        return entityEnderCrystal;
    }
    
    public BlockPos get_best_block() {
        double render_damage_value = 0.0;
        final double n = this.max_self_damage.get_value(1);
        BlockPos render_block_init = null;
        final List<BlockPos> possiblePlacePositions = WurstplusCrystalUtil.possiblePlacePositions((float)this.place_range.get_value(1), this.endcrystal.get_value(true));
        for (final EntityPlayer autoez_target : WurstplusAutoCrystal.mc.world.playerEntities) {
            if (WurstplusFriendUtil.isFriend(autoez_target.getName())) {
                continue;
            }
            for (final BlockPos blockPos : possiblePlacePositions) {
                if (autoez_target == WurstplusAutoCrystal.mc.player) {
                    continue;
                }
                if (autoez_target.getDistance(WurstplusAutoCrystal.mc.player) >= this.enemyRange.get_value(1)) {
                    continue;
                }
                if (!WurstplusBlockUtil.rayTracePlaceCheck(blockPos, this.raytrace.get_value(true))) {
                    continue;
                }
                if (!WurstplusBlockUtil.canSeeBlock(blockPos) && WurstplusAutoCrystal.mc.player.getDistance(blockPos.getX(), blockPos.getY(), blockPos.getZ()) > this.wallPlaceRange.get_value(1)) {
                    continue;
                }
                if (autoez_target.isDead) {
                    continue;
                }
                if (autoez_target.getHealth() <= 0.0f) {
                    continue;
                }
                final boolean b = this.faceplace_check.get_value(true) && WurstplusAutoCrystal.mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                double n2;
                if ((autoez_target.getHealth() < this.faceplace_mode_damage.get_value(1) && this.faceplace_mode.get_value(true) && !b) || (this.get_armor_fucker(autoez_target) && !b)) {
                    n2 = 2.0;
                }
                else {
                    n2 = this.min_player_place.get_value(1);
                }
                final double n3 = WurstplusCrystalUtil.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, autoez_target);
                if (n3 < n2) {
                    continue;
                }
                final double n4 = WurstplusCrystalUtil.calculateDamage(blockPos.getX() + 0.5, blockPos.getY() + 1.0, blockPos.getZ() + 0.5, WurstplusAutoCrystal.mc.player);
                if (n4 > n) {
                    continue;
                }
                if (this.anti_suicide.get_value(true) && WurstplusAutoCrystal.mc.player.getHealth() + WurstplusAutoCrystal.mc.player.getAbsorptionAmount() - n4 <= 0.5) {
                    continue;
                }
                if (n3 <= render_damage_value) {
                    continue;
                }
                render_damage_value = n3;
                render_block_init = blockPos;
                this.autoez_target = autoez_target;
            }
        }
        possiblePlacePositions.clear();
        this.render_damage_value = render_damage_value;
        return this.render_block_init = render_block_init;
    }
    
    public void place_crystal() {
        final BlockPos get_best_block = this.get_best_block();
        if (get_best_block == null) {
            return;
        }
        this.place_delay_counter = 0;
        this.already_attacking = false;
        boolean b = false;
        if (WurstplusAutoCrystal.mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (WurstplusAutoCrystal.mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && this.auto_switch.get_value(true)) {
                if (this.find_crystals_hotbar() == -1) {
                    return;
                }
                WurstplusAutoCrystal.mc.player.inventory.currentItem = this.find_crystals_hotbar();
                return;
            }
        }
        else {
            b = true;
        }
        this.did_anything = true;
        this.rotate_to_pos(get_best_block);
        WurstplusBlockUtil.placeCrystalOnBlock(get_best_block, b ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
        this.placePosList.add(get_best_block);
    }
    
    public boolean get_armor_fucker(final EntityPlayer entityPlayer) {
        for (final ItemStack itemStack : entityPlayer.getArmorInventoryList()) {
            if (itemStack == null || itemStack.getItem() == Items.AIR) {
                return true;
            }
            final float n = (itemStack.getMaxDamage() - itemStack.getItemDamage()) / (float)itemStack.getMaxDamage() * 100.0f;
            if (this.fuck_armor_mode.get_value(true) && this.fuck_armor_mode_precent.get_value(1) >= n) {
                return true;
            }
        }
        return false;
    }
    
    public void break_crystal() {
        final EntityEnderCrystal get_best_crystal = this.get_best_crystal();
        if (get_best_crystal == null) {
            return;
        }
        if (this.anti_weakness.get_value(true) && WurstplusAutoCrystal.mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            boolean b = true;
            if (WurstplusAutoCrystal.mc.player.isPotionActive(MobEffects.STRENGTH) && Objects.requireNonNull(WurstplusAutoCrystal.mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                b = false;
            }
            if (b) {
                if (!this.already_attacking) {
                    this.already_attacking = true;
                }
                int currentItem = -1;
                for (int i = 0; i < 9; ++i) {
                    final ItemStack stackInSlot = WurstplusAutoCrystal.mc.player.inventory.getStackInSlot(i);
                    if (stackInSlot.getItem() instanceof ItemSword || stackInSlot.getItem() instanceof ItemTool) {
                        currentItem = i;
                        WurstplusAutoCrystal.mc.playerController.updateController();
                        break;
                    }
                }
                if (currentItem != -1) {
                    WurstplusAutoCrystal.mc.player.inventory.currentItem = currentItem;
                }
            }
        }
        this.did_anything = true;
        this.rotate_to(get_best_crystal);
        for (int j = 0; j < this.break_trys.get_value(1); ++j) {
            WurstplusEntityUtil.attackEntity(get_best_crystal, true, this.swing);
        }
        this.add_attacked_crystal(get_best_crystal);
        if (this.client_side.get_value(true)) {
            WurstplusAutoCrystal.mc.world.removeEntityFromWorld(get_best_crystal.getEntityId());
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
            if (WurstplusAutoCrystal.mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                return i;
            }
        }
        return -1;
    }
    
    private void add_attacked_crystal(final EntityEnderCrystal entityEnderCrystal) {
        if (this.attacked_crystals.containsKey(entityEnderCrystal)) {
            this.attacked_crystals.put(entityEnderCrystal, this.attacked_crystals.get(entityEnderCrystal) + 1);
        }
        else {
            this.attacked_crystals.put(entityEnderCrystal, 1);
        }
    }
    
    public void rotate_to_pos(final BlockPos blockPos) {
        float[] array;
        if (this.rotate_mode.in("Const")) {
            array = WurstplusMathUtil.calcAngle(WurstplusAutoCrystal.mc.player.getPositionEyes(WurstplusAutoCrystal.mc.getRenderPartialTicks()), new Vec3d(blockPos.getX() + 0.5f, blockPos.getY() + 0.5f, blockPos.getZ() + 0.5f));
        }
        else {
            array = WurstplusMathUtil.calcAngle(WurstplusAutoCrystal.mc.player.getPositionEyes(WurstplusAutoCrystal.mc.getRenderPartialTicks()), new Vec3d(blockPos.getX() + 0.5f, blockPos.getY() - 0.5f, blockPos.getZ() + 0.5f));
        }
        if (this.rotate_mode.in("Off")) {
            this.is_rotating = false;
        }
        if (this.rotate_mode.in("Good") || this.rotate_mode.in("Const")) {
            WurstplusRotationUtil.setPlayerRotations(array[0], array[1]);
        }
        if (this.rotate_mode.in("Old")) {
            this.yaw = array[0];
            this.pitch = array[1];
            this.is_rotating = true;
        }
    }
    
    public void rotate_to(final Entity entity) {
        final float[] calcAngle = WurstplusMathUtil.calcAngle(WurstplusAutoCrystal.mc.player.getPositionEyes(WurstplusAutoCrystal.mc.getRenderPartialTicks()), entity.getPositionVector());
        if (this.rotate_mode.in("Off")) {
            this.is_rotating = false;
        }
        if (this.rotate_mode.in("Good")) {
            WurstplusRotationUtil.setPlayerRotations(calcAngle[0], calcAngle[1]);
        }
        if (this.rotate_mode.in("Old") || this.rotate_mode.in("Cont")) {
            this.yaw = calcAngle[0];
            this.pitch = calcAngle[1];
            this.is_rotating = true;
        }
    }
    
    @Override
    public void render(final WurstplusEventRender wurstplusEventRender) {
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
            WurstplusRenderUtil.drawText(this.render_block_init, ((Math.floor(this.render_damage_value) == this.render_damage_value) ? Integer.valueOf((int)this.render_damage_value) : String.format("%.1f", this.render_damage_value)) + "");
        }
    }
    
    public void render_block(final BlockPos blockPos) {
        final BlockPos blockPos2 = this.top_block.get_value(true) ? blockPos.up() : blockPos;
        final float n = (float)this.height.get_value(1.0);
        if (this.solid) {
            RenderHelp.prepare("quads");
            RenderHelp.draw_cube(RenderHelp.get_buffer_build(), (float)blockPos2.getX(), (float)blockPos2.getY(), (float)blockPos2.getZ(), 1.0f, n, 1.0f, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a.get_value(1), "all");
            RenderHelp.release();
        }
        if (this.outline) {
            RenderHelp.prepare("lines");
            RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), (float)blockPos2.getX(), (float)blockPos2.getY(), (float)blockPos2.getZ(), 1.0f, n, 1.0f, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a_out.get_value(1), "all");
            RenderHelp.release();
        }
    }
    
    public void enable() {
        this.place_timeout = this.place_delay.get_value(1);
        this.break_timeout = this.break_delay.get_value(1);
        this.is_rotating = false;
        this.autoez_target = null;
        this.remove_visual_timer.reset();
        this.detail_name = null;
        this.detail_hp = 20;
    }
    
    public void disable() {
        this.render_block_init = null;
        this.autoez_target = null;
    }
    
    @Override
    public String array_detail() {
        return (this.detail_name != null) ? (this.detail_name + " | " + this.detail_hp) : "None";
    }
    
    private /* synthetic */ void lambda$new$3(final WurstplusEventPacket.ReceivePacket receivePacket) {
        if (receivePacket.get_packet() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect sPacketSoundEffect = (SPacketSoundEffect)receivePacket.get_packet();
            if (sPacketSoundEffect.getCategory() == SoundCategory.BLOCKS && sPacketSoundEffect.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (final Entity entity : WurstplusAutoCrystal.mc.world.loadedEntityList) {
                    if (entity instanceof EntityEnderCrystal && entity.getDistance(sPacketSoundEffect.getX(), sPacketSoundEffect.getY(), sPacketSoundEffect.getZ()) <= 6.0) {
                        WurstplusAutoCrystal.mc.world.removeEntityFromWorld(entity.getEntityId());
                    }
                }
            }
        }
        if (receivePacket.get_packet() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject sPacketSpawnObject = (SPacketSpawnObject)receivePacket.get_packet();
            if (sPacketSpawnObject.getType() == 51 && this.placePosList.contains(new BlockPos(sPacketSpawnObject.getX(), sPacketSpawnObject.getY(), sPacketSpawnObject.getZ()).down())) {
                final CPacketUseEntity cPacketUseEntity = new CPacketUseEntity();
                cPacketUseEntity.action = CPacketUseEntity$Action.ATTACK;
                cPacketUseEntity.entityId = sPacketSpawnObject.getEntityID();
                WurstplusAutoCrystal.mc.getConnection().sendPacket(cPacketUseEntity);
            }
        }
    }
    
    private /* synthetic */ void lambda$new$2(final WurstplusEventMotionUpdate wurstplusEventMotionUpdate) {
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
    }
    
    private /* synthetic */ void lambda$new$1(final WurstplusEventPacket.SendPacket sendPacket) {
        if (sendPacket.get_packet() instanceof CPacketPlayer && this.is_rotating && this.rotate_mode.in("Old")) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Rotating");
            }
            final CPacketPlayer cPacketPlayer = (CPacketPlayer)sendPacket.get_packet();
            cPacketPlayer.yaw = this.yaw;
            cPacketPlayer.pitch = this.pitch;
            this.is_rotating = false;
        }
        if (sendPacket.get_packet() instanceof CPacketPlayerTryUseItemOnBlock && this.is_rotating && this.rotate_mode.in("Old")) {
            if (this.debug.get_value(true)) {
                WurstplusMessageUtil.send_client_message("Rotating");
            }
            final CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock = (CPacketPlayerTryUseItemOnBlock)sendPacket.get_packet();
            cPacketPlayerTryUseItemOnBlock.facingX = (float)this.render_block_init.getX();
            cPacketPlayerTryUseItemOnBlock.facingY = (float)this.render_block_init.getY();
            cPacketPlayerTryUseItemOnBlock.facingZ = (float)this.render_block_init.getZ();
            this.is_rotating = false;
        }
    }
    
    private /* synthetic */ void lambda$new$0(final WurstplusEventEntityRemoved wurstplusEventEntityRemoved) {
        if (wurstplusEventEntityRemoved.get_entity() instanceof EntityEnderCrystal) {
            this.attacked_crystals.remove(wurstplusEventEntityRemoved.get_entity());
        }
    }
}
