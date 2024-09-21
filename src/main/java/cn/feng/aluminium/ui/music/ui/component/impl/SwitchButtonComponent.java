package cn.feng.aluminium.ui.music.ui.component.impl;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ChengFeng
 * @since 2024/9/17
 **/
public class SwitchButtonComponent extends IconButtonComponent {
    private final List<ResourceLocation> icons = new ArrayList<>();

    public SwitchButtonComponent(ResourceLocation iconLocation, ResourceLocation... icons) {
        super(iconLocation, null);
        this.icons.addAll(Arrays.asList(icons));
    }

    @Override
    public void mouseClicked(int mouseButton) {
        if (hovering) {
            int newIndex = icons.indexOf(iconLocation) + 1;
            if (newIndex == icons.size()) newIndex = 0;
            iconLocation = icons.get(newIndex);
        }
        super.mouseClicked(mouseButton);
    }

    public List<ResourceLocation> getIcons() {
        return icons;
    }
}
