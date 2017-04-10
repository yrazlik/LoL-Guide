package com.yrazlik.loltr.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yrazlik on 27/03/17.
 */

public class PassiveDto {

    @SerializedName("image")
    private ImageDto image;
    @SerializedName("sanitizedDescription")
    private String sanitizedDescription;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;

    public ImageDto getImage() {
        return image;
    }

    public void setImage(ImageDto image) {
        this.image = image;
    }

    public String getSanitizedDescription() {
        return sanitizedDescription;
    }

    public void setSanitizedDescription(String sanitizedDescription) {
        this.sanitizedDescription = sanitizedDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
