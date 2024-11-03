package cn.feng.aluminium.event.events;

import cn.feng.aluminium.event.impl.Event;

public class EventChatGUI implements Event {
    private final int mouseX;
    private final int mouseY;

    public EventChatGUI(int mouseX, int mouseY) {
        this.mouseX = mouseX;
        this.mouseY = mouseY;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }
}
