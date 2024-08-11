package anmao.mc.index.screen.index;

import anmao.mc.amlib.screen.widget.simple.SimpleWidgetCore;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class SimpleSlot extends SimpleWidgetCore<SimpleSlot> {
    protected int slotWidth,slotHeight;
    protected SimpleSlot(int x, int y, int w, int h, Component pMessage) {
        super(x, y, w, h, pMessage);
        setLayerZ(1);
        setSlotHeight(16);
        setSlotWidth(16);
    }


    public void setSlotWidth(int slotWidth) {
        this.slotWidth = slotWidth;
    }

    public void setSlotHeight(int slotHeight) {
        this.slotHeight = slotHeight;
    }
    @Override
    protected void renderContent(GuiGraphics guiGraphics, int i, int i1, float v) {

    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pScrollX, double pScrollY) {
        return false;
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        return false;
    }
}
