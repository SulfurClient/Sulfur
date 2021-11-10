package gg.sulfur.client.impl.gui.click.element.impl.setting.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.impl.modules.render.ClickGUI;
import net.minecraft.util.ResourceLocation;
import gg.sulfur.client.impl.gui.click.GuiUtils;
import gg.sulfur.client.impl.gui.click.element.impl.setting.Setting;

import java.awt.*;

public class ToggleSetting extends Setting {

    protected ResourceLocation checked = new ResourceLocation("meme/settings/checked.png");
    protected ResourceLocation unchecked = new ResourceLocation("meme/settings/unchecked.png");
    private final int iconWidth = 13;
    private final int iconHeight = 13;
    private float imagePosX;
    private float imagePosY;

    public ToggleSetting(Value value, int width) {
        this.width = width;
        this.value = value;
        this.height = 16;
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Small1").getRenderer();
        final ClickGUI clickGUI = Sulfur.getInstance().getModuleManager().get(ClickGUI.class);
        boolean enabled = (boolean) value.getValue();
        float yPadding = ((height /*header height*/ - (font.getHeight(value.getName()))) / 2);
        float xPaddingImage = ((width - SETTING_PADDING) - iconWidth);
        float heightPadding = ((height - (iconHeight))) / 2F;
        font.drawString(value.getName(), posX + SETTING_PADDING /*offset * 2*/, posY + yPadding, -1);
        imagePosX = posX + xPaddingImage;
        imagePosY = posY + heightPadding;
        GuiUtils.glResets();
        GuiUtils.drawImage(enabled ? checked : unchecked, imagePosX, imagePosY, iconWidth, iconHeight, enabled ? clickGUI.getGuiColor() : new Color(120, 120, 120).getRGB());
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (GuiUtils.isHovering(mouseX, mouseY, (int) imagePosX, (int) imagePosY, iconWidth, iconHeight)) {
            boolean enabled = (boolean) value.getValue();
            value.setValueAutoSave(!enabled);
        }
    }
}
