package gg.sulfur.client.impl.modules.render;

import com.google.common.eventbus.Subscribe;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.SliderUnit;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.ColorValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.KeyboardEvent;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.gui.tab.Tab2;
import gg.sulfur.client.impl.modules.render.hud.CleanTheme;
import gg.sulfur.client.impl.modules.render.hud.LegitTheme;
import gg.sulfur.client.impl.modules.render.hud.SulfurTheme;
import gg.sulfur.client.impl.utils.networking.ServerUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.Packet;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class Hud extends Module {

    public final EnumValue<TargetHudMode> targetHudMode = new EnumValue<>("Target HUD", this, TargetHudMode.values());
    public final EnumValue<Theme> arraylistMode = new EnumValue<>("Theme", this, Theme.values());
    public final EnumValue<Watermark> watermark = new EnumValue<>("Watermark", this, Watermark.values());
    public final EnumValue<ColorMode> colorMode = new EnumValue<>("Color mode", this, ColorMode.values());
    public final NumberValue targetX = new NumberValue("Target HUD X", this, 20, 0, 300, SliderUnit.X, true);
    public final NumberValue targetY = new NumberValue("Target HUD Y", this, 20, 0, 200, SliderUnit.Y, true);
    public final BooleanValue targetHUD = new BooleanValue("Show target", this, true);
    public final BooleanValue enableWatermark = new BooleanValue("Watermark mode", this, true);
    public final BooleanValue hideRender = new BooleanValue("Hide Render", this, true);
    public final BooleanValue font = new BooleanValue("Custom Font", this, false);

    public final ColorValue gradient1 = new ColorValue("Gradient 1st", this, new Color(255, 60, 0));
    public final ColorValue gradient2 = new ColorValue("Gradient 2nd", this, new Color(255, 0, 0));

    public final BooleanValue showScoreboard = new BooleanValue("Scoreboard", this, true);
    public final BooleanValue showNumbers = new BooleanValue("Show Numbers", this, true);
    public final EnumValue<ScoreboardLocation> scoreboardLocation = new EnumValue<>("Scoreboard Location", this, ScoreboardLocation.values());
    public final NumberValue scoreboardY = new NumberValue("Vertical Location", this, 0, -500, 500, SliderUnit.Y, true);

    public final ColorValue color = new ColorValue("Color", this, new Color(255, 255, 255));

    final Tab2 tab2 = new Tab2();

    private final SulfurTheme sulfurTheme;
    private final CleanTheme cleanTheme;
    private final LegitTheme legitTheme;

    public final Map<Long, Packet> in = new HashMap<>();
    public final Map<Long, Packet> out = new HashMap<>();

    public Hud(ModuleData moduleData) {
        super(moduleData);

        this.sulfurTheme = new SulfurTheme(this);
        this.cleanTheme = new CleanTheme(this);
        this.legitTheme = new LegitTheme(this);

        register(
                targetHudMode, arraylistMode, targetHUD, enableWatermark, watermark, colorMode, color, targetX, targetY, hideRender, font, gradient1, gradient2,

                showScoreboard, showNumbers, scoreboardLocation, scoreboardY
        );
    }

    @Subscribe
    public void onRender(RenderHUDEvent event) {
        if (mc.gameSettings.showDebugInfo) return;

        String bar = font.getValue() ? "|" : "⎜";

        String title = "{client_name}§7| §f{build} - §f{build_type} §7| §f{ip} §7| §fUser: {user}"
                .replaceAll("\\{build}", Sulfur.getInstance().getClientVersion())
                .replaceAll("\\{ip}", ServerUtils.getServer())
                .replaceAll("\\{build_type}", Sulfur.getInstance().getBuildType().getName())
                .replaceAll("\\{client_name}", Sulfur.getInstance().getClientName())
                .replaceAll("\\{user}", Sulfur.getInstance().getUser())
                .replaceAll("\\|", bar);

        CustomFontRenderer fontRenderer = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();

        if (enableWatermark.getValue()) {
            switch (watermark.getValue()) {
                case CLEAN: {

                    if (!font.getValue()) {
                        Gui.drawRect(5, 5, 5 + mc.fontRendererObj.getStringWidth(title) + 8, 22, new Color(0, 0, 0, 105).getRGB());
                        Gui.drawRect(5, 5, 5 + mc.fontRendererObj.getStringWidth(title) + 8, 6, color.getValue().getRGB());
                        mc.fontRendererObj.drawString(title, 9, 10.5F, -1);
                    } else {
                        Gui.drawRect(5, 5, 5 + fontRenderer.getWidth(title) + 8, 22, new Color(0, 0, 0, 105).getRGB());
                        Gui.drawRect(5, 5, 5 + fontRenderer.getWidth(title) + 8, 8, color.getValue().getRGB());
                        fontRenderer.drawString(title, 9, 10.5F, -1);
                    }
                    break;
                }
                case NORMAL: {
                    String tit = Sulfur.getInstance().getClientName().charAt(0) + "§f" + Sulfur.getInstance().getClientName().substring(1);

                    if (!font.getValue())
                        mc.fontRendererObj.drawStringWithShadow(tit, 6, 6, color.getValue().getRGB());
                    else
                        fontRenderer.drawStringWithShadow(tit, 6, 6, color.getValue().getRGB());
                    break;
                }
            }
        }

        try {
            switch (arraylistMode.getValue()) {
                case LEGIT: {
                    legitTheme.render(event);
                    break;
                }
                case SULFUR: {
                    sulfurTheme.render(event);
                    break;
                }
                case CLEAN: {
                    cleanTheme.render(event);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onKeyboard(KeyboardEvent event) {
        if (mc.gameSettings.showDebugInfo)
            return;

        //tab2.updateKeys(event);
    }

    public enum TargetHudMode implements INameable {
        SIMPLE("Simple"), OLD("Old"), NEW("New"), TEST("Test");
        public final String name;

        TargetHudMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum Theme implements INameable {
        CLEAN("Simple"),
        LEGIT("Legit"),
        SULFUR("Sulfur");
        public final String name;

        Theme(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum Watermark implements INameable {
        CLEAN("Clean"),
        NORMAL("Normal"),
        SULFUR("Sulfur");
        public final String name;

        Watermark(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum ScoreboardLocation implements INameable {
        LEFT("Left"),
        RIGHT("Right");
        public final String name;

        ScoreboardLocation(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

    public enum ColorMode implements INameable {
        ASTOLFO("Astolfo"),
        WAVE("Wave"),
        STATIC("Static"),
        GRADIENT2("Gradient"),
        RAINBOW("Rainbow");
        public final String name;

        ColorMode(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }
}
