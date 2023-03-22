package no.hiof.framework30.brunost;

public abstract class Scene {

    protected Camera camera;

    public Scene(){

    }

    public void init(){

    }

    public abstract void onUpdate(float deltaTime);
}
