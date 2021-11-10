package gg.sulfur.client.impl.gui.click.element.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import net.minecraft.client.gui.Gui;
import gg.sulfur.client.impl.gui.click.GuiUtils;
import gg.sulfur.client.impl.gui.click.element.Element;
import gg.sulfur.client.impl.modules.render.ClickGUI;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class CategoryPane extends Element {

    private final ModuleCategory category;
    public final int headerHeight = 16;

    public List<ModulePane> getModPanes() {
        return modPanes;
    }

    private final List<ModulePane> modPanes = new ArrayList<>();
    private boolean expanded = true;
    private boolean selected;

    public CategoryPane(ModuleCategory category, int startX, int startY) {
        this.category = category;
        posX = startX;
        posY = startY;
        width = 100;
        initModPanes();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private void initModPanes() {
        Sulfur.getInstance().getModuleManager().getAllInCategory(category).forEach(module -> modPanes.add(new ModulePane(module, width)));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (ModulePane modPane : modPanes) {
            if (this.isExpanded()) {
                modPane.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    @Override
    public void onDraw(int mouseX, int mouseY, float partialTicks) {
//        posX = 256;
//        posY = 256;
        GuiUtils.glStartFontRenderer();
        ClickGUI clickGUI = Sulfur.getInstance().getModuleManager().get(ClickGUI.class);
        if (expanded) {
            height = headerHeight;
            for (ModulePane modPane : modPanes) {
                height += modPane.getHeight();
            }
            //draw category background
            //Gui.drawRect(posX, posY, posX + width, posY + headerHeight, new Color(19, 19, 19).getRGB());

            int height2 = headerHeight;
            for (ModulePane modPane : modPanes) {
                modPane.setPos(posX, posY + height2);
                modPane.onDraw(mouseX, mouseY, partialTicks);
                height2 += modPane.getHeight();
            }

        }
        //HEADER
        final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();
        Gui.drawRect(posX, posY, posX + width, posY + headerHeight, new Color(25, 25, 25).getRGB());
        //Gui.drawRect(posX, posY, posX + width, posY + headerHeight - 15, new Color(84,97,153).getRGB());
        float widthPadding = ((width - (font.getWidth(category.getName()))) / 2);
        float heightPadding = ((headerHeight - (font.getHeight(category.getName()))) / 2);
        font.drawStringWithShadow(category.getName(), posX + widthPadding, posY + heightPadding, -1);
        //2m + textWidth = 80
    }


    public void toggleExpansion() {
        expanded = !expanded;
    }

    public void movePane(int x, int y) {
        posX += x;
        posY += y;
    }

    public ModuleCategory getCategory() {
        return category;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
