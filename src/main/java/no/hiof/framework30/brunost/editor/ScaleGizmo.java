package no.hiof.framework30.brunost.editor;

import no.hiof.framework30.brunost.components.Sprite;
import no.hiof.framework30.brunost.util.MouseListener;

public class ScaleGizmo extends Gizmo{
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.scale.x -= MouseListener.getWorldDeltaX();
            } else if (yAxisActive) {
                activeGameObject.transform.scale.y -= MouseListener.getWorldDeltaY();
            }
        }

        super.editorUpdate(dt);
    }

}
