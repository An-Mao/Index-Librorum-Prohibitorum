package anmao.mc.index.screen.index;

import anmao.mc.amlib.screen.widget.simple.SimpleWidgetCore;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class SimpleProgressBar extends SimpleWidgetCore<SimpleProgressBar> {
    protected int progress,maxProgress,direction;
    protected SimpleProgressBar(int x, int y, int w, int h,int progress,int maxProgress,int direction, Component pMessage) {
        super(x, y, w, h, pMessage);
        this.direction = direction;
        setProgress(progress);
        setMaxProgress(maxProgress);
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    protected void renderContent(GuiGraphics guiGraphics, int i, int i1, float v) {
        int endX = getContentEndX();
        int endY = getContentEndY();
        if (direction == 0) {
            endX = getContentX() + getScaleProgress(getContentW());
        }else if (direction == 1) {
            endY = getContentY() + getScaleProgress(getContentH());
        }
        guiGraphics.fill(getContentX(),getContentY(),endX,endY,getBorderHoverColor());
    }

    public int getScaleProgress(int i){
        return maxProgress != 0 && progress != 0 ? progress * i / maxProgress : 0;
    }
}
