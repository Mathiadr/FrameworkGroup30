package no.hiof.framework30.brunost.util;

// Callbacks: https://www.glfw.org/docs/3.3/input_guide.html#input_mouse

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, xLast, yLast;

    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.xLast = 0.0;
        this.yLast = 0.0;
    }

    public static MouseListener get(){
        if (MouseListener.instance == null)
            MouseListener.instance = new MouseListener();

        return MouseListener.instance;
    }

    public static void mousePosCallback(long window, double xPos, double yPos){
        get().xLast = get().xPos;
        get().yLast = get().yPos;
        get().xPos = xPos;
        get().yPos = yPos;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int modifier) {
        if (action == GLFW_PRESS) {
            if (button < get().mouseButtonPressed.length)
                get().mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE){
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame(){
        get().scrollX = 0;
        get().scrollY = 0;
        get().xLast = get().xPos;
        get().yLast = get().yPos;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getDeltaX(){
        return (float)(get().xLast - get().xPos);
    }

    public static float getDeltaY(){
        return (float)(get().yLast - get().yPos);
    }

    public static float getScrollX(){
        return (float)get().scrollX;
    }

    public static float getScrollY(){
        return (float)get().scrollY;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length)
            return get().mouseButtonPressed[button];
        else
            return false;
    }
}