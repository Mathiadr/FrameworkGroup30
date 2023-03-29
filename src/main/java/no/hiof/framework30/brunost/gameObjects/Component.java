package no.hiof.framework30.brunost.gameObjects;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

import no.hiof.framework30.brunost.components.Tile;
import no.hiof.framework30.brunost.components.Tileset;

/**
 * Represents a broad range of classes implementing different functionalities.
 * Components are what the GameObject objects can use to gain additional important functionality at compile time or in runtime.
 */
public abstract class Component {

    public GameObject gameObject = null;

    /**
     * Performs the actions that are desired to be performed once at the first frame call.
     */
    public void onStart(){

    }
    /**
     * Performs the actions that are desired to be updated each frame
     *
     * @param   deltaTime the speed at which the engine is being run in
     */
    public abstract void onUpdate(float deltaTime);
}
