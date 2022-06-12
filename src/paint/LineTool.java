package paint;

import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;


public class LineTool {
	Line line;
	GraphicsContext ctx;
	int startX;
	int startY;
	int endX;
	int endY;
	
	public LineTool(GraphicsContext ctx, double x,double y) {
		this.ctx = ctx;
		this.line = new Line(x,y,x,y);
	    this.line.setStrokeWidth(EditTools.lineWidth);
	    this.line.setStroke(EditTools.colorPicker.getValue());
	     
	    this.startX = (int)x;
	    this.startY = (int)y;
	}
	
	public void mouseDrag(double x, double y) {
		this.line.setEndX(x);
        this.line.setEndY(y); 
	}
	
	public void mouseRelease(double x, double y) {
        endX = (int)x;
        endY = (int)y;
        
        draw();
	}
	
	public void draw() {
        this.ctx.strokeLine(startX, startY, endX, endY);
	}
	
}
