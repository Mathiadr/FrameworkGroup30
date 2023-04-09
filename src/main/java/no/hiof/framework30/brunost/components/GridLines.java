package no.hiof.framework30.brunost.components;

import no.hiof.framework30.brunost.renderEngine.DebugDraw;
import no.hiof.framework30.brunost.util.Camera;
import no.hiof.framework30.brunost.util.Settings;
import no.hiof.framework30.brunost.util.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component{

    @Override
    public void onUpdate(float deltaTime) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_HEIGHT;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;

        int numVerticalLines    = (int)(projectionSize.x * camera.getZoom() / Settings.GRID_WIDTH) + 2;
        int numHorizontalLines  = (int)(projectionSize.y * camera.getZoom() / Settings.GRID_HEIGHT) + 2;

        int width = (int)(projectionSize.x * camera.getZoom()) + Settings.GRID_WIDTH * 2;
        int height = (int)(projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;

        int maxLines = Math.max(numVerticalLines, numHorizontalLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
        for (int i=0; i < maxLines; i++){
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVerticalLines)
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            if (i < numHorizontalLines)
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
        }
    }
}