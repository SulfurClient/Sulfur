package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.ColorValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.Render3DEvent;
import gg.sulfur.client.impl.utils.render.Render3DUtil;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class ChestESP extends Module {

    public ChestESP(ModuleData moduleData) {
        super(moduleData);
        register(colorxd, opacity);
    }

    public final ColorValue colorxd = new ColorValue("Color", this, new Color(255, 60, 0));
    private NumberValue opacity = new NumberValue("Opacity", this, 1.0D, 0.1D, 1.0D);

    @Subscribe
    public void onEvent(Render3DEvent e) {
        for (Object o : mc.theWorld.loadedTileEntityList) {
            if (o instanceof TileEntityChest) {
                TileEntityLockable storage = (TileEntityLockable) o;
                this.drawESPOnStorage(storage, storage.getPos().getX(), storage.getPos().getY(), storage.getPos().getZ());
            }
        }
    }

    private void drawESPOnStorage(TileEntityLockable storage, double x, double y, double z) {
        if (!storage.isLocked()) {
            TileEntityChest chest = (TileEntityChest) storage;
            Vec3 vec;
            Vec3 vec2;
            if (chest.adjacentChestZNeg != null) {
                vec = new Vec3(x + 0.0625D, y, z - 0.9375D);
                vec2 = new Vec3(x + 0.9375D, y + 0.875D, z + 0.9375D);
            } else if (chest.adjacentChestXNeg != null) {
                vec = new Vec3(x + 0.9375D, y, z + 0.0625D);
                vec2 = new Vec3(x - 0.9375D, y + 0.875D, z + 0.9375D);
            } else {
                if (chest.adjacentChestXPos != null || chest.adjacentChestZPos != null) {
                    return;
                }

                vec = new Vec3(x + 0.0625D, y, z + 0.0625D);
                vec2 = new Vec3(x + 0.9375D, y + 0.875D, z + 0.9375D);
            }

            GL11.glPushMatrix();
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(3553);
            GL11.glEnable(2848);
            GL11.glDisable(2929);
            GL11.glDepthMask(false);

            mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);

            GL11.glColor4d(colorxd.getValue().getRed(), colorxd.getValue().getGreen(), colorxd.getValue().getBlue(), opacity.getValue());

            Render3DUtil.drawBoundingBox(new AxisAlignedBB(vec.xCoord - mc.getRenderManager().renderPosX, vec.yCoord - mc.getRenderManager().renderPosY, vec.zCoord - mc.getRenderManager().renderPosZ, vec2.xCoord - mc.getRenderManager().renderPosX, vec2.yCoord - mc.getRenderManager().renderPosY, vec2.zCoord - mc.getRenderManager().renderPosZ));

            GL11.glDisable(2848);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glColor4f(1f, 1f, 1f, 1f);
        }
    }
}