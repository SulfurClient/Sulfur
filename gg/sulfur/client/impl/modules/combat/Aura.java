package gg.sulfur.client.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.DoCriticalEvent;
import gg.sulfur.client.impl.events.PacketEvent;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.gui.click.GuiUtils;
import gg.sulfur.client.impl.modules.render.Hud;
import gg.sulfur.client.impl.sense.Start;
import gg.sulfur.client.impl.utils.combat.AimUtil;
import gg.sulfur.client.impl.utils.combat.MathUtils;
import gg.sulfur.client.impl.utils.combat.RotationUtil;
import gg.sulfur.client.impl.utils.combat.extras.Rotation;
import gg.sulfur.client.impl.utils.networking.PacketUtil;
import gg.sulfur.client.impl.utils.render.ColorCreator;
import gg.sulfur.client.impl.utils.render.ColorUtil;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import gg.sulfur.client.impl.utils.time.Stopwatch;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Aura extends Module {
    public Aura(ModuleData data) {
        super(data);

        register(
                //enum values:
                mode, rotation, targetPriority, crackType, autoblockMode, swingMode,

                //sliders:
                reach, crackSize, cps, rand, smoothness,

                //booleans
                crack, randomizeCps, doAim, silent, minecraftRotation, keepSprint, block, monsters, sleeping, invisible, teleportReach, blood, gcd, allowInInventory, sulfurUsers
        );
    }

    public EnumValue<Mode> mode = new EnumValue<>("Mode", this, Mode.values());
    public EnumValue<RotationMode> rotation = new EnumValue<>("Rotation Mode", this, RotationMode.values());
    public EnumValue<TargetPriority> targetPriority = new EnumValue<>("Target Priority", this, TargetPriority.values());
    public EnumValue<AutoblockMode> autoblockMode = new EnumValue<>("Autoblock Mode", this, AutoblockMode.values());
    public EnumValue<CrackType> crackType = new EnumValue<>("Crack Type", this, CrackType.values());
    public EnumValue<SwingMode> swingMode = new EnumValue<>("Swing Mode", this, SwingMode.values());
    public NumberValue crackSize = new NumberValue("Crack Size", this, 8, 1, 20);
    public NumberValue cps = new NumberValue("CPS", this, 12, 1, 20);
    public BooleanValue randomizeCps = new BooleanValue("Randomize CPS", this, true);
    public NumberValue rand = new NumberValue("Randomize", this, 3, 1, 10);
    public BooleanValue doAim = new BooleanValue("Rotate", this, true);
    public NumberValue reach = new NumberValue("Attack Range", this, 4.5f, 2.5f, 9f);
    public NumberValue smoothness = new NumberValue("Smoothness", this, 5, 0, 100);
    public BooleanValue rayCheck = new BooleanValue("Ray Check", this, true);
    public BooleanValue block = new BooleanValue("Auto Block", this, true);
    public BooleanValue monsters = new BooleanValue("Monsters", this, true);
    public BooleanValue sleeping = new BooleanValue("Sleeping", this, false);
    public BooleanValue invisible = new BooleanValue("Invisibles", this, true);
    public BooleanValue silent = new BooleanValue("Silent", this, true);
    public BooleanValue keepSprint = new BooleanValue("Keep Sprint", this, false);
    public BooleanValue minecraftRotation = new BooleanValue("Minecraft Rotation", this, true);
    public BooleanValue crack = new BooleanValue("Crack", this, false);
    public BooleanValue blood = new BooleanValue("Blood Particles", this, false);
    public BooleanValue teleportReach = new BooleanValue("Teleport Reach", this, false);
    public BooleanValue gcd = new BooleanValue("GCD", this, false);
    public BooleanValue allowInInventory = new BooleanValue("In Inventory", this, false);
    public BooleanValue sulfurUsers = new BooleanValue("Sulfur Users", this, false);

    public static EntityLivingBase target;

    public static boolean isBlocking, swinging;


    public final Stopwatch attackTimer = new Stopwatch();
    public Vector2f currentRotation = null;
    private float animated;
    //EntityLivingBase target;
    Object o;
    ScaledResolution lr;
    FontRenderer fontRenderer;
    String name;
    Double lastDamage;
    float modelWidth, sWidth, sHeight, middleX, middleY, top, xOffset, nameYOffset, nameHeight, scale, healthTextHeight, healthPercentage, width, height, half, left, right, bottom, textLeft, healthTextY, health, downScale, healthBarY, healthBarHeight, healthBarRight, dif, healthWidth, healthBarEnd, damage, damageWidth;
    String healthText;
    int fadeColor = 0;
    private final Map<EntityLivingBase, Double> entityDamageMap = new HashMap<EntityLivingBase, Double>();

    private Rotation lastRotation;

    @Override
    public void onEnable() {
        this.attackTimer.resetTime();
    }

    @Override
    public void onDisable() {
        this.target = null;
        currentRotation = null;
        if (mc.gameSettings.keyBindUseItem.pressed) mc.gameSettings.keyBindUseItem.pressed = false;
        isBlocking = false;
        swinging = false;
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        target = this.findTarget();
        if (target == null) {
            if (autoblockMode.getValue() == AutoblockMode.GHOSTLY && block.getValue() && !checkEnemiesNearby() && mc.gameSettings.keyBindUseItem.isPressed()) {
                mc.gameSettings.keyBindUseItem.pressed = false;
            }
            isBlocking = false;
        }
        if (target != null) {
            mc.thePlayer.setSprinting(this.keepSprint.getValue());

            if (!this.keepSprint.getValue()) {
                mc.gameSettings.keyBindSprint.pressed = false;
            }

            if (mc.thePlayer.getDistanceToEntity(target) >= reach.getValue()) {
                return;
            }

            if (!doAim.getValue()) return;

            if (!silent.getValue() && target != null && mc.objectMouseOver.entityHit != target) {
                Vector2f rotation = RotationUtil.getRotations(target);

                if (currentRotation == null)
                    currentRotation = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);

                if (smoothness.getValue() > 0f) {
                    float yaw = RotationUtil.updateYawRotation(currentRotation.x, rotation.x,
                            Math.max(1, 180 * (1 - smoothness.getValue().floatValue() / 100)));
                    float pitch = RotationUtil.updatePitchRotation(currentRotation.y, rotation.y,
                            Math.max(1, 90f * (1 - smoothness.getValue().floatValue() / 100)));

                    rotation.x = yaw;
                    rotation.y = pitch;

                    currentRotation = rotation;
                }

                if (minecraftRotation.getValue()) rotation = RotationUtil.clampRotation(rotation);

                mc.thePlayer.rotationYaw = rotation.x;
                mc.thePlayer.rotationPitch = rotation.y;
            }
        }
    }

    @Subscribe
    public void onMotion(UpdateEvent event) {
        if (target != null && event.isPre()) {

            if (mc.thePlayer.getDistanceToEntity(target) >= reach.getValue()) {
                swinging = false;
                return;
            }

            if (silent.getValue() && doAim.getValue()) {
                aimAtTarget(event, target);
            }

            swing(target);

            if (block.getValue() && autoblockMode.getValue() == AutoblockMode.FAKE) {
                isBlocking = true;
            }

            sendUseItem();
        }
    }


    public EntityLivingBase findTarget() {
        EntityLivingBase currentTarget = null;

        double currentDistance = 0;
        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity != null) {
                if (!this.isEntityValid(entity)) continue;
                if (!(entity instanceof EntityLivingBase)) continue;
                EntityLivingBase target = (EntityLivingBase) entity;
                if (currentTarget != null) {
                    switch (targetPriority.getValue()) {
                        case HEALTH: {
                            if (target.getHealth() > currentTarget.getHealth()) currentTarget = target;
                            break;
                        }
                        case DIST: {
                            if (target.getDistanceToEntity(mc.thePlayer) < currentDistance) {
                                currentTarget = target;
                                currentDistance = currentTarget.getDistanceToEntity(mc.thePlayer);
                            }
                            break;
                        }
                        case ARMOR: {
                            if (target.getTotalArmorValue() < currentTarget.getTotalArmorValue())
                                currentTarget = target;
                            break;
                        }
                        default:
                            return target;
                    }
                    ;
                } else {
                    currentTarget = target;
                    if (targetPriority.getValue() == TargetPriority.DIST) //So we don't unnecessary do distance checks
                        currentDistance = currentTarget.getDistanceToEntity(mc.thePlayer);
                }
            }
        }

        return currentTarget;
    }

    private boolean isEntityValid(Entity entity) {
        if (mc.thePlayer.isEntityEqual(entity)) return false;

        if (mc.thePlayer.getDistanceToEntity(entity) >= reach.getValue()) return false;

        if (!(entity instanceof EntityLivingBase))
            return false;

        if (!sleeping.getValue() && ((EntityLivingBase) entity).isPlayerSleeping())
            return false;

        if (entity.isInvisible() && !invisible.getValue())
            return false;

        if (entity instanceof EntityArmorStand)
            return false;

        if (Start.getInstance().getSenseManager().isSulfurUser(entity.getName()) && !sulfurUsers.getValue())
            return false;

        if (Sulfur.getInstance().getFriendManager().getObjects().contains(entity.getName().toLowerCase()))
            return false;

        if (entity.getName().equalsIgnoreCase("UPGRADES") || entity.getName().equalsIgnoreCase("SHOP"))
            return false;

        if (entity.isInvisible() && !invisible.getValue())
            return false;

        if (!monsters.getValue() && (entity instanceof EntityMob || entity instanceof EntityVillager))
            return false;

        return monsters.getValue() || entity instanceof EntityPlayer;
    }

    public boolean sendUseItem() {
        if (autoblockMode.getValue() == AutoblockMode.REAL && block.getValue()) {
            if (block.getValue() && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
                mc.playerController.syncCurrentPlayItem();
                mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                ItemStack itemstack = mc.thePlayer.getHeldItem().useItemRightClick(mc.theWorld, mc.thePlayer);
                if (itemstack != mc.thePlayer.getHeldItem() || itemstack != null) {
                    mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = itemstack;
                    if (itemstack.stackSize == 0)
                        mc.thePlayer.inventory.mainInventory[mc.thePlayer.inventory.currentItem] = null;
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public void doCritical() {
        DoCriticalEvent criticalEvent = new DoCriticalEvent();
        Sulfur.getInstance().getEventBus().post(criticalEvent);

        if (this.crack.getValue()) {
            for (int i = 0; i < this.crackSize.getValue(); i++) {
                if (this.crackType.getValue() == CrackType.NORMAL) {
                    mc.thePlayer.onCriticalHit(target);
                }

                if (this.crackType.getValue() == CrackType.ENCHANT) {
                    mc.thePlayer.onEnchantmentCritical(target);
                }
            }
        }
    }

    public void swing(Entity target) {
        swinging = true;
        double aps = cps.getValue() + MathUtils.getRandomInRange(0, rand.getValue());

        if (!allowInInventory.getValue() && mc.currentScreen != null) {
            return;
        }

        if (this.attackTimer.timeElapsed((long) (1000L / aps))) {
            this.attackTimer.resetTime();
            //old ghostly autoblock
            if (autoblockMode.getValue() == AutoblockMode.GHOSTLY && block.getValue()) {
                mc.gameSettings.keyBindUseItem.pressed = true;
                isBlocking = true;
            }

            switch (swingMode.getValue()) {
                case ATTACK: {
                    mc.thePlayer.swingItem();
                    break;
                }
                case SILENT: {
                    PacketUtil.sendPacket(new C0APacketAnimation());
                    break;
                }
            }

            final double distance = mc.thePlayer.getDistanceToEntity(target) - 0.5657;

            //spawn blood particles
            if (blood.getValue()) {
                for (int i = 0; i < this.crackSize.getValue(); i++) {
                    World targetWorld = target.getEntityWorld();
                    double x, y, z;
                    x = target.posX;
                    y = target.posY;
                    z = target.posZ;

                    targetWorld.spawnParticle(EnumParticleTypes.BLOCK_CRACK, x + MathUtils.getRandomInRange(-0.5, 0.5), y + MathUtils.getRandomInRange(-1, 1), z + MathUtils.getRandomInRange(-0.5, 0.5), 23, 23, 23, 152);
                }
            }

            //This thing does the tp reach
            //Credits: Auth
            if (teleportReach.getValue() && distance > 2.5) {
                if (mc.theWorld.getBlockState(new BlockPos(target.posX, target.posY, target.posZ)).getBlock() instanceof BlockAir) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(target.posX, target.posY, target.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                }
            } else {
                //do a normal hit if tp reach isnt on
                doCritical();
                mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
            }
        }
    }

    public void aimAtTarget(UpdateEvent event, Entity target) {

        if (!(target instanceof EntityLivingBase)) {
            return;
        }

        Rotation rot = AimUtil.getRotationsRandom((EntityLivingBase) target);

        if (lastRotation == null) {
            lastRotation = rot;
            attackTimer.resetTime();
            return;
        }

        Rotation temp = rot;

        rot = lastRotation;

        switch (rotation.getValue()) {
            case DEFAULT: {
                Vector2f rotation = RotationUtil.getRotations(target);

                if (smoothness.getValue() > 0f) {
                    if (currentRotation == null)
                        currentRotation = new Vector2f(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
                    float yaw = RotationUtil.updateYawRotation(currentRotation.x, rotation.x,
                            Math.max(1, 180 * (1 - smoothness.getValue().floatValue() / 100f)));
                    float pitch = RotationUtil.updatePitchRotation(currentRotation.y, rotation.y,
                            Math.max(1, 90f * (1 - smoothness.getValue().floatValue() / 100f)));

                    rotation.x = yaw;
                    rotation.y = pitch;
                    currentRotation = rotation;
                }

                if (minecraftRotation.getValue()) rotation = RotationUtil.clampRotation(rotation);
                event.setRotationYaw(rotation.x);
                event.setRotationPitch(Math.min(Math.max(rotation.y, -90), 90));
                temp = new Rotation(rotation.x, Math.min(Math.max(rotation.y, -90), 90));
                break;
            }
            case DOWN: {
                Vector2f rotation = RotationUtil.getRotations(target);
                temp = new Rotation(rotation.x, 90.0f);
                event.setRotationPitch(90.0F);
                event.setRotationYaw(rotation.x);
                break;
            }
            case NCP: {
                lastRotation = temp = rot = Rotation.fromFacing((EntityLivingBase) target);
                event.setRotationYaw(rot.getRotationYaw());
                break;
            }
        }

        if (Sulfur.getInstance().getModuleManager().get(gg.sulfur.client.impl.modules.render.Rotations.class).isToggled()) {
            mc.thePlayer.renderPitchHead = temp.getRotationPitch();
            mc.thePlayer.renderYawOffset = temp.getRotationYaw();
            mc.thePlayer.renderYawHead = temp.getRotationYaw();
        }
    }

    @Subscribe
    public void onRender(RenderHUDEvent event) {
        final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Small1").getRenderer();

        final ScaledResolution sr = event.getSr();
        final Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);

        if (hud == null) return;
        if (font == null) return;

        if (target == null) return;

        final float x = ((sr.getScaledWidth() / 2.0f) + hud.targetX.getValue().floatValue() - 80.0F - font.getWidth(String.valueOf(Math.round(target.getHealth()))));
        final float y = (sr.getScaledHeight() / 2.0f) + hud.targetY.getValue().floatValue();


        if (hud.targetHUD.getValue() && mc.thePlayer != null) {
            switch (hud.targetHudMode.getValue()) {
                case SIMPLE: {
                    final Color healthColor = new Color(ColorUtil.getHealthColor(target));
                    final float xSpeed = 100F / (Minecraft.func_175610_ah() * 1.05F);
                    final float desiredWidth = (150.0F / target.getMaxHealth()) * Math.min(target.getHealth(), target.getMaxHealth());

                    GuiUtils.drawRect1(x - 1, y - 1, 150.0F, 12.0F, new Color(0, 0, 0, 100).getRGB());

                    if (desiredWidth < animated || desiredWidth > animated) {
                        if (Math.abs(desiredWidth - animated) <= xSpeed) {
                            animated = desiredWidth;
                        } else {
                            animated += (animated < desiredWidth ? xSpeed * 3 : -xSpeed);
                        }
                    }

                    GuiUtils.drawRect1(x - 1, y - 1, animated, 12.0F, healthColor.getRGB());

                    font.drawStringWithShadow(target.getName(), x, y + 2, -1);
                    font.drawStringWithShadow(String.valueOf(Math.round(target.getHealth())), x + 149.0F - font.getWidth(String.valueOf(Math.round(target.getHealth()))), y + 2, -1);
                    break;
                }
                case OLD: {
                    final Color healthColor = new Color(ColorUtil.getHealthColor(target));
                    final String playerName = StringUtils.stripControlCodes(target.getName());
                    final int distance = (int) ((mc.thePlayer.getDistanceToEntity(target)));
                    final float xSpeed = 133F / (Minecraft.func_175610_ah() * 1.05F);
                    final float desiredWidth = (140F / target.getMaxHealth()) * Math.min(target.getHealth(), target.getMaxHealth());
                    final float shit = (80f / target.getMaxHealth()) * Math.min(target.getHealth(), target.getMaxHealth());

                    GuiUtils.drawRect1(x, y, 157F, 42F, new Color(45, 45, 45, 255).getRGB());

                    mc.fontRendererObj.drawStringWithShadow(playerName, x + 34.5F, y + 4F, -1);
                    mc.fontRendererObj.drawStringWithShadow("§7» §c" + MathUtils.round((double) target.getHealth(), 2) + "❤ §7(Hurt: " + target.hurtTime + ")", x + 34.5F, y + 16F, -1);


                    GuiInventory.drawEntityOnScreen((int) x + 12, (int) y + 38, 18, target.rotationYaw, -target.rotationPitch, target);

                    int off = 0;
                    for (int i = 1; i < shit; i++) {
                        GuiUtils.drawRect1(x + i - 1 , y + 40F, i, 2F, ColorCreator.createRainbowFromOffset2(-4000, off));
                        off = off + 5;
                    }
                    break;
                }
                case NEW: {
                    //  target = target;
                    lr = event.getSr();
                    fontRenderer = mc.fontRendererObj;
                    sWidth = (float) lr.getScaledWidth();
                    sHeight = (float) lr.getScaledHeight();
                    middleX = sWidth / 2.0f;
                    middleY = sHeight / 2.0f;
                    top = middleY + 20.0f;
                    xOffset = 0.0f;
                    if (target instanceof EntityPlayer) {
                        name = ((EntityPlayer) target).getGameProfile().getName();
                    } else {
                        name = target.getDisplayName().getUnformattedText();
                    }
                    modelWidth = 30.0f;
                    nameYOffset = 4.0f;
                    nameHeight = fontRenderer.FONT_HEIGHT;
                    width = Math.max(120.0f, modelWidth + 4.0f + fontRenderer.getStringWidth(name) + 2.0f);
                    height = 50.0f;
                    half = width / 2.0f;
                    left = middleX - half + xOffset;
                    right = middleX + half + xOffset;
                    bottom = top + height;
                    Gui.drawRect(left, top, right, bottom, Integer.MIN_VALUE);
                    GL11.glDisable(3553);
                    GL11.glLineWidth(0.5f);
                    GL11.glColor3f(0.0f, 0.0f, 0.0f);
                    GL11.glBegin(2);
                    GL11.glVertex2f(left, top);
                    GL11.glVertex2f(left, bottom);
                    GL11.glVertex2f(right, bottom);
                    GL11.glVertex2f(right, top);
                    GL11.glEnd();
                    GL11.glEnable(3553);
                    textLeft = left + modelWidth;
                    fontRenderer.drawStringWithShadow(name, textLeft, top + nameYOffset, -1);
                    healthTextY = top + nameHeight + nameYOffset;
                    health = target.getHealth();
                    healthText = String.format("%.1f", health);
                    scale = 2.0f;
                    healthTextHeight = fontRenderer.FONT_HEIGHT * scale;
                    healthPercentage = health / target.getMaxHealth();
                    fadeColor = ColorUtil.getModeColor();
                    /*/if (this.pulsingProperty.isAvailable() && this.pulsingProperty.getValue()) {
                        fadeColor = RenderingUtils.fadeBetween(fadeColor, RenderingUtils.darker(fadeColor, 0.49f), System.currentTimeMillis() % 3000L / 1500.0f);
                    }/*/
                    downScale = 1.0f / scale;
                    GL11.glScalef(scale, scale, 1.0f);
                    fontRenderer.drawStringWithShadow(healthText, textLeft / scale, healthTextY / scale + 2.0f, fadeColor);
                    GL11.glScalef(downScale, downScale, 1.0f);
                    healthBarY = healthTextY + healthTextHeight + 2.0f;
                    healthBarHeight = 8.0f;
                    healthBarRight = right - 4.0f;
                    dif = healthBarRight - textLeft;
                    Gui.drawRect(textLeft, healthBarY, healthBarRight, healthBarY + healthBarHeight, 1342177280);
                    target.healthProgressX = (float) RenderUtils.progressiveAnimation(target.healthProgressX, healthPercentage, 1.0);
                    lastDamage = this.entityDamageMap.get(target);
                    healthWidth = dif * target.healthProgressX;
                    healthBarEnd = textLeft + healthWidth;
                    if (lastDamage != null && lastDamage > 0.0) {
                        damage = lastDamage.floatValue();
                        damageWidth = dif * (damage / target.getMaxHealth());
                        Gui.drawRect(healthBarEnd, healthBarY, Math.min(healthBarEnd + damageWidth, healthBarRight), healthBarY + healthBarHeight, new Color(0, 0, 0, 45).getRGB());
                    }
                    Gui.drawRect(textLeft, healthBarY, healthBarEnd, healthBarY + healthBarHeight, fadeColor);
                    GL11.glColor3f(1.0f, 1.0f, 1.0f);
                    GuiInventory.drawEntityOnScreen((int) (left + modelWidth / 2.0f), (int) bottom - 2, 23, 0.0f, 0.0f, target);
                    break;
                }
                case TEST: {
                    String name = target.getName();

                    float startX = 20;
                    float renderX = (sr.getScaledWidth() / 2) + startX;
                    float renderY = (sr.getScaledHeight() / 2) + 10;
                    int maxX2 = 30;
                    if (target.getCurrentArmor(3) != null) {
                        maxX2 += 15;
                    }
                    if (target.getCurrentArmor(2) != null) {
                        maxX2 += 15;
                    }
                    if (target.getCurrentArmor(1) != null) {
                        maxX2 += 15;
                    }
                    if (target.getCurrentArmor(0) != null) {
                        maxX2 += 15;
                    }
                    if (target.getHeldItem() != null) {
                        maxX2 += 15;
                    }
                    float maxX = Math.max(maxX2, mc.fontRendererObj.getStringWidth(name) + 30);
                    Gui.drawRect(renderX, renderY, renderX + maxX, renderY + 40, new Color(0, 0, 0, 0.6f).getRGB());
                    Gui.drawRect(renderX, renderY + 38, renderX + (maxX * healthPercentage), renderY + 40, hud.color.getValue().getRGB());
                    mc.fontRendererObj.drawStringWithShadow(name, renderX + 25, renderY + 7, -1);
                    int xAdd = 0;
                    double multiplier = 0.85;
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(multiplier, multiplier, multiplier);
                    if (target.getCurrentArmor(3) != null) {
                        mc.getRenderItem().func_175042_a(target.getCurrentArmor(3), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (target.getCurrentArmor(2) != null) {
                        mc.getRenderItem().func_175042_a(target.getCurrentArmor(2), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (target.getCurrentArmor(1) != null) {
                        mc.getRenderItem().func_175042_a(target.getCurrentArmor(1), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (target.getCurrentArmor(0) != null) {
                        mc.getRenderItem().func_175042_a(target.getCurrentArmor(0), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                        xAdd += 15;
                    }
                    if (target.getHeldItem() != null) {
                        mc.getRenderItem().func_175042_a(target.getHeldItem(), (int) ((((sr.getScaledWidth() / 2) + startX + 23) + xAdd) / multiplier), (int) (((sr.getScaledHeight() / 2) + 28) / multiplier));
                    }
                    GlStateManager.popMatrix();
                    GuiInventory.drawEntityOnScreen((int)renderX + 12, (int)renderY + 33, 15, target.rotationYaw, target.rotationPitch, target);
                    break;
                }
            }
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (!keepSprint.getValue()) {
            if (event.getPacket() instanceof C0BPacketEntityAction) {
                event.setCancelled(true);
            }
        }

        if (gcd.getValue() && target != null && event.getPacket() instanceof C03PacketPlayer && ((C03PacketPlayer) event.getPacket()).getRotating()) {
            C03PacketPlayer p = event.getPacket();
            float m = (float) (0.005 * mc.gameSettings.mouseSensitivity / 0.005);
            double f = m * 0.6 + 0.2;
            double gcd = m * m * m * 1.2;
            p.pitch -= p.pitch % gcd;
            p.yaw -= p.yaw % gcd;
        }
    }

    private boolean checkEnemiesNearby() {
        for (Entity ent : mc.theWorld.playerEntities) {
            if (ent.getDistanceSqToEntity(mc.thePlayer) <= 10) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getSuffix() {
        return " Singular";
    }

    public enum AutoblockMode implements INameable {
        REAL("Real"), FAKE("Fake"), VERUS("Verus"), GHOSTLY("Ghostly");
        private final String name;

        AutoblockMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum CrackType implements INameable {
        NORMAL("Normal"), ENCHANT("Enchant");
        private final String name;

        CrackType(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum SwingMode implements INameable {
        ATTACK("Attack"), NOSWING("None"), SILENT("Silent");
        private final String name;

        SwingMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum TargetPriority implements INameable {
        HEALTH("Health"), DIST("Distance"), ARMOR("Armor"), NONE("None");
        private final String name;

        TargetPriority(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum Rotations implements INameable {
        NORMAL("Normal"), DOWN("Down");
        private final String name;

        Rotations(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum Mode implements INameable {
        SINGLE("Single");
        private final String name;

        Mode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum RotationMode implements INameable {
        DEFAULT("Default"),
        DOWN("Down"),
        NCP("NCP"),
        AAC("AAC"),
        GWEN("GWEN");
        private final String name;

        RotationMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public static boolean isSwinging() {
        return swinging;
    }
}