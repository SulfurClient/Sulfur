package gg.sulfur.client.impl.gui.click.guis.material.plane.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.module.enums.ModuleCategory;
import gg.sulfur.client.impl.gui.click.guis.material.components.Component;
import gg.sulfur.client.impl.gui.click.guis.material.components.impl.CategoryComponent;
import gg.sulfur.client.impl.gui.click.guis.material.plane.Plane;
import gg.sulfur.client.impl.utils.MouseUtil;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;

public class MainPlane extends Plane {
    private ModuleCategory selectedCategory = ModuleCategory.COMBAT;
    private ArrayList<Component> components = new ArrayList<>();
    final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();
    final CustomFontRenderer font2 = Sulfur.getInstance().getFontManager().getFont("Small").getRenderer();
    final CustomFontRenderer fint = Sulfur.getInstance().getFontManager().getFont("icons-Temp").getRenderer();

    public MainPlane(String label, float posX, float posY, float width, float height) {
        super(label, posX, posY, width, height);
    }

    @Override
    public void initializePlane() {
        super.initializePlane();
        for (ModuleCategory category : ModuleCategory.values()) {
            components.add(new CategoryComponent(category,getPosX(),getPosY(),46.5f,45f,getWidth() - 46.5f,getHeight() - 45f));
        }
        components.forEach(Component::initializeComponent);
    }

    @Override
    public void planeMoved(float movedX, float movedY) {
        super.planeMoved(movedX, movedY);
        components.forEach(component -> component.componentMoved(movedX, movedY));
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        if (isDragging()) {
            setPosX(mouseX + getLastPosX());
            setPosY(mouseY + getLastPosY());
            planeMoved(getPosX(), getPosY());
        }
        if (getPosX() < 0) {
            setPosX(0);
            planeMoved(getPosX(), getPosY());
        }
        if (getPosX() + getWidth() > new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth()) {
            setPosX(new ScaledResolution(Minecraft.getMinecraft()).getScaledWidth() - getWidth());
            planeMoved(getPosX(), getPosY());
        }
        if (getPosY() < 0) {
            setPosY(0);
            planeMoved(getPosX(), getPosY());
        }
        if (getPosY() + getHeight() > new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight()) {
            setPosY(new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight() - getHeight());
            planeMoved(getPosX(), getPosY());
        }
        RenderUtils.drawRoundedRect(getPosX(), getPosY(), getWidth(), getHeight(), 3, new Color(45, 45, 45, 255).getRGB());
        RenderUtils.drawImage(new ResourceLocation("textures/client/logo.png"), getPosX() + 5.5f, getPosY() + 6, 32, 32);
        RenderUtils.drawUnfilledCircle(getPosX() + 5f, getPosY() + 5f, 33, new Color(45, 45, 45, 255).getRGB());
        float categoryOffsetY = getPosY() + 55;
        for (ModuleCategory category : ModuleCategory.values()) {
            if (getSelectedCategory() == category) {
                RenderUtils.drawRect(getPosX(), categoryOffsetY - (fint.getHeight("c") * 2.5f) / 4 - 4, 42.5f, fint.getHeight("c") * 2.5f, new Color(35, 35, 35, 255).getRGB());
                RenderUtils.drawRect(getPosX(), categoryOffsetY - (fint.getHeight("c") * 2.5f) / 4 - 4, 2, fint.getHeight("c") * 2.5f, new Color(0xff689FFF).getRGB());
            }
            fint.drawStringWithShadow(category.getChar(), getPosX() + 12, categoryOffsetY, getSelectedCategory() == category ? new Color(0xff689FFF).getRGB() : new Color(229, 229, 223, 255).getRGB());
            categoryOffsetY += fint.getHeight("c") * 2.5f;
        }
        RenderUtils.drawRect(getPosX() + 42.5, getPosY(), 100, getHeight(), new Color(35, 35, 35, 255).getRGB());
        fint.drawStringWithShadow(StringUtils.capitalize(getSelectedCategory().name().toLowerCase()) + " (" + Sulfur.getInstance().getModuleManager().getAllInCategory(getSelectedCategory()).size() + ")", getPosX() + 45, getPosY() + 24, new Color(229, 229, 223, 255).getRGB());
        RenderUtils.drawRect(getPosX() + 42.5, getPosY() + 36, 100, 1, new Color(45, 45, 45, 255).getRGB());
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onDrawScreen(mouseX, mouseY, partialTicks);
            }
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        final boolean hovered = MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY(), getWidth(), 15);
        if (button == 0) {
            if (hovered) {
                setLastPosX(getPosX() - mouseX);
                setLastPosY(getPosY() - mouseY);
                setDragging(true);
            }
            float categoryOffsetY = getPosY() + 55;
            for (ModuleCategory category : ModuleCategory.values()) {
                if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), categoryOffsetY - (fint.getHeight("c") * 2.5f) / 4 - 4, 42.5f, fint.getHeight("c") * 2.5f))
                    setSelectedCategory(category);
                categoryOffsetY += fint.getHeight("c") * 2.5f;
            }
        }
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onMouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (button == 0 && isDragging()) {
            setDragging(false);
        }
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onMouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory())
                    categoryComponent.onKeyTyped(character, keyCode);
            }
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        for (Component component : getComponents()) {
            if (component instanceof CategoryComponent) {
                final CategoryComponent categoryComponent = (CategoryComponent) component;
                if (categoryComponent.getCategory() == getSelectedCategory()) categoryComponent.onGuiClosed();
            }
        }
    }

    public ModuleCategory getSelectedCategory() {
        return selectedCategory;
    }

    public void setSelectedCategory(ModuleCategory selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public ArrayList<Component> getComponents() {
        return components;
    }
}

