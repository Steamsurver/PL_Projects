package org.engine.Rendering.Objects.Components;

public class Frame {
    public Sprite sprite;
    public float frameTime;

    public Frame(){

    }

    public Frame(Sprite sprite, float time){
        this.sprite = sprite;
        this.frameTime = time;
    }
}
