package paint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PencilTool {
	GraphicsContext graphicsContext;
	
	public PencilTool(GraphicsContext ctx) {
		this.graphicsContext = ctx;
	}
	
	public void mousePress(double x, double y) {
		graphicsContext.beginPath();
        graphicsContext.moveTo(x, y);
        graphicsContext.setStroke(EditTools.colorPicker.getValue()); //Sets the color for the brush
        graphicsContext.stroke();
	}
	
	public void mouseDrag(double x, double y) {
		 graphicsContext.lineTo(x, y);
         graphicsContext.stroke();
         graphicsContext.closePath();
         graphicsContext.beginPath();
         graphicsContext.moveTo(x, y);	
	}
	
	public void mouseRelease(double x, double y) {
		graphicsContext.lineTo(x, y);
        graphicsContext.stroke();
        graphicsContext.closePath();
	}
}
