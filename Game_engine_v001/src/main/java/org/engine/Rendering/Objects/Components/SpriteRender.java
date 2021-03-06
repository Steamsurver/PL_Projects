package org.engine.Rendering.Objects.Components;

import imgui.ImGui;
import org.engine.Rendering.Assets.Texture.Texture;
import org.engine.Resources.Utils.AssetsPool;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class SpriteRender extends Component {

    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();
    private transient Transform lastTransform;//принимает в себя вектор из игрового объекта
    private transient boolean isDirty = true;//флаг для обновления в реальном времени

    public Vector2f[] getTexCoords() {
        return this.sprite.getTexCoords();
    }

    public Texture getTexture() {
        return this.sprite.getTexture();
    }

    public Vector4f getColor() {
        return color;
    }


    @Override
    public void start(){
        if (this.sprite.getTexture() != null) {
            this.sprite.setTexture(AssetsPool.getTexture(this.sprite.getTexture().getFilepath()));
        }
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if(!this.lastTransform.equals(this.gameObject.transform)){
            this.gameObject.transform.copy(this.lastTransform);
            this.isDirty = true;
        }
    }

    @Override
    public void editorUpdate(float dt){
        if(!this.lastTransform.equals(this.gameObject.transform)){
            this.gameObject.transform.copy(this.lastTransform);
            this.isDirty = true;
        }
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color){
        if(!this.color.equals(color)) {
            this.isDirty = true;
            this.color.set(color);
        }
    }

    public boolean isDirty(){
        return this.isDirty;
    }


    public void setClean(){
        this.isDirty = false;
    }

    public void setTexture(Texture texture){
        this.sprite.setTexture(texture);
    }

    @Override
    public void imGui() {
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker: ", imColor)) {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            this.isDirty = true;
        }
    }

    public void setDirty() {
        this.isDirty = true;
    }
}
