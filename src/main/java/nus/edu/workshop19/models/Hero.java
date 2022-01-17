package nus.edu.workshop19.models;

import jakarta.json.JsonObject;

public class Hero {
    
    private String name;
    private String description;
    private String imageUrl;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Hero create(JsonObject obj) {
        Hero h = new Hero();
        h.setName(obj.getString("name"));
        h.setDescription(obj.getString("description"));
        h.setImageUrl(obj.getJsonObject("thumbnail").getString("path"));
        return h;
    }
    
}
