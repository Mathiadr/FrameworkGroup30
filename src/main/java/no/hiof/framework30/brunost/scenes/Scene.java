package no.hiof.framework30.brunost.scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import no.hiof.framework30.brunost.Transform;
import no.hiof.framework30.brunost.components.ComponentDeserializer;
import no.hiof.framework30.brunost.components.GameObjectDeserializer;
import no.hiof.framework30.brunost.components.Component;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.physicsEngine.Physics2D;
import no.hiof.framework30.brunost.renderEngine.Renderer;
import no.hiof.framework30.brunost.util.Camera;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class Scene {

    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private Physics2D physics;

    private SceneInitializer sceneInitializer;

    public Scene(SceneInitializer sceneInitializer){
        this.sceneInitializer = sceneInitializer;
        this.physics = new Physics2D();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    public void init(){
        this.camera = new Camera(new Vector2f(-250, 0));
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }

    /**
     * Performs the actions that are desired to be performed once at the first frame call
     * for each GameObjects within the Scene.
     */
    public void onStart(){
        for (int i=0; i<gameObjects.size(); i++){
            GameObject gameObject = gameObjects.get(i);
            gameObject.onStart();
            this.renderer.add(gameObject);
            this.physics.add(gameObject);
        }
        isRunning = true;
    }

    public GameObject createGameObject(String name){
        GameObject gameObject = new GameObject(name);
        gameObject.addComponent(new Transform());
        gameObject.transform = gameObject.getComponent(Transform.class);
        return gameObject;
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
            this.physics.add(gameObject);
        }
    }

    public GameObject getGameObject(int gameObjectId){
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }



    /**
     * Performs the actions that are desired to be performed for each frame.
     *
     * @param deltaTime the speed at which the game runs in
     */
    public void onUpdate(float deltaTime){
        this.camera.adjustProjection();
        this.physics.onUpdate(deltaTime);

        for(int i=0; i<gameObjects.size(); i++){
            GameObject gameObject = gameObjects.get(i);
            gameObject.onUpdate(deltaTime);

            if (gameObject.isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObject);
                this.physics.destroyGameObject(gameObject);
                i--;
            }
        }
    }

    public void editorUpdate(float deltaTime){
        this.camera.adjustProjection();

        for(int i=0; i<gameObjects.size(); i++){
            GameObject gameObject = gameObjects.get(i);
            gameObject.editorUpdate(deltaTime);

            if (gameObject.isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(gameObject);
                this.physics.destroyGameObject(gameObject);
                i--;
            }
        }
    }

    public void render(){
        this.renderer.render();
    }

    public Camera camera(){
        return this.camera;
    }

    public void sceneImgui(){
        imgui();
    }

    public void imgui(){
        this.sceneInitializer.imgui();
    }

    public void save(){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            List<GameObject> objsToSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects) {
                if (obj.doSerialization()) {
                    objsToSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(objsToSerialize));
            writer.close();
        } catch(IOException e) {
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
        }
    }

    public void destroy(){
        for (GameObject gameObject : gameObjects){
            gameObject.destroy();
        }
    }


    public List<GameObject> getGameObjects(){
        return this.gameObjects;
    }
}
