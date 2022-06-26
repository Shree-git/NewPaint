package paint;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tools.CircleTool;
import tools.CopyTool;
import tools.EllipseTool;
import tools.EraserTool;
import tools.LineTool;
import tools.MoveTool;
import tools.PencilTool;
import tools.PolygonTool;
import tools.RectTool;
import tools.SelectTool;
import tools.SquareTool;
import tools.TriangleTool;

public class DrawShapes {
	Canvas canvas;
	GraphicsContext graphicsContext;
	Pane pane;
	ImageView selectedImg;

	public PencilTool pencil;
	public EraserTool eraser;
	public RectTool rectangle;
	public SelectTool selectRect;
	public SquareTool square;
	public EllipseTool ellipse;
	public CircleTool circle;
	public TriangleTool triangle;
	public PolygonTool polygonTool;
	public LineTool line;
	public MoveTool moveTool;
	public CopyTool copyTool;

	public int startX;
	public int startY;
	public int endX;
	public int endY;

	public DrawShapes(Canvas canvas, Pane pane) {
		this.canvas = canvas;
		this.graphicsContext = canvas.getGraphicsContext2D();
		this.pane = pane;
		polyTool();
	}

	public void polyTool() {
		EditTools.polygonTool.setOnAction(e -> {
			polygonTool = new PolygonTool(this.graphicsContext);
		});
	}

