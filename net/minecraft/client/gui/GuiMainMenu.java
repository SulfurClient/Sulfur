package net.minecraft.client.gui;

import gg.sulfur.client.impl.gui.alt.boom.GuiAltLogin;
import gg.sulfur.client.impl.gui.alt.boom.GuiAltManager;
import gg.sulfur.client.impl.modules.render.Hud;
import gg.sulfur.client.impl.utils.render.ColorCreator;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.impl.gui.alt.GUIAltManager;
import gg.sulfur.client.impl.gui.alt.GUICredits;
import gg.sulfur.client.soundfx.downloader.DownloaderThread;
import gg.sulfur.client.soundfx.downloader.References;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GuiMainMenu extends GuiScreen implements GuiYesNoCallback {

    private static ArrayList<Clip> clips = new ArrayList<>();
    public static Clip clip;
    private GuiButton viaVersion;

    public boolean doesGuiPauseGame() {
        return false;
    }

    private ResourceLocation background = new ResourceLocation("meme/Background.jpg");

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    public static void startMusic() {

    }


    public static void stopMusic() {
        for (Clip clip : clips) {
            clip.stop();
        }
        clips.clear();
    }

    @Override
    public void onGuiClosed() {
        //stopMusic();
    }

    public void initGui() {
        System.out.println(clips.size());

        //this should fix music starting in singleplayer
        if (clips.size() < 1)
            startMusic();
        else
            stopMusic();

        int var3 = this.height / 4 + 48;
        this.addSingleplayerMultiplayerButtons(var3);

        this.buttonList.add(new GuiButton(0, this.width / 2F - 100, var3 + 92 - 8, 102, 20, "Settings"));
        this.buttonList.add(new GuiButton(4, this.width / 2F + 2, var3 + 92 - 8, 98, 20, I18n.format("menu.quit")));
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
    }

    private void addSingleplayerMultiplayerButtons(int p_73969_1_) {

        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, p_73969_1_, I18n.format("menu.singleplayer")));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 100, p_73969_1_ + 24 - 4, I18n.format("menu.multiplayer")));
        this.buttonList.add(new GuiButton(14, this.width / 2 - 100, p_73969_1_ + 24 + 8 * 2, "Alt Manager"));
        this.buttonList.add(new GuiButton(69420, this.width / 2 - 100, p_73969_1_ + 24 + 12 * 3, "Credits"));
        viaVersion = new GuiButton(2158, this.width - 104, 0, 100, 20, "§fVersion Switcher: " + Sulfur.getInstance().isViaVersionEnabled());
        this.buttonList.add(viaVersion);
        //this.buttonList.add(new GuiButton(8008, this.width - 94, 0, 100, 20, "Volume Slider"));
    }

    protected void actionPerformed(GuiButton button) throws IOException {

        switch (button.id) {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                stopMusic();
                break;

            case 5:
                this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
                stopMusic();
                break;

            case 1:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                stopMusic();
                break;

            case 2:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                stopMusic();
                break;

            case 14:
                this.mc.displayGuiScreen(new GuiAltManager());
                stopMusic();
                break;

            case 4:
                this.mc.shutdown();
                stopMusic();
                break;

            case 11:
                this.mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
                stopMusic();
                break;

            case 69420:
                this.mc.displayGuiScreen(new GUICredits(this));
                break;

            case 12:
                ISaveFormat var2 = this.mc.getSaveLoader();
                WorldInfo var3 = var2.getWorldInfo("Demo_World");

                if (var3 != null) {
                    GuiYesNo var4 = GuiSelectWorld.func_152129_a(this, var3.getWorldName(), 12);
                    this.mc.displayGuiScreen(var4);
                }
                break;
            case 1337: {
                if (!References.Files.MAINMENUFILE.getFile().exists() || !References.Files.MEMEFILE.getFile().exists()) {
                    new DownloaderThread().start();
                }
                break;
            }
            case 2158: {
                Sulfur.getInstance().setViaVersion(!Sulfur.getInstance().isViaVersionEnabled());
                break;
            }
            /*/case 8008: {
                Client.INSTANCE.getVolSlider().setVisible(true);
                break;
            }/*/
        }
    }


    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        for (GuiButton button : this.buttonList) {
            if (!References.Files.MAINMENUFILE.getFile().exists() /*|| !References.Files.MEMEFILE.getFile().exists()*/) {
                if (button.id == 1337) {
                    button.visible = true;
                }
            } else if (button.id == 1337) {
                button.visible = false;
            } else if (button.id == 2158) {
                /*
                §fVersion Switcher: §a✓
                §fVersion Switcher: §c☓
                 */
                //✓, ☓
                button.displayString = "§fVersion Switcher: " + Sulfur.getInstance().isViaVersionEnabled();
            }
        }

        ResourceLocation clown = new ResourceLocation("meme/skull.png");
        ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        //Gui.drawRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(50, 50, 50).getRGB());
        int var3 = this.height / 4 + 48 - 30;
        final CustomFontRenderer extreme = Sulfur.getInstance().getFontManager().getFont("Extreme").getRenderer();
        final CustomFontRenderer large = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();

        final Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
        //  extreme.drawCenteredString("H\u00a7relium", (scaledResolution.getScaledWidth() / 2.0F), var3, ColorUtil.getModeColor());
        //   GuiUtils.drawImage(clown, (scaledResolution.getScaledWidth() / 2.0F) - 19, var3 - 60, 38, 38, -1); // old vals: var3 - 60, 38, 38 | christmas vals: var3 - 70, 38, 48
        String var10 = "Sulfur";
        String var11 = "Made by: Kansio, Divine, Dort and shitbowxd"; // old string: "Made by: Dort, Newb, Auth"
        String var12 = "Sulfur b" + Sulfur.getInstance().getClientVersion() + " - " + Sulfur.getInstance().getBuildType().getName();
        mc.getTextureManager().bindTexture(this.background);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        float x = (scaledResolution.getScaledWidth() / 2.0F) / 2f + 8 - mc.fontRendererObj.getStringWidth("Sulfur") / 2f;
        float y = height - 20;
        boolean hovered = mouseX >= x && mouseY >= y && mouseX < x + mc.fontRendererObj.getStringWidth("Sulfur") && mouseY < y + mc.fontRendererObj.FONT_HEIGHT;

        RenderUtils.drawRect(-30, -30, 40000, 40000, new Color(49, 49, 49, 255).getRGB());
        RenderUtils.drawRect((scaledResolution.getScaledWidth() / 2.0F) - 120, var3 - 30, 240, 190, new Color(0,0,0, 100).getRGB());
        RenderUtils.drawRect((scaledResolution.getScaledWidth() / 2.0F) - 120, var3 - 30, 240, 1, hud.color.getValue().getRGB());

        extreme.drawCenteredString("Sulfur", (scaledResolution.getScaledWidth() / 2.0F), var3, hud.color.getValue().getRGB());
        large.drawString(var11, this.width - large.getWidth(var11) - 2, this.height - 14, -1);
        large.drawString(var12, 4, this.height - 14, -1);
// da sex
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        viaVersion.displayString = "§fVersion Switcher: " + Sulfur.getInstance().isViaVersionEnabled();
    }
}