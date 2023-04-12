package no.hiof.framework30.brunost.components;

// Source: GamesWithGabe, 27.09.21 - https://www.youtube.com/playlist?list=PLtrSb4XxIVbp8AKuEAlwNXDxr99e3woGE

import imgui.ImGui;
import imgui.type.ImInt;
import no.hiof.framework30.brunost.components.Tile;
import no.hiof.framework30.brunost.components.Tileset;
import no.hiof.framework30.brunost.editor.JImGui;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Represents a broad range of classes implementing different functionalities.
 * Components are what the GameObject objects can use to gain additional important functionality at compile time or in runtime.
 */
public abstract class Component {
    private static int ID_COUNTER = 0;
    private  int uid = -1;

    public transient GameObject gameObject = null;

    /**
     * Performs the actions that are desired to be performed once at the first frame call.
     */
    public void onStart(){}
    /**
     * Performs the actions that are desired to be updated each frame
     *
     * @param   deltaTime the speed at which the engine is being run in
     */
    public void onUpdate(float deltaTime){}

    public void editorUpdate(float deltaTime){}

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
                    field.set(this, JImGui.dragInt(name, val));
                } else if (type == float.class) {
                    float val = (float)value;
                    field.set(this, JImGui.dragFloat(name, val));
                } else if (type == boolean.class) {
                    boolean val = (boolean) value;
                    if (ImGui.checkbox(name + ": ", val))
                        field.set(this, !val);
                } else if (type == Vector2f.class) {
                    Vector2f val = (Vector2f) value;
                    JImGui.drawVec2Control(name, val);
                } else if (type == Vector3f.class) {
                    Vector3f val = (Vector3f) value;
                    float[] imVec = {val.x, val.y, val.z};
                    if (ImGui.dragFloat(name + ": ", imVec))
                        val.set(imVec[0], imVec[1], imVec[2]);
                } else if (type == Vector4f.class) {
                    Vector4f val = (Vector4f) value;
                    JImGui.colorPicker4(name, val);
                } else if (type.isEnum()) {
                    String[] enumValues = getEnumValues(type);
                    String enumType = ((Enum)value).name();
                    ImInt index = new ImInt(indexOf(enumType, enumValues));
                    if (ImGui.combo(field.getName(), index, enumValues, enumValues.length)){
                        field.set(this, type.getEnumConstants()[index.get()]);
                    }
                }


                if (isPrivate)
                    field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void generateId(){
        if (this.uid == -1)
            this.uid = ID_COUNTER++;

    }

    private <T extends Enum<T>> String[] getEnumValues(Class<T> enumType){
        String[] enumValues = new String[enumType.getEnumConstants().length];
        int i = 0;
        for (T enumIntegerValue : enumType.getEnumConstants()){
            enumValues[i] = enumIntegerValue.name();
            i++;
        }
        return enumValues;
    }

    private int indexOf(String s, String[] arr){
        for (int i = 0; i < arr.length; i++){
            if (s.equals(arr[i]))
                return i;
        }
        return -1;
    }

    public void destroy(){

    }

    public int getUid(){
        return this.uid;
    }

    public static void init(int maxId){
        ID_COUNTER = maxId;
    }
}
