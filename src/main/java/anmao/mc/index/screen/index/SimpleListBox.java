package anmao.mc.index.screen.index;

import anmao.dev.core.color.ColorScheme;
import anmao.dev.core.debug.DeBug;
import anmao.dev.core.math._Math;
import anmao.mc.amlib.amlib.color.ColorSchemes;
import anmao.mc.amlib.screen.widget.DT_ListBoxData;
import anmao.mc.amlib.screen.widget.simple.SimpleWidgetCore;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Optional;

public class SimpleListBox extends SimpleWidgetCore<SimpleListBox> {
    protected List<DT_ListBoxData> data;
    protected int
            dataSize,
            line ,
            row,
            index,
            startIndex,
            elementalWidth,
            elementalHeight,
            widthSpace,
            heightSpace,
            strX = 1,
            strY,
            elementalBorderUsualColor,
            elementalBorderHoverColor,
            elementalTextUsualColor,
            elementalTextHoverColor,
            elementalBackgroundUsualColor,
            elementalBackgroundHoverColor;

    public SimpleListBox(int x, int y, int w, int h,int elementalWidth, int elementalHeight, Component pMessage, List<DT_ListBoxData> data) {
        super(x, y, w, h, pMessage);
        this.data = data;
        this.dataSize = this.data.size();
        this.elementalWidth = elementalWidth;
        this.elementalHeight = elementalHeight;
        this.widthSpace = 4;
        this.heightSpace = 4;
        this.index = -1;
        this.startIndex = 0;
        setStrY();
        resetAutoSpace();
        //setColorScheme(ColorSchemes.getGlobal());
    }

    @Override
    public SimpleListBox setColorScheme(ColorScheme colorScheme) {
        super.setColorScheme(colorScheme);
        ColorScheme.Color color = ColorSchemes.getGlobal().getColor("element_border");
        this.elementalBorderHoverColor = color.HoverColor();
        this.elementalBorderUsualColor = color.UsualColor();
        color = ColorSchemes.getGlobal().getColor("element_text");
        this.elementalTextHoverColor = color.HoverColor();
        this.elementalTextUsualColor = color.UsualColor();
        color = ColorSchemes.getGlobal().getColor("element_background");
        this.elementalBackgroundHoverColor = color.HoverColor();
        this.elementalBackgroundUsualColor = color.UsualColor();
        return self();
    }

    public SimpleListBox setElementalTextHoverColor(int elementalTextHoverColor) {
        this.elementalTextHoverColor = elementalTextHoverColor;
        return self();
    }
    public int getElementalTextHoverColor() {
        return elementalTextHoverColor;
    }
    public SimpleListBox setElementalTextUsualColor(int elementalTextUsualColor) {
        this.elementalTextUsualColor = elementalTextUsualColor;
        return self();
    }
    public int getElementalTextUsualColor() {
        return elementalTextUsualColor;
    }
    public SimpleListBox setElementalBorderHoverColor(int elementalBorderHoverColor) {
        this.elementalBorderHoverColor = elementalBorderHoverColor;
        return self();
    }
    public int getElementalBorderHoverColor() {
        return elementalBorderHoverColor;
    }
    public SimpleListBox setElementalBorderUsualColor(int elementalBorderUsualColor) {
        this.elementalBorderUsualColor = elementalBorderUsualColor;
        return self();
    }
    public int getElementalBorderUsualColor() {
        return elementalBorderUsualColor;
    }
    public SimpleListBox setElementalBackgroundHoverColor(int elementalBackgroundHoverColor) {
        this.elementalBackgroundHoverColor = elementalBackgroundHoverColor;
        return self();
    }
    public int getElementalBackgroundHoverColor() {
        return elementalBackgroundHoverColor;
    }
    public SimpleListBox setElementalBackgroundUsualColor(int elementalBackgroundUsualColor) {
        this.elementalBackgroundUsualColor = elementalBackgroundUsualColor;
        return self();
    }
    public int getElementalBackgroundUsualColor() {
        return elementalBackgroundUsualColor;
    }

    public SimpleListBox setData(List<DT_ListBoxData> data) {
        this.data = data;
        this.dataSize = this.data.size();
        this.index = -1;
        this.startIndex = 0;
        return self();
    }

