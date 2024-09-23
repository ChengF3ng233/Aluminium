package cn.feng.aluminium.ui.music.ui.component.type;

import cn.feng.aluminium.ui.music.ui.component.AbstractComponent;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public abstract class SliderComponent extends AbstractComponent {
    protected boolean dragging, cursorRestored = false;
    protected float progressWidth, dragDelta;

    public void mouseClicked(int mouseButton) {
        if (hovering && mouseButton == 0 && !dragging) {
            dragging = true;
        }
    }

    public void mouseReleased() {
        if (dragging) {
            dragging = false;
        }
    }
}
