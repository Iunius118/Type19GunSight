package com.github.iunius118.type19gunsight.config;

import com.github.iunius118.type19gunsight.Type19GunSight;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;

@Config(modid = Type19GunSight.MOD_ID)
public class GunSightConfig {
    @Comment("Enable Type 19 Gun Sight")
    @LangKey(Type19GunSight.MOD_ID + ".config.enable")
    public static boolean enable = true;

    @Comment("Color of marker on HUD, in ARGB8888 format")
    @LangKey(Type19GunSight.MOD_ID + ".config.marker_color")
    public static int markerColor = 0xFFFFFFFF;

    @Comment("Settings of sights for each item, in JSON {\"id\":\"item_id\", \"v\":initialVelocity[0.0 - 20.0], \"r\":resistanceFactor[0.0 - 2.0], \"g\":gravityFactor[-5.0 - 5.0]}")
    @LangKey(Type19GunSight.MOD_ID + ".config.item_id")
    public static String[] sightSettings = {"{\"id\":\"minecraft:bow\",\"v\":3.0,\"r\":0.99,\"g\":0.05}", "{\"id\":\"mod_id:example\",\"v\":3.0,\"r\":0.99,\"g\":0.03}"};
}
