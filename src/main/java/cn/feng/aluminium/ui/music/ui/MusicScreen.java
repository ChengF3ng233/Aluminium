package cn.feng.aluminium.ui.music.ui;

import cn.feng.aluminium.Aluminium;
import cn.feng.aluminium.ui.font.awt.AWTFontLoader;
import cn.feng.aluminium.ui.music.api.player.MusicPlayer;
import cn.feng.aluminium.ui.music.ui.component.impl.*;
import cn.feng.aluminium.ui.music.ui.component.type.SidebarComponent;
import cn.feng.aluminium.ui.music.ui.component.type.SliderComponent;
import cn.feng.aluminium.ui.music.ui.page.AbstractPage;
import cn.feng.aluminium.ui.music.ui.page.Pages;
import cn.feng.aluminium.util.animation.advanced.Animation;
import cn.feng.aluminium.util.animation.advanced.Direction;
import cn.feng.aluminium.util.animation.advanced.impl.EaseBackIn;
import cn.feng.aluminium.util.animation.advanced.impl.EaseOutCubic;
import cn.feng.aluminium.util.data.resource.ResourceType;
import cn.feng.aluminium.util.data.resource.ResourceUtil;
import cn.feng.aluminium.util.render.RenderUtil;
import cn.feng.aluminium.util.render.shader.ShaderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

/**
 * @author ChengFeng
 * @since 2024/9/16
 **/
public class MusicScreen extends GuiScreen {
    private final float width;
    private final float height;
    private final float topHeight;
    private final float bottomHeight;
    private final boolean shouldClose;

    // UI
    private final SidebarComponent categorySidebar = new CategorySidebarComponent();
    private final SidebarComponent userSidebar = new UserSidebarComponent();
    private final SliderComponent playerSlider = new PlayerSliderComponent();
    private final SliderComponent volumeSlider = new VolumeSliderComponent();
    private final PlayerBarComponent playerBar = new PlayerBarComponent();

    // Position
    private float x, y;
    private Animation windowScale;

    // Drag
    private boolean dragging;
    private float lastMouseX, lastMouseY;
    private AbstractPage currentPage = Pages.HOME;


    public MusicScreen() {
        // Constants
        width = 470f;
        height = 310f;
        topHeight = 30f;
        bottomHeight = 55f;

        // Positions
        x = 20f;
        y = 20f;
        shouldClose = true;
    }

    public void setCurrentPage(AbstractPage currentPage) {
        if (currentPage == this.currentPage) return;
        currentPage.setParent(this.currentPage);
        this.currentPage.onClose();
        this.currentPage = currentPage;
        this.currentPage.onOpen();
    }

    @Override
    public void initGui() {
        windowScale = new EaseBackIn(300, 1.0, 2f);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (windowScale.finished(Direction.BACKWARDS)) {
            mc.displayGuiScreen(null);
            return;
        }

        // Drag logic
        if (RenderUtil.holding(mouseX, mouseY, x, y, width, topHeight, 0)) {
            if (!dragging) {
                lastMouseX = mouseX;
                lastMouseY = mouseY;
                dragging = true;
            }

            x += mouseX - lastMouseX;
            y += mouseY - lastMouseY;

            lastMouseX = mouseX;
            lastMouseY = mouseY;
        } else if (dragging) dragging = false;

        // Update component position
        categorySidebar.update(x + 10f, y + 50f, 25f, 0f, mouseX, mouseY);
        userSidebar.update(x + 10f, y + height - bottomHeight - 3f - userSidebar.getHeight(), 25f, 0f, mouseX, mouseY);
        currentPage.update(x + 45f, y + topHeight, width - 45f, height - topHeight - bottomHeight, mouseX, mouseY, x + width / 2f, y + height / 2f, windowScale.getOutput().floatValue());
        currentPage.handleScroll();
        playerSlider.update(x, y + height - bottomHeight - 2f, width, 2f, mouseX, mouseY);
        volumeSlider.update(x + width / 2f + 170f, y + height - bottomHeight / 2f, 50f, 5f, mouseX, mouseY);
        playerBar.update(x + width / 2f, y + height - bottomHeight + 10f, 100f, 20f, mouseX, mouseY);

        // Render
        RenderUtil.scaleStart(x + width / 2f, y + height / 2f, windowScale.getOutput().floatValue());
        ShaderUtil.drawRound(x, y, width, height, 3f, Theme.dark);

        // Sidebar
        RenderUtil.drawImage(ResourceUtil.getResource("music_note.png", ResourceType.ICON), x + 10f, y + 10f, 25f, 25f, Theme.primary);
        categorySidebar.render();
        userSidebar.render();

        // Page
        currentPage.render();

        // Player
        ShaderUtil.drawVaryingRound(x, y + height - bottomHeight, width, bottomHeight, 0f, 0f, 3f, 3f, Theme.darkAlt);
        playerSlider.render();
        volumeSlider.render();
        playerBar.render();
        ShaderUtil.drawRound(x + 20f, y + height - bottomHeight / 2f - 15f, 30f, 30f, 15f, Theme.grey);
        MusicPlayer player = Aluminium.INSTANCE.musicManager.getPlayer();
        if (player.available()) {
            RenderUtil.bindTexture(player.getCurrentMusic().getAlbum().getCoverImage());
            ShaderUtil.drawRoundTextured(x + 21f, y + height - bottomHeight / 2f - 14f, 28f, 28f, 14f, 1f);
            AWTFontLoader.noto(16f).drawString(player.getCurrentMusic().getTitle(), x + 53f, y + height - bottomHeight / 2f - 12f, Color.WHITE);
            AWTFontLoader.noto(14f).drawString(player.getCurrentMusic().getArtist(), x + 53f, y + height - bottomHeight / 2f, Theme.grey);
        }
        RenderUtil.scaleEnd();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        categorySidebar.mouseClicked(mouseButton);
        userSidebar.mouseClicked(mouseButton);
        categorySidebar.updateButtons(categorySidebar.getSelectedButton());
        categorySidebar.updateButtons(userSidebar.getSelectedButton());
        userSidebar.updateButtons(categorySidebar.getSelectedButton());
        userSidebar.updateButtons(userSidebar.getSelectedButton());
        playerSlider.mouseClicked(mouseButton);
        volumeSlider.mouseClicked(mouseButton);
        playerBar.mouseClicked(mouseButton);

        if (RenderUtil.hovering(mouseX, mouseY, x + 45f, y + topHeight, width - 45f, height - topHeight - bottomHeight)) {
            currentPage.mouseClicked(mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int button) {
        currentPage.mouseReleased();
        playerSlider.mouseReleased();
        volumeSlider.mouseReleased();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE && shouldClose) {
            windowScale = (windowScale instanceof EaseBackIn) ? new EaseOutCubic(300, 1.0, Direction.BACKWARDS) : new EaseBackIn(300, 1.0, 2f);
        }
    }
}
