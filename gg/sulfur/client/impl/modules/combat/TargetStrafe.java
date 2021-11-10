package gg.sulfur.client.impl.modules.combat;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.impl.modules.movement.Flight;
import gg.sulfur.client.impl.modules.render.Hud;
import net.minecraft.entity.Entity;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.SliderUnit;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.events.Render3DEvent;
import gg.sulfur.client.impl.events.UpdateEvent;
import gg.sulfur.client.impl.utils.combat.MathUtils;
import gg.sulfur.client.impl.utils.movement.MotionUtils;

import static org.lwjgl.opengl.GL11.*;

// Credits: Sulfur
public class TargetStrafe extends Module {

    public static double dir = -1;

    public final NumberValue range = new NumberValue("Range", this, 3, 0.1, 6, SliderUnit.BLOCKS);
    private final BooleanValue render = new BooleanValue("Render", this, true);
    public final NumberValue width = new NumberValue("Width", this, 3, 1, 5, true, render);
    private final BooleanValue alwaysWhileFly = new BooleanValue("Always while flying", this, true);
    private final BooleanValue controllable = new BooleanValue("Controllable", this, true);
    private final BooleanValue jumpOnly = new BooleanValue("Jump Only", this, false);
    private final BooleanValue always = new BooleanValue("Always", this, true);
    private final BooleanValue playerDist = new BooleanValue("Circle on local player", this, false);

    public TargetStrafe(ModuleData moduleData) {
        super(moduleData);
        register(range, width, controllable, jumpOnly, always, render, alwaysWhileFly, playerDist);
    }

    @Subscribe
    public void onRender(Render3DEvent event) {
        if (canStrafe() && render.getValue()) {
            drawCircle(Aura.target, mc.timer.renderPartialTicks);
        }
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (canStrafe() && always.getValue()) {
                MotionUtils.setMotion(MotionUtils.getSpeed());
            }

            if (controllable.getValue()) {
                if (mc.gameSettings.keyBindLeft.getIsKeyPressed()) {
                    dir = 1;
                } else if (mc.gameSettings.keyBindRight.getIsKeyPressed()) {
                    dir = -1;
                }
            }

            if (mc.thePlayer.isCollidedHorizontally || (!MotionUtils.isBlockUnder() && !Sulfur.getInstance().getModuleManager().get(Flight.class).isToggled())) {
                invertStrafe();
            }
        }
    }

    private void invertStrafe() {
        dir = -dir;
    }

    private void drawCircle(Entity entity, float partialTicks) {
        Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);

        glPushMatrix();
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glLineWidth(width.getValue().floatValue());
        glBegin(GL_LINE_STRIP);

        glColor3d(hud.color.getValue().getRed() / 255.0, hud.color.getValue().getGreen() / 255.0, hud.color.getValue().getBlue() / 255.0);

        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - mc.getRenderManager().viewerPosZ;

        if (playerDist.getValue())
            y = entity.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTicks - mc.getRenderManager().viewerPosY;

        final double pix2 = Math.PI * 2.0D;
        if (!playerDist.getValue()) {
            for (int i = 0; i <= 90; ++i) {
                glVertex3d(x + (range.getValue() - 0.5) * Math.cos(i * pix2 / 45), y, z + (range.getValue() - 0.5) * Math.sin(i * pix2 / 45));
            }
        } else {
            for (int i = 0; i <= 90; ++i) {
                    glVertex3d(x + (entity.getDistanceToEntity(mc.thePlayer)) * Math.cos(i * pix2 / 45), y, z + (entity.getDistanceToEntity(mc.thePlayer)) * Math.sin(i * pix2 / 45));

            }
        }

        glEnd();
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static boolean canStrafe() {
        TargetStrafe targetStrafe = Sulfur.getInstance().getModuleManager().get(TargetStrafe.class);

        if (!Sulfur.getInstance().getModuleManager().get(TargetStrafe.class).isToggled())
            return false;

        if (targetStrafe.alwaysWhileFly.getValue() && Sulfur.getInstance().getModuleManager().get(Flight.class).isToggled() && Aura.target != null)
            return true;

        if (targetStrafe.jumpOnly.getValue() && !mc.gameSettings.keyBindJump.pressed)
            return false;

        if (Aura.target == null)
            return false;

        return true;

        ///Auth why did you have to make it a huge ternary statement
        //return !targetStrafe.jumpOnly.getValue() ? Aura.target != null && Client.INSTANCE.getModuleManager().get(TargetStrafe.class).isToggled() /*!Client.INSTANCE.getModuleManager().get(LongJump.class).isToggled()*/ : Aura.target != null && Client.INSTANCE.getModuleManager().get(TargetStrafe.class).isToggled() && mc.gameSettings.keyBindJump.getIsKeyPressed();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        dir = -1;
    }
}
