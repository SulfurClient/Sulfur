package gg.sulfur.client.impl.files;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.file.FileManager;
import gg.sulfur.client.api.file.MFile;
import gg.sulfur.client.impl.gui.click.PaneState;
import gg.sulfur.client.impl.modules.render.ClickGUI;

import java.io.BufferedReader;

public class GuiStateFile extends MFile {

    public GuiStateFile() {
        super("GuiState.json");
    }

    private final Gson gson = new Gson();
    final ClickGUI clickGUI = Sulfur.getInstance().getModuleManager().get(ClickGUI.class);

    @Override
    public void load(FileManager fileManager) {
        System.out.println("Loaded.");
        BufferedReader fileContents;
        try {
            fileContents = fileManager.getBufferedReaderForFile(this);
        } catch (Exception e) {
            fileManager.initializeFile(this);
            return;
        }
        try {
            JsonArray jsonArray = gson.fromJson(fileContents, JsonArray.class);
            if (jsonArray == null) {
                return;
            }
            for (JsonElement jsonElement : jsonArray) {
                clickGUI.addPreloadPaneState(gson.fromJson(jsonElement, PaneState.class));
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void save() {
        final FileManager fileManager = Sulfur.getInstance().getFileManager();
        final JsonArray json = new JsonArray();

        for (PaneState state : clickGUI.getPaneStates()) {
            json.add(gson.toJsonTree(state));
        }

        try {
            fileManager.writeFile(this, json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