    @Override
    public SimpleListBox setFont(Font font) {
        super.setFont(font);
        setStrY();
        return self();
    }
    public void resetAutoSpace(){
        //计算宽度
        int i = getElementalWidth() + getWidthSpace() * 2;
        this.row = getContentW() /i ;
        int space = (getContentW() - row * i) / row;
        this.widthSpace += _Math.half1(space);
        //计算高度
        i  = getElementalHeight() + getHeightSpace() * 2;
        this.line = getContentH()/i;
        space = (getContentH() - line * i) / line;
        this.heightSpace += _Math.half1(space);
    }
    public void setStrY() {
        this.strY = _Math.half1(getElementalContentHeight() /font.lineHeight) + 1;
    }

    public int getStrY() {
        return strY;
    }

    public void setElementalHeight(int elementalHeight) {
        this.elementalHeight = elementalHeight;
        resetAutoSpace();
    }
    public int getElementalHeight() {
        return elementalHeight;
    }
    public void setElementalWidth(int elementalWidth) {
        this.elementalWidth = elementalWidth;
        resetAutoSpace();
    }
    public int getElementalWidth() {
        return elementalWidth;
    }
    public int getWidthSpace() {
        return widthSpace;
    }
    public void setWidthSpace(int widthSpace) {
        this.widthSpace = widthSpace;
        resetAutoSpace();
    }
    public int getHeightSpace() {
        return heightSpace;
    }
    public void setHeightSpace(int heightSpace) {
        this.heightSpace = heightSpace;
        resetAutoSpace();
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setLine(int line) {
        this.line = line;
    }


    @Override
    protected void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int idex = -1;
        for (int i = 0; i < line; i++) {
            int elemY = getContentY() + getHeightSpace() + i * ( getElementalHeight() + getHeightSpace() + getHeightSpace());
            for (int r = 0; r < row; r++) {
                if (startIndex < dataSize) {
                    int elemIndex = startIndex + i * row + r;
                    if (elemIndex < dataSize) {
                        int elemX = getContentX() + getWidthSpace() + r * (getElementalWidth() +getWidthSpace()+ getWidthSpace());
                        int borderColor = getElementalBorderUsualColor();
                        int backgroundColor = getElementalBackgroundUsualColor();
                        int txtColor = getElementalTextUsualColor();
                        if (
                                mouseX > elemX
                                && mouseX < elemX + elementalWidth
                                && mouseY > elemY
                                && mouseY < elemY + elementalHeight
                        ) {
                            borderColor = getElementalBorderHoverColor();
                            backgroundColor = getElementalBackgroundHoverColor();
                            txtColor = getElementalTextHoverColor();
                            idex = elemIndex;
                            guiGraphics.renderTooltip(font, getData(elemIndex).getTooltip(), Optional.empty(), mouseX, mouseY);
                        }
                        PoseStack poseStack = guiGraphics.pose();
                        poseStack.pushPose();
                        renderShape(poseStack, elemX, elemY, elementalWidth, elementalHeight,getRadius(), borderColor, backgroundColor);
                        poseStack.popPose();
                        drawString(guiGraphics, elemX+getRadius()+ strX, elemY +getRadius() + getStrY(), txtColor, FixStrWidth(getDataComponent(elemIndex)));
                    }
                } else {
                    break;
                }
            }
        }
        index = idex;
    }
    public DT_ListBoxData getData(int index){
        if (index < this.data.size()){
            return this.data.get(index);
        }
        DeBug.ThrowError("error index");
        return null;
    }
    public Component getDataComponent(int index){
        DT_ListBoxData d = getData(index);
        if (d != null){
            return d.getComponent();
        }
        DeBug.ThrowError("error data");
        return Component.literal("Error :: Null");
    }
    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double sx,double sy) {
        if (sy < 0 && startIndex < dataSize - row){
            startIndex = startIndex + row;
        }else if (startIndex >= row){
            startIndex = startIndex - row;
        }
        index = -1;
        return super.mouseScrolled(pMouseX, pMouseY, sx,sy);
    }
    @Override
    public void onClick(double pMouseX, double pMouseY) {
        if (isMouseOver(pMouseX,pMouseY) && index >= 0){
            DT_ListBoxData d = getData(index);
            if (d != null){
                d.OnPress(d.getValue());
            }
        }
    }

    public String FixStrWidth(String s){
        return font.plainSubstrByWidth(s,getElementalContentWidth());
    }
    public String FixStrWidth(Component s){
        return FixStrWidth(s.getString());
    }
    public int getElementalContentWidth() {
        return getElementalWidth() - 2 * getRadius();
    }
    public int getElementalContentHeight() {
        return getElementalHeight() - 2 * getRadius();
    }
}
