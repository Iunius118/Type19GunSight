package com.github.iunius118.type19gunsight.config;

import com.github.iunius118.type19gunsight.Type19GunSight;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.LangKey;
import net.minecraftforge.common.config.Config.RangeInt;

@Config(modid = Type19GunSight.MOD_ID, category = "client")
public class GunSightConfig {
    @Comment("Enable Type 19 Gun Sight")
    @LangKey(Type19GunSight.MOD_ID + ".config.enable")
    public static boolean enable = true;

    @Comment("Max flight time of the projectile, in ticks")
    @LangKey(Type19GunSight.MOD_ID + ".config.max_flight_tick")
    @RangeInt(min = 0, max = 255)
    public static int maxFlightTick = 255;

    @Comment("Color of reticle, integer in ARGB8888 format")
    @LangKey(Type19GunSight.MOD_ID + ".config.reticle_color")
    public static int reticleColor = -1; // 0xFFFFFFFF = white

    @Comment("Settings of sights for each item, list of json string:\n  {\"ids\":[\"item_id_1\",\"item_id_2\",\"...\"], \"v\":initialVelocity[0.0 - 20.0], \"r\":resistanceFactor[0.0 - 2.0], \"g\":gravityFactor[-5.0 - 5.0]}")
    @LangKey(Type19GunSight.MOD_ID + ".config.sights")
    public static String[] sightSettings = {"{\"ids\":[\"minecraft:bow\"],\"v\":3.0,\"r\":0.99,\"g\":0.05}", "{\"ids\":[\"modid:example\"],\"v\":3.0,\"r\":0.99,\"g\":0.03}"};
}
