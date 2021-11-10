package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.EventEntityRender;
import gg.sulfur.client.impl.utils.render.VisualHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class MemeESP extends Module {

    public static Color colorr;
    private float hue;
    private Hud hud;
    private final ResourceLocation roblox;
    private final ResourceLocation zerotwo;
    private final ResourceLocation headed;
    private final ResourceLocation purple;
    private final ResourceLocation hentaitwo;
    private final ResourceLocation hentaithree;
    private final ResourceLocation sagetwo;
    private final ResourceLocation killjoy;
    private final ResourceLocation clayray;
    private final ResourceLocation headed2;
    private final ResourceLocation headed3;
    private final ResourceLocation headed4;
    private final ResourceLocation headed5;
    private final ResourceLocation headed6;
    private final ResourceLocation michael;
    private final ResourceLocation mark;
    private final ResourceLocation jeffery;

    private final EnumValue<MemeModes> memeModesEnumValue = new EnumValue<>("Mode", this, MemeModes.values());

    public MemeESP(ModuleData moduleData) {
        super(moduleData);
        this.roblox = new ResourceLocation("meme/roblox.png");
        this.zerotwo = new ResourceLocation("meme/zerotwo.png");
        this.headed = new ResourceLocation("meme/headed.png");
        this.purple = new ResourceLocation("meme/hentai1.jpg");
        this.hentaitwo = new ResourceLocation("meme/hentai2.jpg");
        this.hentaithree = new ResourceLocation("meme/hentai3.jpg");
        this.sagetwo = new ResourceLocation("meme/sage.jpg");
        this.killjoy = new ResourceLocation("meme/killjoy.png");
        this.headed2 = new ResourceLocation("meme/headed2.png");
        this.headed3 = new ResourceLocation("meme/headed3.png");
        this.headed4 = new ResourceLocation("meme/headed4.png");
        this.headed5 = new ResourceLocation("meme/headed5.png");
        this.headed6 = new ResourceLocation("meme/headed6.png");
        this.clayray = new ResourceLocation("meme/clayray.jpg");
        this.michael = new ResourceLocation("meme/michael2.png");
        this.mark = new ResourceLocation("meme/mark2.png");
        this.jeffery = new ResourceLocation("meme/jeff.png");

        this.register(this.memeModesEnumValue);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public String getSuffix() {
        String mode = this.memeModesEnumValue.getValue().getDisplayName();
        return " \2477" + mode;
    }

    @Subscribe
    public void onEntityRender(EventEntityRender event) {

        for (final EntityPlayer p : mc.thePlayer.getEntityWorld().playerEntities) {
            if (VisualHelper.isInFrustumView(p) && !p.isInvisible() && p.isEntityAlive()) {
                if (p == mc.thePlayer) {
                    continue;
                }

                switch (memeModesEnumValue.getValue()) {
                    case ROBLOX: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.roblox);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, 1.0f, 1.0f, (float) (252.0 * (scale / 2.0)), (float) (476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case ZEROTWO: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.zerotwo);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HEADED: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.headed);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float) 1.0, (float) 1.0, (float) (252.0 * (scale / 2.0)), (float) (476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case PURPLEHENTAI: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.purple);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HENTAITWO: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.hentaitwo);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HENTAITHREE: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.hentaithree);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case SAGETWO: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.sagetwo);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case KILLJOY: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.killjoy);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case CLAYRAY: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.clayray);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HEADEDTWO: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.headed2);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HEADEDTHREE: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.headed3);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HEADEDFOUR: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.headed4);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HEADEDFIVE: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.headed5);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case HEADEDSIX: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.headed6);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case MICHAEL: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.michael);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case MARK: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.mark);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                    case JEFFERY: {
                        final double x = VisualHelper.interp(p.posX, p.lastTickPosX) - Minecraft.getMinecraft().getRenderManager().renderPosX;
                        final double y = VisualHelper.interp(p.posY, p.lastTickPosY) - Minecraft.getMinecraft().getRenderManager().renderPosY;
                        final double z = VisualHelper.interp(p.posZ, p.lastTickPosZ) - Minecraft.getMinecraft().getRenderManager().renderPosZ;
                        GlStateManager.pushMatrix();
                        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                        GL11.glDisable(2929);
                        final float distance = MathHelper.clamp_float(mc.thePlayer.getDistanceToEntity(p), 20.0f, Float.MAX_VALUE);
                        final double scale = 0.005 * distance;
                        GlStateManager.translate(x, y, z);
                        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.scale(-0.1, -0.1, 0.0);
                        Minecraft.getMinecraft().getTextureManager().bindTexture(this.jeffery);
                        Gui.drawScaledCustomSizeModalRect(p.width / 2.0f - distance / 3.0f, -p.height - distance, 0.0f, 0.0f, (float)1.0, (float)1.0, (float)(252.0 * (scale / 2.0)), (float)(476.0 * (scale / 2.0)), 1.0f, 1.0f);
                        GL11.glEnable(2929);
                        GlStateManager.popMatrix();
                        break;
                    }
                }
            }
        }
    }

    public enum MemeModes implements INameable {
        ZEROTWO("ZeroTwo"), ROBLOX("Roblox"), HEADED("Headed1"), HEADEDTWO("Headed2"), HEADEDTHREE("Headed3"), HEADEDFOUR("Headed4"), HEADEDFIVE("Headed5"), HEADEDSIX("Headed6"), JEFFERY("Jeffery"),  MICHAEL("Michael"), MARK("Mark"), PURPLEHENTAI("Hentai1"), HENTAITWO("Hentai2"), HENTAITHREE("Hentai3"), CLAYRAY("Dream"), SAGETWO("Sage"), KILLJOY("Killjoy");

        private final String name;

        MemeModes(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}


