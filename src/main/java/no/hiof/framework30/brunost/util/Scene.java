package no.hiof.framework30.brunost.util;

import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.util.Camera;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

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
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject){
        if (!isRunning){
            gameObjects.add(gameObject);
        } else {
            gameObjects.add(gameObject);
            gameObject.onStart();
        }
    }

    public abstract void onUpdate(float deltaTime);
}
