package com.github.iunius118.type19gunsight.client.gui;

import com.github.iunius118.type19gunsight.Type19GunSight;
import com.github.iunius118.type19gunsight.config.GunSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Set;

public class ConfigGuiFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft minecraft) {

    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        List<IConfigElement> elements;
        elements = ConfigElement.from(GunSightConfig.class).getChildElements();

        return new GuiConfig(parentScreen, elements, Type19GunSight.MOD_ID, false, false, Type19GunSight.MOD_NAME);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
}
