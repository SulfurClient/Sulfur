package gg.sulfur.client.impl.modules.render.hud;

import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.ModuleData;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.impl.events.RenderHUDEvent;
import gg.sulfur.client.impl.utils.render.ColorCreator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.impl.modules.render.Hud;
import gg.sulfur.client.impl.utils.render.RenderUtils;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CleanTheme extends Theme {

    List<String> hiddenModules = Arrays.asList(
            "Auto Register",
            "Commands",
            "HUD",
            "InvMove",
            "NameProt",
            "No Fall",
            "Target Strafe",
            "Auto Pot",
            "MCF",
            "Sprint",
            "Velocity",
            "Camera",
            "Rotations"
    );

    public CleanTheme(Module module) {
        super(module);
    }

    @Override
    public void render(RenderHUDEvent event) {
        final Hud hud1 = Sulfur.getInstance().getModuleManager().get(Hud.class);

        Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);

        int y = 3;
        final ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

        for (Module module : Sulfur.getInstance().getModuleManager().getSorted(mc.fontRendererObj)) {
            Color color1;

            switch (hud.colorMode.getValue()) {
                case ASTOLFO: {
                    color1 = ColorCreator.getGradientOffset(new Color(255, 60, 234), new Color(27, 179, 255), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                    break;
                }
                case WAVE: {
                    color1 = ColorCreator.getGradientOffset(hud1.color.getValue(), new Color(RenderUtils.darker(hud1.color.getValue().getRGB(), 0.49f)), (Math.abs(((System.currentTimeMillis()) / 10)) / 100D) + y / mc.fontRendererObj.FONT_HEIGHT * 9.95);
                    break;
                }
                case RAINBOW: {
                    color1 = new Color(255, 255, 255);
                    break;
                }
                case STATIC: {
                    color1 = new Color(hud1.color.getValue().getRGB());
                    break;
                }
                default:
                    color1 = new Color(255, 255, 255);
                    break;
            }

            if (!module.isToggled()) continue;
            if (hiddenModules.contains(module.getModuleData().getName())) continue;
            if (module.getModuleData().category() == ModuleCategory.RENDER) continue;

            ModuleData moduleData = module.getModuleData();
            String name = moduleData.getName();
            String suffix = module.getSuffix() != null ? " §7" + module.getSuffix().replaceAll(" ", "") : "";
            String fullName = name + suffix;

            float xPos2 = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(fullName) - 10;

            Gui.drawRect(xPos2 + 3.5, y - 1, scaledResolution.getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y, new Color(0,0,0, 65).getRGB());
            Gui.drawRect(scaledResolution.getScaledWidth() - 1.5, y - 1, scaledResolution.getScaledWidth(), mc.fontRendererObj.FONT_HEIGHT + y, color1.getRGB());

            if (module.getSuffix() != null) {
                float pos = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(fullName) - 4;
                mc.fontRendererObj.drawStringWithShadow(fullName, pos, y, color1.getRGB());
            } else {
                float pos = scaledResolution.getScaledWidth() - mc.fontRendererObj.getStringWidth(name) - 4;
                mc.fontRendererObj.drawStringWithShadow(name, pos, y, color1.getRGB());
            }
            y += 10.5;
        }
    }
}
