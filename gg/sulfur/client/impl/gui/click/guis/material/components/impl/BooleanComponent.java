package gg.sulfur.client.impl.gui.click.guis.material.components.impl;

import gg.sulfur.client.Sulfur;
import gg.sulfur.client.api.font.CustomFontRenderer;
import gg.sulfur.client.api.property.impl.BooleanValue;
import gg.sulfur.client.impl.gui.click.guis.material.components.Component;
import gg.sulfur.client.impl.utils.MouseUtil;
import gg.sulfur.client.impl.utils.render.RenderUtils;


import java.awt.*;

public class BooleanComponent extends Component {

    private BooleanValue booleanValue;
    final CustomFontRenderer font = Sulfur.getInstance().getFontManager().getFont("Large").getRenderer();
    final CustomFontRenderer font2 = Sulfur.getInstance().getFontManager().getFont("Small").getRenderer();

    public BooleanComponent(BooleanValue booleanValue, float posX, float posY, float offsetX, float offsetY, float width, float height) {
        super(booleanValue.getName(), posX, posY, offsetX, offsetY, width, height);
        this.booleanValue = booleanValue;
    }

    @Override
    public void componentMoved(float movedX, float movedY) {
        super.componentMoved(movedX, movedY);
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        font.drawStringWithShadow(getLabel(), getPosX(), getPosY() + 3, new Color(229, 229, 223, 255).getRGB());
        RenderUtils.drawOutlinedRoundedRect(getPosX() + getWidth() - 15,getPosY(),10,10,3,1,new Color(0xff689FFF).getRGB());
        if (booleanValue.getValue()) {
            RenderUtils.drawRoundedRect(getPosX() + getWidth() - 15,getPosY(),10,10,3,new Color(0xff689FFF).getRGB());
            RenderUtils.drawCheckMark(getPosX() + getWidth() - 8,getPosY() - 1,8, new Color(229, 229, 223, 255).getRGB());
        }
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int button) {
        super.onMouseClicked(mouseX, mouseY, button);
        if (button == 0 && MouseUtil.mouseWithinBounds(mouseX,mouseY,getPosX() + getWidth() - 15,getPosY(),10,10)) {
            booleanValue.setValue(!booleanValue.getValue());
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int button) {
        super.onMouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onKeyTyped(char character, int keyCode) {
        super.onKeyTyped(character, keyCode);
    }

    public BooleanValue getBooleanValue() {
        return booleanValue;
    }
}
