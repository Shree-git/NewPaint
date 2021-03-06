package paint;

import java.util.Stack;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaintCanvas {
    public static String selectedToolText;

    public Canvas canvas;
    public static GraphicsContext globalGC;
    public static GraphicsContext graphicsContext;

    public Stack<Image> redoStack = new Stack<Image>();

    public PaintCanvas(double width, double height) {
        createCanvas(width, height);
        canvasTools();
    }

    /**
     * Takes in width and height and creates a canvas
     * 
     * @param width  Canvas's width
     * @param height Canvas's height
     */
    // Canvas
    public void createCanvas(double width, double height) {
        // Creates a canvas with certain width, height.
        canvas = new Canvas();
        canvas.setWidth(width);
        canvas.setHeight(height);
        globalGC = canvas.getGraphicsContext2D();
        globalGC.setFill(Color.WHITE); // Fills the canvas with white color
        // Draws the canvas
        globalGC.fillRect(
                0, // x of the upper left corner
                0, // y of the upper left corner
                width, // width of the rectangle
                height); // height of the rectangle
    }

    // Canvas
    public void canvasTools() {
        checkSelectedTool("No Tool Selected");
        EditTools.pencilTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Pencil Tool Selected");
        });

        EditTools.lineTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Line Tool Selected");
        });
        EditTools.rectangleTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Rectangle Tool Selected");

        });
        EditTools.squareTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Square Tool Selected");

        });
        EditTools.ellipseTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Ellipse Tool Selected");
        });
        EditTools.circleTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Circle Tool Selected");

        });
        EditTools.eraserTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Eraser Tool Selected");

        });
        EditTools.selectTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Select Tool Selected");
        });
        EditTools.moveTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Move Tool Selected");

        });
        EditTools.copyTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Copy Tool Selected");

        });
        EditTools.triangleTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Triangle Tool Selected");

        });
        EditTools.polygonTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Polygon Tool Selected");

        });
        EditTools.colorGrabberTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Color Grabber Tool Selected");
        });
        EditTools.emojiTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Emoji Tool Selected");
        });
        EditTools.zoomIn.setOnAction(e -> {
            PainT.pane.setScaleX(PainT.pane.getScaleX() * 2);
            PainT.pane.setScaleY(PainT.pane.getScaleY() * 2);
        });

        EditTools.zoomOut.setOnAction(e -> {
            PainT.pane.setScaleX(PainT.pane.getScaleX() / 2);
            PainT.pane.setScaleY(PainT.pane.getScaleY() / 2);
        });

        EditTools.undoTool.setOnMouseClicked(e -> {
            redoStack.push(PainT.undoStack.pop());
            Image tempImage = (Image) PainT.undoStack.peek();
            canvas.getGraphicsContext2D().drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("popped");
        });

        EditTools.redoTool.setOnMouseClicked(e -> {
            Image tempImage = (Image) redoStack.peek();
            PainT.undoStack.push(redoStack.pop());

            canvas.getGraphicsContext2D().drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("redone");
        });

        EditTools.textTool.setOnAction(e -> {
            checkSelectedTool("Text Tool selected");
        });

        Image tempImge = canvas.snapshot(null, null);
        // Undo Stack
        PainT.undoStack.push(tempImge);

    }

    public static void checkSelectedTool(String selectedToolTxt) {
        if (EditTools.toggleGroup.getSelectedToggle() == null) {
            selectedToolText = "No Tool Selected";
        } else {
            selectedToolText = selectedToolTxt;
        }
        EditTools.toolInfo.setText(selectedToolText);
        Log.tLogS(selectedToolText);
    }
}
