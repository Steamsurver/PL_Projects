package org.engine.GameElements;


import org.engine.Editor.FileLoader;
import org.engine.Editor.ImGuiLayer;
import org.engine.EventSystem.Events.Event;
import org.engine.EventSystem.Observers.EventSystem;
import org.engine.EventSystem.Observers.Observer;
import org.engine.GameController.KeyListener;
import org.engine.GameController.MouseListener;
import org.engine.Rendering.DebugDraw;
import org.engine.Rendering.FrameBuffer;
import org.engine.Rendering.Assets.Texture.PickingTexture;
import org.engine.Rendering.Objects.GameObject;
import org.engine.Rendering.Renderer;
import org.engine.Rendering.Shaders.Shader;
import org.engine.Resources.Utils.AssetsPool;
import org.engine.scenes.LevelSceneInitializer;
import org.engine.scenes.Scene;
import org.engine.scenes.SceneInitializer;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window implements Observer {
    private int width, height;
    private FrameBuffer framebuffer;
    private String title;
    private static Window window = null;//наш статический экземпляр окна
    private static long glfwWindow;
    private static Scene currentScene;//Текущая сцена
    private ImGuiLayer imGuiLayer;//Графический слой
    private boolean runtimePlaying = false;//флаг для игры в реальном времени
    private PickingTexture pickingTexture;

    private long audioContext;//текущий контекст аудио-устройства
    private long audioDevice;//аудио-устройство

    public Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Engine v0.0.2";
        EventSystem.addObserver(this);//добавляем наш объект в список слушателей, регистрируем
    }

    public static void changeScene(SceneInitializer sceneInitializer){//метод смены сцены в реальном времени
        if(currentScene != null){
            currentScene.destroy();
        }

        getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);
        currentScene = new Scene(sceneInitializer);//подгрузка инициализатора в сцену
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


        //освобождаем память==========================
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

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


        //инициализация аудио библиотек===========
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);//имя дефолтного аудио девайса
        audioDevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0}; //здесь можно задавать настройки аудио
        audioContext = alcCreateContext(audioDevice, attributes);//объявление аудиоконтекста
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if(!alCapabilities.OpenAL10 ){
            assert false: "ERROR: Audio library not supported.";
        }
        //========================================

        GL.createCapabilities();


        //включение возможности смешивания цветов
        glEnable(GL_BLEND);
        //делаем альфа-канал прозрачным
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.framebuffer = new FrameBuffer(1920, 1080);
        this.pickingTexture = new PickingTexture(1920, 1080);
        glViewport(0, 0, 1920, 1080);

        //инициализируем графический слой
        this.imGuiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imGuiLayer.initImGui();

        Window.changeScene(new LevelSceneInitializer());
    }


    public void loop(){
        float beginTime = (float)glfwGetTime() ;//переменная начала кадра
        float endTime;//переменная конца кадра
        float dt = -1.0f; //разница во времени между концом и началом кадра
        Shader defaultShader = AssetsPool.getShader("src/main/java/org/engine/Rendering/Shaders/default.glsl");
        Shader pickingShader = AssetsPool.getShader("src/main/java/org/engine/Rendering/Shaders/pickingShader.glsl");



        while(!glfwWindowShouldClose(glfwWindow)){
            //извлекаем ивенты
            glfwPollEvents();

            //рендеринг выбранной текстуры
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 1920, 1080);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);


            //рисуем сетку
            DebugDraw.beginFrame();
            this.framebuffer.bind();

            //заполняем сцену цветом
            glClearColor(0.3f, 0.3f, 0.3f, 0);
            glClear(GL_COLOR_BUFFER_BIT);


            if (dt >= 0) {
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                //Главный цикл сцены

                if(runtimePlaying) {
                    currentScene.update(dt);
                }else {
                    currentScene.editorUpdate(dt);
                }

                currentScene.render();
            }

            this.framebuffer.unbind();
            this.imGuiLayer.update(dt ,currentScene);

            //обмениваем буфферы в окно
            glfwSwapBuffers(glfwWindow);

            //MouseListener.endFrame();//зануляем все значения зуммирования

            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static ImGuiLayer getImGuiLayer() {
        return window.imGuiLayer;
    }

    @Override
    public void onNotify(GameObject object, Event event) {//прозвон различных событий
        switch(event.type){
            case GameEngineStartPlay -> {
                this.runtimePlaying = true;
                currentScene.save();
                Window.changeScene(new LevelSceneInitializer());
            }
            case GameEngineStopPlay -> {
                this.runtimePlaying = false;
                Window.changeScene(new LevelSceneInitializer());
            }
            case LoadLevel -> {
                Window.changeScene(new LevelSceneInitializer());
            }
            case SaveLevel -> {
                currentScene.save();
            }
            case LoadFileTexture -> {

            }

        }
    }
}
