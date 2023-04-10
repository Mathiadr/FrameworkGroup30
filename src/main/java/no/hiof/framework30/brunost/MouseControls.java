package no.hiof.framework30.brunost;

import no.hiof.framework30.brunost.components.Component;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.util.MouseListener;
import no.hiof.framework30.brunost.util.Settings;
import no.hiof.framework30.brunost.util.Window;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject gameObject){
        this.holdingObject = gameObject;
        Window.getScene().addGameObjectToScene(gameObject);
    }

    public void place(){
        this.holdingObject = null;
    }

    @Override
    public void editorUpdate(float deltaTime){
        if (holdingObject != null){
            holdingObject.transform.position.x = MouseListener.getOrthoX();
            holdingObject.transform.position.y = MouseListener.getOrthoY();
            holdingObject.transform.position.x = (int)(holdingObject.transform.position.x / Settings.GRID_WIDTH) * Settings.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(holdingObject.transform.position.y / Settings.GRID_HEIGHT) * Settings.GRID_HEIGHT;


            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
                place();
        }
    }
}