	public void drawShape() {
		// Handles mouse events
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
			graphicsContext.setStroke(EditTools.colorPicker.getValue()); // Sets the color for the brush
			graphicsContext.setLineWidth(EditTools.slider.getValue()); // Sets line width
			graphicsContext.setFill(EditTools.fillPicker.getValue());
			if (EditTools.pencilTool.isSelected()) {
				pencil = new PencilTool(graphicsContext);
				pencil.mousePress(e.getX(), e.getY());
			} else if (EditTools.eraserTool.isSelected()) {
				eraser = new EraserTool(graphicsContext);
				eraser.mousePress(e.getX(), e.getY());
			} else if (EditTools.lineTool.isSelected()) {
				line = new LineTool(graphicsContext, e.getX(), e.getY());
				pane.getChildren().add(line.line);
			} else if (EditTools.rectangleTool.isSelected()) {
				rectangle = new RectTool(graphicsContext, e.getX(), e.getY());
				rectangle.style();
				pane.getChildren().add(rectangle.rect);
			} else if (EditTools.squareTool.isSelected()) {
				square = new SquareTool(graphicsContext, e.getX(), e.getY());
				square.style();
				pane.getChildren().add(square.rect);

			} else if (EditTools.ellipseTool.isSelected()) {
				ellipse = new EllipseTool(graphicsContext, e.getX(), e.getY());
				pane.getChildren().add(ellipse.ellipse);
			} else if (EditTools.circleTool.isSelected()) {
				circle = new CircleTool(graphicsContext, e.getX(), e.getY());
				pane.getChildren().add(circle.ellipse);
			} else if (EditTools.selectTool.isSelected()) {
				selectRect = new SelectTool(graphicsContext, e.getX(), e.getY());
				selectRect.style(1, Color.BLACK, Color.TRANSPARENT);
				pane.getChildren().add(selectRect.rect);
			} else if (EditTools.triangleTool.isSelected()) {
				triangle = new TriangleTool(graphicsContext, e.getX(), e.getY());
				pane.getChildren().add(triangle.triangle);
			} else if (EditTools.polygonTool.isSelected()) {
				polygonTool.mousePress(e.getX(), e.getY());
			} else if (EditTools.moveTool.isSelected()) {
				if (selectedImg != null) {
					moveTool = new MoveTool(graphicsContext, selectedImg, selectRect);
					moveTool.mousePress(e.getX(), e.getY());

					pane.getChildren().add(selectedImg);
				}
			} else if (EditTools.copyTool.isSelected()) {
				if (selectedImg != null) {
					copyTool = new CopyTool(graphicsContext, selectedImg);
					copyTool.mousePress(e.getX(), e.getY());
					pane.getChildren().add(selectedImg);
				}
			} else if (EditTools.emojiTool.isSelected()) {
				ImageView emoView = (ImageView) EditTools.emojiList.getValue();
				Image emo = emoView.getImage();
				graphicsContext.drawImage(emo, e.getX(), e.getY(), 50, 50);
			} else if (EditTools.colorGrabberTool.isSelected()) {
				Image tempImage = canvas.snapshot(null, null);
				PixelReader pR = tempImage.getPixelReader();

				Color c = pR.getColor((int) e.getX(), (int) e.getY());
				EditTools.colorPicker.setValue(c);
				canvas.setOnMouseClicked(null);
			} else if (EditTools.textTool.isSelected()) {
				Stage textStage = new Stage();
				textStage.initModality(Modality.APPLICATION_MODAL);
				textStage.setTitle("Set Text");
				VBox setTextBox = new VBox();
				Label infoLabel = new Label("Text:");
				TextField editText = new TextField();
				Button okay = new Button("Ok");
				setTextBox.setAlignment(Pos.CENTER);
				setTextBox.getChildren().addAll(infoLabel, editText, okay);
				okay.setOnAction(f -> {
					canvas.getGraphicsContext2D().strokeText(editText.getText(), e.getX(), e.getY());
					textStage.close();
				});

				Scene textScene = new Scene(setTextBox, 300, 200);
				textStage.setScene(textScene);
				textStage.showAndWait();
			}

		});

		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent e) -> {
			if (EditTools.pencilTool.isSelected()) {
				pencil.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.eraserTool.isSelected()) {
				eraser.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.lineTool.isSelected()) {
				line.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.rectangleTool.isSelected()) {
				rectangle.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.squareTool.isSelected()) {
				square.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.ellipseTool.isSelected()) {
				ellipse.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.circleTool.isSelected()) {
				circle.mouseDrag(e.getX());
			} else if (EditTools.selectTool.isSelected()) {
				selectRect.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.triangleTool.isSelected()) {
				triangle.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.moveTool.isSelected()) {
				moveTool.mouseDrag(e.getX(), e.getY());
			} else if (EditTools.copyTool.isSelected()) {
				copyTool.mouseDrag(e.getX(), e.getY());
			}

		});

		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent e) -> {
			if (EditTools.pencilTool.isSelected()) {
				eraser.mouseRelease(e.getX(), e.getY());
			} else if (EditTools.eraserTool.isSelected()) {
				eraser.mouseRelease(e.getX(), e.getY());
			} else if (EditTools.lineTool.isSelected()) {
				pane.getChildren().remove(line.line);
				line.mouseRelease(e.getX(), e.getY());
			} else {
				endX = (int) e.getX();
				endY = (int) e.getY();
				if (EditTools.lineTool.isSelected()) {
					line.mouseRelease(e.getX(), e.getY());
				} else if (EditTools.rectangleTool.isSelected()) {
					rectangle.mouseRelease(e.getX(), e.getY());
					pane.getChildren().remove(rectangle.rect);
				} else if (EditTools.squareTool.isSelected()) {
					square.mouseRelease(e.getX(), e.getY());
					pane.getChildren().remove(square.rect);
				} else if (EditTools.ellipseTool.isSelected()) {
					ellipse.mouseRelease(e.getX(), e.getY());
					pane.getChildren().remove(ellipse.ellipse);
				} else if (EditTools.circleTool.isSelected()) {
					circle.mouseRelease(e.getX());
					pane.getChildren().remove(circle.ellipse);
				} else if (EditTools.selectTool.isSelected()) {
					selectedImg = selectRect.mouseRelease(e.getX(), e.getY(), (Image) PainT.undoStack.peek());
					pane.getChildren().remove(selectRect.rect);
				} else if (EditTools.triangleTool.isSelected()) {
					triangle.mouseRelease(e.getX(), e.getY());
					pane.getChildren().remove(triangle.triangle);
				} else if (EditTools.moveTool.isSelected()) {
					if (selectedImg != null) {
						moveTool.mouseRelease(e.getX(), e.getY());
						pane.getChildren().remove(selectedImg);
						selectedImg = null;
					}
				} else if (EditTools.copyTool.isSelected()) {
					if (selectedImg != null) {
						copyTool.mouseRelease(e.getX(), e.getY());
						pane.getChildren().remove(selectedImg);
					}
				} else if (EditTools.polygonTool.isSelected()) {
					polygonTool.mouseRelease();
				}

			}
			Image tempImage = canvas.snapshot(null, null);

			PainT.undoStack.push(tempImage);
		});

	}
}
