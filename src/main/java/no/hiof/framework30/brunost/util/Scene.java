package no.hiof.framework30.brunost.util;

import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.renderEngine.Renderer;
import no.hiof.framework30.brunost.util.Camera;

import java.util.ArrayList;
import java.util.List;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    public Scene(){

    }

    public void init(){

    }

    public void onStart(){
        for(GameObject gameObject : gameObjects){
            gameObject.onStart();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject){
        if (!isRunning){
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.onStart();
            this.renderer.add(gameObject);
        }
    }

    public abstract void onUpdate(float deltaTime);

    public Camera camera(){
        return this.camera;
    }
}
