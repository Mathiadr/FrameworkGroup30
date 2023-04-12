package no.hiof.framework30.brunost.util;

import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.observers.EventSystem;
import no.hiof.framework30.brunost.observers.Observer;
import no.hiof.framework30.brunost.observers.events.Event;
import no.hiof.framework30.brunost.observers.events.EventType;
import no.hiof.framework30.brunost.renderEngine.*;
import no.hiof.framework30.brunost.scenes.LevelEditorSceneInitializer;
import no.hiof.framework30.brunost.scenes.Scene;
import no.hiof.framework30.brunost.scenes.SceneInitializer;
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
public class Window implements Observer {
    private int width, height;
    private String title;
    private long glfwWindow;
    private ImGuiLayer imguiLayer;
    private Framebuffer framebuffer;
    private PickingTexture pickingTexture;
    private boolean editorMode = true;

    private static Window window = null;
    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;
    // Color values RGBA

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Brunost Engine";
        EventSystem.addObserver(this);
    }

    public static void changeScene(SceneInitializer sceneInitializer){
        if (currentScene != null){
            currentScene.destroy();
        }


        getImguiLayer().getPropertiesWindow().setActiveGameObject(null);
        currentScene = new Scene(sceneInitializer);
        currentScene.load();
        currentScene.init();
        currentScene.onStart();
    }

    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run(){
        System.out.println("Hello World!");

        initWindow();
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

        // Configure window hints, which helps us perform operations against windows
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

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
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
           Window.setWidth(newWidth);
           Window.setHeight(newHeight);
        });


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
        this.pickingTexture = new PickingTexture(1920, 1080);
        glViewport(0,0, 1920, 1080);

        this.imguiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imguiLayer.init();

        Window.changeScene(new LevelEditorSceneInitializer());
    }

    public void loop(){
        float beginTime = (float)glfwGetTime();
        float endTime;
        float deltaTime = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");


        while(!glfwWindowShouldClose(glfwWindow)){
            // Poll events. Gets all key, mouse events.
            glfwPollEvents();


            glDisable(GL_BLEND);
            pickingTexture.enableWriting();

            glViewport(0, 0, 1920, 1080);
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);

            DebugDraw.beginFrame();
            this.framebuffer.bind();
            // Sets Clear Color to white
            glClearColor(1, 1, 1, 1);
            // Tells OpenGL how to buffer. Sets the clear color(above) to flush our entire screen.
            glClear(GL_COLOR_BUFFER_BIT);

            if(deltaTime >= 0) {
                DebugDraw.draw();
                Renderer.bindShader(defaultShader);
                if (editorMode)
                    currentScene.editorUpdate(deltaTime);

                else
                    currentScene.onUpdate(deltaTime);
                currentScene.render();
            }
            this.framebuffer.unbind();

            imguiLayer.onUpdate(deltaTime, currentScene);
            glfwSwapBuffers(glfwWindow);
            MouseListener.endFrame();

            endTime = (float)glfwGetTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public void destroy(){
        imguiLayer.destroy();
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch  (event.type){
            case GameEngineStartPlay:
                System.out.println("Starting play!");
                this.editorMode = false;
                currentScene.save();
                Window.changeScene(new LevelEditorSceneInitializer()); // Save & Reset
                break;
            case GameEngineStopPlay:
                System.out.println("Stopping play");
                this.editorMode = true;
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case LoadLevel:
                System.out.println("Loading scene");
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case SaveLevel:
                System.out.println("Saving scene");
                currentScene.save();
                break;
        }
        if(event.type == EventType.GameEngineStartPlay){
            System.out.println("Starting play");
        }
        if (event.type == EventType.GameEngineStopPlay){
            System.out.println("Stopping play");
        }
    }


    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }


    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

    public static Framebuffer getFramebuffer(){
        return get().framebuffer;
    }

    public static float getTargetAspectRatio(){
        return 16.0f / 9.0f;
    }

    public boolean isInEditorMode() {
        return editorMode;
    }

    public void setEditorMode(boolean editorMode) {
        this.editorMode = editorMode;
    }

    public static ImGuiLayer getImguiLayer() {
        return get().imguiLayer;
    }

}
