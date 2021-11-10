package gg.sulfur.client.impl.command;

import gg.sulfur.client.api.command.Command;
import gg.sulfur.client.api.command.CommandData;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.api.property.impl.CustomStringValue;
import gg.sulfur.client.impl.managers.ValueManager;
import gg.sulfur.client.impl.utils.player.ChatUtil;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.api.property.impl.StringValue;

public class ModuleCommandBase extends Command {
    private final Module module;

    public ModuleCommandBase(CommandData data, Module module) {
        super(data);
        this.module = module;
    }

    @Override
    public void run(String command, String... args) {
        ValueManager valueManager = Sulfur.getInstance().getValueManager();
        if (command.equalsIgnoreCase(this.getData().getName())) {
            for (Value<?> value : valueManager.getObjects()) {
                if (value.getName().equalsIgnoreCase(args[1])) {
                    String text = buildStringFromArray(args);
                    if (!text.isEmpty()) {
                        if (value instanceof CustomStringValue) {
                            CustomStringValue customStringValue = (CustomStringValue) value;
                            customStringValue.setValueAutoSave(text);
                        } else if (value instanceof EnumValue<?>) {
                            EnumValue<?> enumValue = (EnumValue<?>) value;
                            enumValue.setValueAutoSave(text);
                        } else if (value instanceof StringValue) {
                            StringValue stringValue = (StringValue) value;
                            stringValue.setValueAutoSave(text);
                        } else if (value instanceof NumberValue) {
                            NumberValue numberValue = (NumberValue) value;
                            double parsed = Double.parseDouble(text);
                            numberValue.setValueAutoSave(parsed);
                        }
                    } else {
                        ChatUtil.displayChatMessage("Not enough arguments.");
                        return;
                    }
                }
                return;
            }
            ChatUtil.displayChatMessage("nice brain.");
        }
    }

    private String buildStringFromArray(Object[] array) {
        if (2 > array.length) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 2; i < array.length; ++i) {
            stringBuilder.append(array[i]);
        }
        return stringBuilder.toString();
    }

    public Module getModule() {
        return module;
    }
}
