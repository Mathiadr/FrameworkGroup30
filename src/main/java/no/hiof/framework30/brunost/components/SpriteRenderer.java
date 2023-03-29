package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.gameObjects.Component;
import org.joml.Vector4f;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

/**
 * Facilitates the rendering of Sprites
 */
public class SpriteRenderer extends Component {

    private Vector4f color;

    public SpriteRenderer(Vector4f color){
        this.color = color;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onUpdate(float deltaTime) {

    }

    public Vector4f getColor(){
        return this.color;
    }
}
