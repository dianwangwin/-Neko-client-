package lgbt.vaimok.neko.nekohax.modules.combat;

import lgbt.vaimok.neko.nekohax.NekoHax;
import lgbt.vaimok.neko.nekohax.event.events.EventEntityRemoved;
import lgbt.vaimok.neko.nekohax.event.events.EventMotionUpdate;
import lgbt.vaimok.neko.nekohax.event.events.EventPacket;
import lgbt.vaimok.neko.nekohax.event.events.EventRender;
import lgbt.vaimok.neko.nekohax.guiscreen.settings.Setting;
import lgbt.vaimok.neko.nekohax.modules.Category;
import lgbt.vaimok.neko.nekohax.modules.Module;
import lgbt.vaimok.neko.nekohax.modules.chat.AutoEz;
import lgbt.vaimok.neko.nekohax.turok.draw.RenderHelp;
import lgbt.vaimok.neko.nekohax.util.*;
import me.zero.alpine.fork.listener.EventHandler;
import me.zero.alpine.fork.listener.Listener;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class AutoCrystal extends Module {
    public AutoCrystal() {
        super(Category.combat);
        this.name        = "Auto Neko";
        this.tag         = "AutoCrystal";
        this.description = "kills people (if ur good)";
    }

    /**
     * @author ObsidianBreaker
     * @since 16/04/2021
     * Remapped and adapted
     */

    Setting place_crystal = create("Place", "CaPlace", true);
    Setting place_delay = create("Place Delay", "CaPlaceDelay", 1, 0, 10);
    Setting place_range = create("Place Range", "CaPlaceRange", 5.1D, 1.0D, 6.0D);
    Setting wallPlaceRange = create("Place Wall Range", "CaPlaceWallRange", 4.0D, 0.0D, 6.0D);
    Setting break_crystal = create("Break", "CaBreak", true);
    Setting break_delay = create("Break Delay", "CaBreakDelay", 1, 0, 10);
    Setting hit_range = create("Break Range", "CaHitRange", 5.1D, 1.0D, 6.0D);
    Setting hit_range_wall = create("Break Wall Range", "CaRangeWall", 4.0D, 1.0D, 6.0D);
    Setting break_trys = create("Break Attempts", "CaBreakAttempts", 1, 1, 6);
    Setting anti_weakness = create("Anti-Weakness", "CaAntiWeakness", true);
    Setting enemyRange = create("Enemy Range", "CaEnemyRange", 9.0D, 5.0D, 15.0D);
    Setting min_player_place = create("Min Enemy Place", "CaMinEnemyPlace", 6, 0, 20);
    Setting min_player_break = create("Min Enemy Break", "CaMinEnemyBreak", 6, 0, 20);
    Setting max_self_damage = create("Max Self Damage", "CaMaxSelfDamage", 8, 0, 20);
    Setting rotate_mode = create("Rotate", "CaRotateMode", "Off", combobox("Off", "Old", "Const", "Good"));
    Setting raytrace = create("Raytrace", "CaRaytrace", false);
    Setting auto_switch = create("Auto Switch", "CaAutoSwitch", false);
    Setting anti_suicide = create("Anti Suicide", "CaAntiSuicide", true);
    Setting client_side = create("Client Side", "CaClientSide", false);
    Setting break_all = create("Break All", "BreakAll", false);
    Setting anti_stuck = create("Anti Stuck", "CaAntiStuck", false);
    Setting antiStuckTries = create("Anti Stuck Tries", "CaAntiStuckTries", 5, 1, 15);
    Setting endcrystal = create("1.13 Mode", "CaThirteen", false);
    Setting faceplace_mode = create("Faceplace Mode", "CaFaceplaceMode", true);
    Setting faceplace_mode_damage = create("Faceplace Health", "CaFaceplaceModeHealth", 8, 0, 36);
    Setting fuck_armor_mode = create("Armor Destroy", "CaArmorDestory", true);
    Setting fuck_armor_mode_precent = create("Armor %", "CaArmorPercent", 25, 0, 100);
    Setting stop_while_mining = create("Stop While Mining", "CaStopWhileMining", false);
    Setting faceplace_check = create("No Sword FP", "CaJumpyFaceMode", false);
    Setting swing = create("Swing", "CaSwing", "Mainhand", combobox("Mainhand", "Offhand", "Both", "None"));
    Setting render_mode = create("Render", "CaRenderMode", "Pretty", combobox("Pretty", "Solid", "Outline", "None"));
    Setting old_render = create("Old Render", "CaOldRender", false);
    Setting future_render = create("Future Render", "CaFutureRender", false);
    Setting top_block = create("Top Block", "CaTopBlock", false);
    Setting r = create("R", "CaR", 255, 0, 255);
    Setting g = create("G", "CaG", 255, 0, 255);
    Setting b = create("B", "CaB", 255, 0, 255);
    Setting a = create("A", "CaA", 100, 0, 255);
    Setting a_out = create("Outline A", "CaOutlineA", 255, 0, 255);
    Setting rainbow_mode = create("Rainbow", "CaRainbow", false);
    Setting sat = create("Satiation", "CaSatiation", 0.8D, 0.0D, 1.0D);
    Setting brightness = create("Brightness", "CaBrightness", 0.8D, 0.0D, 1.0D);
    Setting height = create("Height", "CaHeight", 1.0D, 0.0D, 1.0D);
    Setting render_damage = create("Render Damage", "RenderDamage", true);
    
    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attacked_crystals = new ConcurrentHashMap<>();
    private final List<BlockPos> placePosList = new CopyOnWriteArrayList<>();
    private final Timer remove_visual_timer = new Timer();
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
    private final Listener<EventEntityRemoved> on_entity_removed = new Listener<>(event -> {
        if (event.get_entity() instanceof EntityEnderCrystal)
            attacked_crystals.remove(event.get_entity());
    });

    @EventHandler
    private final Listener<EventPacket.SendPacket> send_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof CPacketPlayer && is_rotating && rotate_mode.in("Old")) {
            CPacketPlayer p = (CPacketPlayer) event.get_packet();
            p.yaw = yaw;
            p.pitch = pitch;
            is_rotating = false;
        }
        if (event.get_packet() instanceof CPacketPlayerTryUseItemOnBlock && is_rotating && rotate_mode.in("Old")) {
            CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock) event.get_packet();
            p.facingX = render_block_init.getX();
            p.facingY = render_block_init.getY();
            p.facingZ = render_block_init.getZ();
            is_rotating = false;
        }
    });
    
    @EventHandler
    private final Listener<EventMotionUpdate> on_movement = new Listener<>(event -> {
        if (event.stage == 0 && (rotate_mode.in("Good") || rotate_mode.in("Const"))) {
            PosManager.updatePosition();
            RotationUtil.updateRotations();
            do_ca();
        }
        if (event.stage == 1 && (rotate_mode.in("Good") || rotate_mode.in("Const"))) {
            PosManager.restorePosition();
            RotationUtil.restoreRotations();
        }
    });

    @EventHandler
    private final Listener<EventPacket.ReceivePacket> receive_listener = new Listener<>(event -> {
        if (event.get_packet() instanceof SPacketSoundEffect) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.get_packet();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE)
                for (Entity e : mc.world.loadedEntityList) {
                    if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0D)
                        mc.world.removeEntityFromWorld(e.getEntityId());
                }
        }
        if (event.get_packet() instanceof SPacketSpawnObject) {
            SPacketSpawnObject spawnPacket = (SPacketSpawnObject) event.get_packet();
            if (spawnPacket.getType() == 51 && placePosList.contains((new BlockPos(spawnPacket.getX(), spawnPacket.getY(), spawnPacket.getZ())).down())) {
                CPacketUseEntity useEntity = new CPacketUseEntity();
                useEntity.action = CPacketUseEntity.Action.ATTACK;
                useEntity.entityId = spawnPacket.getEntityID();
                Objects.requireNonNull(mc.getConnection()).sendPacket(useEntity);
            }
        }
    });
    
        public void do_ca() {
            did_anything = false;

            if (mc.player == null || mc.world == null)
                return;
            if (rainbow_mode.get_value(true)) {
                cycle_rainbow();
            }

            if (remove_visual_timer.passed(1000L)) {
                remove_visual_timer.reset();
                attacked_crystals.clear();
            }

            if (check_pause()) {
                return;
            }

            if (place_crystal.get_value(true) && place_delay_counter > place_timeout) {
                place_crystal();
            }

            if (break_crystal.get_value(true) && break_delay_counter > break_timeout) {
                break_crystal();
            }

            if (!did_anything) {
                if (old_render.get_value(true)) {
                    render_block_init = null;
                }
                autoez_target = null;
                is_rotating = false;
            }

            if (autoez_target != null) {
                AutoEz.add_target(autoez_target.getName());
                detail_name = autoez_target.getName();
                detail_hp = Math.round(autoez_target.getHealth() + autoez_target.getAbsorptionAmount());
            }

            render_block_old = render_block_init;

            break_delay_counter++;
            place_delay_counter++;
        }


        public void update() {
            if (rotate_mode.in("Off") || rotate_mode.in("Old")) {
                do_ca();
            }
        }
        
        public void cycle_rainbow() {
            float[] tick_color = { (float)(System.currentTimeMillis() % 11520L),  11520.0F };


            int color_rgb_o = Color.HSBtoRGB(tick_color[0], sat.get_value(1), brightness.get_value(1));

            r.set_value(color_rgb_o >> 16 & 0xFF);
            g.set_value(color_rgb_o >> 8 & 0xFF);
            b.set_value(color_rgb_o & 0xFF);
        }



        public EntityEnderCrystal get_best_crystal() {
            double best_damage = 0.0D;


            double maximum_damage_self = max_self_damage.get_value(1);

            double best_distance = 0.0D;

            EntityEnderCrystal best_crystal = null;

            for (Entity c : mc.world.loadedEntityList) {

                if (!(c instanceof EntityEnderCrystal))
                    continue;
                EntityEnderCrystal crystal = (EntityEnderCrystal)c;
                if (mc.player.getDistance(crystal) > (!mc.player.canEntityBeSeen(crystal) ? hit_range_wall.get_value(1) : hit_range.get_value(1))) {
                    continue;
                }
                if (!mc.player.canEntityBeSeen(crystal) && raytrace.get_value(true)) {
                    continue;
                }

                if (attacked_crystals.containsKey(crystal) && attacked_crystals.get(crystal) > antiStuckTries.get_value(1) && anti_stuck.get_value(true))
                    continue;
                for (EntityPlayer player : mc.world.playerEntities) {
                    double minimum_damage;
                    if (player == mc.player)
                        continue;
                    if (FriendUtil.isFriend(player.getName()))
                        continue;
                    if (player.getDistance(mc.player) >= enemyRange.get_value(1))
                        continue;
                    if (player.isDead || player.getHealth() <= 0.0F)
                        continue;
                    boolean no_place = (faceplace_check.get_value(true) && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD);
                    if ((player.getHealth() < faceplace_mode_damage.get_value(1) && faceplace_mode.get_value(true) && !no_place) || (get_armor_fucker(player) && !no_place)) {
                        minimum_damage = 2.0D;
                    } else {
                        minimum_damage = min_player_break.get_value(1);
                    }

                    double target_damage = CrystalUtil.calculateDamage(crystal, player);

                    if (target_damage < minimum_damage)
                        continue;
                    double self_damage = CrystalUtil.calculateDamage(crystal, mc.player);

                    if (self_damage > maximum_damage_self || (anti_suicide.get_value(true) && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5D))
                        continue;
                    if (target_damage > best_damage && !break_all.get_value(true)) {
                        best_damage = target_damage;
                        best_crystal = crystal;
                    }
                }


                if (break_all.get_value(true) && mc.player.getDistanceSq(crystal) > best_distance) {
                    best_distance = mc.player.getDistanceSq(crystal);
                    best_crystal = crystal;
                }
            }


            return best_crystal;
        }


        public BlockPos get_best_block() {
            double best_damage = 0.0D;

            double maximum_damage_self = max_self_damage.get_value(1);

            BlockPos best_block = null;

            List<BlockPos> blocks = CrystalUtil.possiblePlacePositions(place_range.get_value(1), endcrystal.get_value(true), true);

            for (EntityPlayer target : mc.world.playerEntities) {

                if (FriendUtil.isFriend(target.getName()))
                    continue;
                for (BlockPos block : blocks) {
                    double minimum_damage;
                    if (target == mc.player)
                        continue;
                    if (target.getDistance(mc.player) >= enemyRange.get_value(1))
                        continue;
                    if (!BlockUtil.rayTracePlaceCheck(block, raytrace.get_value(true))) {
                        continue;
                    }

                    if (!BlockUtil.canSeeBlock(block) && mc.player.getDistance(block.getX(), block.getY(), block.getZ()) > wallPlaceRange.get_value(1)) {
                        continue;
                    }


                    if (target.isDead || target.getHealth() <= 0.0F)
                        continue;
                    boolean no_place = (faceplace_check.get_value(true) && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD);
                    if ((target.getHealth() < faceplace_mode_damage.get_value(1) && faceplace_mode.get_value(true) && !no_place) || (get_armor_fucker(target) && !no_place)) {
                        minimum_damage = 2.0D;
                    } else {
                        minimum_damage = min_player_place.get_value(1);
                    }

                    double target_damage = CrystalUtil.calculateDamage(block.getX() + 0.5D, block.getY() + 1.0D, block.getZ() + 0.5D, target);

                    if (target_damage < minimum_damage)
                        continue;
                    double self_damage = CrystalUtil.calculateDamage(block.getX() + 0.5D, block.getY() + 1.0D, block.getZ() + 0.5D, mc.player);

                    if (self_damage > maximum_damage_self || (anti_suicide.get_value(true) && (mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5D))
                        continue;
                    if (target_damage > best_damage) {
                        best_damage = target_damage;
                        best_block = block;
                        autoez_target = target;
                    }
                }
            }



            blocks.clear();

            render_damage_value = best_damage;
            render_block_init = best_block;

            return best_block;
        }




        public void place_crystal() {
            BlockPos target_block = get_best_block();

            if (target_block == null) {
                return;
            }

            place_delay_counter = 0;

            already_attacking = false;

            boolean offhand_check = false;
            if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && auto_switch.get_value(true)) {
                    if (find_crystals_hotbar() == -1)
                        return;  mc.player.inventory.currentItem = find_crystals_hotbar();
                    return;
                }
            } else {
                offhand_check = true;
            }

            did_anything = true;
            rotate_to_pos(target_block);
            BlockUtil.placeCrystalOnBlock(target_block, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            placePosList.add(target_block);
        }


        public boolean get_armor_fucker(EntityPlayer p) {
            for (ItemStack stack : p.getArmorInventoryList()) {

                if (stack == null || stack.getItem() == Items.AIR) return true;

                float armor_percent = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / (float) stack.getMaxDamage()) * 100.0f;

                if (fuck_armor_mode.get_value(true) && fuck_armor_mode_precent.get_value(1) >= armor_percent) return true;

            }

            return false;
        }


        public void break_crystal() {
            EntityEnderCrystal crystal = get_best_crystal();
            if (crystal == null) {
                return;
            }

            if (anti_weakness.get_value(true) && mc.player.isPotionActive(MobEffects.WEAKNESS)) {

                boolean should_weakness = true;

                if (mc.player.isPotionActive(MobEffects.STRENGTH))
                {
                    if (Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH)).getAmplifier() == 2) {
                        should_weakness = false;
                    }
                }


                if (should_weakness) {

                    if (!already_attacking) {
                        already_attacking = true;
                    }

                    int new_slot = -1;

                    for (int j = 0; j < 9; j++) {

                        ItemStack stack = mc.player.inventory.getStackInSlot(j);

                        if (stack.getItem() instanceof net.minecraft.item.ItemSword || stack.getItem() instanceof net.minecraft.item.ItemTool) {
                            new_slot = j;
                            mc.playerController.updateController();

                            break;
                        }
                    }

                    if (new_slot != -1) {
                        mc.player.inventory.currentItem = new_slot;
                    }
                }
            }

            did_anything = true;

            rotate_to(crystal);
            for (int i = 0; i < break_trys.get_value(1); i++) {
                EntityUtil.attackEntity(crystal, true, swing);
            }
            add_attacked_crystal(crystal);

            if (client_side.get_value(true)) {
                mc.world.removeEntityFromWorld(crystal.getEntityId());
            }

            break_delay_counter = 0;
        }


        public boolean check_pause() {
            if (find_crystals_hotbar() == -1 && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
                return true;
            }

            if (stop_while_mining.get_value(true) && mc.gameSettings.keyBindAttack.isKeyDown() && mc.player.getHeldItemMainhand().getItem() instanceof net.minecraft.item.ItemPickaxe) {
                if (old_render.get_value(true)) {
                    render_block_init = null;
                }
                return true;
            }

            if (NekoHax.get_hack_manager().get_module_with_tag("Surround").is_active()) {
                if (old_render.get_value(true)) {
                    render_block_init = null;
                }
                return true;
            }

            if (NekoHax.get_hack_manager().get_module_with_tag("HoleFill").is_active()) {
                if (old_render.get_value(true)) {
                    render_block_init = null;
                }
                return true;
            }

            if (NekoHax.get_hack_manager().get_module_with_tag("AutoTrap").is_active()) {
                if (old_render.get_value(true)) {
                    render_block_init = null;
                }
                return true;
            }

            return false;
        }

        private int find_crystals_hotbar() {
            for (int i = 0; i < 9; i++) {
                if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
                    return i;
                }
            }
            return -1;
        }

        private void add_attacked_crystal(EntityEnderCrystal crystal) {
            if (attacked_crystals.containsKey(crystal)) {
                int value = attacked_crystals.get(crystal);
                attacked_crystals.put(crystal, value + 1);
            } else {
                attacked_crystals.put(crystal, 1);
            }
        }


        public void rotate_to_pos(BlockPos pos) {
            float[] angle;
            if (rotate_mode.in("Const")) {
                angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F)));
            } else {
                angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((pos.getX() + 0.5F), (pos.getY() - 0.5F), (pos.getZ() + 0.5F)));
            }
            if (rotate_mode.in("Off")) {
                is_rotating = false;
            }
            if (rotate_mode.in("Good") || rotate_mode.in("Const")) {
                RotationUtil.setPlayerRotations(angle[0], angle[1]);
            }
            if (rotate_mode.in("Old")) {
                yaw = angle[0];
                pitch = angle[1];
                is_rotating = true;
            }
        }

        public void rotate_to(Entity entity) {
            float[] angle = MathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
            if (rotate_mode.in("Off")) {
                is_rotating = false;
            }
            if (rotate_mode.in("Good")) {
                RotationUtil.setPlayerRotations(angle[0], angle[1]);
            }
            if (rotate_mode.in("Old") || rotate_mode.in("Cont")) {
                yaw = angle[0];
                pitch = angle[1];
                is_rotating = true;
            }
        }



        public void render(EventRender event) {
            if (render_block_init == null)
                return;
            if (render_mode.in("None"))
                return;
            if (render_mode.in("Pretty")) {
                outline = true;
                solid = true;
            }

            if (render_mode.in("Solid")) {
                outline = false;
                solid = true;
            }

            if (render_mode.in("Outline")) {
                outline = true;
                solid = false;
            }

            render_block(render_block_init);

            if (future_render.get_value(true) && render_block_old != null) {
                render_block(render_block_old);
            }

            if (render_damage.get_value(true)) {
                RenderUtil.drawText(render_block_init, ((Math.floor(render_damage_value) == render_damage_value) ? (int)render_damage_value : String.format("%.1f", render_damage_value)) + "");
            }
        }


        public void render_block(BlockPos pos) {
            BlockPos render_block = top_block.get_value(true) ? pos.up() : pos;

            float h = (float)height.get_value(1.0D);

            if (solid) {
                RenderHelp.prepare("quads");
                RenderHelp.draw_cube(RenderHelp.get_buffer_build(), render_block
                        .getX(), render_block.getY(), render_block.getZ(), 1.0F, h, 1.0F, r

                        .get_value(1), g.get_value(1), b.get_value(1), a.get_value(1), "all");


                RenderHelp.release();
            }

            if (outline) {
                RenderHelp.prepare("lines");
                RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), render_block
                        .getX(), render_block.getY(), render_block.getZ(), 1.0F, h, 1.0F, r

                        .get_value(1), g.get_value(1), b.get_value(1), a_out.get_value(1), "all");


                RenderHelp.release();
            }
        }


        public void enable() {
            place_timeout = place_delay.get_value(1);
            break_timeout = break_delay.get_value(1);
            is_rotating = false;
            autoez_target = null;
            remove_visual_timer.reset();
            detail_name = null;
            detail_hp = 20;
        }


        public void disable() {
            render_block_init = null;
            autoez_target = null;
        }

        public String array_detail() {
            return (detail_name != null) ? (detail_name + " | " + detail_hp) : "None";
        }
    }
