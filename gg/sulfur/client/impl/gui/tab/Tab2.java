package gg.sulfur.client.impl.gui.tab;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.events.KeyboardEvent;
import gg.sulfur.client.impl.utils.combat.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import gg.sulfur.client.impl.modules.render.Hud;

import java.awt.*;
import java.util.List;

/**
 * @author Auth
 * @author Intent (friend helped me and made me fucking use intent without me knowing)
 */

public class Tab2 {
    /*/final FontRenderer font1 = Minecraft.getMinecraft().fontRendererObj;
    final Minecraft mc = Minecraft.getMinecraft();

    private int tab, index, settingIndex;

    private Value selectedValue;

    public static boolean expanded;

    public boolean editingValue = false;

    public Module expandedModule;

    public void render() {
        final List<Module> modules = Sulfur.getInstance().getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);
        final Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
        int offset = hud.watermark.getValue() ? -5 : -20;
        final BooleanValue tabGui = hud.tabGui;

        if (!tabGui.getValue()) return;

        //if (expandedModule != null)
        //    selectedValue = Client.INSTANCE.getValueManager().getValuesFromOwner(expandedModule).get(settingIndex);

        //System.out.println(settingIndex);
        Gui.drawRect(2, offset + 23, 70, offset + 6 + ModuleCategory.values().length * 16, new Color(0, 0, 0, 192).getRGB());
        Gui.drawRect(2, offset + 23 + tab * 16, 70, offset + 28 + tab * 16 + 10, new Color(99, 133, 255).getRGB());

        int y = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            if (category.equals(ModuleCategory.HIDDEN))
                continue;

            font1.drawStringWithShadow(category.getName(), 6, offset + 26 + y * 16, -1);
            font1.drawStringWithShadow(expanded && category == ModuleCategory.values()[tab] ? "+" : "-", 62, offset + 26 + y * 16, -1);

            y++;
        }

        // BELOW IS MODULES

        if (expanded) {
            Gui.drawRect(72, offset + 23, 162, offset + 6 + (modules.size() + 1) * 16, new Color(0, 0, 0, 192).getRGB());
            Gui.drawRect(72, offset + 23 + index * 16, 162, offset + 28 + index * 16 + 10, new Color(99, 133, 255).getRGB());

            y = 0;
            for (Module module : modules) {
                font1.drawStringWithShadow(module.getModuleData().getName(), 75, 26 + offset + y * 16, module.isToggled() ? -1 : Color.LIGHT_GRAY.getRGB());

                y++;
            }
        }

        if (expandedModule != null) {
            Gui.drawRect(164, offset + 23, 262, offset + 6 + (Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule).size() + 1) * 16, new Color(0, 0, 0, 192).getRGB());
            Gui.drawRect(164, offset + 23 + settingIndex * 16, 262, offset + 28 + settingIndex * 16 + 10, new Color(99, 133, 255).getRGB());
            y = 0;
            for (Value value : Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule)) {
                if (value instanceof BooleanValue) {
                    font1.drawStringWithShadow(value.getName(), 166, 26 + offset + y * 16, ((boolean) value.getValue() ? -1 : Color.GRAY.getRGB()));
                } else {
                    font1.drawStringWithShadow(value.getName(), 166, 26 + offset + y * 16, -1);
                }
                y++;
            }
        } else {
            settingIndex = 0;
        }

        if (selectedValue != null) {
            if (!editingValue) Gui.drawRect(264, offset + 23, 362, offset + 6 + offset + 23 + 15, new Color(0, 0, 0, 192).getRGB());
            if (editingValue) Gui.drawRect(264, offset + 23, 362, offset + 23 + 15, new Color(99, 133, 255).getRGB());


            //font1.drawStringWithShadow(selectedValue.getName(), 166, 26 + offset, -1);
            if (selectedValue instanceof NumberValue)
                font1.drawStringWithShadow(String.valueOf(MathUtils.round((Double)selectedValue.getValue(), 3)), 266, 26 + offset, -1);
            else
                font1.drawStringWithShadow(selectedValue.getValue().toString(), 266, 26 + offset, -1);
        }
    }

    public void updateKeys(KeyboardEvent event) {
        final int keyCode = event.getKey();

        final Hud hud = Sulfur.getInstance().getModuleManager().get(Hud.class);
        final BooleanValue tabGui = hud.tabGui;

        final List<Module> modules = Sulfur.getInstance().getModuleManager().getAllInCategory(ModuleCategory.values()[tab]);

        if (!tabGui.getValue())
            return;

        switch (keyCode) {
            case Keyboard.KEY_UP:
                if (expandedModule != null) {
                    if (settingIndex <= 0) {
                        settingIndex = Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule).size() - 1;
                    } else {
                        settingIndex--;
                    }
                    return;
                }
                if (expanded) {
                    if (index <= 0) {
                        index = modules.size() - 1;
                    } else {
                        index--;
                    }
                } else {
                    if (tab <= 0) {
                        tab = ModuleCategory.values().length - 2;
                    } else {
                        tab--;
                    }
                }
                break;

            case Keyboard.KEY_DOWN:
                if (expandedModule != null) {
                    if (settingIndex >= Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule).size() - 1) {
                        settingIndex = 0;
                    } else {
                        settingIndex++;
                    }
                    return;
                }
                if (expanded) {
                    if (index >= modules.size() - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                } else {
                    if (tab >= ModuleCategory.values().length - 2) {
                        tab = 0;
                    } else {
                        tab++;
                    }
                }
                break;

            case Keyboard.KEY_RIGHT:
                if (selectedValue != null && editingValue) {
                    if (selectedValue instanceof NumberValue) {
                        if (selectedValue.getValue() instanceof Double) {
                            selectedValue.setValue((Double)selectedValue.getValue() + 0.1);
                        }
                    }
                    else if (selectedValue instanceof BooleanValue) {
                        if (((BooleanValue) selectedValue).getValue() != null) {
                            selectedValue.setValue(!(Boolean)selectedValue.getValue());
                        }
                    }
                    else if (selectedValue instanceof EnumValue) {
                        EnumValue<INameable> enumValue = (EnumValue<INameable>) selectedValue;
                        List<INameable> modes = enumValue.getValues();
                        int index = modes.indexOf(enumValue.getValue());
                        enumValue.setValueAutoSave(index >= 1 ? modes.get(index - 1) : modes.get(modes.size() - 1));
                    }
                    return;
                }
                if (expandedModule != null) {
                    selectedValue = Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule).get(settingIndex);
                    return;
                }
                if (expanded) {
                    expandedModule = modules.get(index);
                } else {
                    index = 0;
                    expanded = true;
                    settingIndex = 0;
                }
                break;

            case Keyboard.KEY_RETURN:
                if (expandedModule != null && selectedValue == null) {
                    if (Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule).get(settingIndex) instanceof BooleanValue) {
                        Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule).get(settingIndex).setValue(
                                !(Boolean) Sulfur.getInstance().getValueManager().getValuesFromOwner(expandedModule).get(settingIndex).getValue()
                        );
                    }
                    return;
                }
                if (selectedValue != null) {
                    editingValue = !editingValue;
                    return;
                }
                if (expanded) {
                    modules.get(index).toggle();
                } else {
                    index = 0;
                    expanded = true;
                }
                break;


            case Keyboard.KEY_LEFT:
                if (editingValue) {
                    if (selectedValue instanceof NumberValue) {
                        if (selectedValue.getValue() instanceof Double) {
                            selectedValue.setValue((Double) selectedValue.getValue() - 0.1);
                        }
                    } else if (selectedValue instanceof BooleanValue) {
                        if (((BooleanValue) selectedValue).getValue() != null) {
                            selectedValue.setValue(!(Boolean) selectedValue.getValue());
                        }
                    } else if (selectedValue instanceof EnumValue) {
                        EnumValue<INameable> enumValue = (EnumValue<INameable>) selectedValue;
                        List<INameable> modes = enumValue.getValues();
                        int index = modes.indexOf(enumValue.getValue());
                        enumValue.setValueAutoSave(index <= modes.size() - 2 ? modes.get(index + 1) : modes.get(0));
                    }
                    return;
                }
                if (selectedValue != null) {
                    selectedValue = null;
                    return;
                }
                if (expandedModule != null) {
                    expandedModule = null;
                    return;
                }
                expanded = false;
                settingIndex = 0;
                break;
        }
    }/*/
}