package no.hiof.framework30.brunost.util;

import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.renderEngine.Renderer;
import no.hiof.framework30.brunost.util.Camera;

import java.util.ArrayList;
import java.util.List;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

/**
 * Handles everything that is necessary within runtime.
 * Contains the rendering, camera and actions to be performed for each object on start or each frame.
 * Can be thought of as "Stages", "Maps", or "Levels" in that they hold all objects within a certain playable area,
 * in addition to handling their rendering and object logic.
 *
 * @see GameObject
 * @see Camera
 * @see Renderer
 */
public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){

    }

    public void init(){

    }

    /**
     * Performs the actions that are desired to be performed once at the first frame call
     * for each GameObjects within the Scene.
     */
    public void onStart(){
        for(GameObject gameObject : gameObjects){
            gameObject.onStart();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    /**
     * Adds a GameObject to the Scene.
     * Can be used to dynamically transfer Objects from one Scene to another in runtime.
     * @param gameObject The GameObject to be added to the Scene
     */
    public void addGameObjectToScene(GameObject gameObject){
        if (!isRunning){
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.onStart();
            this.renderer.add(gameObject);
        }
    }

    /**
     * Performs the actions that are desired to be performed for each frame.
     *
     * @param deltaTime the speed at which the game runs in
     */
    public abstract void onUpdate(float deltaTime);

    public Camera camera(){
        return this.camera;
    }
}
