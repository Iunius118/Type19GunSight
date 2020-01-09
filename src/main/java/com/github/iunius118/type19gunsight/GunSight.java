package com.github.iunius118.type19gunsight;

import com.google.gson.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GunSight {
    public final Item[] items;
    public final float initialVelocity;
    public final float resistanceFactor;
    public final float gravityFactor;

    public GunSight(Item[] items, float initialVelocity, float resistanceFactor, float gravityFactor) {
        this.items = (items != null && items.length > 0) ? items : new Item[]{Items.AIR};
        this.initialVelocity = initialVelocity;
        this.resistanceFactor = resistanceFactor;
        this.gravityFactor = gravityFactor;

        // Check args
        if (areArgumentsValid(initialVelocity, resistanceFactor, gravityFactor)) {
            // TODO: Calc degrees?
        } else {

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

    public Optional<GunSight> create() {
        // TODO: If areArgumentsValid?
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

            return create(items, v, r, g);

        } catch (JsonSyntaxException e) {
            // Print json syntax error to StdErr
            System.err.println(e.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
