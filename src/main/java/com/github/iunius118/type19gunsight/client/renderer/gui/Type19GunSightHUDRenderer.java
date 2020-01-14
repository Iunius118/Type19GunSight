package com.github.iunius118.type19gunsight.client.renderer.gui;

import com.github.iunius118.type19gunsight.config.GunSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Type19GunSightHUDRenderer {
    private float cotHalfFOV = 1.428148F;  // = 1 / tan 35°
    private float red = 1.0F;
    private float green = 1.0F;
    private float blue = 1.0F;
    private float alpha = 1.0F;

    public void doRenderer(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.player;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        FontRenderer fontRenderer = mc.fontRenderer;
        double width = scaledResolution.getScaledWidth_double();
        double height = scaledResolution.getScaledHeight_double();
        Entity viewEntity = mc.getRenderViewEntity();
        float pitch = viewEntity.prevRotationPitch + (viewEntity.rotationPitch - viewEntity.prevRotationPitch) * partialTicks;

        GlStateManager.disableDepth();
        GlStateManager.enableBlend();
        setColor(GunSightConfig.reticleColor);
        GlStateManager.color(red, green, blue, alpha);
        GlStateManager.glLineWidth(1.0F);

        // TODO: Render reticle

        GlStateManager.enableDepth();
    }

    public void setCotHalfFOV(float f) {
        cotHalfFOV = f;
    }

    private void setColor(int color) {
        int nColor = color;
        blue = (float) (nColor & 0xFF) / 255.0F;
        nColor >>= 8;
        green = (float) (nColor & 0xFF) / 255.0F;
        nColor >>= 8;
        red = (float) (nColor & 0xFF) / 255.0F;
        nColor >>= 8;
        alpha = (float) (nColor & 0xFF) / 255.0F;
    }
}
