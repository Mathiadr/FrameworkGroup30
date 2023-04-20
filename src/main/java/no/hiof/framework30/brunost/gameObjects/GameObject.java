package no.hiof.framework30.brunost.gameObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import no.hiof.framework30.brunost.components.Transform;
import no.hiof.framework30.brunost.components.Component;
import no.hiof.framework30.brunost.util.ComponentDeserializer;
import no.hiof.framework30.brunost.util.GameObjectDeserializer;
import no.hiof.framework30.brunost.components.SpriteRenderer;
import no.hiof.framework30.brunost.util.AssetPool;

import java.util.ArrayList;
import java.util.List;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

/**
 * Generic Class that describes any object within a Scene
 * @author Mathias R.
 *
 */
public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;


    private String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;
    private boolean isDead = false;

    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
        this.uid = ID_COUNTER++;
    }


    /**
     * Returns the Component that is desired from this instance.
     *
     * @param   componentClass the Class type of the Component that is desired
     * @return  The actual Component attached to this object
     * @see     Component
     */
    public <T extends Component> T getComponent(Class<T> componentClass){
        for (Component c :
             components) {
            if(componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: (Casting Component)";
                }
            }
        }

        return null;
    }

    /**
     * Removes the specified Component from this instance.
     *
     * @param   componentClass the Class type of the Component that is desired
     * @see     Component
     */
    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++){
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    /**
     * Adds the specified Component to this instance.
     *
     * @param   component the Component object
     * @see     Component
     */
    public void addComponent (Component component) {
        component.generateId();
        this.components.add(component);
        component.gameObject = this;
    }

    /**
     * Accesses each of the components' onUpdate method.
     * Performs all the actions that is to be updated each frame for each component.
     *
     * @param   deltaTime the speed at which the engine is being run in
     */
    public void onUpdate(float deltaTime){
        for (int i = 0; i < components.size(); i++){
            components.get(i).onUpdate(deltaTime);
        }
    }

    public void editorUpdate(float deltaTime){
        for (int i=0; i < components.size(); i++)
            components.get(i).editorUpdate(deltaTime);
    }

    /**
     * Accesses each of the components' onStart method.
     * This method runs at the first frame.
     */
    public void onStart(){
        for (int i = 0; i < components.size(); i++){
            components.get(i).onStart();
        }
    }

    public void imgui(){
        for (Component component : components){
            if (ImGui.collapsingHeader(component.getClass().getSimpleName()))
                component.imgui();
        }
    }

    public void destroy(){
        this.isDead = true;
        for (int i=0; i < components.size(); i++)
            components.get(i).destroy();
    }

    public GameObject copy(){
        // TODO: come up with cleaner solution
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Component.class, new ComponentDeserializer())
                .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                .enableComplexMapKeySerialization()
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);

        obj.generateUid();
        for (Component c : obj.getAllComponents()) {
            c.generateId();
        }

        SpriteRenderer sprite = obj.getComponent(SpriteRenderer.class);
        if (sprite != null && sprite.getTexture() != null) {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilepath()));
        }

        return obj;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }

    public int getUid(){
        return this.uid;
    }

    public void generateUid(){
        this.uid = ID_COUNTER++;
    }

    public List<Component> getAllComponents(){
        return this.components;
    }

    public boolean doSerialization() {
        return this.doSerialization;
    }

    public void setNoSerialization() {
        this.doSerialization = false;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }
}
