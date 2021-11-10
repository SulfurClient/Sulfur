package gg.sulfur.client.impl.utils.render;

import net.minecraft.entity.EntityLivingBase;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.impl.modules.render.Hud;

import java.awt.*;

public class ColorUtil {

    public static int rainbow(int delay, double speed) {
        double rainbowState = Math.ceil(((System.currentTimeMillis() * speed) + -delay) / 20.0);
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0f), 1f, 0.7f).getRGB();
    }

    public static Color getCategoryColor(ModuleCategory category) {
        /*/switch (category) {
            case COMBAT: return new Color(255, 0, 251);
            case MOVEMENT: return new Color(31, 255, 247);
            case MISC: return new Color(54, 70, 250);
            case PLAYER: return new Color(255, 106, 0);
            case EXPLOIT: return new Color(255, 220, 62);
            case RENDER: return new Color(115, 255, 33);

            default: return new Color(255, 255,255);
        }/*/
        return new Color(125,135,216);
    }

    public static int astolfoColors(int timeOffset, int yTotal) {
        final float speed = 2900F;
        float hue = (float) (System.currentTimeMillis() % (int) speed) + ((yTotal - timeOffset) * 9);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, .65F, 1F);
    }

    public static int getHealthColor(EntityLivingBase entityLivingBase) {
        final float percentage = 100 * ((entityLivingBase.getHealth() / 2) / (entityLivingBase.getMaxHealth() / 2));
        return percentage > 75 ? 0x19ff19 : percentage > 50 ? 0xffff00 : percentage > 25 ? 0xff5500 : 0xff0900;
    }

    public static int getModeColor() {
        final Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
        return hud.color.getValue().getRGB();
    }
}
