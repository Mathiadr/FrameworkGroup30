package no.hiof.framework30.brunost.util;

// Callbacks: https://www.glfw.org/docs/3.3/input_guide.html#input_mouse

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, xLast, yLast, worldX, worldY, lastWorldX, lastWorldY;

    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private int mouseButtonDown = 0;

    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f gameViewportSize = new Vector2f();

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
        if (get().mouseButtonDown > 0)
            get().isDragging = true;


        get().xLast = get().xPos;
        get().yLast = get().yPos;
        get().lastWorldX = get().worldX;
        get().lastWorldY = get().worldY;
        get().xPos = xPos;
        get().yPos = yPos;
        calcOrthoX();
        calcOrthoY();
    }

    public static void mouseButtonCallback(long window, int button, int action, int modifier) {
        if (action == GLFW_PRESS) {
            get().mouseButtonDown++;
            if (button < get().mouseButtonPressed.length)
                get().mouseButtonPressed[button] = true;
        } else if (action == GLFW_RELEASE){
            get().mouseButtonDown--;
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
        get().lastWorldX = get().worldX;
        get().lastWorldY = get().worldY;
    }

    public static float getX(){
        return (float)get().xPos;
    }

    public static float getY(){
        return (float)get().yPos;
    }

    public static float getOrthoX(){
        return (float)get().worldX;
    }

    public static float getOrthoY(){
        return (float)get().worldY;
    }

    public static boolean mouseButtonDown(int button){
        if(button < get().mouseButtonPressed.length)
            return get().mouseButtonPressed[button];
        else
            return false;
    }

    public static void setGameViewPos(Vector2f gameViewPos) {
        get().gameViewportPos.set(gameViewPos);
    }

    public static void setGameViewSize(Vector2f gameViewSize) {
        get().gameViewportSize.set(gameViewSize);
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

    public static float getWorldDeltaX(){
        return (float)(get().lastWorldX - get().worldX);
    }

    public static float getWorldDeltaY(){
        return (float)(get().lastWorldY - get().worldY);
    }

    public static float getScreenX() {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * 1920.0f;
        return currentX;
    }

    public static float getScreenY() {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = 1080.0f - ((currentY / get().gameViewportSize.y) * 1080.0f);
        return currentY;
    }

    private static void calcOrthoX() {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * 2.0f - 1.0f;
        Vector4f tmp = new Vector4f(currentX, 0, 0, 1);

        Camera camera = Window.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        get().worldX = tmp.x;
    }

    private static void calcOrthoY() {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = -((currentY / get().gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f tmp = new Vector4f(0, currentY, 0, 1);

        Camera camera = Window.getScene().camera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseView().mul(camera.getInverseProjection(), viewProjection);
        tmp.mul(viewProjection);

        get().worldY = tmp.y;
    }


    public static boolean isDragging(){
        return get().isDragging;
    }

}