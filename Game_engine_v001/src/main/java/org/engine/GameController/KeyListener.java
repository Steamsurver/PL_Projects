package org.engine.GameController;


import java.security.Key;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];
    private boolean keyBeginPress[] = new boolean[350];
    private KeyListener(){

    }

    public static KeyListener get(){//возврат экземпляра кейлистнера
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void  keyCallBack(long window, int key, int scanCode, int action, int mods){//проверка нажатия на клавишу
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;
            get().keyBeginPress[key] = true;
        }else if(action == GLFW_RELEASE){
            get().keyPressed[key] = false;
            get().keyBeginPress[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){//возврат нажата ли клавиша по кей коду
        return get().keyPressed[keyCode];
    }

    public static boolean keyBeginPress(int glfwKeyD) {
        boolean result = get().keyBeginPress[glfwKeyD];
        if(result){
            get().keyBeginPress[glfwKeyD] = false;
        }
        return result;
    }
}
