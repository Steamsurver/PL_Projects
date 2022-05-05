package org.engine.GameElements;


import org.engine.Engine_GUI.ImGuiLayer;
import org.engine.GameController.KeyListener;
import org.engine.GameController.MouseListener;
import org.engine.Rendering.DebugDraw;
import org.engine.Rendering.FrameBuffer;
import org.engine.scenes.LevelEditorScene;
import org.engine.scenes.Scene;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private float r, g, b ,a;
    private int width, height;
    private FrameBuffer framebuffer;
    private String title;
    private static Window window = null;//наш статический экземпляр окна
    private static long glfwWindow;
    private static Scene currentScene;//Текущая сцена
    private ImGuiLayer imGuiLayer;//Графический слой


    public Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "GAME";
        this.r = 0.5f;
        this.g = 0.5f;
        this.b = 0.5f;
        this.a = 1;
    }

    public static void changeScene(int newScene){//метод смены сцены в реальном времени
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                break;

            default:
                assert false: "Unknown scene in '" + newScene + "'";
                break;
        }
        currentScene.load();//подгрузка всех значений объектов, десирреализация игровых объектов на сцену.
        currentScene.init();//подготовка объектов внутри сцены
        currentScene.start();//запуск игровой сцены
    }


    public static Window getWindow(){//геттер текущего экземпляра окна
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public static FrameBuffer getFramebuffer() {
        return window.framebuffer;
    }

    public static float getTargetAspectRatio(){//Наше целевое соотношение сторон
        return 16.0f/9.0f;
    }

    public static int getHeight() {
        return Window.window.height;
    }//гетер текущей высоты окна

    public static int getWidth() {
        return Window.window.width;
    }//гетер текушей ширины окна

    public static void setHeight(int newHeight) {
       Window.window.height = newHeight;
    }//

    public static void setWidth(int newWidth) {
        Window.window.width = newWidth;
    }//

    public static Scene getCurrentScene() {
        return currentScene;
    }




    public void run(){
        System.out.println("Window is running!");
        init();//инициализация текущего окна
        loop();//Главный цикл движка

        //освобождаем память
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //уничтожаем GLFW и свобождаем коллбэк ошибок
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }


    public void init(){
        //вывод ошибок
        GLFWErrorCallback.createPrint(System.err).set();

        if(!glfwInit()){
            throw new IllegalStateException("glfw error.");
        }

        //настраиваем GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE); //развернуто на полный экран


        //создание окна
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Error to creating window!");
        }

        //привязывание коллбэков к окну
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallBack);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight)->{
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });



        //Создаем текущий контекст для openGL
        glfwMakeContextCurrent(glfwWindow);
        //vSync
        glfwSwapInterval(1);
        //делаем окно видимым
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();


        //включение возможности смешивания цветов
        glEnable(GL_BLEND);
        //делаем альфа-канал прозрачным
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        //инициализируем графический слой
        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        this.framebuffer = new FrameBuffer(1920, 1080);
        Window.changeScene(0);
    }


    public void loop(){
        float dt = -1.0f; //разница во времени между концом и началом кадра
        float beginTime = (float)glfwGetTime() ;//переменная начала кадра
        float endTime;//переменная конца кадра




        while(!glfwWindowShouldClose(glfwWindow)){
            //извлекаем ивенты
            glfwPollEvents();

            //рисуем сетку
            DebugDraw.beginFrame();

            this.framebuffer.bind();

            //заполняем сцену цветом
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);


            if (dt >= 0) {
                DebugDraw.draw();
                //Главный цикл сцены
                currentScene.update(dt);
            }

            this.framebuffer.unbind();


            this.imGuiLayer.update(dt ,currentScene);

            //обмениваем буфферы в окно
            glfwSwapBuffers(glfwWindow);

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.saveExit();
    }

}
