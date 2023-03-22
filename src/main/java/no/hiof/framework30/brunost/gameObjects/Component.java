package no.hiof.framework30.brunost.gameObjects;

public abstract class Component {

    public GameObject gameObject = null;

    public void onStart(){

    }
    public abstract void onUpdate(float deltaTime);
}
