package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.impl.events.Render3DEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.modules.combat.Aura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Kansio
 * @created 8:05 PM
 * @project Client
 */

public class HitEffects extends Module {

    private ArrayList<Hit> hits = new ArrayList<Hit>();
    private float lastHealth;
    private EntityLivingBase lastTarget = null;

    public HitEffects(ModuleData data) {
        super(data);
    }

    @Subscribe
    public void onUpdate(UpdateEvent e) {
        if (Aura.target == null) {
            this.lastHealth = 20;
            lastTarget = null;
            return;
        }

        if (this.lastTarget == null || Aura.target != this.lastTarget) {
            this.lastTarget = Aura.target;
            this.lastHealth = Aura.target.getHealth();
            return;
        }

        if (Aura.target.getHealth() != this.lastHealth) {
            if (Aura.target.getHealth() < this.lastHealth) {
                this.hits.add(new Hit(Aura.target.getPosition().add(ThreadLocalRandom.current().nextDouble(-0.5, 0.5), ThreadLocalRandom.current().nextDouble(1, 1.5), ThreadLocalRandom.current().nextDouble(-0.5, 0.5)), this.lastHealth - Aura.target.getHealth()));
            }
            this.lastHealth = Aura.target.getHealth();
        }
    }

    @Subscribe
    public void on2D(Render3DEvent event) {
        try {
            for (Hit h : hits) {
                if (h.isFinished()) {
                    hits.remove(h);
                } else {
                    h.onRender();
                }
            }
        } catch (Exception ignored) {

        }
    }
}

class Hit {

    protected static Minecraft mc = Minecraft.getMinecraft();
    private long startTime = System.currentTimeMillis();
    private BlockPos pos;
    private double healthVal;

    private long maxTime = 1000;

    public Hit(BlockPos pos, double healthVal) {
        this.startTime = System.currentTimeMillis();
        this.pos = pos;
        this.healthVal = healthVal;
    }

    public void onRender() {
        final double x = this.pos.getX() + (this.pos.getX() - this.pos.getX()) * mc.timer.renderPartialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosX;
        final double y = this.pos.getY() + (this.pos.getY() - this.pos.getY()) * mc.timer.renderPartialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosY;
        final double z = this.pos.getZ() + (this.pos.getZ() - this.pos.getZ()) * mc.timer.renderPartialTicks - Minecraft.getMinecraft().getRenderManager().viewerPosZ;

        final float var10001 = (mc.gameSettings.thirdPersonView == 2) ? -1.0f : 1.0f;
        final double size = (1.5);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        Minecraft.getMinecraft().entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 0);
        GL11.glTranslated(x, y, z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-mc.getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(mc.getRenderManager().playerViewX, var10001, 0.0f, 0.0f);
        GL11.glScaled(-0.01666666753590107 * size, -0.01666666753590107 * size, 0.01666666753590107 * size);
        float sizePercentage = 1;
        long timeLeft = (this.startTime + this.maxTime) - System.currentTimeMillis();
        float yPercentage = 0;
        if (timeLeft < 75) {
            sizePercentage = Math.min((float) timeLeft / 75F, 1F);
            yPercentage = Math.min((float) timeLeft / 75F, 1F);
        } else {
            sizePercentage = Math.min((float) (System.currentTimeMillis() - this.startTime) / 300F, 1F);
            yPercentage = Math.min((float) (System.currentTimeMillis() - this.startTime) / 600F, 1F);
        }
        GlStateManager.scale(2 * sizePercentage, 2 * sizePercentage, 2 * sizePercentage);
        Gui.drawRect(-100, -100, 100, 100, new Color(255, 0, 0, 0).getRGB());


        Color render = new Color(0, 255, 0);
        if (this.healthVal < 3 && this.healthVal > 1) {
            render = new Color(255, 255, 0);
        } else if (this.healthVal <= 1) {
            render = new Color(255, 0, 0);
        }
        mc.fontRendererObj.drawStringWithShadow("-" + new DecimalFormat("#.#").format(this.healthVal), 0, -(yPercentage * 4), render.getRGB());

        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - this.startTime >= maxTime;
    }
}