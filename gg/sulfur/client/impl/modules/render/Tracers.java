package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.ColorValue;
import gg.sulfur.client.impl.events.Render3DEvent;
import gg.sulfur.client.impl.utils.math.MathUtil;
import gg.sulfur.client.impl.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class Tracers extends Module {

    public Tracers(ModuleData moduleData) {
        super(moduleData);
        register(colorxd);
    }

    public final ColorValue colorxd = new ColorValue("Color", this, new Color(255, 60, 0));

    public void onEnable() {

    }

    public void onDisable() {

    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        for (Object o : mc.theWorld.loadedEntityList) {
            Entity entity = (Entity) o;
            if (entity instanceof net.minecraft.entity.player.EntityPlayer && entity != mc.thePlayer && isInTablist((EntityLivingBase) entity)) {
                double posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - RenderManager.renderPosX;
                double posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - RenderManager.renderPosY;
                double posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - RenderManager.renderPosZ;
                boolean old = mc.gameSettings.viewBobbing;
                RenderUtil.startDrawing();
                mc.gameSettings.viewBobbing = false;
                mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
                mc.gameSettings.viewBobbing = old;
                float color = (float) Math.round(255.0D - mc.thePlayer.getDistanceSqToEntity(entity) * 255.0D /
                        MathUtil.square(mc.gameSettings.renderDistanceChunks * 2.5D)) / 255.0F;
                drawLine(entity, new double[3], posX, posY, posZ);
                RenderUtil.stopDrawing();
            }
        }
        GL11.glColor4f(colorxd.getValue().getRed(), colorxd.getValue().getGreen(), colorxd.getValue().getBlue(), 1.0F);
    }

    private void drawLine(Entity entity, double[] color, double x, double y, double z) {
        GL11.glEnable(2848);
        if (color.length >= 4) {
            if (color[3] <= 0.1D)
                return;
            GL11.glColor4d(color[0], color[1], color[2], color[3]);
        } else {
            GL11.glColor3d(color[0], color[1], color[2]);
        }
        GL11.glLineWidth(1.0F);
        GL11.glBegin(1);
        GL11.glVertex3d(0.0D, mc.thePlayer.getEyeHeight(), 0.0D);
        GL11.glVertex3d(x, y, z);
        GL11.glEnd();
        GL11.glDisable(2848);
    }

    private boolean isInTablist(EntityLivingBase player) {
        if (mc.isSingleplayer()) {
            return true;
        }
        for (Object o : mc.getNetHandler().getPlayerInfoMap()) {
            NetworkPlayerInfo playerInfo = (NetworkPlayerInfo) o;
            if (playerInfo.getGameProfile().getName().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }


}