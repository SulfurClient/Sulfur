package gg.sulfur.client.impl.gui.click.element.impl.setting.impl;

import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import net.minecraft.client.gui.Gui;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.impl.gui.click.GuiUtils;
import gg.sulfur.client.impl.gui.click.element.impl.setting.Setting;

import java.awt.*;
import java.util.List;

public class EnumSetting extends Setting {

    public EnumSetting(Value value, int width) {
        this.value = value;
        this.width = width;
        this.height = 16;
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        if (GuiUtils.isHovering(mouseX, mouseY, this.posX, this.posY, width, height)) {
            Gui.drawRect(this.posX, this.posY, this.posX + width, this.posY + height, new Color(0, 0, 0, 0.6F).getRGB());
        }
        EnumValue<? extends INameable> enumValue = (EnumValue<? extends INameable>) value;
        String mode = value.getName() + ": " + enumValue.getValue().getDisplayName();
        final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Small1").getRenderer();
        float yPadding = (height - font.getHeight(mode)) / 2;
        float widthPadding = ((width - (font.getWidth(mode))) / 2);
        font.drawString(mode, posX + widthPadding, posY + yPadding, -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (GuiUtils.isHovering(mouseX, mouseY, this.posX, this.posY, width, height)) {
            switch (mouseButton) {
                case 0: {
                    EnumValue<INameable> enumValue = (EnumValue<INameable>) value;
                    List<INameable> modes = enumValue.getValues();
                    int index = modes.indexOf(enumValue.getValue());
                    enumValue.setValueAutoSave(index <= modes.size() - 2 ? modes.get(index + 1) : modes.get(0));
                }
                break;

                case 1: {
                    EnumValue<INameable> enumValue = (EnumValue<INameable>) value;
                    List<INameable> modes = enumValue.getValues();
                    int index = modes.indexOf(enumValue.getValue());
                    enumValue.setValueAutoSave(index >= 1 ? modes.get(index - 1) : modes.get(modes.size() - 1));
                }
                break;
            }
        }
    }
}

