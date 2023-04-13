package no.hiof.framework30.brunost.renderEngine;


import imgui.*;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;
import imgui.internal.ImGuiDockNode;
import imgui.internal.ImGuiWindow;
import imgui.type.ImBoolean;
import no.hiof.framework30.brunost.editor.GameViewWindow;
import no.hiof.framework30.brunost.editor.MenuBar;
import no.hiof.framework30.brunost.editor.PropertiesWindow;
import no.hiof.framework30.brunost.scenes.Scene;
import no.hiof.framework30.brunost.util.MouseListener;
import no.hiof.framework30.brunost.util.Window;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class ImGuiLayer {
    private long glfwWindow;
    private String glslVersion = "#version 330 core";
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    private MenuBar menuBar;
    private GameViewWindow gameViewWindow;
    private PropertiesWindow propertiesWindow;

    public ImGuiLayer(long glfwWindow, PickingTexture pickingTexture){
        this.glfwWindow = glfwWindow;
        this.gameViewWindow = new GameViewWindow();
        this.propertiesWindow = new PropertiesWindow(pickingTexture);
        this.menuBar = new MenuBar();
    }
    public void init(){
        ImGui.createContext();

        final ImGuiIO io = ImGui.getIO();

        io.setIniFilename("imgui.ini");

        // Configs =================================================================================================
        io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
        //io.setConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setConfigFlags(ImGuiConfigFlags.DockingEnable);


        glfwSetMouseButtonCallback(glfwWindow, (w, button, action, mods) -> {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW_MOUSE_BUTTON_1 && action != GLFW_RELEASE;
            mouseDown[1] = button == GLFW_MOUSE_BUTTON_2 && action != GLFW_RELEASE;
            mouseDown[2] = button == GLFW_MOUSE_BUTTON_3 && action != GLFW_RELEASE;
            mouseDown[3] = button == GLFW_MOUSE_BUTTON_4 && action != GLFW_RELEASE;
            mouseDown[4] = button == GLFW_MOUSE_BUTTON_5 && action != GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() || gameViewWindow.getWantCaptureMouse()) {
                MouseListener.mouseButtonCallback(w, button, action, mods);
            }

            if (!io.getWantCaptureMouse() && mouseDown[1]) {
                ImGui.setWindowFocus(null);
            }
        });

        glfwSetScrollCallback(glfwWindow, (w, xOffset, yOffset) -> {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            MouseListener.mouseScrollCallback(w, xOffset, yOffset);
        });


        // Fonts ==================================================================================================
        ImFontAtlas fontAtlas = io.getFonts();
        ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());


        // Default font
        // fontAtlas.addFontDefault();

        // Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF("assets/fonts/Silkscreen/slkscr.ttf", 16, fontConfig);



        fontConfig.destroy();

        imGuiGl3.init(glslVersion);
        imGuiGlfw.init(glfwWindow, true);
    }

    public void onUpdate(float deltaTime, Scene currentScene){
        startFrame(deltaTime);

        ImGui.dockSpaceOverViewport(ImGui.getMainViewport(), ImGuiDockNodeFlags.PassthruCentralNode);
        setupDockspace();
        currentScene.imgui();
        gameViewWindow.imgui();
        propertiesWindow.update(deltaTime, currentScene);
        propertiesWindow.imgui();
        currentScene.sceneImgui();

        endFrame();
    }

    public void startFrame(float deltaTime){
        imGuiGlfw.newFrame();
        ImGui.newFrame();
    }

    public void endFrame(){
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glViewport(0,0, Window.getWidth(), Window.getHeight());
        glClearColor(0,0,0,1);
        glClear(GL_COLOR_BUFFER_BIT);

        ImGui.render();

        imGuiGl3.renderDrawData(ImGui.getDrawData());
        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
        }
    }

    private void setupDockspace(){
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        //ImGui.dockSpaceOverViewport(ImGui.getMainViewport());
        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(Window.getWidth(), Window.getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);
        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;
        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        //ImGuiDockNodeFlags.PassthruCentralNode

        // Dockspace
        ImGui.dockSpace(ImGui.getID("Dockspace Demo"));
        menuBar.imgui();
        ImGui.end();
    }

    public void destroy(){
        imGuiGl3.dispose();
        imGuiGlfw.dispose();
        ImGui.destroyContext();
        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
    }

    public long getGlfwWindow() {
        return glfwWindow;
    }

    public void setGlfwWindow(long glfwWindow) {
        this.glfwWindow = glfwWindow;
    }

    public String getGlslVersion() {
        return glslVersion;
    }

    public void setGlslVersion(String glslVersion) {
        this.glslVersion = glslVersion;
    }

    public GameViewWindow getGameViewWindow() {
        return gameViewWindow;
    }

    public void setGameViewWindow(GameViewWindow gameViewWindow) {
        this.gameViewWindow = gameViewWindow;
    }

    public PropertiesWindow getPropertiesWindow() {
        return propertiesWindow;
    }

    public void setPropertiesWindow(PropertiesWindow propertiesWindow) {
        this.propertiesWindow = propertiesWindow;
    }
}