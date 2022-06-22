package tools;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class MoveTool extends CopyTool{
	
	SelectTool selectRect;

	public MoveTool(GraphicsContext ctx, ImageView selectedImg, SelectTool selectRect) {
		super(ctx, selectedImg);
		this.selectRect = selectRect;
	}
	
	public void mousePress(double x, double y) {
		super.mousePress(x, y);
		this.ctx.setFill(Color.WHITE);
		this.ctx.fillRect(selectRect.rect.getX(), selectRect.rect.getY(), selectRect.rect.getWidth(), selectRect.rect.getHeight());
		
	}
}
