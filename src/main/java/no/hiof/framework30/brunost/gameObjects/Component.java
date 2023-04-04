package no.hiof.framework30.brunost.gameObjects;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

import imgui.ImGui;
import no.hiof.framework30.brunost.components.Tile;
import no.hiof.framework30.brunost.components.Tileset;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Represents a broad range of classes implementing different functionalities.
 * Components are what the GameObject objects can use to gain additional important functionality at compile time or in runtime.
 */
public abstract class Component {

    public transient GameObject gameObject = null;

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
    public void onUpdate(float deltaTime){

    }

    public void imgui(){
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields){
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if (isTransient)
                    continue;
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if (isPrivate)
                    field.setAccessible(true);


                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if (type == int.class) {
                    int val = (int)value;
                    int[] imInt = {val};
                    if (ImGui.dragInt(name + ": ", imInt))
                        field.set(this, imInt);
                } else if (type == float.class) {
                    float val = (float)value;
                    float[] imFloat = {val};
                    if (ImGui.dragFloat(name + ": ", imFloat))
                        field.set(this, imFloat);
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val))
                        field.set(this, !val);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat(name + ": ", imVec))
                        val.set(imVec[0], imVec[1], imVec[2]);
                }
                else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if (ImGui.dragFloat(name + ": ", imVec))
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                }
                if (isPrivate)
                    field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
