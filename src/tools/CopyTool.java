package tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class CopyTool {
	public ImageView selectedImg;
	public GraphicsContext ctx;
	int startX;
	int startY;
	int endX;
	int endY;
	double tempX;
	double tempY;
	
	public CopyTool(GraphicsContext ctx, ImageView selectedImg) {
		this.ctx = ctx;
		this.selectedImg = selectedImg;
	}
	
	public void mousePress(double x, double y) {
		this.selectedImg.setX(x);
        this.selectedImg.setY(y);
	}
	
	public void mouseDrag(double x, double y) {
		if(this.selectedImg != null){
            this.selectedImg.setX(x);
            this.selectedImg.setY(y);
        }
	}
	
	public void mouseRelease(double x, double y) {
		this.ctx.drawImage(this.selectedImg.getImage(), this.selectedImg.getX(), this.selectedImg.getY());
        this.selectedImg = null;
	}
}
