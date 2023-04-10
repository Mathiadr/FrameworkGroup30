package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.editor.PropertiesWindow;
import no.hiof.framework30.brunost.util.MouseListener;

public class TranslateGizmo extends Gizmo{

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldDeltaX();
            } else if (yAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldDeltaY();
            }
        }

        super.editorUpdate(dt);
    }
}
