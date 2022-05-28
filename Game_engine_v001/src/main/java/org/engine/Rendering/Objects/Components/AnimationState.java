package org.engine.Rendering.Objects.Components;

import org.engine.Resources.Utils.AssetsPool;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    public String title; //название состояния
    public List<Frame> animationFrames = new ArrayList<>(); //контейнер с кадрами анимации

    //TODO для дефолтного спрайта можно сделать что-нибудь вроде текстуры ошибки
    private static Sprite defaultSprite = new Sprite(); //дефолтное состояние анимации
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0; //текущий кадр спрайта
    public boolean doesLoop = false;


    public void addFrame(Sprite sprite, float frameTime){//добавление кадра анимации
        this.animationFrames.add(new Frame(sprite, frameTime));
    }

    public void setLoop(boolean doesLoop){
        this.doesLoop = doesLoop;
    }

    public void update(float dt){
        if(currentSprite < animationFrames.size()){
            timeTracker -= dt;
            if(timeTracker <= 0){
                if(!(currentSprite == animationFrames.size()-1 && !doesLoop)){
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                }
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
    }


    public Sprite getCurrentSprite(){
        if(currentSprite < animationFrames.size()){
            return animationFrames.get(currentSprite).sprite;
        }

        return  defaultSprite;
    }


    public void refreshTextures() {
         for(Frame frame : animationFrames){
             frame.sprite.setTexture(AssetsPool.getTexture(frame.sprite.getTexture().getFilepath()));
         }
    }
}
