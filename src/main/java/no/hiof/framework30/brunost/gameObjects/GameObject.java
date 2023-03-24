package no.hiof.framework30.brunost.gameObjects;

import no.hiof.framework30.brunost.Transform;

import java.util.ArrayList;
import java.util.List;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class GameObject {

    private String name;
    private List<Component> components;
    public Transform transform;


    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = new Transform();
    }

    public GameObject(String name, Transform transform){
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

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

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++){
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent (Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    public void onUpdate(float deltaTime){
        for (int i = 0; i < components.size(); i++){
            components.get(i).onUpdate(deltaTime);
        }
    }

    public void onStart(){
        for (int i = 0; i < components.size(); i++){
            components.get(i).onStart();
        }
    }


}
