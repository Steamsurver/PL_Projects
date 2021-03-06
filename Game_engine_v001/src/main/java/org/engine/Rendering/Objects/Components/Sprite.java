package org.engine.Rendering.Objects.Components;

import org.engine.Rendering.Assets.Texture.Texture;
import org.joml.Vector2f;
//TODO для дефолтного спрайта можно сделать что-нибудь вроде текстуры ошибки

public class Sprite {
    private float width, height;
    private Texture texture = null;
    private Vector2f[] texCoords = {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
    };

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTexCoords() {
        return texCoords;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public void setTexCoords(Vector2f[] texCoords) {
        this.texCoords = texCoords;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId(){
        return texture == null ? -1: texture.getId();
    }

}
