package cn.feng.aluminium.ui.music.gui.component.impl.button;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class SwitchButton extends IconButton {
    private final List<ResourceLocation> iconList = new ArrayList<>();
    private final Consumer<Integer> action;

    public SwitchButton(Consumer<Integer> action, ResourceLocation currentIcon, ResourceLocation... icons) {
        super(currentIcon, null);
        iconList.addAll(Arrays.asList(icons));
        this.action = action;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (hovering) {
            int index = iconList.indexOf(currentIcon) + 1;
            if (index == iconList.size()) index = 0;
            action.accept(index);
            currentIcon = iconList.get(index);
        }
    }
}
