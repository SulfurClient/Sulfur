package gg.sulfur.client.impl.gui.click.element.impl.setting.impl;

/**
 * @author Kansio
 * @created 5:58 PM
 * @project Client
 */

import gg.sulfur.client.api.property.impl.ColorValue;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.impl.gui.click.GuiUtils;
import gg.sulfur.client.impl.gui.click.element.impl.setting.Setting;
import gg.sulfur.client.impl.utils.HSLColor;

import java.awt.*;

public class ColorSetting extends Setting {

    private int startPos;
    private int endPos;
    private int sliderHeight;
    private ColorValue numberValue;
    private boolean selected;
    public float sat;
    public float light;
    int selectedHue;

    public ColorSetting(Value<?> value, int width) {
        this.value = value;
        this.numberValue = (ColorValue) value;
        this.width = width;
        this.height = 16;
        selectedHue = (int) HSLColor.fromRGB(numberValue.getValue())[0];
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Small").getRenderer();

        //dumb values
        startPos = posX + SETTING_PADDING;
        endPos = posX + width - SETTING_PADDING;
        sliderHeight = posY + height - 4;
        sat = numberValue.getSat();
        light = numberValue.getLight();

        //get the current hue
        selectedHue = (int) HSLColor.fromRGB(numberValue.getValue())[0] - 3;

        //fairly sure this isn't even needed anymore
        if (selectedHue == 0) selectedHue = 1;

        for (int i = 0; i < 360; i++) {
            int r, g, b;
            r = HSLColor.HSLtoRGB(i, sat, light, 1)[0];
            g = HSLColor.HSLtoRGB(i, sat, light, 1)[1];
            b = HSLColor.HSLtoRGB(i, sat, light, 1)[2];

            if (r == 256) r = 255;
            if (g == 256) g = 255;
            if (b == 256) b = 255;

            int startPos1 = startPos + i;
            int endPos1 = startPos + i / 4;

            if (startPos + i >= endPos) startPos1 = endPos;
            if (startPos + i / 4 >= endPos) endPos1 = endPos;

            GuiUtils.drawStraightLine(startPos1, endPos1, sliderHeight, 2, new Color(r, g, b).getRGB());
        }

        //render the "Color" text
        GuiUtils.glStartFontRenderer();
        font.drawString(value.getName(), startPos, sliderHeight - font.getHeight(this.value.getName()) - 4, -1);
        GuiUtils.glResets();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        moveSlider(mouseX, mouseY, mouseButton, false);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        selected = false;
    }

    @Override
    public boolean mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick,
                                  boolean otherSelected) {
        return moveSlider(mouseX, mouseY, clickedMouseButton, otherSelected);
    }

    private boolean moveSlider(int mouseX, int mouseY, int clickedMouseButton, boolean otherSelected) {
        double diff = endPos - startPos;
        double length = endPos - mouseX;
        if (length < 0) {
            length = 0;
        }
        if (length > diff) {
            length = diff;
        }

        if (selected || GuiUtils.isHovering(mouseX, mouseY, startPos, sliderHeight - 2, endPos - startPos, 4) && !otherSelected) {
            selected = true;
            float value = (float) (360 - ((diff * 0) - (length * 0) + (length * 360)) / diff + 0);
            numberValue.setValueAutoSave(HSLColor.toRGB(value, sat, light));
            return selected;
        }
        return selected;
    }

    private static float hue2rgb(float p, float q, float h) {
        if (h < 0) {
            h += 1;
        }

        if (h > 1) {
            h -= 1;
        }

        if (6 * h < 1) {
            return p + ((q - p) * 6 * h);
        }

        if (2 * h < 1) {
            return q;
        }

        if (3 * h < 2) {
            return p + ((q - p) * 6 * ((2.0f / 3.0f) - h));
        }

        return p;
    }

    static public Color hslColor(float h, float s, float l) {
        float q, p, r, g, b;

        if (s == 0) {
            r = g = b = l; // achromatic
        } else {
            q = l < 0.5 ? (l * (1 + s)) : (l + s - l * s);
            p = 2 * l - q;
            r = hue2rgb(p, q, h + 1.0f / 3);
            g = hue2rgb(p, q, h);
            b = hue2rgb(p, q, h - 1.0f / 3);
        }
        return new Color(Math.round(r * 255), Math.round(g * 255), Math.round(b * 255));
    }
}
