package gg.sulfur.client.impl.gui.vaziakclick.element.impl.setting.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.api.property.impl.StringValue;
import gg.sulfur.client.impl.gui.vaziakclick.GuiUtils;
import gg.sulfur.client.impl.gui.vaziakclick.element.impl.setting.Setting;
import net.minecraft.client.gui.Gui;

import java.awt.*;
import java.util.List;


public class ModeSetting extends Setting {

    public ModeSetting(Value value, int width) {
        this.value = value;
        this.width = width;
        this.height = 16;
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        if (GuiUtils.isHovering(mouseX, mouseY, this.posX, this.posY, width, height)) {
            Gui.drawRect(this.posX, this.posY, this.posX + width, this.posY + height, new Color(0, 0, 0, 0.6F).getRGB());
        }
        final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Small1").getRenderer();
        String mode = value.getName() + ": " + value.getValue();
        // Hud.guiFont3.drawString(name, posX + settingPadding /*padding * 2*/, posY + 2, -1);
        float yPadding = (height /*header height*/ - font.getHeight(mode)) / 2;
        float widthPadding = ((width - (font.getWidth(mode))) / 2);
        font.drawString(mode, posX + widthPadding, posY + yPadding, -1);

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (GuiUtils.isHovering(mouseX, mouseY, this.posX, this.posY, width, height)) {
            StringValue stringValue = (StringValue) value;
            List<String> modes = stringValue.getChoices();

            //shitty workaround for dorts horrible string modes
            int index = modes.indexOf(stringValue.getValue());
            stringValue.setValueAutoSave(index <= modes.size() - 2 ? modes.get(index + 1) : modes.get(0));

        }
    }
}

