package gg.sulfur.client.impl.modules.render.hud;

/**
 * @author Kansio
 * @created 5:33 PM
 * @project Client
 */
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.modules.movement.Sprint;
import gg.sulfur.client.impl.modules.render.Hud;
import gg.sulfur.client.impl.utils.networking.ServerUtils;
import gg.sulfur.client.impl.utils.render.ColorCreator;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class SulfurTheme extends Theme {

    public SulfurTheme(Module module) {
        super(module);
    }

    @Override
    public void render(RenderHUDEvent event) {
        Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
        CustomFontRenderer fontRenderer = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();

        int y = 3;
        final ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        for (Module module : hud.font.getValue() ? Sulfur.getInstance().getModuleManager().getSorted(fontRenderer) : Sulfur.getInstance().getModuleManager().getSorted(mc.fontRendererObj)) {
            Color color1;

            switch (hud.colorMode.getValue()) {
                case ASTOLFO: {
                    color1 = ColorCreator.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                    break;
                }
                case WAVE: {
                    color1 = ColorCreator.getGradientOffset(hud.color.getValue(), new Color(RenderUtils.darker(hud.color.getValue().getRGB(), 0.49f)), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                    break;
                }
                case RAINBOW: {
                    color1 = new Color(255, 255, 255);
                    break;
                }
                case STATIC: {
                    color1 = new Color(hud.color.getValue().getRGB());
                    break;
                }
                case GRADIENT2: {
                    color1 = ColorCreator.getGradientOffset(hud.gradient1.getValue(), hud.gradient2.getValue(), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                    break;
                }
                default:
                    color1 = new Color(255, 255, 255);
                    break;
            }

            if (!module.isToggled()) continue;
            if (hud.hideRender.getValue() && module.getModuleData().category() == ModuleCategory.RENDER) continue;

            ModuleData moduleData = module.getModuleData();
            String name = moduleData.getName();
            String suffix = module.getSuffix() != null ? "§7" + module.getSuffix() : "";
            String fullName = name + suffix;


            if (!hud.font.getValue()) {
                float xPos2 = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(fullName) - 10;
                Gui.drawRect(xPos2 + 3.5, y - 2, scaledResolution.getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, new Color(0, 0, 0, 65).getRGB());
                Gui.drawRect(scaledResolution.getScaledWidth() - 1.5, y - 2, scaledResolution.getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y + 1, color1.getRGB());

                if (module.getSuffix() != null) {
                    float pos = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(fullName) - 4;
                    mc.fontRendererObj.drawStringWithShadow(fullName, pos, y, color1.getRGB());
                } else {
                    float pos = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 4;
                    mc.fontRendererObj.drawStringWithShadow(name, pos, y, color1.getRGB());
                }
            } else {
                float xPos2 = scaledResolution.getScaledWidth() - fontRenderer.getWidth(fullName) - 10;
                float x = fontRenderer.getWidth(fullName);
                Gui.drawRect(xPos2 + 3.5, y - 2, scaledResolution.getScaledWidth(),  fontRenderer.getHeight(fullName) + y + 0.5, new Color(0, 0, 0, 65).getRGB());
                Gui.drawRect(scaledResolution.getScaledWidth() - 1.5, y - 2, scaledResolution.getScaledWidth(), fontRenderer.getHeight(fullName) + y + 0.5, color1.getRGB());

                //Gui.drawRect(scaledResolution.getScaledWidth() - 7.5 - x, y - 2, scaledResolution.getScaledWidth() - x - 6.5, fontRenderer.getHeight(fullName) + y + 0.5, color1.getRGB());
                //Gui.drawRect(scaledResolution.getScaledWidth() - 7.5 - x, y + 9, scaledResolution.getScaledWidth() - x - 2.5, fontRenderer.getHeight(fullName) + y + 0.5, color1.getRGB());
                if (module.getSuffix() != null) {
                    float pos = scaledResolution.getScaledWidth() - fontRenderer.getWidth(fullName) - 4;
                    fontRenderer.drawStringWithShadow(fullName, pos, y, color1.getRGB());
                } else {
                    float pos = scaledResolution.getScaledWidth() - fontRenderer.getWidth(name) - 4;
                    fontRenderer.drawStringWithShadow(name, pos, y, color1.getRGB());
                }
            }
            y += 12.0;
        }
    }
}