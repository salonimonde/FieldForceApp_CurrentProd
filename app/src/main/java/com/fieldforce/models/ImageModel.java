package com.fieldforce.models;

import java.io.Serializable;

/**
 * Created by Saloni on 15-01-2020.
 */
public class ImageModel implements Serializable
{
    public String name;
    public String image;
    public String content_type;

    public ImageModel(String name, String image, String content_type)
    {
        this.name = name;
        this.image = image;
        this.content_type = content_type;
    }

    public ImageModel()
    {
    }
}
