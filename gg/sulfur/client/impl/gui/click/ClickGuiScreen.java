package gg.sulfur.client.impl.gui.click;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.impl.gui.click.element.impl.CategoryPane;
import gg.sulfur.client.impl.gui.click.element.impl.ModulePane;
import gg.sulfur.client.impl.gui.click.element.impl.setting.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import gg.sulfur.client.impl.modules.render.ClickGUI;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ClickGuiScreen extends GuiScreen {

    protected final List<CategoryPane> categoryPanes = new CopyOnWriteArrayList<>();
    protected int prevMouseX = 0, prevMouseY = 0;
    protected CategoryPane selected;
    protected boolean otherSelected;
    protected boolean otherSliderSelected = false;

    public ClickGuiScreen() {
        int cx = 20;
        for (ModuleCategory category : ModuleCategory.values()) {
            if (category != ModuleCategory.HIDDEN) {
                categoryPanes.add(new CategoryPane(category, cx, 20));
                cx += 120;
            }
        }
    }

    public List<CategoryPane> getCategoryPanes() {
        return categoryPanes;
    }

    protected void resetMouse() {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int var3 = Mouse.getEventButton();
        this.mouseReleased(var1, var2, var3);
    }

    protected void setPrevCursor(int x, int y) {
        prevMouseX = x;
        prevMouseY = y;
    }

    protected boolean isPrevCursorSet() {
        return prevMouseX == 0 || prevMouseY == 0;
    }

}