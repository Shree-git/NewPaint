package tools;

import javafx.scene.canvas.GraphicsContext;

public class CircleTool extends EllipseTool{

	public CircleTool(GraphicsContext ctx, double x, double y) {
		super(ctx, x, y);
	}
	
	public void mouseDrag(double x) {
		if(x - this.startX >= 0){
			this.tempX = x - this.startX;
        }else{
        	this.tempX = this.startX - x;            
        }
        this.ellipse.setRadiusX(this.tempX);
        this.ellipse.setRadiusY(this.tempX); 
	}
	
	public void mouseRelease(double x) {
		this.endX = (int)x;
		double tempRadius = this.ellipse.getRadiusX();
        if(this.endX - this.startX >= 0){
        	this.tempX = this.endX - this.startX;
        }else{
        	this.tempX = this.startX - this.endX;
        }
        
		this.ctx.strokeOval(this.startX - tempRadius, this.startY - tempRadius, this.tempX + tempRadius, this.tempX + tempRadius);
        this.ctx.fillOval(this.startX - tempRadius, this.startY - tempRadius, this.tempX + tempRadius, this.tempX + tempRadius);
    }
	
}
