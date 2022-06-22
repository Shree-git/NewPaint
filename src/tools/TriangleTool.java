package tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import paint.EditTools;

public class TriangleTool {
	GraphicsContext ctx;
	public Polygon triangle;
	int startX;
	int startY;
	int endX;
	int endY;
	double tempX;
	double tempY;
	
	public TriangleTool(GraphicsContext ctx, double x, double y) {
		this.ctx = ctx;
		triangle = new Polygon();
        triangle.getPoints().setAll(x,y);
        triangle.setStrokeWidth(EditTools.lineWidth);
        triangle.setStroke(EditTools.colorPicker.getValue());
        triangle.setFill(EditTools.fillPicker.getValue());          
        
        startX = (int)x;
        startY = (int)y;
	}
	
	public void mouseDrag(double x, double y) {
		if(x - startX >= 0){
            tempX = x;
        }else{
            tempX = x;
        }
        if(y - startY >= 0){
            tempY = y;
        }else{
            tempY = y;
        }
        triangle.getPoints().setAll((double)startX, (double)startY, (double)startX, tempY, tempX, tempY );
	}
	
	public void mouseRelease(double x, double y) {
		this.endX = (int)x;
        this.endY = (int)y;
		if(endX - startX >= 0){
            tempX = endX - startX;
        }else{
            tempX = startX - endX;
        }
        if(endY - startY >= 0){
            tempY = endY - startY;
        }else{
            tempY = startY - endY;
        }
	        
        this.ctx.strokePolygon(new double[]{startX, startX, triangle.getPoints().get(4)}, new double[]{startY, triangle.getPoints().get(3), triangle.getPoints().get(5)}, 3);
        this.ctx.fillPolygon(new double[]{startX, startX, triangle.getPoints().get(4)}, new double[]{startY, triangle.getPoints().get(3), triangle.getPoints().get(5)}, 3);

	}
}
