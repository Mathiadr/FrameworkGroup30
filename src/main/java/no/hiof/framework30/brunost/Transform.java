package no.hiof.framework30.brunost;

import org.joml.Vector2f;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE
public class Transform {

    public Vector2f position;
    public Vector2f scale;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }
    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }
    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale){
        this.position =  position;
        this.scale = scale;
    }
}
