package gg.sulfur.client.impl.notification;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.notification.AbstractNotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import gg.sulfur.client.impl.modules.render.Hud;

import java.awt.*;

public class Info extends AbstractNotification {

//    private static final ResourceLocation WARNING_ICON = new ResourceLocation("dortware/warning.png");

    public Info(String message, int x, int y, long endTime) {
        super(message, x, y, endTime);
    }


    @Override
    public void update() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
//        final CustomFontRenderer fontRenderer = Client.INSTANCE.getFontManager().getFont("Large").getRenderer();
        if (System.currentTimeMillis() >= getEndTime()) {
            setX(getX() + 1);
            if (getX() > resolution.getScaledWidth() + 155) {
                Sulfur.getInstance().getNotificationManager().getObjects().remove(this);
            }
        }

        int height = 20;

        Gui.drawRect(getX(), getY() - height, resolution.getScaledWidth(), getY(), new Color(0, 0, 0, 125).getRGB());

        Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
        Color hudColor = hud.color.getValue();

        Gui.drawRect(getX() - 2, getY() - height, getX(), getY(), hudColor.getRGB());
        mc.fontRendererObj.drawString(this.getMessage(), getX() + 7.5F, getY() - height / 1.5f, -1);

    }
}
