package tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import paint.EditTools;

public class EllipseTool {
	
	public Ellipse ellipse;
	GraphicsContext ctx;
	int startX;
	int startY;
	int endX;
	int endY;
	double tempX;
	double tempY;
	
	public EllipseTool(GraphicsContext ctx, double x,double y) {
		this.ctx = ctx;
		this.ellipse = new Ellipse(x, y, 0, 0);
	    this.ellipse.setStrokeWidth(EditTools.lineWidth);
	    this.ellipse.setStroke(EditTools.colorPicker.getValue());
	    this.ellipse.setFill(EditTools.fillPicker.getValue());          
	     
	    this.startX = (int)x;
	    this.startY = (int)y;
	}
	
	public void mouseDrag(double x, double y) {
		
        if(x - this.startX >= 0){
            this.tempX = x - this.startX;
        }else{
            this.tempX = this.startX - x;
        }
        if(y - this.startY >= 0){
            this.tempY = y - this.startY;
        }else{
            this.tempY = this.startY - y;
        }
            
        this.ellipse.setRadiusX(this.tempX);
        this.ellipse.setRadiusY(this.tempY); 
	}
	
	public void mouseRelease(double x, double y) {
		this.endX = (int)x;
        this.endY = (int)y;
		if(this.endX - this.startX >= 0){
			this.tempX = this.endX - this.startX;
        }else{
        	this.tempX = this.startX - this.endX;
        }
        if(this.endY - this.startY >= 0){
        	this.tempY = this.endY - this.startY;
        }else{
        	this.tempY = this.startY - this.endY;
        }
	    draw();
  	}
	
	public void draw() {
		this.ctx.strokeOval(this.startX - this.ellipse.getRadiusX(), this.startY - this.ellipse.getRadiusY(), this.tempX + this.ellipse.getRadiusX(), this.tempY + this.ellipse.getRadiusY());
        this.ctx.fillOval(this.startX - this.ellipse.getRadiusX(), this.startY - this.ellipse.getRadiusY(), this.tempX + this.ellipse.getRadiusX(), this.tempY + this.ellipse.getRadiusY()); 

	}
	

}
