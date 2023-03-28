package no.hiof.framework30.brunost.util;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class KeyListener {
    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350]; // 350 is amount of keybindings GLFW has

    private KeyListener(){

    }

    public static KeyListener get(){
        if (KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int modifiers){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode){
        return get().keyPressed[keyCode];
    }


}
