package gg.sulfur.client.impl.utils.player;

import gg.sulfur.client.impl.events.MovementEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;

/**
 * @author Kansio
 * @created 2:11 PM
 * @project Client
 */
public class MovementUtils {

    public static void setSpeed(MovementEvent e, double speed, float forward, float strafing, float yaw) {
        boolean reversed = forward < 0.0F;
        float strafingYaw = 90.0F * (forward > 0.0F ? 0.5F : (reversed ? -0.5F : 1.0F));
        if (reversed) {
            yaw += 180.0F;
        }

        if (strafing > 0.0F) {
            yaw -= strafingYaw;
        } else if (strafing < 0.0F) {
            yaw += strafingYaw;
        }

        double x = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
        double z = Math.cos(Math.toRadians((double)yaw));
        e.setMotionX(x * speed);
        e.setMotionZ(z * speed);
    }

    public static void setSpeed(MovementEvent e, double speed) {
        EntityPlayerSP player = Minecraft.theMinecraft.thePlayer;
        if (player.isMoving()) {
            /*/if (targetStrafe.getState() && (!(Boolean)targetStrafe.holdspace.getValue() || Keyboard.isKeyDown(57))) {
                EntityLivingBase target = killAura.target;
                if (target != null) {
                    float dist = mc.thePlayer.getDistanceToEntity(target);
                    double radius = (Double)targetStrafe.radius.getValue();
                    setSpeed(e, speed, (double)dist <= radius + 1.0E-4D ? 0.0F : 1.0F, (double)dist <= radius + 1.0D ? (float)targetStrafe.direction : 0.0F, RotationUtil.getYawToEntity(target));
                    return;
                }
            }/*/

            setSpeed(e, speed, player.moveForward, player.moveStrafing, player.rotationYaw);
        }

    }

}
