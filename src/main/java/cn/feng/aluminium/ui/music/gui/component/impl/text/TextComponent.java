package cn.feng.aluminium.ui.music.gui.component.impl.text;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.music.gui.component.Component;

import java.awt.*;

public class TextComponent extends Component {
    private String text;
    private boolean bold;
    private int size = 16;

    public TextComponent(String text, boolean bold) {
        this.text = text;
        this.bold = bold;
    }

    public TextComponent(String text, boolean bold, int size) {
        this.text = text;
        this.bold = bold;
        this.size = size;
    }

    public TextComponent(String text) {
        this.text = text;
    }

    @Override
    public void render() {
        (bold? FontManager.noto(size) : FontManager.notoBold(size)).drawString(text, x, y, Color.WHITE.getRGB());
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setBold(boolean bold) {
        this.bold = bold;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
