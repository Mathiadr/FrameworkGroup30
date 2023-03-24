package no.hiof.framework30.brunost.gameObjects;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public abstract class Component {

    public GameObject gameObject = null;

    public void onStart(){

    }
    public abstract void onUpdate(float deltaTime);
}
