package tools;

import javafx.scene.shape.Rectangle;
import paint.EditTools;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;


public class RectTool {
	public Rectangle rect;
	GraphicsContext ctx;
	int startX;
	int startY;
	int endX;
	int endY;
	double tempX;
	double tempY;
	
	public RectTool(GraphicsContext ctx, double x,double y) {
		this.ctx = ctx;
		this.rect = new Rectangle(x,y,0,0);
	            
	    this.startX = (int)x;
	    this.startY = (int)y;
	}
	
	
	public void style() {
		 this.rect.setStrokeWidth(EditTools.lineWidth);
		 this.rect.setStroke(EditTools.colorPicker.getValue());
	     this.rect.setFill(EditTools.fillPicker.getValue()); 
	}
	
	public void style(int lineWidth, Color stroke, Color fill) {
		 this.rect.setStrokeWidth(lineWidth);
		 this.rect.setStroke(stroke);
	     this.rect.setFill(fill); 
	}
	
	public void mouseDrag(double x, double y) {
		if(x - this.startX >= 0){
            tempX = x - this.startX;
        }else{
            tempX = this.startX - x;
            this.rect.setX(x);
        }
        this.rect.setWidth(tempX);
        
        if(y - this.startY >= 0){
            tempY = y - this.startY;
        }else{
            tempY = this.startY - y;
            this.rect.setY(y);
        } 
        
        this.rect.setHeight(tempY); 
	}
	
	public void mouseRelease(double x, double y) {
        calcDist(x,y);
       
        draw(this.tempX, this.tempY);
	}
	
	public void calcDist(double x, double y) {
		this.endX = (int)x;
        this.endY = (int)y;
        
        if(this.endX - this.startX >= 0){
            tempX = this.endX - this.startX;
        }else{
            tempX = this.startX - this.endX;
            this.startX = this.endX;
        }
        if(this.endY - this.startY >= 0){
            tempY = this.endY - this.startY;
        }else{
            tempY = this.startY - this.endY;
            this.startY = this.endY;
        }  
	}

	public void draw(double x, double y) {
		this.ctx.strokeRect(startX, startY, x, y);
        this.ctx.fillRect(startX, startY, x, y);
	}
	
}
