package gg.sulfur.client.impl.gui.alt;

import gg.sulfur.client.impl.utils.render.ColorCreator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class GUICredits extends GuiScreen {

    private GuiScreen parentScreen;
    private ResourceLocation background = new ResourceLocation("meme/creds.png");

    List<String> credits = Arrays.asList(
            "Dort, Newb, Auth, and Aidan -> Making the client base",
            "Auth -> Ghostly Reach Disabler",
            "Verble -> Auto Pot",
            "oHare -> ChestESP"

    );

    public GUICredits(final GuiScreen parent) {
        this.parentScreen = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        GuiMainMenu.stopMusic();
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        mc.getTextureManager().bindTexture(this.background);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        // Draw.drawImg(new ResourceLocation("meme/Background.jpg"), 1.0, 1.0, this.width, this.height);
        GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.enableAlpha();
       // Gui.drawRect(0, 0, this.width, this.height, new Color(0, 0, 0, 150).getRGB());
      //  this.drawGradientRect(0, 0, this.width, this.height, new Color(0, 0, 0).getRGB(), new Color(0, 0, 0, 120).getRGB());
     //   this.drawGradientRect(0, 0, this.width, this.height, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 120).getRGB());
        GlStateManager.popMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final int logoPositionY = this.height / 2 - 130;
        int y = 0;
        int color = 0;
        color = ColorCreator.createRainbowFromOffset2(-6000, 0);
        mc.fontRendererObj.drawStringWithShadow("Credits: ", 15.0f, 10.0f, color);
        for (String credit : credits) {
            mc.fontRendererObj.drawStringWithShadow(credit, 15.0f, 20 + y, new Color(255, 255, 255).getRGB());
            y += 11;
        }
       /*/ //Start beta testers
        mc.fontRendererObj.drawStringWithShadow("Beta Testers: ", 873.0f, 10.0f, color);
        mc.fontRendererObj.drawStringWithShadow("Deppressing", 880.0f, 30.0f, new Color(190, 37, 255).getRGB());
        mc.fontRendererObj.drawStringWithShadow("bwunny", 906.0f, 40.0f, new Color(190, 37, 255).getRGB());
        mc.fontRendererObj.drawStringWithShadow("Melody", 908.9f, 50.0f, new Color(190, 37, 255).getRGB());
        mc.fontRendererObj.drawStringWithShadow("tomas", 914.0f, 60.0f, new Color(190, 37, 255).getRGB());
        mc.fontRendererObj.drawStringWithShadow("vrx", 924.0f, 69.0f, new Color(190, 37, 255).getRGB());/*/
        //mc.fontRendererObj.drawStringWithShadow("I love these fuckers ^", 800.0f, 160.0f, color);
        //mc.getTextureManager().bindTexture(this.background);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void onGuiClosed() {
        //GuiMainMenu.stopMusic();
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().displayGuiScreen(this.parentScreen);
        }
        super.keyTyped(typedChar, keyCode);
    }
}