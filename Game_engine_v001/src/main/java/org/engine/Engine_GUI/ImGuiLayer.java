package org.engine.Engine_GUI;

import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;
import org.engine.Editor.GameViewWindow;
import org.engine.GameController.KeyListener;
import org.engine.GameController.MouseListener;
import org.engine.scenes.Scene;
import org.engine.GameElements.Window;

import static org.lwjgl.glfw.GLFW.*;


public class ImGuiLayer {

    private long glfwWindow;
    private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];// Курсоры для мыши предоставленые GLFW
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();// LWJGL3 renderer (ДОЛЖЕН быть инициализирован)



    public ImGuiLayer(long glfwWindow){
        this.glfwWindow = glfwWindow;
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
        io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard); // Навигация с клавиатуры
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);//включаем флаги окон
        //io.setConfigFlags(ImGuiConfigFlags.ViewportsEnable);//включаем флаги окон


        io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors); // Курсоры мыши для отображения при изменении размера окон и т. д.
        io.setBackendPlatformName("imgui_java_impl_glfw");

        // ------------------------------------------------------------
        // Отображение клавиатуры. ImGui будет использовать эти индексы для просмотра массива io.KeysDown[].
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW_KEY_Z;
        io.setKeyMap(keyMap);

        // ------------------------------------------------------------
        // Отображение курсоров мыши
        mouseCursors[ImGuiMouseCursor.Arrow] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = glfwCreateStandardCursor(GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = glfwCreateStandardCursor(GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = glfwCreateStandardCursor(GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = glfwCreateStandardCursor(GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = glfwCreateStandardCursor(GLFW_ARROW_CURSOR);

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

        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
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

            if (!io.getWantCaptureMouse() || GameViewWindow.getWantCaptureMouse()) {
                MouseListener.mouseButtonCallback(w, button, action, mods);
            }

        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
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
        imGuiGl3.init("#version 330 core");
    }


    //==============================================================================================
    private void startFrame(final float deltaTime) {
        // Получаем свойства окна и положение мыши

        float[] winWidth = {Window.getWidth()};
        float[] winHeight = {Window.getHeight()};

        double[] mousePosX = {0};
        double[] mousePosY = {0};
        glfwGetCursorPos(glfwWindow, mousePosX, mousePosY);

        // Мы ДОЛЖНЫ вызывать эти методы для обновления состояния Dear ImGui для текущего кадра.
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(deltaTime);

        // Обновление курсора мыши.
        final int imguiCursor = ImGui.getMouseCursor();
        glfwSetCursor(glfwWindow, mouseCursors[imguiCursor]);
        glfwSetInputMode(glfwWindow, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
    }

    private void endFrame() {
        // После того, как Dear ImGui подготовил данные отрисовки, мы используем их в рендерере LWJGL3.
        // В этот момент ImGui будет отображаться в текущем контексте OpenGL.
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    private void destroyImGui() {
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }
    //==============================================================================================

    public void update(float dt, Scene currentScene){
        startFrame(dt);

        // Любой код Dear ImGui ДОЛЖЕН находиться между методами ImGui.newFrame()/ImGui.render()
        ImGui.newFrame();

        setupDockspace();
        currentScene.sceneImGui();
        ImGui.showDemoWindow();
        GameViewWindow.imgui();
        ImGui.end();
        ImGui.render();

        endFrame();
    }


    private void setupDockspace() {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse |
                ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove |
                ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // Dockspace
        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }
}
