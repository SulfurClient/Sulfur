package gg.sulfur.client.impl.gui.click.element.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.impl.utils.render.ColorUtil;
import net.minecraft.client.gui.Gui;
import gg.sulfur.client.impl.gui.click.element.impl.setting.SettingPane;
import gg.sulfur.client.impl.gui.click.GuiUtils;
import gg.sulfur.client.impl.gui.click.element.Element;
import gg.sulfur.client.impl.modules.render.ClickGUI;

import java.awt.*;

public class ModulePane extends Element {

    private final Module module;
    public static final int modButtonHeight = 18;
    private boolean expanded;
    private final boolean canExpand;
    private boolean bindingMode;

    public SettingPane getSettingPane() {
        return settingPane;
    }

    private final SettingPane settingPane;

    //no way to grab atm with normal mc font renderer
    public ModulePane(Module module, int width) {
        this.module = module;
        this.width = width;
        this.height = modButtonHeight;
        this.canExpand = valueManager.getValuesFromOwner(module).size() > 0;
        settingPane = new SettingPane(module, this.width);
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
        //BUTTON
        final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();
        GuiUtils.glResets();
        /*/if (GuiUtils.isHovering(mouseX, mouseY, this.posX, this.posY, this.width, modButtonHeight)) {
            Gui.drawRect(posX, posY, posX + width, posY + modButtonHeight, new Color(133, 133, 133).getRGB());
        }/*/
        GuiUtils.glStartFontRenderer();
        ClickGUI clickGUI = Sulfur.getInstance().getModuleManager().get(ClickGUI.class);
        int color = clickGUI.getGuiColor();
        String modName = module.getModuleData().getName();
        float centerAlignment = ((modButtonHeight - font.getHeight(modName)) / 2);
        Gui.drawRect(posX, posY, posX + width, posY + modButtonHeight, new Color(35, 35, 35, 255).getRGB());
        if (expanded) {
            Gui.drawRect(posX, posY, posX + width, posY + modButtonHeight + settingPane.calculateHeight(), new Color(35, 35, 35, 255).getRGB());
            Gui.drawRect(posX, posY, posX + 1.5, posY + modButtonHeight + settingPane.calculateHeight(), clickGUI.getGuiColor());
            settingPane.setPos(this.posX, this.posY + modButtonHeight);
            settingPane.onDraw(mouseX, mouseY, partialTicks);
        }
        Gui.drawRect(posX, posY, posX + width, posY + modButtonHeight, module.isToggled() ? clickGUI.getGuiColor() :  new Color(35, 35, 35, 255).getRGB());
        font.drawStringWithShadow(bindingMode ? "Press any key..." : modName, posX + PADDING + 5, posY + centerAlignment, -1);
        //render expansion icon
        if (canExpand) {
            int iconWidth = 8;
            int iconHeight = 5;
            int testPadding = ((modButtonHeight - (iconHeight)) / 2);
            if (expanded) GuiUtils.drawImage(collapseIcon, posX + (width - (iconWidth / 2F) - 10), posY + testPadding, iconWidth, iconHeight, -1);
        }
        int size = settingPane.calculateHeight();
        height = modButtonHeight + (expanded ? size : 0);

    }

    public void keyTyped(char typedChar, int keyCode) {
        if (bindingMode) {
            if (keyCode == 1) {
                bindingMode = false;
                return;
            }
            module.setKeyBind(keyCode);
            ChatUtil.displayChatMessage(String.format("%s was successfully bound to %s", module.getModuleData().getName(), String.valueOf(typedChar).toUpperCase()));
            bindingMode = false;
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (expanded) {
            settingPane.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (GuiUtils.isHovering(mouseX, mouseY, this.posX, this.posY, this.width, modButtonHeight)) {
            switch (mouseButton) {
                case 0:
                    module.toggle();
                    break;
                case 1:
                    if (canExpand) {
                        expanded = !expanded;
                        int size = settingPane.calculateHeight();
                        height = modButtonHeight + (expanded ? size : 0);
                    }
                    break;
                case 2:
                    bindingMode = true;
                    break;
            }
        }

    }
}
