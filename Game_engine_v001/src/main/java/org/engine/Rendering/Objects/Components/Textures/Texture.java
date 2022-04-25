package org.engine.Rendering.Objects.Components.Textures;


import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private int texID;
    private int width, height;

    /*public Texture(String filepath) {

    }*/

    public void init(String filepath){
        this.filepath = filepath;

        //генерируем текстуру в GPU
        texID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texID);

        //устанавливаем параметры для текстуры
        //Повторяем изображение в одоих направлениях
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_R, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        //glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        //T - x
        //S - y

        //когда расстягиваем - вместо размыливания, мы пикселезуем изображение(пикселезуются близ стоящие пиксели)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        //когда сжимаем - вместо размыливания, мы пикселезуем изображение(пикселезуются близ стоящие пиксели)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);


        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channel = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channel, 0);

        if(image != null){

            //получаем значение ширины и высоты из буффера
            this.width = width.get(0);
            this.height = height.get(0);

            if(channel.get(0) == 3) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0),
                        height.get(0), 0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }else if(channel.get(0) == 4) {
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0),
                        height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            } else{
                assert false: "ERROR: (texture) unknown number of channel + " + channel.get(0);
            }
        }else{
            assert false: "ERROR: (texture) could no load image " + filepath;
        }

        stbi_image_free(image);//освобождаем память, доступ будем осуществлять уже через texID
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, texID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getId() {
        return texID;
    }
}
