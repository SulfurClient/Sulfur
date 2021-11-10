package gg.sulfur.client.impl.gui.click.guis.material.components.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.module.Module;
import gg.sulfur.client.api.property.Value;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.api.property.impl.ColorValue;
import gg.sulfur.client.api.property.impl.EnumValue;
import gg.sulfur.client.api.property.impl.NumberValue;
import gg.sulfur.client.impl.gui.click.guis.material.components.Component;
import gg.sulfur.client.impl.utils.MouseUtil;
import gg.sulfur.client.impl.utils.render.RenderUtils;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class ModuleComponent extends gg.sulfur.client.impl.gui.click.guis.material.components.Component {
    private Module module;
    private ArrayList<gg.sulfur.client.impl.gui.click.guis.material.components.Component> components = new ArrayList<>();
    private boolean extended;
    private CategoryComponent categoryComponent;
    private int scrollY;
    final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();
    final CustomFontRenderer font2 = Sulfur.getInstance().getFontManager().getFont("Small").getRenderer();

    public ModuleComponent(CategoryComponent categoryComponent, Module module, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(module.getModuleData().getName(), posX, posY, offsetX, offsetY, width, height);
        this.module = module;
        this.categoryComponent = categoryComponent;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        if (!module.getValues().isEmpty()) {
            float valueOffsetY = 0;
            for (Value value : module.getValues()) {
                if (value instanceof BooleanValue) {
                    components.add(new BooleanComponent((BooleanValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
                if (value instanceof EnumValue) {
                    components.add(new EnumComponent((EnumValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
                if (value instanceof NumberValue) {
                    components.add(new NumberComponent((NumberValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
                if (value instanceof ColorValue) {
                    components.add(new ColorComponent((ColorValue) value, getCategoryComponent().getPosX(), getCategoryComponent().getPosY(), 105, valueOffsetY, 165, 20));
                    valueOffsetY += 20;
                }
            }
        }
        components.forEach(gg.sulfur.client.impl.gui.click.guis.material.components.Component::initializeComponent);
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
        if (isExtended()) {
            for (gg.sulfur.client.impl.gui.click.guis.material.components.Component component : getComponents()) {
                component.componentMoved(getPosX(), getCategoryComponent().getPosY());
            }
        }
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        double scrollbarHeight = (getCategoryComponent().getHeight() / getComponentHeight()) * getCategoryComponent().getHeight();
        if (MouseUtil.mouseWithinBounds(mouseX, mouseY, getCategoryComponent().getPosX() + 100, getCategoryComponent().getPosY(), 175, getCategoryComponent().getHeight()) && getComponentHeight() >= getCategoryComponent().getHeight()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                if (getScrollY() - 6 < -(getComponentHeight() - getCategoryComponent().getHeight()))
                    setScrollY((int) -(getComponentHeight() - getCategoryComponent().getHeight()));
                else setScrollY(getScrollY() - 6);
            } else if (wheel > 0) {
                setScrollY(getScrollY() + 6);
            }
        }
        if (getScrollY() > 0) setScrollY(0);
        if (getComponentHeight() >= getCategoryComponent().getHeight()) {
            if (getScrollY() - 6 < -(getComponentHeight() - getCategoryComponent().getHeight()))
                setScrollY((int) -(getComponentHeight() - getCategoryComponent().getHeight()));
        } else if (getScrollY() < 0) setScrollY(0);
        font.drawStringWithShadow(getLabel(), getPosX(), getPosY(), module.isToggled() ? new Color(229, 229, 223, 255).getRGB() : new Color(167, 167, 161, 255).getRGB());
        if (!getComponents().isEmpty())
            font.drawStringWithShadow("...", getPosX() + getWidth() - font.getWidth("..."), getPosY() - 2, module.isToggled() ? new Color(229, 229, 223, 255).getRGB() : new Color(167, 167, 161, 255).getRGB());
        if (isExtended()) {
            for (gg.sulfur.client.impl.gui.click.guis.material.components.Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onDrawScreen(mouseX, mouseY, partialTicks);
                component.setOffsetY(component.getOriginalOffsetY() + getScrollY());
                component.componentMoved(getPosX(), getCategoryComponent().getPosY());
            }
            if (getComponentHeight() >= getCategoryComponent().getHeight()) {
                RenderUtils.drawRect(getCategoryComponent().getPosX() + 275, getCategoryComponent().getPosY() - 6, 2, getCategoryComponent().getHeight() + 6, new Color(55, 55, 55, 255).getRGB());
                RenderUtils.drawRect(getCategoryComponent().getPosX() + 275, getCategoryComponent().getPosY() - 6 - (((getCategoryComponent().getHeight() - (scrollbarHeight - 4)) / (getComponentHeight() - (getCategoryComponent().getHeight()))) * getScrollY()), 2, scrollbarHeight, new Color(40, 40, 40, 255).getRGB());
            }
            setupHeight();
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY() - 4, getWidth(), getHeight() - 8)) {
            getModule().toggle();
        }
        if (button == 1 && !getComponents().isEmpty() && MouseUtil.mouseWithinBounds(mouseX, mouseY, getPosX(), getPosY() - 4, getWidth(), getHeight() - 8)) {
            getCategoryComponent().getComponents().stream().filter(component -> component instanceof ModuleComponent && component != this).forEach(component -> ((ModuleComponent) component).setExtended(false));
            setExtended(!isExtended());
        }
        if (isExtended()) {
            for (gg.sulfur.client.impl.gui.click.guis.material.components.Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onMouseClicked(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
        if (isExtended()) {
            for (gg.sulfur.client.impl.gui.click.guis.material.components.Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onMouseReleased(mouseX, mouseY, button);
            }
        }
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
        if (isExtended()) {
            for (gg.sulfur.client.impl.gui.click.guis.material.components.Component component : getComponents()) {
                if (component.isHidden()) continue;
                component.onKeyTyped(character, keyCode);
            }
        }
    }

    public Module getModule() {
        return module;
    }

    public ArrayList<gg.sulfur.client.impl.gui.click.guis.material.components.Component> getComponents() {
        return components;
    }

    public boolean isExtended() {
        return extended;
    }

    public void setExtended(boolean extended) {
        this.extended = extended;
    }

    public CategoryComponent getCategoryComponent() {
        return categoryComponent;
    }

    public int getComponentHeight() {
        int h = 0;
        for (gg.sulfur.client.impl.gui.click.guis.material.components.Component component : getComponents()) {
            if (component.isHidden()) continue;
            h += component.getHeight();
        }
        return h;
    }

    public void setupHeight() {
        int h = getScrollY();
        for (Component component : getComponents()) {
            if (component instanceof BooleanComponent) {
                final BooleanComponent booleanComponent = (BooleanComponent) component;
                if (booleanComponent.getBooleanValue().getParent() != null && !booleanComponent.getBooleanValue().getParent().getValueAsString().equalsIgnoreCase(booleanComponent.getBooleanValue().getParent().getValueAsString())) {
                    booleanComponent.setHidden(true);
                    continue;
                }
                booleanComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
            if (component instanceof NumberComponent) {
                final NumberComponent numberComponent = (NumberComponent) component;
                if (numberComponent.getNumberValue().getParent() != null && !numberComponent.getNumberValue().getParent().getValueAsString().equalsIgnoreCase(numberComponent.getNumberValue().getParent().getValueAsString())) {
                    numberComponent.setHidden(true);
                    continue;
                }
                numberComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
            if (component instanceof ColorComponent) {
                final ColorComponent colorComponent = (ColorComponent) component;
                if (colorComponent.getColorValue().getParent() != null && !colorComponent.getColorValue().getParent().getValueAsString().equalsIgnoreCase(colorComponent.getColorValue().getParent().getValueAsString())) {
                    colorComponent.setHidden(true);
                    continue;
                }
                colorComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
            if (component instanceof EnumComponent) {
                final EnumComponent enumComponent = (EnumComponent) component;
                if (enumComponent.getEnumValue().getParent() != null && !enumComponent.getEnumValue().getParent().getValueAsString().equalsIgnoreCase(enumComponent.getEnumValue().getParent().getValueAsString())) {
                    enumComponent.setHidden(true);
                    continue;
                }
                enumComponent.setHidden(false);
                component.setOffsetY(h);
                h += component.getHeight();
            }
        }
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }
}
