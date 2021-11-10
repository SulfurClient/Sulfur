package gg.sulfur.client.impl.managers;

import gg.sulfur.client.api.font.CustomFont;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.manager.Manager;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

public class FontManager extends Manager<CustomFont> {

    public FontManager() {
        super(new ArrayList<>());
    }

    @Override
    public void onCreated() {
        try {
            add(new CustomFont("Chat", createRenderer("/Roboto-Regular.ttf", 20.0F)));
            add(new CustomFont("Small", createRenderer("/Roboto-Regular.ttf", 13.0F)));
            add(new CustomFont("Small1", createRenderer("/Roboto-Regular.ttf", 16.0F)));
            add(new CustomFont("Large", createRenderer("/Roboto-Regular.ttf", 19.0F)));
            add(new CustomFont("Extreme", createRenderer("/Roboto-Regular.ttf", 84.0F)));
            add(new CustomFont("HUD", createRenderer("/Roboto-Regular.ttf", 24.0F)));
            add(new CustomFont("Tahoma", createRenderer("/Tahoma.ttf", 28.0F)));
            add(new CustomFont("Tahoma-HUD", createRenderer("/Tahoma.ttf", 24.0F)));
            add(new CustomFont("Target", createRenderer("/Roboto-Regular.ttf", 18.0F)));
            add(new CustomFont("autism", createRenderer("/Pervitina-Dex-FFP.ttf", 24.0F)));
            add(new CustomFont("Skidma", createRenderer("/Jello.otf", 52.0F)));
            add(new CustomFont("SkidmaSmall", createRenderer("/Jello.otf", 18.0F)));
            add(new CustomFont("SkidmaArray", createRenderer("/Jello.otf", 22.0F)));
            add(new CustomFont("SmallJello", createRenderer("/Jello.otf", 16.0F)));
            add(new CustomFont("Verdana-18", createRenderer("/verdana.ttf", 18.0F)));
            add(new CustomFont("Verdana-16", createRenderer("/verdana.ttf", 16.0F)));
            add(new CustomFont("NiggerFont", createRenderer("/Dosis-Bold.ttf", 20.0F)));
            add(new CustomFont("NiggerFont2", createRenderer("/Dosis-Bold.ttf", 19.0F)));
            add(new CustomFont("icons-Temp", createRenderer("/Icon-Font.ttf", 20.0F)));

        } catch (FontFormatException | IOException ignored) {

        }
    }

    private CustomFontRenderer createRenderer(String name, float size) throws IOException, FontFormatException {
        return new CustomFontRenderer(Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream(name)))
                .deriveFont(Font.PLAIN, size), true);
    }

    public CustomFont getFont(String name) {
        for (CustomFont customFont : getObjects())
            if (customFont.getName().equalsIgnoreCase(name))
                return customFont;

        throw new NoSuchElementException("No font found by name: " + name);
    }

}
