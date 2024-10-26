package cn.feng.aluminium.ui.music.gui.component.impl.text;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.font.impl.UFontRenderer;
import cn.feng.aluminium.ui.music.gui.component.Component;

import java.awt.*;

public class SearchComponent extends Component {
    @Override
    public void render() {
        UFontRenderer notoBold = FontManager.notoBold(20);
        FontManager.archivo(13).drawCenteredString("S E A R C H", x + width / 2f, y + height / 2f + 2f, Color.WHITE.getRGB());
        notoBold.drawString("『", x, y, Color.WHITE.getRGB());
        notoBold.drawString("』", x + width - notoBold.getStringWidth("』"), y + height - notoBold.getHeight(), Color.WHITE.getRGB());
    }
}
