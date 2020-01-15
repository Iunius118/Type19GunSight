package com.github.iunius118.type19gunsight.client;

import com.github.iunius118.type19gunsight.Type19GunSight;
import com.github.iunius118.type19gunsight.client.renderer.gui.Type19GunSightHUDRenderer;
import com.github.iunius118.type19gunsight.config.GunSightConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class ClientEventHandler {
    private final Type19GunSightHUDRenderer sightHUDRenderer = new Type19GunSightHUDRenderer();
    private static final FloatBuffer matrixBuf = BufferUtils.createFloatBuffer(16);

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Type19GunSight.MOD_ID)) {
            ConfigManager.sync(Type19GunSight.MOD_ID, Config.Type.INSTANCE);
            Type19GunSight.initSightSettings();
            Type19GunSight.lastMainHandItem = null;
            Type19GunSight.lastOffHandItem = null;
            Type19GunSight.lastMainHandSight = null;
            Type19GunSight.lastOffHandSight = null;
        }
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        matrixBuf.clear();
        GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, matrixBuf);
        sightHUDRenderer.setCotHalfFOV(matrixBuf.get(5));
    }

    @SubscribeEvent
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event) {
        if (GunSightConfig.enable && event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            GameSettings options = Minecraft.getMinecraft().getRenderManager().options;

            if (options != null && options.thirdPersonView < 1) {
                GunSight sight = getHoldingGunSight(Minecraft.getMinecraft().player);
                sightHUDRenderer.doRenderer(sight, event.getPartialTicks());
            }
        }
    }

    private GunSight getHoldingGunSight(EntityPlayer player) {
        ItemStack mainStack = player.getHeldItemMainhand();
        Item mainItem = mainStack.getItem();

        if (mainStack.isEmpty()) {
            Type19GunSight.lastMainHandItem = null;
            Type19GunSight.lastMainHandSight = null;

        } else if (mainItem == Type19GunSight.lastMainHandItem && Type19GunSight.lastMainHandSight != null) {
            return Type19GunSight.lastMainHandSight;

        } else {
            Type19GunSight.lastMainHandItem = mainItem;
            Type19GunSight.lastMainHandSight = null;

            for (Item item : Type19GunSight.GunSights.keySet()) {
                if (item == mainItem) {
                    Type19GunSight.lastMainHandSight = Type19GunSight.GunSights.get(item);
                    return Type19GunSight.lastMainHandSight;
                }
            }
        }

        ItemStack OffStack = player.getHeldItemOffhand();
        Item OffItem = OffStack.getItem();

        if (OffStack.isEmpty()) {
            Type19GunSight.lastOffHandItem = null;
            Type19GunSight.lastOffHandSight = null;

        } else if (OffItem == Type19GunSight.lastOffHandItem && Type19GunSight.lastOffHandSight != null) {
            return Type19GunSight.lastOffHandSight;

        } else {
            Type19GunSight.lastOffHandItem = OffItem;
            Type19GunSight.lastOffHandSight = null;

            for (Item item : Type19GunSight.GunSights.keySet()) {
                if (item == OffItem) {
                    Type19GunSight.lastOffHandSight = Type19GunSight.GunSights.get(item);
                    return Type19GunSight.lastOffHandSight;
                }
            }
        }

        return null;
    }
}
