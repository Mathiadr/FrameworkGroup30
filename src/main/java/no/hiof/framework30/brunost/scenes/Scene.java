package no.hiof.framework30.brunost.scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import no.hiof.framework30.brunost.Transform;
import no.hiof.framework30.brunost.components.ComponentDeserializer;
import no.hiof.framework30.brunost.components.GameObjectDeserializer;
import no.hiof.framework30.brunost.components.Component;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.renderEngine.Renderer;
import no.hiof.framework30.brunost.util.Camera;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

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

    public void sceneImgui(){
        if (activeGameObject != null){
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui(){

    }

    public GameObject createGameObject(String name){
        GameObject gameObject = new GameObject(name);
        gameObject.addComponent(new Transform());
        gameObject.transform = gameObject.getComponent(Transform.class);
        return gameObject;
    }

    public void saveExit(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!inFile.equals("")) {
            int maxGameObjectId = -1;
            int maxCompId = -1;
            GameObject[] gameObjectsArray = gson.fromJson(inFile, GameObject[].class);
            for (int i=0; i < gameObjectsArray.length; i++){
                addGameObjectToScene(gameObjectsArray[i]);

                for (Component component : gameObjectsArray[i].getAllComponents()){
                    if (component.getUid() > maxCompId)
                        maxCompId = component.getUid();
                }
                if (gameObjectsArray[i].getUid() > maxGameObjectId)
                    maxGameObjectId = gameObjectsArray[i].getUid();
            }

            maxGameObjectId++;
            maxCompId++;
            GameObject.init(maxGameObjectId++);
            Component.init(maxCompId++);
            this.levelLoaded = true;
        }
    }
}
