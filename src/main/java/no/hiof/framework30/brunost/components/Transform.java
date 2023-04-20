package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.editor.JImGui;
import org.joml.Vector2f;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

/**
 * The 2D properties of the object in position and scale.
 * @see no.hiof.framework30.brunost.gameObjects.GameObject
 */
public class Transform extends Component {

    public Vector2f position;
    public Vector2f scale;
    public float rotation = 0.0f;
    public int zIndex;

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
        this.zIndex = 0;
    }

    @Override
    public void imgui() {
        JImGui.drawVec2Control("Position", this.position);
        JImGui.drawVec2Control("Scale", this.scale, 32.0f);
        this.rotation = JImGui.dragFloat("Rotation", this.rotation);
        this.zIndex = JImGui.dragInt("Z-Index", this.zIndex);
    }

    public Transform copy(){
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to){
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Transform)) return false;
        Transform transform = (Transform) obj;
        return transform.position.equals(this.position) && transform.scale.equals(this.scale) &&
                transform.rotation == this.rotation && transform.zIndex == this.zIndex;
    }
}
