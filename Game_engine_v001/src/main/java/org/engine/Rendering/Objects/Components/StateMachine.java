package org.engine.Rendering.Objects.Components;

import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImString;
import org.lwjgl.util.spvc.SpvcMslSamplerYcbcrConversion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

//Класс по обработке состояния объекта
public class StateMachine extends Component{

    //============================================================================
    private class StateTrigger{
        public String state;
        public String trigger;

        public StateTrigger(){//1 - конструктор по умолчанию

        }

        public StateTrigger(String state, String trigger){//1 - конструктор c параметрами
            this.state = state;
            this.trigger = trigger;
        }

        @Override
        public boolean equals(Object o){//перегрузка сравнивания
            if(o.getClass() != StateTrigger.class) return false;
            StateTrigger t2 = (StateTrigger)o; //приводим к типу
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode(){//перегрузка генератора хэш-кода
            return Objects.hash(trigger, state);
        }
    }
    //============================================================================

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();//передатчик состояний
    private List<AnimationState> states = new ArrayList<>(); //список состояний
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void refreshTextures(){
        for(AnimationState state : states){
            state.refreshTextures();
        }
    }

    public void addStateTrigger(String from, String to, String onTrigger){
        this.stateTransfers.put(new StateTrigger(from, onTrigger), to);
    }

    public void addState(AnimationState state){//добавление состояния
        this.states.add(state);
    }

    public void setDefaultState(String animationTitle) {
        for(AnimationState state : states){
            if(state.title.equals(animationTitle)){
                defaultStateTitle = animationTitle;
                if(currentState == null){
                    currentState = state;
                    return;
                }
            }
        }

        System.out.println("Unable to find state'" + animationTitle + "' in set default state");
    }

    //TODO реализовать через хэшмап
    public void trigger(String trigger){//функция для обработки триггера
        for(StateTrigger state : stateTransfers.keySet()){
            if(state.state.equals(currentState.title) && state.trigger.equals(trigger)){
                //для всех состояний у которых совпадает заголовок с текущим состоянием
                //и если в состоянии есть переданный триггер
                if(stateTransfers.get(state) != null){
                    int newStateIndex = -1;
                    int index = 0;
                    for(AnimationState s : states){
                        if(s.title.equals(stateTransfers.get(state))){
                            newStateIndex = index;
                            break;
                        }
                        index++;
                    }

                    if(newStateIndex > -1) {
                        currentState = states.get(newStateIndex);
                    }
                }
                return;
            }
        }
        System.out.println("Unable to find trigger: " + trigger + "'");
    }


    @Override
    public void start(){//установка дефолтного состояния
        for(AnimationState state : states){
            if(state.title.equals(defaultStateTitle)){
                currentState = state;
                break;
            }
        }
    }


    @Override
    public void update(float dt){//метод отработки анимации
        if(currentState != null){
            currentState.update(dt);
            SpriteRender sprite = gameObject.getComponent(SpriteRender.class);//рендерим спрайт через объект
            if(sprite != null){
                sprite.setSprite(currentState.getCurrentSprite());//отправка самого спрайта в рендерер
            }
        }
    }

    @Override
    public void editorUpdate(float dt){//метод отработки анимации
        if(currentState != null){
            currentState.update(dt);
            SpriteRender sprite = gameObject.getComponent(SpriteRender.class);//рендерим спрайт через объект
            if(sprite != null){
                sprite.setSprite(currentState.getCurrentSprite());//отправка самого спрайта в рендерер
            }
        }
    }

    @Override
    public void imGui(){//добавление в GUI движка информации об анимации
        int index = 0;
        for(AnimationState state : states){
            ImString title = new ImString(state.title);
            ImGui.inputText("State: ", title);//вывод имени состояния
            state.title = title.get();

            ImBoolean doesLoop = new ImBoolean(state.doesLoop);
            ImGui.checkbox("Does Loop ", doesLoop);//вывод инф. о зацикливании
            state.setLoop(doesLoop.get());

            for(Frame frame : state.animationFrames){//вывод информации о кадре
                float[] tmp = new float[1];
                tmp[0] = frame.frameTime;
                ImGui.dragFloat("Frame(" + index + ") Time: ", tmp, 0.01f);
                frame.frameTime = tmp[0];
                index++;
            }
        }
    }


}
