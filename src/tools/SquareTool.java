package tools;

import javafx.scene.canvas.GraphicsContext;

public class SquareTool extends RectTool{

	public SquareTool(GraphicsContext ctx, double x, double y) {
		super(ctx, x, y);
		
	}
	
	public void mouseDrag(double x, double y) {
		if(x - this.startX >= 0){
            tempX = x - this.startX;
        }else{
            tempX = this.startX - x;
            this.rect.setX(x);
        }
        this.rect.setWidth(tempX);
        this.rect.setHeight(tempX); 
	}
	
	public void mouseRelease(double x, double y) {
		this.endX = (int)x;
        
        if(this.endX - this.startX >= 0){
            tempX = this.endX - this.startX;
        }else{
            tempX = this.startX - this.endX;
            this.startX = this.endX;
        }
        
        draw(this.tempX, this.tempX);
	}

}
