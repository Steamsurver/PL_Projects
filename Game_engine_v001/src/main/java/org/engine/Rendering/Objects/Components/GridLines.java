package org.engine.Rendering.Objects.Components;

import org.engine.GameElements.Camera;
import org.engine.GameElements.Window;
import org.engine.Rendering.DebugDraw;
import org.engine.Resources.Utils.Settings;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class GridLines extends Component {
    @Override
    public void editorUpdate(float dt) {
        Camera camera = Window.getCurrentScene().getCamera();//получаем экземпляр текущей камеры
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int)Math.floor(cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_HEIGHT;//координата х откуда будут рисоваться первые линии
        float firstY = ((int)Math.floor(cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;//координата y откуда будут рисоваться первые линии

        int numVtLines = (int)(projectionSize.x * camera.getZoom() / Settings.GRID_WIDTH) + 2;//вертикальные линии
        int numHzLines = (int)(projectionSize.y * camera.getZoom() / Settings.GRID_HEIGHT) + 2;//горизонтальные линии

        float width = (int)(projectionSize.x * camera.getZoom())+ Settings.GRID_WIDTH * 2;//ширина до куда будут рисоваться линии
        float height = (int)(projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;//высота до куда будут рисоваться линии

        int maxLines = Math.max(numVtLines, numHzLines);
        Vector3f color = new Vector3f(0, 0, 0);
        for (int i=0; i < maxLines; i++) {
            float x = firstX + (Settings.GRID_WIDTH * i);
            float y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVtLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
