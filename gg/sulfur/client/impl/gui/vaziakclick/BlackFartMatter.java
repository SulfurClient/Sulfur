package gg.sulfur.client.impl.gui.vaziakclick;

import gg.sulfur.client.impl.gui.click.ClickGuiScreen;
import gg.sulfur.client.impl.gui.vaziakclick.element.impl.CategoryPane;
import gg.sulfur.client.impl.gui.vaziakclick.element.impl.ModulePane;
import gg.sulfur.client.impl.gui.vaziakclick.element.impl.setting.Setting;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.impl.modules.render.ClickGUI;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class BlackFartMatter extends ClickGuiScreen {

    private final List<CategoryPane> categoryPanes = new CopyOnWriteArrayList<>();
    private CategoryPane selected;
    private boolean otherSelected;
    private boolean otherSliderSelected = false;
    private int prevMouseX = 0, prevMouseY = 0;

    public BlackFartMatter() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        try {
            this.handleInput();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ClickGUI clickGUI = Sulfur.getInstance().getModuleManager().get(ClickGUI.class);

        if (clickGUI.booleanValue2.getValue()) {
            ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

            double alpha1 = clickGUI.alpha.getCastedValue();

            int startColor = new Color(0, 0, 0, 0).getRGB();
            int endColor = clickGUI.getGuiColor();
            Gui.drawGradientRectDiagonal(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), startColor, endColor);
        }

        categoryPanes.forEach(categoryPane -> categoryPane.onDraw(mouseX, mouseY, partialTicks));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        for (CategoryPane categoryPane : categoryPanes) {
            for (ModulePane modulePane : categoryPane.getModPanes()) {
                modulePane.keyTyped(typedChar, keyCode);
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onGuiClosed() {
        if (mc.entityRenderer.theShaderGroup != null) {
            mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            mc.entityRenderer.theShaderGroup = null;
        }
        selected = null;
        otherSelected = false;
        otherSliderSelected = false;
        setPrevCursor(0, 0);
        resetMouse();
        super.onGuiClosed();
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (CategoryPane categoryPane : categoryPanes) {
            for (ModulePane modulePane : categoryPane.getModPanes()) {
                for (Setting setting : modulePane.getSettingPane().getSettings()) {
                    boolean otherSelected = setting.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick, otherSliderSelected);
                    if (otherSelected) {
                        otherSliderSelected = true;
                        selected = null;
                        this.otherSelected = true;
                    }
                }
            }
        }

        if (isPrevCursorSet()) {
            setPrevCursor(mouseX, mouseY);
        }
        if (selected != null && (mouseX == 0 || mouseY == 0)) {
            {
                selected = null;
            }
        }
        for (int i = categoryPanes.size() - 1; i >= 0; i--) {
            CategoryPane pane = categoryPanes.get(i);
            boolean headerSelected = GuiUtils.isHeaderHovering(mouseX, mouseY, pane);
            if ((((headerSelected) || selected == pane) && !otherSelected) && clickedMouseButton == 0) {
                if (selected == null) {
                    prevMouseX = mouseX;
                    prevMouseY = mouseY;
                }
                selected = pane;
                //cringe but efficient way to move to the back of the list
                categoryPanes.remove(pane);
                categoryPanes.add(pane);
                pane.movePane(mouseX - prevMouseX, mouseY - prevMouseY);
                setPrevCursor(mouseX, mouseY);
                break;
            }
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        // mouseClickMove(mouseX, mouseY, mouseButton, 0);
        for (CategoryPane pane : categoryPanes) {
            if (GuiUtils.isHovering(mouseX, mouseY, pane)) {
                pane.mouseClicked(mouseX, mouseY, mouseButton);
            }
            boolean headerSelected = GuiUtils.isHeaderHovering(mouseX, mouseY, pane);
            if (headerSelected && mouseButton == 1) {
                pane.toggleExpansion();
            }
        }
        selected = null;
        otherSelected = false;
        otherSliderSelected = false;
        resetMouse();
        setPrevCursor(0, 0);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

}