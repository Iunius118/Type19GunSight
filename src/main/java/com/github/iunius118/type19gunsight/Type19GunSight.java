package com.github.iunius118.type19gunsight;

import com.github.iunius118.type19gunsight.client.ClientEventHandler;
import com.github.iunius118.type19gunsight.config.GunSightConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(   modid = Type19GunSight.MOD_ID,
        name = Type19GunSight.MOD_NAME,
        version = Type19GunSight.MOD_VERSION,
        guiFactory = "com.github.iunius118.type19gunsight.client.gui.ConfigGuiFactory")
@EventBusSubscriber
public class Type19GunSight {
    public static final String MOD_ID = "type19gunsight";
    public static final String MOD_NAME = "Type 19 Gun Sight";
    public static final String MOD_VERSION = "1.12.2-0.0.1.0";
    public static Logger LOGGER;

    public static final GunSightConfig CONFIG = new GunSightConfig();

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();

        if (event.getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        }
    }
}
