package gg.sulfur.client.impl.gui.click.guis.material;

import gg.sulfur.client.impl.gui.click.ClickGuiScreen;
import gg.sulfur.client.impl.gui.click.guis.material.plane.impl.MainPlane;
import net.minecraft.client.Minecraft;

import java.io.IOException;

public class MaterialUI extends ClickGuiScreen {
    private MainPlane mainPlane = null;

    public MaterialUI() {
        initializedUI();
    }

    public void initializedUI() {
        if (mainPlane == null) {
            mainPlane = new MainPlane("Sulfur",2,2,325,315);
            mainPlane.initializePlane();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mainPlane.onDrawScreen(mouseX,mouseY,partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        mainPlane.onKeyTyped(typedChar,keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        mainPlane.onMouseClicked(mouseX,mouseY,mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        mainPlane.onMouseReleased(mouseX,mouseY,mouseButton);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        mainPlane.onGuiClosed();
        if (mc.entityRenderer.theShaderGroup != null) {
            mc.entityRenderer.theShaderGroup.deleteShaderGroup();
            mc.entityRenderer.theShaderGroup = null;
        }
        selected = null;
        otherSelected = false;
        otherSliderSelected = false;
        setPrevCursor(0, 0);
        resetMouse();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void func_175273_b(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
        super.func_175273_b(mcIn, p_175273_2_, p_175273_3_);
        if (mainPlane.getPosX() + mainPlane.getWidth() > p_175273_2_) mainPlane.setPosX(p_175273_2_ - mainPlane.getWidth());
        if (mainPlane.getPosY() + mainPlane.getHeight() > p_175273_3_) mainPlane.setPosY(p_175273_3_ - mainPlane.getHeight());
    }
}

