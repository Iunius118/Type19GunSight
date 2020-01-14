package com.github.iunius118.type19gunsight.client;

import com.github.iunius118.type19gunsight.Type19GunSight;
import com.github.iunius118.type19gunsight.client.renderer.gui.Type19GunSightHUDRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class ClientEventHandler {
    private final Type19GunSightHUDRenderer sightHUDRenderer = new Type19GunSightHUDRenderer();
    private static final FloatBuffer matrixBuf = BufferUtils.createFloatBuffer(16);

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        matrixBuf.clear();
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, matrixBuf);
        sightHUDRenderer.setCotHalfFOV(matrixBuf.get(5));
    }

    private boolean isHoldingGunItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        if (stack.getItem() == Type19GunSight.lastItem) {
            return true;
        }

        for (Item item : Type19GunSight.GunSights.keySet()) {
            if (stack.getItem() == item) {
                Type19GunSight.lastItem = item;
                Type19GunSight.lastSight = Type19GunSight.GunSights.get(item);
                return true;
            }
        }

        return false;
    }
}
