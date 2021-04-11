package me.travis.wurstplus.wurstplustwo.hacks.combat;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import me.travis.turok.draw.RenderHelp;
import me.travis.wurstplus.Wurstplus;
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
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WurstplusAutoCrystal extends WurstplusHack {
   WurstplusSetting debug = this.create("Debug", "CaDebug", false);
   WurstplusSetting place_crystal = this.create("Place", "CaPlace", true);
   WurstplusSetting break_crystal = this.create("Break", "CaBreak", true);
   WurstplusSetting break_trys = this.create("Break Attempts", "CaBreakAttempts", 2, 1, 6);
   WurstplusSetting anti_weakness = this.create("Anti-Weakness", "CaAntiWeakness", true);
   WurstplusSetting enemyRange = this.create("Enemy Range", "CaEnemyRange", 9.0D, 5.0D, 15.0D);
   WurstplusSetting hit_range = this.create("Hit Range", "CaHitRange", 5.199999809265137D, 1.0D, 6.0D);
   WurstplusSetting place_range = this.create("Place Range", "CaPlaceRange", 5.199999809265137D, 1.0D, 6.0D);
   WurstplusSetting hit_range_wall = this.create("Range Wall", "CaRangeWall", 4.0D, 1.0D, 6.0D);
   WurstplusSetting wallPlaceRange = this.create("Place Wall Range", "CaPlaceWallRange", 4.0D, 0.0D, 6.0D);
   WurstplusSetting place_delay = this.create("Place Delay", "CaPlaceDelay", 0, 0, 10);
   WurstplusSetting break_delay = this.create("Break Delay", "CaBreakDelay", 2, 0, 10);
   WurstplusSetting min_player_place = this.create("Min Enemy Place", "CaMinEnemyPlace", 8, 0, 20);
   WurstplusSetting min_player_break = this.create("Min Enemy Break", "CaMinEnemyBreak", 6, 0, 20);
   WurstplusSetting max_self_damage = this.create("Max Self Damage", "CaMaxSelfDamage", 6, 0, 20);
   WurstplusSetting rotate_mode = this.create("Rotate", "CaRotateMode", "Good", this.combobox(new String[]{"Off", "Old", "Const", "Good"}));
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
   WurstplusSetting swing = this.create("Swing", "CaSwing", "Mainhand", this.combobox(new String[]{"Mainhand", "Offhand", "Both", "None"}));
   WurstplusSetting render_mode = this.create("Render", "CaRenderMode", "Pretty", this.combobox(new String[]{"Pretty", "Solid", "Outline", "None"}));
   WurstplusSetting old_render = this.create("Old Render", "CaOldRender", false);
   WurstplusSetting future_render = this.create("Future Render", "CaFutureRender", false);
   WurstplusSetting top_block = this.create("Top Block", "CaTopBlock", false);
   WurstplusSetting r = this.create("R", "CaR", 255, 0, 255);
   WurstplusSetting g = this.create("G", "CaG", 255, 0, 255);
   WurstplusSetting b = this.create("B", "CaB", 255, 0, 255);
   WurstplusSetting a = this.create("A", "CaA", 100, 0, 255);
   WurstplusSetting a_out = this.create("Outline A", "CaOutlineA", 255, 0, 255);
   WurstplusSetting rainbow_mode = this.create("Rainbow", "CaRainbow", false);
   WurstplusSetting sat = this.create("Satiation", "CaSatiation", 0.8D, 0.0D, 1.0D);
   WurstplusSetting brightness = this.create("Brightness", "CaBrightness", 0.8D, 0.0D, 1.0D);
   WurstplusSetting height = this.create("Height", "CaHeight", 1.0D, 0.0D, 1.0D);
   WurstplusSetting render_damage = this.create("Render Damage", "RenderDamage", true);
   private final ConcurrentHashMap attacked_crystals = new ConcurrentHashMap();
   private final List placePosList = new CopyOnWriteArrayList();
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
   private Listener on_entity_removed = new Listener((event) -> {
      if (event.get_entity() instanceof EntityEnderCrystal) {
         this.attacked_crystals.remove(event.get_entity());
      }

   }, new Predicate[0]);
   @EventHandler
   private Listener send_listener = new Listener((event) -> {
      if (event.get_packet() instanceof CPacketPlayer && this.is_rotating && this.rotate_mode.in("Old")) {
         if (this.debug.get_value(true)) {
            WurstplusMessageUtil.send_client_message("Rotating");
         }

         CPacketPlayer px = (CPacketPlayer)event.get_packet();
         px.yaw = this.yaw;
         px.pitch = this.pitch;
         this.is_rotating = false;
      }

      if (event.get_packet() instanceof CPacketPlayerTryUseItemOnBlock && this.is_rotating && this.rotate_mode.in("Old")) {
         if (this.debug.get_value(true)) {
            WurstplusMessageUtil.send_client_message("Rotating");
         }

         CPacketPlayerTryUseItemOnBlock p = (CPacketPlayerTryUseItemOnBlock)event.get_packet();
         p.facingX = (float)this.render_block_init.getX();
         p.facingY = (float)this.render_block_init.getY();
         p.facingZ = (float)this.render_block_init.getZ();
         this.is_rotating = false;
      }

   }, new Predicate[0]);
   @EventHandler
   private Listener on_movement = new Listener((event) -> {
      if (event.stage == 0 && (this.rotate_mode.in("Good") || this.rotate_mode.in("Const"))) {
         if (this.debug.get_value(true)) {
            WurstplusMessageUtil.send_client_message("updating rotation");
         }

         WurstplusPosManager.updatePosition();
         WurstplusRotationUtil.updateRotations();
         this.do_ca();
      }

      if (event.stage == 1 && (this.rotate_mode.in("Good") || this.rotate_mode.in("Const"))) {
         if (this.debug.get_value(true)) {
            WurstplusMessageUtil.send_client_message("resetting rotation");
         }

         WurstplusPosManager.restorePosition();
         WurstplusRotationUtil.restoreRotations();
      }

   }, new Predicate[0]);
   @EventHandler
   private final Listener receive_listener = new Listener((event) -> {
      if (event.get_packet() instanceof SPacketSoundEffect) {
         SPacketSoundEffect packet = (SPacketSoundEffect)event.get_packet();
         if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
            Iterator var3 = mc.world.loadedEntityList.iterator();

            while(var3.hasNext()) {
               Entity e = (Entity)var3.next();
               if (e instanceof EntityEnderCrystal && e.getDistance(packet.getX(), packet.getY(), packet.getZ()) <= 6.0D) {
                  mc.world.removeEntityFromWorld(e.getEntityId());
               }
            }
         }
      }

      if (event.get_packet() instanceof SPacketSpawnObject) {
         SPacketSpawnObject spawnPacket = (SPacketSpawnObject)event.get_packet();
         if (spawnPacket.getType() == 51 && this.placePosList.contains((new BlockPos(spawnPacket.getX(), spawnPacket.getY(), spawnPacket.getZ())).down())) {
            CPacketUseEntity useEntity = new CPacketUseEntity();
            useEntity.action = Action.ATTACK;
            useEntity.entityId = spawnPacket.getEntityID();
            mc.getConnection().sendPacket(useEntity);
         }
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
      if (mc.player != null && mc.world != null) {
         if (this.rainbow_mode.get_value(true)) {
            this.cycle_rainbow();
         }

         if (this.remove_visual_timer.passed(1000L)) {
            this.remove_visual_timer.reset();
            this.attacked_crystals.clear();
         }

         if (!this.check_pause()) {
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
      }
   }

   public void update() {
      if (this.rotate_mode.in("Off") || this.rotate_mode.in("Old")) {
         this.do_ca();
      }

   }

   public void cycle_rainbow() {
      float[] tick_color = new float[]{(float)(System.currentTimeMillis() % 11520L) / 11520.0F};
      int color_rgb_o = Color.HSBtoRGB(tick_color[0], (float)this.sat.get_value(1), (float)this.brightness.get_value(1));
      this.r.set_value(color_rgb_o >> 16 & 255);
      this.g.set_value(color_rgb_o >> 8 & 255);
      this.b.set_value(color_rgb_o & 255);
   }

   public EntityEnderCrystal get_best_crystal() {
      double best_damage = 0.0D;
      double maximum_damage_self = (double)this.max_self_damage.get_value(1);
      double best_distance = 0.0D;
      EntityEnderCrystal best_crystal = null;
      Iterator var10 = mc.world.loadedEntityList.iterator();

      label144:
      while(true) {
         EntityEnderCrystal crystal;
         do {
            do {
               do {
                  Entity c;
                  do {
                     if (!var10.hasNext()) {
                        return best_crystal;
                     }

                     c = (Entity)var10.next();
                  } while(!(c instanceof EntityEnderCrystal));

                  crystal = (EntityEnderCrystal)c;
               } while(mc.player.getDistance(crystal) > (float)(!mc.player.canEntityBeSeen(crystal) ? this.hit_range_wall.get_value(1) : this.hit_range.get_value(1)));
            } while(!mc.player.canEntityBeSeen(crystal) && this.raytrace.get_value(true));
         } while(this.attacked_crystals.containsKey(crystal) && (Integer)this.attacked_crystals.get(crystal) > this.antiStuckTries.get_value(1) && this.anti_stuck.get_value(true));

         Iterator var13 = mc.world.playerEntities.iterator();

         while(true) {
            double target_damage;
            double self_damage;
            do {
               do {
                  double minimum_damage;
                  do {
                     EntityPlayer player;
                     do {
                        do {
                           do {
                              do {
                                 do {
                                    if (!var13.hasNext()) {
                                       if (this.jumpy_mode.get_value(true) && mc.player.getDistanceSq(crystal) > best_distance) {
                                          best_distance = mc.player.getDistanceSq(crystal);
                                          best_crystal = crystal;
                                       }
                                       continue label144;
                                    }

                                    player = (EntityPlayer)var13.next();
                                 } while(player == mc.player);
                              } while(WurstplusFriendUtil.isFriend(player.getName()));
                           } while(player.getDistance(mc.player) >= (float)this.enemyRange.get_value(1));
                        } while(player.isDead);
                     } while(player.getHealth() <= 0.0F);

                     boolean no_place = this.faceplace_check.get_value(true) && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                     if ((!(player.getHealth() < (float)this.faceplace_mode_damage.get_value(1)) || !this.faceplace_mode.get_value(true) || no_place) && (!this.get_armor_fucker(player) || no_place)) {
                        minimum_damage = (double)this.min_player_break.get_value(1);
                     } else {
                        minimum_damage = 2.0D;
                     }

                     target_damage = (double)WurstplusCrystalUtil.calculateDamage(crystal, player);
                  } while(target_damage < minimum_damage);

                  self_damage = (double)WurstplusCrystalUtil.calculateDamage(crystal, mc.player);
               } while(self_damage > maximum_damage_self);
            } while(this.anti_suicide.get_value(true) && (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5D);

            if (target_damage > best_damage && !this.jumpy_mode.get_value(true)) {
               best_damage = target_damage;
               best_crystal = crystal;
            }
         }
      }
   }

   public BlockPos get_best_block() {
      double best_damage = 0.0D;
      double maximum_damage_self = (double)this.max_self_damage.get_value(1);
      BlockPos best_block = null;
      List blocks = WurstplusCrystalUtil.possiblePlacePositions((float)this.place_range.get_value(1), this.endcrystal.get_value(true));
      Iterator var9 = mc.world.playerEntities.iterator();

      label113:
      while(true) {
         EntityPlayer target;
         do {
            if (!var9.hasNext()) {
               blocks.clear();
               this.render_damage_value = best_damage;
               this.render_block_init = best_block;
               return best_block;
            }

            target = (EntityPlayer)var9.next();
         } while(WurstplusFriendUtil.isFriend(target.getName()));

         Iterator var11 = blocks.iterator();

         while(true) {
            BlockPos block;
            double target_damage;
            double self_damage;
            do {
               do {
                  double minimum_damage;
                  do {
                     do {
                        do {
                           do {
                              do {
                                 do {
                                    do {
                                       if (!var11.hasNext()) {
                                          continue label113;
                                       }

                                       block = (BlockPos)var11.next();
                                    } while(target == mc.player);
                                 } while(target.getDistance(mc.player) >= (float)this.enemyRange.get_value(1));
                              } while(!WurstplusBlockUtil.rayTracePlaceCheck(block, this.raytrace.get_value(true)));
                           } while(!WurstplusBlockUtil.canSeeBlock(block) && mc.player.getDistance((double)block.getX(), (double)block.getY(), (double)block.getZ()) > (double)this.wallPlaceRange.get_value(1));
                        } while(target.isDead);
                     } while(target.getHealth() <= 0.0F);

                     boolean no_place = this.faceplace_check.get_value(true) && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
                     if ((!(target.getHealth() < (float)this.faceplace_mode_damage.get_value(1)) || !this.faceplace_mode.get_value(true) || no_place) && (!this.get_armor_fucker(target) || no_place)) {
                        minimum_damage = (double)this.min_player_place.get_value(1);
                     } else {
                        minimum_damage = 2.0D;
                     }

                     target_damage = (double)WurstplusCrystalUtil.calculateDamage((double)block.getX() + 0.5D, (double)block.getY() + 1.0D, (double)block.getZ() + 0.5D, target);
                  } while(target_damage < minimum_damage);

                  self_damage = (double)WurstplusCrystalUtil.calculateDamage((double)block.getX() + 0.5D, (double)block.getY() + 1.0D, (double)block.getZ() + 0.5D, mc.player);
               } while(self_damage > maximum_damage_self);
            } while(this.anti_suicide.get_value(true) && (double)(mc.player.getHealth() + mc.player.getAbsorptionAmount()) - self_damage <= 0.5D);

            if (target_damage > best_damage) {
               best_damage = target_damage;
               best_block = block;
               this.autoez_target = target;
            }
         }
      }
   }

   public void place_crystal() {
      BlockPos target_block = this.get_best_block();
      if (target_block != null) {
         this.place_delay_counter = 0;
         this.already_attacking = false;
         boolean offhand_check = false;
         if (mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
            if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && this.auto_switch.get_value(true)) {
               if (this.find_crystals_hotbar() == -1) {
                  return;
               }

               mc.player.inventory.currentItem = this.find_crystals_hotbar();
               return;
            }
         } else {
            offhand_check = true;
         }

         this.did_anything = true;
         this.rotate_to_pos(target_block);
         WurstplusBlockUtil.placeCrystalOnBlock(target_block, offhand_check ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
         this.placePosList.add(target_block);
      }
   }

   public boolean get_armor_fucker(EntityPlayer p) {
      Iterator var2 = p.getArmorInventoryList().iterator();

      float armor_percent;
      do {
         if (!var2.hasNext()) {
            return false;
         }

         ItemStack stack = (ItemStack)var2.next();
         if (stack == null || stack.getItem() == Items.AIR) {
            return true;
         }

         armor_percent = (float)(stack.getMaxDamage() - stack.getItemDamage()) / (float)stack.getMaxDamage() * 100.0F;
      } while(!this.fuck_armor_mode.get_value(true) || !((float)this.fuck_armor_mode_precent.get_value(1) >= armor_percent));

      return true;
   }

   public void break_crystal() {
      EntityEnderCrystal crystal = this.get_best_crystal();
      if (crystal != null) {
         if (this.anti_weakness.get_value(true) && mc.player.isPotionActive(MobEffects.WEAKNESS)) {
            boolean should_weakness = true;
            if (mc.player.isPotionActive(MobEffects.STRENGTH) && ((PotionEffect)Objects.requireNonNull(mc.player.getActivePotionEffect(MobEffects.STRENGTH))).getAmplifier() == 2) {
               should_weakness = false;
            }

            if (should_weakness) {
               if (!this.already_attacking) {
                  this.already_attacking = true;
               }

               int new_slot = -1;

               for(int i = 0; i < 9; ++i) {
                  ItemStack stack = mc.player.inventory.getStackInSlot(i);
                  if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemTool) {
                     new_slot = i;
                     mc.playerController.updateController();
                     break;
                  }
               }

               if (new_slot != -1) {
                  mc.player.inventory.currentItem = new_slot;
               }
            }
         }

         this.did_anything = true;
         this.rotate_to(crystal);

         for(int i = 0; i < this.break_trys.get_value(1); ++i) {
            WurstplusEntityUtil.attackEntity(crystal, true, this.swing);
         }

         this.add_attacked_crystal(crystal);
         if (this.client_side.get_value(true)) {
            mc.world.removeEntityFromWorld(crystal.getEntityId());
         }

         this.break_delay_counter = 0;
      }
   }

   public boolean check_pause() {
      if (this.find_crystals_hotbar() == -1 && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL) {
         return true;
      } else if (this.stop_while_mining.get_value(true) && mc.gameSettings.keyBindAttack.isKeyDown() && mc.player.getHeldItemMainhand().getItem() instanceof ItemPickaxe) {
         if (this.old_render.get_value(true)) {
            this.render_block_init = null;
         }

         return true;
      } else if (Wurstplus.get_hack_manager().get_module_with_tag("Surround").is_active()) {
         if (this.old_render.get_value(true)) {
            this.render_block_init = null;
         }

         return true;
      } else if (Wurstplus.get_hack_manager().get_module_with_tag("HoleFill").is_active()) {
         if (this.old_render.get_value(true)) {
            this.render_block_init = null;
         }

         return true;
      } else if (Wurstplus.get_hack_manager().get_module_with_tag("Trap").is_active()) {
         if (this.old_render.get_value(true)) {
            this.render_block_init = null;
         }

         return true;
      } else {
         return false;
      }
   }

   private int find_crystals_hotbar() {
      for(int i = 0; i < 9; ++i) {
         if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_CRYSTAL) {
            return i;
         }
      }

      return -1;
   }

   private void add_attacked_crystal(EntityEnderCrystal crystal) {
      if (this.attacked_crystals.containsKey(crystal)) {
         int value = (Integer)this.attacked_crystals.get(crystal);
         this.attacked_crystals.put(crystal, value + 1);
      } else {
         this.attacked_crystals.put(crystal, 1);
      }

   }

   public void rotate_to_pos(BlockPos pos) {
      float[] angle;
      if (this.rotate_mode.in("Const")) {
         angle = WurstplusMathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F)));
      } else {
         angle = WurstplusMathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), new Vec3d((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() - 0.5F), (double)((float)pos.getZ() + 0.5F)));
      }

      if (this.rotate_mode.in("Off")) {
         this.is_rotating = false;
      }

      if (this.rotate_mode.in("Good") || this.rotate_mode.in("Const")) {
         WurstplusRotationUtil.setPlayerRotations(angle[0], angle[1]);
      }

      if (this.rotate_mode.in("Old")) {
         this.yaw = angle[0];
         this.pitch = angle[1];
         this.is_rotating = true;
      }

   }

   public void rotate_to(Entity entity) {
      float[] angle = WurstplusMathUtil.calcAngle(mc.player.getPositionEyes(mc.getRenderPartialTicks()), entity.getPositionVector());
      if (this.rotate_mode.in("Off")) {
         this.is_rotating = false;
      }

      if (this.rotate_mode.in("Good")) {
         WurstplusRotationUtil.setPlayerRotations(angle[0], angle[1]);
      }

      if (this.rotate_mode.in("Old") || this.rotate_mode.in("Cont")) {
         this.yaw = angle[0];
         this.pitch = angle[1];
         this.is_rotating = true;
      }

   }

   public void render(WurstplusEventRender event) {
      if (this.render_block_init != null) {
         if (!this.render_mode.in("None")) {
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
               WurstplusRenderUtil.drawText(this.render_block_init, (Math.floor(this.render_damage_value) == this.render_damage_value ? (int)this.render_damage_value : String.format("%.1f", this.render_damage_value)) + "");
            }

         }
      }
   }

   public void render_block(BlockPos pos) {
      BlockPos render_block = this.top_block.get_value(true) ? pos.up() : pos;
      float h = (float)this.height.get_value(1.0D);
      if (this.solid) {
         RenderHelp.prepare("quads");
         RenderHelp.draw_cube(RenderHelp.get_buffer_build(), (float)render_block.getX(), (float)render_block.getY(), (float)render_block.getZ(), 1.0F, h, 1.0F, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a.get_value(1), "all");
         RenderHelp.release();
      }

      if (this.outline) {
         RenderHelp.prepare("lines");
         RenderHelp.draw_cube_line(RenderHelp.get_buffer_build(), (float)render_block.getX(), (float)render_block.getY(), (float)render_block.getZ(), 1.0F, h, 1.0F, this.r.get_value(1), this.g.get_value(1), this.b.get_value(1), this.a_out.get_value(1), "all");
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

   public String array_detail() {
      return this.detail_name != null ? this.detail_name + " | " + this.detail_hp : "None";
   }
}
