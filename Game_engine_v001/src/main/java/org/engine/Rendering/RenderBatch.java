package org.engine.Rendering;

import org.engine.GameElements.Window;
import org.engine.Rendering.Objects.Components.SpriteRender;
import org.engine.Rendering.Objects.Components.Textures.Texture;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Shaders.Shader;
import org.engine.Resources.Utils.AssetsPool;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>{
    //vertex
    //======
    //Pos                       Color                           texCoords        texID
    //float, float,             float, float, float, float      float, float    float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEX_COORDS_SIZE = 2;
    private final int TEX_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;
    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEX_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEX_ID_OFFSET = TEX_COORDS_OFFSET + TEX_COORDS_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEX_ID_OFFSET + TEX_ID_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;


    private SpriteRender[] sprites;
    private List<Texture> texturesContainer;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;
    private int zIndex; //ндексация по координате Z
    private Renderer renderer;

    private int vaoID, vboID;
    private int maxBatchSize;
    //private Shader shader;
    private int[] texSlots = {0, 1, 2 ,3, 4, 5, 6, 7};

    public RenderBatch(int maxButchSize, int zIndex, Renderer renderer){
        this.renderer = renderer;
        this.zIndex = zIndex;
        //shader = AssetsPool.getShader("src/main/java/org/engine/Rendering/Shaders/default.glsl");
        this.sprites = new SpriteRender[maxButchSize];
        this.maxBatchSize = maxButchSize;

        //4 vertices quads
        vertices = new float[maxButchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
        this.texturesContainer = new ArrayList<>();
    }

    public void start(){
        //generate and bind a Vertex array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //выделяем пространство под вершины
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //создаем и загружаем буффер индексов
        int eboID = glGenBuffers();
        int[]indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        //активируем атрибуты для буфферов
        //первый индекс - это индекс позиции в шейдере layout (location=0) in vec3 aPos;
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);
        //первый индекс - это индекс позиции в шейдере layout (location=1) in vec4 aColor;
        //последнй индекс - это то, на сколько мы перепрыгнем от индексов положения вершины(это на 3(индекса три xyz) и умноженой на 4(float - 4 байта))
        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEX_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_COORDS_OFFSET);
        glEnableVertexAttribArray(2);
        glVertexAttribPointer(3, TEX_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEX_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }


    public void addSprite(SpriteRender spr){
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        if(spr.getTexture() != null){
            if(!texturesContainer.contains(spr.getTexture())){
                texturesContainer.add(spr.getTexture());
            }
        }

        //add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites >= this.maxBatchSize){
            this.hasRoom = false;
        }
    }


    public void render(){
        boolean rebufferData = false;
        for(int i = 0; i < numSprites; i++){
            SpriteRender spr = sprites[i];
            if(spr.isDirty()){
                loadVertexProperties(i);
                spr.setClean();
                rebufferData = true;
            }

            //поддержка изменения z-индексирования в реальном времени
            if(spr.gameObject.transform.zIndex != this.zIndex){
                destroyIfExists(spr.gameObject);
                //перезыписываем текущий объект
                renderer.add(spr.gameObject);
                i--;
            }
        }


        //здесь будем выгружать данные каждый кадр
        if(rebufferData) {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }


        Shader shader = Renderer.getBoundShader();
        shader.uploadMat4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());



        //textures=============
        for(int i = 0; i < texturesContainer.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i  + 1);
            texturesContainer.get(i).bind();

        }
        shader.uploadIntArray("uTextures", texSlots);
        //=====================

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);
        //начинаем с 0-го элемента


        //после рисовки мы отвязываемся от всего
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glBindVertexArray(0);
        for(int i = 0; i < texturesContainer.size(); i++){
            texturesContainer.get(i).unbind();
        }
        shader.detach();
    }


    private int[] generateIndices(){
        //6 indices per squad (3 indices per triangle)
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[]elements, int index){
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //3,2,0,0,2,1   7,6,4,4,6,5
        //triangle 1
        elements[offsetArrayIndex]     = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        //triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;


    }

    private void loadVertexProperties(int index){
        SpriteRender sprite = this.sprites[index];
        int offset = index * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        Vector2f[] texCoords = sprite.getTexCoords();

        int texID = 0;
        if(sprite.getTexture() != null){
            for(int i = 0; i < texturesContainer.size(); i++){
                if (texturesContainer.get(i).equals(sprite.getTexture())) {
                    texID = i + 1;
                    break;
                }
            }
        }


        boolean isRotated = sprite.gameObject.transform.rotation != 0.0f;//флаг надо ли переворачивать
        Matrix4f transformMatrix = new Matrix4f().identity();
        if(isRotated){
            transformMatrix.translate(sprite.gameObject.transform.position.x,
                    sprite.gameObject.transform.position.y, 0f);

            //вращение вокруг оси z
            transformMatrix.rotate((float)Math.toRadians(sprite.gameObject.transform.rotation), 0, 0, 1);

            transformMatrix.scale(sprite.gameObject.transform.scale.x, sprite.gameObject.transform.scale.y, 1);

        }

        //add vertices with the appropriate properties
        float xAdd = 0.5f;
        float yAdd = 0.5f;

        for(int i=0; i < 4; i++){
            if(i == 1){
                yAdd = -0.5f;
            }else if(i == 2){
                xAdd = -0.5f;
            }else if(i == 3){
                yAdd = 0.5f;
            }
            //load position
            Vector4f currentPos = new Vector4f(sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x),
                    sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y), 0, 1);

            if(isRotated){
                currentPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transformMatrix);
            }

            vertices[offset] = currentPos.x;
            vertices[offset+1] = currentPos.y;

            //load color
            vertices[offset+2] = color.x;
            vertices[offset+3] = color.y;
            vertices[offset+4] = color.z;
            vertices[offset+5] = color.w;

            //load Texture coordinates
            vertices[offset+6] = texCoords[i].x;
            vertices[offset+7] = texCoords[i].y;

            //load texture id
            vertices[offset+8] = texID;

            //load entity id
            vertices[offset+9] = sprite.gameObject.getUid() + 1;


            offset += VERTEX_SIZE;
        }


    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

    public boolean hasTextureRoom() {
        return this.texturesContainer.size() < 8;
    }

    public boolean hasTexture(Texture tex) {
        return this.texturesContainer.contains(tex);
    }

    public int zIndex() {
        return this.zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex());
    }

    public boolean destroyIfExists(GameObject go) {//очистка спрайтов объекта
        SpriteRender sprite = go.getComponent(SpriteRender.class);
        for(int i = 0; i < numSprites; i++){
            if(sprites[i] == sprite){
               for(int j = i; j < numSprites - 1;j++){//перезаписывание спрайтов
                   sprites[j] = sprites[j + 1];
                   sprites[j].setDirty();
               }
               numSprites--;
               return true;
            }
        }
        return false;
    }
}


