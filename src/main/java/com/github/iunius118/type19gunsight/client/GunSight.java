package com.github.iunius118.type19gunsight.client;

import com.github.iunius118.type19gunsight.config.GunSightConfig;
import com.google.gson.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GunSight {
    public final Item[] items;
    public final double initialVelocity;
    public final double resistanceFactor;
    public final double gravityFactor;

    private final short[] elevationTable;

    private final static int MIN_ELEVATION = -90;
    private final static int MAX_ELEVATION = 90;
    private final static short INVALID_ELEVATION = 0x7FFF;

    public GunSight(Item[] items, float initialVelocity, float resistanceFactor, float gravityFactor) {
        this.items = (items != null && items.length > 0) ? items : new Item[]{Items.AIR};
        this.initialVelocity = initialVelocity;
        this.resistanceFactor = resistanceFactor;
        this.gravityFactor = gravityFactor;

        // Check args
        if (areArgumentsValid(initialVelocity, resistanceFactor, gravityFactor)) {
            elevationTable = new short[(MAX_ELEVATION - MIN_ELEVATION + 1) * Distance.values().length];
            initElevationTable();
        } else {
            elevationTable = new short[0];
        }
    }

    private boolean areArgumentsValid (float initialVelocity, float resistanceFactor, float gravityFactor) {
        if (initialVelocity < 0 || initialVelocity > 20) {
            return false;
        }

        if (resistanceFactor < 0 || resistanceFactor > 2) {
            return false;
        }

        if (gravityFactor < -5 || gravityFactor > 5) {
            return false;
        }

        return true;
    }

    private void initElevationTable() {
        int maxElevationIndex = (MAX_ELEVATION - MIN_ELEVATION + 1);

        for (int i = 0; i < maxElevationIndex; i++) {
            int gunElevation = MIN_ELEVATION + i;
            Projectile projectile = new Projectile(gunElevation, initialVelocity, resistanceFactor, gravityFactor);
            double targetElevation = 0;
            int tick = 0;

            for (Distance markedDistance : Distance.values()) {
                int tableIndex = i + (markedDistance.ordinal() - Distance.D50.ordinal()) * maxElevationIndex;
                elevationTable[tableIndex] = INVALID_ELEVATION;

                if (projectile.hasReachedTarget(markedDistance.getDistance())) {
                    targetElevation = projectile.getElevation(markedDistance.getDistance());
                    elevationTable[tableIndex] = (short) Math.round(targetElevation * 100);
                    continue;
                }

                while (tick < GunSightConfig.maxFlightTick) {
                    tick++;
                    projectile.update();

                    if (projectile.hasReachedTarget(markedDistance.getDistance())) {
                        targetElevation = projectile.getElevation(markedDistance.getDistance());
                        elevationTable[tableIndex] = (short) Math.round(targetElevation * 100);
                        break;
                    }
                }
            }
        }
    }


    public Optional<GunSight> create() {
        if (elevationTable.length == 0) {
            return Optional.empty();
        }

        return Optional.of(this);
    }

    public static Optional<GunSight> create(Item[] items, float initialVelocity, float resistanceFactor, float gravityFactor) {
        return new GunSight(items, initialVelocity, resistanceFactor, gravityFactor).create();
    }

    public static Optional<GunSight> create(String json) {
        Optional<GunSight> result = Optional.empty();

        try {
            JsonObject jsonObj = new Gson().fromJson(json, JsonObject.class);

            JsonArray itemIDs = JsonUtils.getJsonArray(jsonObj, "ids");
            List<Item> itemList = new ArrayList<>();

            for (JsonElement itemID : itemIDs) {
                itemList.add(JsonUtils.getItem(itemID, "each element in ids"));
            }

            Item[] items = new Item[itemList.size()];
            itemList.toArray(items);
            float v = JsonUtils.getFloat(jsonObj,"v");
            float r = JsonUtils.getFloat(jsonObj,"r");
            float g = JsonUtils.getFloat(jsonObj,"g");

            result = create(items, v, r, g);

        } catch (JsonSyntaxException e) {
            // Print json syntax error to StdErr
            System.err.println(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public float getTargetElevation(float gunElevation, Distance distance) {
        if (gunElevation < MIN_ELEVATION || gunElevation > MAX_ELEVATION) {
            return Float.NaN;
        }

        // Calculate indices of elevation table
        int maxEg = (int) Math.ceil(gunElevation);
        int minEg = (int) Math.floor(gunElevation);
        int offsetByDistance = (distance.ordinal() - Distance.D50.ordinal()) * (MAX_ELEVATION - MIN_ELEVATION + 1);
        int maxIndex = maxEg - MIN_ELEVATION + offsetByDistance;
        int minIndex = minEg - MIN_ELEVATION + offsetByDistance;

        if (minIndex < 0 || maxIndex >= elevationTable.length) {
            return Float.NaN;
        }

        // Calculate target elevation
        float maxEt = elevationTable[maxIndex];
        float minEt = elevationTable[minIndex];

        if (maxEt == INVALID_ELEVATION || minEt == INVALID_ELEVATION) {
            return Float.NaN;
        }

        float maxRatio = gunElevation - minEg;
        float minRatio = 1 - maxRatio;

        return (maxRatio * maxEt + minRatio * minEt) / 100.0F;
    }

    public enum Distance {
        D50(50F), D100(100F), D150(150F), D200(200F), D250(250F);

        private float distance;

        Distance(float dist) {
            distance = dist;
        }

        public float getDistance() {
            return distance;
        }
    }

    private static class Projectile {
        public final double resistanceFactor;
        public final double gravityFactor;
        public double vx = 0;
        public double vy = 0;
        public double posX = 0;
        public double posY = 0;
        public double prevPosX = 0;
        public double prevPosY = 0;
        public double distance = 0;
        public double prevDistance = 0;

        public Projectile(int gunElevation, double initialVelocity, double resistanceFactor, double gravityFactor) {
            vx = initialVelocity * Math.cos(Math.toRadians(gunElevation));
            vy = initialVelocity * Math.sin(Math.toRadians(gunElevation));
            this.resistanceFactor = resistanceFactor;
            this.gravityFactor = gravityFactor;
        }

        public void update() {
            // Save previous tick position
            prevPosX = posX;
            prevPosY = posY;
            prevDistance = distance;

            // Update position
            posX = posX + vx;
            posY = posY + vy;
            distance = Math.sqrt(posX * posX + posY * posY);

            // Update velocity
            vx = vx * resistanceFactor;
            vy = vy * resistanceFactor - gravityFactor;
        }

        public boolean hasReachedTarget(double targetDistance) {
            return targetDistance >= prevDistance && targetDistance <= distance;
        }

        public double getElevation(double targetDistance) {
            double interval = Math.abs(distance - prevDistance);

            if (interval < 0.00001) {
                return Math.toDegrees(Math.atan2(posY, posX));
            }

            double ratioNear = Math.abs(targetDistance - distance) / interval;
            double ratioFar = Math.abs(targetDistance - prevDistance) / interval;
            double interX = ratioNear * prevPosX + ratioFar * posX;
            double interY = ratioNear * prevPosY + ratioFar * posY;
            return Math.toDegrees(Math.atan2(interY, interX));
        }
    }
}
