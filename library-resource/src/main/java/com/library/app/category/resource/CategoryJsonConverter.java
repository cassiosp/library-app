package com.library.app.category.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.category.model.Category;
import com.library.app.common.json.JsonReader;

@ApplicationScoped
public class CategoryJsonConverter {

    public Category convertFrom(String json) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        final Category category = new Category();
        category.setName(JsonReader.getStringOrNull(jsonObject, "name"));

        return category;
    }

    public JsonElement convertToJsonElement(Category category) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", category.getId());
        jsonObject.addProperty("name", category.getName());
        return jsonObject;
    }

    public JsonElement convertToJsonElement(List<Category> categories) {
        final JsonArray jsonArray = new JsonArray();

        categories.forEach((category) -> {
            jsonArray.add(convertToJsonElement(category));
        });

        return jsonArray;
    }

}
