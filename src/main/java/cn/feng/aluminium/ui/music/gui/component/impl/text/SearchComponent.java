package cn.feng.aluminium.ui.music.gui.component.impl.text;

import cn.feng.aluminium.ui.font.FontManager;
import cn.feng.aluminium.ui.font.impl.UFontRenderer;
import cn.feng.aluminium.ui.music.gui.component.Component;
import cn.feng.aluminium.ui.music.thread.SearchMusicThread;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.impl.DecelerateAnimation;
import cn.feng.aluminium.util.misc.TimerUtil;
import cn.feng.aluminium.util.render.ColorUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class SearchComponent extends Component {
    private String text = "";
    private boolean focused;
    private SearchMusicThread thread;
    private final Animation backgroundTextAnim = new DecelerateAnimation(200, 1.0);
    private final Animation cursorAnim = new DecelerateAnimation(200, 1.0);
    private final TimerUtil timerUtil = new TimerUtil();

    @Override
    public void update(float x, float y, float width, float height, int mouseX, int mouseY) {
        super.update(x, y, width, height, mouseX, mouseY);
        if (hovering) {
            if (backgroundTextAnim.getDirection().forwards()) backgroundTextAnim.changeDirection();
        } else if (!focused && text.isEmpty()) {
            if (backgroundTextAnim.getDirection().backwards()) backgroundTextAnim.changeDirection();
        }
    }

    @Override
    public void render() {
        UFontRenderer notoBold = FontManager.notoBold(20);
        UFontRenderer noto = FontManager.noto(13);

        FontManager.archivo(13).drawCenteredString("S E A R C H", x + width / 2f, y + height / 2f + 2f, ColorUtil.applyOpacity(Color.WHITE.getRGB(), backgroundTextAnim.getOutput().floatValue()));
        String toRender = noto.trimString(text, width - 20f, true, true);
        noto.drawCenteredString(toRender, x + width / 2f, y + height / 2f + 2f, Color.WHITE.getRGB());

        notoBold.drawString("『", x, y, Color.WHITE.getRGB());
        notoBold.drawString("』", x + width - notoBold.getStringWidth("』"), y + height - notoBold.getHeight(), Color.WHITE.getRGB());

        if (focused) {
            if (timerUtil.hasTimeElapsed(300)) {
                cursorAnim.changeDirection();
                timerUtil.reset();
            }
            float cursorX = Math.min(x + width / 2f + noto.getStringWidth(toRender) / 2f, x + width - 10f);
            RenderUtil.drawRect(cursorX, y + 4f, cursorX + 1f, y + 4f + height - 6f, ColorUtil.applyOpacity(Color.WHITE, cursorAnim.getOutput().floatValue()).getRGB());
        }

        if (thread != null && !thread.isAlive()) {
            thread = null;
        } else if (thread != null) {
            FontManager.noto(15).drawString("搜索中...", x, y + height + 1f, new Color(200, 200, 200, 200).getRGB());
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering && mouseButton == 0 && thread == null) {
            focused = true;
        } else if (!hovering) {
            focused = false;
        }
    }

    @Override
    public void keyTyped(char c, int keyCode) {
        if (!focused) return;
        switch (keyCode) {
            case Keyboard.KEY_RETURN: {
                if (thread == null) {
                    thread = new SearchMusicThread(text);
                    thread.start();
                    focused = false;
                }
                break;
            }
            case Keyboard.KEY_BACK: {
                text = text.substring(0, Math.max(text.length() - 1, 0));
                break;
            }
            default: {
                if (ChatAllowedCharacters.isAllowedCharacter(c)) {
                    text += c;
                }
            }
        }
    }
}
