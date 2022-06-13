package paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class EraserTool extends PencilTool{

	public EraserTool(GraphicsContext ctx) {
		super(ctx);
	}
	
	public void mousePress(double x, double y) {
		graphicsContext.beginPath();
        graphicsContext.moveTo(x, y);
        graphicsContext.setStroke(Color.WHITE); //Sets the color for the brush
        graphicsContext.stroke();
	}
}
