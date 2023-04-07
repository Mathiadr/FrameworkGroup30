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
import no.hiof.framework30.brunost.scenes.Scene;
import no.hiof.framework30.brunost.util.MouseListener;
import no.hiof.framework30.brunost.util.Window;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {
    private long glfwWindow;
    private String glslVersion = null;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public ImGuiLayer(){
    }
    public void init(long glfwWindow){
        this.glfwWindow = glfwWindow;
        ImGui.createContext();

        glslVersion = "#version 330 core";

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

            if (!io.getWantCaptureMouse() || GameViewWindow.getWantCaptureMouse()) {
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

        imGuiGlfw.newFrame();
        ImGui.newFrame();
        ImGui.dockSpaceOverViewport(ImGui.getMainViewport(), ImGuiDockNodeFlags.PassthruCentralNode);
        setupDockspace();
        currentScene.sceneImgui();
        GameViewWindow.imgui();
        ImGui.end();
        ImGui.render();

        endFrame();

    }

    public void startFrame(float deltaTime){

    }

    public void endFrame(){
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
}