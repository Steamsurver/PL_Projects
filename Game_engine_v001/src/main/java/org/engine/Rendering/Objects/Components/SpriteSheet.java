package org.engine.Rendering.Objects.Components;

import org.engine.Rendering.Objects.Components.Textures.Texture;
import org.joml.Vector2f;


import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {

    private Texture texture;
    private List<Sprite> spriteContainer;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.spriteContainer = new ArrayList<>();

        this.texture = texture;
        int currentX = 0; //место откуда начинается отсчет по координате x
        int currentY = texture.getHeight() - spriteHeight; //место откуда начинается отсчет по координате y
        //int currentY = spriteHeight

        for(int i = 0; i < numSprites; i++){
            float topY = (currentY + spriteHeight) / (float)texture.getHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float)texture.getWidth();
            float bottomY = currentY / (float)texture.getHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };
            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTexCoords(texCoords);

            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);

            this.spriteContainer.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index){
        return this.spriteContainer.get(index);
    }

    public int size(){
        return spriteContainer.size();
    }
}
