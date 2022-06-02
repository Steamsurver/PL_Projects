package org.engine.Editor;

import imgui.*;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.type.ImBoolean;
import org.engine.GameController.KeyListener;
import org.engine.GameController.MouseListener;
import org.engine.Rendering.Assets.Texture.PickingTexture;
import org.engine.scenes.Scene;
import org.engine.GameElements.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

//отвечает за основные взаимодействия с GUI редактора
public class ImGuiLayer {

    private long glfwWindow;
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();// LWJGL3 renderer (ДОЛЖЕН быть инициализирован)
    private GameViewWindow gameViewWindow;
    private PropertiesWindow propertiesWindow;
    private MenuBar menuBar;//меню с различными настройками/действиями
    private SceneHierarchyWindow sceneHierarchyWindow; //панель с иерархичным отображением объектов

    private final ImGuiImplGl3 imGuiG13 = new ImGuiImplGl3();
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();

    public ImGuiLayer(long glfwWindow, PickingTexture pickingTexture){
        this.glfwWindow = glfwWindow;
        this.gameViewWindow = new GameViewWindow();
        this.propertiesWindow = new PropertiesWindow(pickingTexture);
        this.menuBar = new MenuBar();
        this.sceneHierarchyWindow = new SceneHierarchyWindow();
    }


    //==============================================================================================
    public void initImGui() {
        // ВАЖНО!!
        // Эта строка критична для работы Dear ImGui.
        ImGui.createContext();

        // ------------------------------------------------------------
        //инициализировать конфиг ImGuiIO
        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("src/main/java/org/engine/Config/ImGuiConfig.txt"); //сохранение конфигов
        //io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Навигация с клавиатуры
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);//включаем флаги окон
        //io.setConfigFlags(ImGuiConfigFlags.ViewportsEnable);



        //io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Курсоры мыши для отображения при изменении размера окон и т. д.
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // ------------------------------------------------------------
        // Обратные вызовы GLFW для обработки пользовательского ввода

        glfwSetKeyCallback(glfwWindow, (w, key, scancode, action, mods) -> {
            if (action == GLFW_PRESS) {
                io.setKeysDown(key, true);
            } else if (action == GLFW_RELEASE) {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW_KEY_RIGHT_SUPER));

            if(!io.getWantCaptureKeyboard()){
                KeyListener.keyCallBack(w, key, scancode, action, mods);
            }
        });

        glfwSetCharCallback(glfwWindow, (w, c) -> {
            if (c != GLFW_KEY_DELETE) {
                io.addInputCharacter(c);
            }
        });

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {//коллбэк для клавиш мыши
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }

            if (!io.getWantCaptureMouse() || gameViewWindow.getWantCaptureMouse()) {//блок мыши
                MouseListener.mouseButtonCallback(w, button, action, mods);//установка значений нажатий для мыши
            }

        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {//коллбэк для прокрутки мыши
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            if (!io.getWantCaptureMouse() || gameViewWindow.getWantCaptureMouse()) {//блок мыши
                MouseListener.mouseScrollCallback(w, xOffset, yOffset);//установка скролл значений для мыши
            }
        });

        io.setSetClipboardTextFn(new ImStrConsumer() {
            @Override
            public void accept(final String s) {
                glfwSetClipboardString(glfwWindow, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier() {
            @Override
            public String get() {
                final String clipboardString = glfwGetClipboardString(glfwWindow);
                if (clipboardString != null) {
                    return clipboardString;
                } else {
                    return "";
                }
            }
        });

        // ------------------------------------------------------------
        // Fonts configuration
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); //изначально выделенный объект должен быть явно уничтожен
        // Глифы могут быть добавлены для каждого шрифта, а также для конфигурации, используемой глобально, как здесь.
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesCyrillic());


        // Пример слияния шрифтов
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("src/main/java/org/engine/Resources/Textures/Font/impact.ttf", 16, fontConfig);
        fontConfig.destroy(); // После добавления всех шрифтов этот конфиг нам больше не нужен


        // Метод инициализирует средство визуализации LWJGL3.
        // Этот метод СЛЕДУЕТ вызывать после того, как вы инициализировали конфигурацию ImGui (шрифты и т. д.).
        // Также должен быть создан контекст ImGui.
        imGuiGlfw.init(glfwWindow, false);
        imGuiGl3.init("#version 330 core");
    }


    //==============================================================================================
    private void startFrame(final float deltaTime) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    private void endFrame() {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);//отвязка от фрейм баффера
        glViewport(0, 0, Window.getWidth(), Window.getHeight());//установка вьюпорта в дефолтное положение с дефолтным размером
        glClearColor(0, 0, 0, 1);
        glClear(GL_COLOR_BUFFER_BIT);

        // После того, как Dear ImGui подготовил данные отрисовки, мы используем их в рендерере LWJGL3.
        // В этот момент ImGui будет отображаться в текущем контексте OpenGL.

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        //система бэкапов текущего состояния
        //long backupWindowPtr = glfwGetCurrentContext();
        //ImGui.updatePlatformWindows();
        //ImGui.renderPlatformWindowsDefault();
        //glfwMakeContextCurrent(backupWindowPtr);
    }

    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
    //==============================================================================================

    public void update(float dt, Scene currentScene){
        startFrame(dt);

        // Любой код Dear ImGui ДОЛЖЕН находиться между методами ImGui.newFrame()/ImGui.render()
        setupDockspace();
        currentScene.imGui();
        //ImGui.showDemoWindow();//демо окно
        gameViewWindow.imGui();
        propertiesWindow.update(dt, currentScene);
        propertiesWindow.imGui();
        sceneHierarchyWindow.imGui();

        endFrame();
    }


    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        //ImGuiViewport mainViewport = ImGui.getMainViewport();
        //ImGui.setNextWindowPos(mainViewport.getWorkPosX(), mainViewport.getWorkPosY());
        //ImGui.setNextWindowSize(mainViewport.getWorkSizeX(), mainViewport.getWorkSizeY());
        //ImGui.setNextWindowViewport(mainViewport.getID());

        ImGui.setNextWindowPos(0.0f, 0.0f);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Система доккинга
        ImGui.dockSpace(ImGui.getID("Dockspace"));
        menuBar.imGui();//меню панель с инструментами
        ImGui.end();
    }

    public PropertiesWindow getPropertiesWindow() {
        return this.propertiesWindow;
    }
}
