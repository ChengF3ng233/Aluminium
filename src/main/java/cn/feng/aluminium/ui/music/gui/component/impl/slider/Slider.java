package cn.feng.aluminium.ui.music.gui.component.impl.slider;

import cn.feng.aluminium.ui.music.gui.component.Component;

public abstract class Slider extends Component {
    protected boolean dragging, cursorRestored = false;
    protected float progressWidth, dragDelta;

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0 && !dragging) {
            dragging = true;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (dragging) {
            dragging = false;
        }
    }
}
