package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.editor.PropertiesWindow;
import no.hiof.framework30.brunost.gameObjects.GameObject;
import no.hiof.framework30.brunost.util.KeyListener;
import no.hiof.framework30.brunost.util.Settings;
import no.hiof.framework30.brunost.util.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component{
    private float debounceTime = 0.2f;
    private float debounce = 0.0f;
}
