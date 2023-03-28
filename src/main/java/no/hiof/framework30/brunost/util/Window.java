package no.hiof.framework30.brunost.util;

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
public class Window {
    int width, height;
    String title;
    private long glfwWindow;

    private static Window window = null;
    private long audioContext;
    private long audioDevice;

    private static Scene currentScene;
    // Color values RGBA
    public float r = 0, b = 0, g = 0, a = 0;

    private Window(){
        this.width = 1920;
        this.height = 1080;
        this.title = "Brunost Engine";
        r = 1;
        b = 1;
        g = 1;
        a = 1;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                currentScene.onStart();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                currentScene.onStart();
                break;
            case 2:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get(){
        if (Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run(){
        System.out.println("Hello World!");

        init();
        loop();

        //Destoy the audio context
        alcDestroyContext(audioContext);
        alcCloseDevice(audioDevice);

        // Free the memory after loop has finished
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        // Setup error callback. As in WHERE the framework will print to when errors occur.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Handles the window for the framework.
        if ( !glfwInit())
            throw new IllegalStateException("unable to initialize GLFW");

        // Configure GLFW
        // Configure window hints, which helps us perform operations against windows
        glfwDefaultWindowHints();
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

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float deltaTime = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            // Poll events. Gets all key, mouse events.
            glfwPollEvents();

            // Sets Clear Color to white
            glClearColor(r, g,b, a);
            // Tells OpenGL how to buffer. Sets the clear color(above) to flush our entire screen.
            glClear(GL_COLOR_BUFFER_BIT);

            if(deltaTime >= 0)
                currentScene.onUpdate(deltaTime);


            glfwSwapBuffers(glfwWindow);

            endTime = Time.getTime();
            deltaTime = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
