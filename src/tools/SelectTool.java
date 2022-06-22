package tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;

public class SelectTool extends RectTool {

	public SelectTool(GraphicsContext ctx, double x, double y) {
		super(ctx, x, y);
	}
	
	public ImageView mouseRelease(double x, double y, Image imgToMove) {
		calcDist(x,y);
        
        PixelReader pR = imgToMove.getPixelReader();
        WritableImage newImg = new WritableImage(pR, this.startX, this.startY, (int)this.rect.getWidth(), (int)this.rect.getHeight());
        ImageView selectedImg = new ImageView(newImg);
        selectedImg.setX(this.rect.getX());
        selectedImg.setY(this.rect.getY());
        return selectedImg;
	}
}
