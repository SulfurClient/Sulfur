package gg.sulfur.client.impl.modules.render;

import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.ColorValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.gui.click.ClickGuiScreen;
import gg.sulfur.client.impl.gui.click.PaneState;
import gg.sulfur.client.impl.gui.click.element.impl.CategoryPane;
import gg.sulfur.client.impl.gui.click.guis.*;
import gg.sulfur.client.impl.gui.click.guis.material.MaterialUI;
import gg.sulfur.client.impl.gui.vaziakclick.BlackFartMatter;
import gg.sulfur.client.impl.utils.render.ColorUtil;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends Module {

    private ClickGuiScreen uiScreen;

    private final List<PaneState> preloadPaneStates = new ArrayList<>();

    public EnumValue<Modes> mode = new EnumValue<Modes>("ClickGUI Mode", this, Modes.values());
    public final ColorValue colorValue = new ColorValue("Color", this, new Color(255, 255, 255));
    public final NumberValue alpha = new NumberValue("Gradient Alpha", this, 150, 0, 255, true);
    public final BooleanValue booleanValue2 = new BooleanValue("Gradient", this, false);
    private final BooleanValue booleanValue1 = new BooleanValue("Rainbow", this, false);
    public final BooleanValue booleanValue = new BooleanValue("Blur", this, true);
    public final BooleanValue damnepicgui = new BooleanValue("punjab gui", this, false);

    public ClickGUI(ModuleData moduleData) {
        super(moduleData);
        register(mode, colorValue, alpha, booleanValue2, booleanValue1, booleanValue);
    }

    public int getGuiColor() {
        if (booleanValue1.getValue()) {
            return ColorUtil.rainbow(-6000, 0);
        }

        double red1 = colorValue.getValue().getRed();
        double green1 = colorValue.getValue().getGreen();
        double blue1 = colorValue.getValue().getBlue();

        Color color = new Color((float) red1 / 255.0f, (float) green1 / 255.0f, (float) blue1 / 255.0f);

        return color.getRGB();
    }

    @Override
    public void onEnable() {
        if (uiScreen == null) {
            uiScreen = new ClickGuiScreen();
            preloadPaneStates.forEach(this::setPaneState);
        }

        if (booleanValue.getValue()) {
            mc.entityRenderer.theShaderGroup = null;
            mc.entityRenderer.func_175069_a(new ResourceLocation("shaders/post/blur.json"));
        }


        switch (mode.getValue()) {

            case Sulfur: {
                uiScreen = new SulfurUI();
                break;
            }
            case Material: {
                uiScreen = new SulfurUI();
                break;
            }
            case punjab: {
                uiScreen = new BlackFartMatter();
                break;
            }

        }

        mc.displayGuiScreen(uiScreen);
        toggle();
    }

    public void addPreloadPaneState(PaneState paneState) {
        this.preloadPaneStates.add(paneState);
    }

    public void setPaneState(PaneState paneState) {
        for (CategoryPane pane : uiScreen.getCategoryPanes()) {
            if (pane.getCategory().getName().equalsIgnoreCase(paneState.getName())) {
                pane.setPos(paneState.getPosX(), paneState.getPosY());
                pane.setExpanded(paneState.isExpanded());
            }
        }
    }

    public List<PaneState> getPaneStates() {
        final List<PaneState> paneStates = new ArrayList<>();
        if (uiScreen == null) {
            return null;
        }
        uiScreen.getCategoryPanes().forEach(pane -> paneStates.add(new PaneState(pane.getCategory().getName(), pane.getPosX(), pane.getPosY(), pane.isExpanded())));
        return paneStates;
    }

    public enum Modes implements INameable {
        Sulfur("Sulfur"), Material("Material"), punjab("punjab");

        private final String name;

        Modes(String name) {
            this.name = name;
        }

        @Override
        public String getDisplayName() {
            return name;
        }
    }

}