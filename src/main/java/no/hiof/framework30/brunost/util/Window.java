package no.hiof.framework30.brunost.util;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import no.hiof.framework30.brunost.renderEngine.DebugDraw;
import no.hiof.framework30.brunost.renderEngine.Framebuffer;
import no.hiof.framework30.brunost.renderEngine.ImGuiLayer;
import no.hiof.framework30.brunost.scenes.LevelEditorScene;
import no.hiof.framework30.brunost.scenes.LevelScene;
import no.hiof.framework30.brunost.scenes.Scene;
import org.lwjgl.glfw.GLFW;
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

// Setup borrowed from https://www.lwjgl.org/guide

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

/**
 * Represents the Window the game is to be run inside.
 */
public class Window {
    int width, height;
    String title;
    private long glfwWindow;
    private ImGuiLayer imGuiLayer;
    private Framebuffer framebuffer;

    private static Window window = null;
    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;
    // Color values RGBA
    public float r = 0, b = 0, g = 0, a = 0;

    private Window(ImGuiLayer layer){
        this.width = 1920;
        this.height = 1080;
        this.title = "Brunost Engine";
        this.imGuiLayer = layer;
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();

                break;
            case 1:
                currentScene = new LevelScene();
                break;
            case 2:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }

        currentScene.load();
        currentScene.init();
        currentScene.onStart();
    }

    public static Window get(){
        if (Window.window == null){
            Window.window = new Window(new ImGuiLayer());
        }

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run(){
        System.out.println("Hello World!");

        initWindow();
        imGuiLayer.init(glfwWindow);
        loop();

        //Destroy the audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free the memory after loop has finished
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void initWindow(){
        // Setup error callback. As in WHERE the framework will print to when errors occur.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Handles the window for the framework.
        if ( !glfwInit())
            throw new IllegalStateException("unable to initialize GLFW");



        // Configure GLFW
        imGuiLayer.setGlslVersion("#version 130");
        // Configure window hints, which helps us perform operations against windows
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_FALSE);

        // Create the window. Holds the address space for where the window is stored
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create the GLFW window.");
        }
        // :: = lambda function. Means we're forwarding the position callback function to MouseListener's mousePosCallback
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);


        // Make the OpenGL context current.
        glfwMakeContextCurrent(glfwWindow);
        // Enables v-sync.
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(glfwWindow);

        //Initialize the audio device
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        audioDevice = alcOpenDevice(defaultDeviceName);

        int [] attributes = {0};
        audioContext = alcCreateContext(audioDevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audioDevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library not supported.";
        }

        // CRITICAL for LWJGL and GLFW's OpenGL context
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

        this.framebuffer = new Framebuffer(1920, 1080);

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = (float)glfwGetTime();
        float endTime;
        float deltaTime = -1.0f;


        while(!glfwWindowShouldClose(glfwWindow)){
            // Poll events. Gets all key, mouse events.
            glfwPollEvents();

            DebugDraw.beginFrame();

            // Sets Clear Color to white
            glClearColor(r, g,b, a);
            // Tells OpenGL how to buffer. Sets the clear color(above) to flush our entire screen.
            glClear(GL_COLOR_BUFFER_BIT);

            //this.framebuffer.bind();
            if(deltaTime >= 0) {
                DebugDraw.draw();
                currentScene.onUpdate(deltaTime);
            }
            this.framebuffer.unbind();

            imGuiLayer.onUpdate(deltaTime, currentScene);


            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();

            endTime = (float)glfwGetTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }

        currentScene.saveExit();
    }

    public void destroy(){
        imGuiLayer.destroy();
    }

    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }


    public void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public void setHeight(int newHeight) {
        get().height = newHeight;
    }
}
