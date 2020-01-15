package com.github.iunius118.type19gunsight.client.renderer.gui;

import com.github.iunius118.type19gunsight.client.GunSight;
import com.github.iunius118.type19gunsight.config.GunSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class Type19GunSightHUDRenderer {
    private float cotHalfFOV = 1.428148F;  // = 1 / tan 35°
    private float red = 1.0F;
    private float green = 1.0F;
    private float blue = 1.0F;
    private float alpha = 1.0F;

    public void doRenderer(GunSight sight, float partialTicks) {
        if (sight == null) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
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

        for (GunSight.Distance distance : GunSight.Distance.values()) {
            double elevation = sight.getTargetElevation(-pitch, distance);

            double angle = elevation + pitch;

            if (angle < -60.0D || angle > 60.0D) {
                continue;
            }

            double y = (1 - Math.tan(angle * Math.PI / 180.0D) * cotHalfFOV) * height / 2;

            GlStateManager.disableTexture2D();

            vertexBuffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
            vertexBuffer.pos(width * 0.4875D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.496875D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.503125D, y, 0.0D).endVertex();
            vertexBuffer.pos(width * 0.5125D, y, 0.0D).endVertex();
            tessellator.draw();

            GlStateManager.enableTexture2D();

            fontRenderer.drawString(distance.getLabel(), (float) width * 0.525F, (float) y - fontRenderer.FONT_HEIGHT / 2.0F, GunSightConfig.reticleColor, false);
        }

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
