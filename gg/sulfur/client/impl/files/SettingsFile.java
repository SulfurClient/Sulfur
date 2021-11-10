package gg.sulfur.client.impl.files;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.file.FileManager;
import gg.sulfur.client.api.file.MFile;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.api.property.impl.*;
import gg.sulfur.client.api.property.impl.interfaces.INameable;
import gg.sulfur.client.impl.commands.SearchEngineCommand;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.util.List;

public class SettingsFile extends MFile {

    public SettingsFile() {
        super("module-settings.json");
    }

    @Override
    public void load(FileManager fileManager) {
        BufferedReader fileContents;
        try {
            fileContents = fileManager.getBufferedReaderForFile(this);
        } catch (FileNotFoundException e) {
            fileManager.initializeFile(this);
            return;
        }
        try {
            JsonObject json = new JsonParser().parse(fileContents).getAsJsonObject();
            try {
                if (SearchEngineCommand.key.isEmpty()) {
                    SearchEngineCommand.key = json.get("Yanchop-Key").getAsString();
                }
            } catch (Exception ignored) {

            }
            for (Module module : Sulfur.getInstance().getModuleManager().getObjects()) {
                List<Value> valueList = Sulfur.getInstance().getValueManager().getValuesFromOwner(module);
                for (Value value : valueList) {
                    JsonObject jsonObject = json.getAsJsonObject(module.getModuleData().getName());
                    if (value instanceof StringValue) {
                        StringValue stringValue = (StringValue) value;
                        stringValue.setValue(jsonObject.getAsJsonPrimitive(value.getName()).getAsString());
                    } else if (value instanceof EnumValue) {
                        EnumValue enumValue = (EnumValue) value;
                        enumValue.setValue(jsonObject.getAsJsonPrimitive(value.getName()).getAsString());
                    } else if (value instanceof BooleanValue) {
                        BooleanValue booleanValue = (BooleanValue) value;
                        booleanValue.setValue(jsonObject.getAsJsonPrimitive(value.getName()).getAsBoolean());
                    } else if (value instanceof NumberValue) {
                        NumberValue numberValue = (NumberValue) value;
                        numberValue.setValue(jsonObject.getAsJsonPrimitive(value.getName()).getAsDouble());
                    } else if (value instanceof ColorValue) {
                        ColorValue numberValue = (ColorValue) value;
                        //numberValue.setValue(jsonObject.getAsJsonPrimitive(value.getName()).getAsInt());
                        int r,g,b;
                        r = jsonObject.getAsJsonPrimitive(value.getName() + "-r").getAsInt();
                        g = jsonObject.getAsJsonPrimitive(value.getName() + "-g").getAsInt();
                        b = jsonObject.getAsJsonPrimitive(value.getName() + "-b").getAsInt();
                        numberValue.setValue(new Color(r,g,b));
                    }
                }
            }
        } catch (Exception ignored) {

        }
    }

    @Override
    public void save() {
        final FileManager fileManager = Sulfur.getInstance().getFileManager();
        final JsonObject json = new JsonObject();
        if (SearchEngineCommand.key.length() == 0) {
            json.addProperty("Yanchop-Key", "");
        } else {
            json.addProperty("Yanchop-Key", SearchEngineCommand.key);
        }
        for (Module module : Sulfur.getInstance().getModuleManager().getObjects()) {
            List<Value> valueList = Sulfur.getInstance().getValueManager().getValuesFromOwner(module);
            JsonObject jsonObject = new JsonObject();
            for (Value value : valueList) {
                if (value instanceof StringValue) {
                    StringValue stringValue = (StringValue) value;
                    jsonObject.addProperty(value.getName(), stringValue.getValue());
                } else if (value instanceof EnumValue) {
                    EnumValue<? extends INameable> enumValue = (EnumValue<? extends INameable>) value;
                    jsonObject.addProperty(value.getName(), enumValue.getValue().name());
                } else if (value instanceof BooleanValue) {
                    BooleanValue booleanValue = (BooleanValue) value;
                    jsonObject.addProperty(value.getName(), booleanValue.getValue());
                } else if (value instanceof NumberValue) {
                    NumberValue numberValue = (NumberValue) value;
                    jsonObject.addProperty(value.getName(), numberValue.isInteger() ? numberValue.getValue().intValue() : numberValue.getValue().floatValue());
                } else if (value instanceof ColorValue) {
                    ColorValue colorValue = (ColorValue) value;
                    jsonObject.addProperty(value.getName() + "-r", colorValue.getValue().getRed());
                    jsonObject.addProperty(value.getName() + "-g", colorValue.getValue().getGreen());
                    jsonObject.addProperty(value.getName() + "-b", colorValue.getValue().getBlue());
                }
            }
            json.add(module.getModuleData().getName(), jsonObject);
        }
        try {
            fileManager.writeFile(this, json.toString());
        } catch (Exception ignored) {

        }
    }
}
