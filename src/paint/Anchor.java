package paint;

import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

// a draggable anchor displayed around a point.
public class Anchor extends Circle {
    private final DoubleProperty x, y;
    public Canvas canvas;

    public Anchor(Color color, DoubleProperty x, DoubleProperty y, Canvas canvas) {
        super(x.get(), y.get(), 10);
        this.canvas = canvas;
        setFill(color.deriveColor(1, 1, 1, 0.5));
        setStroke(color);
        setStrokeWidth(2);
        setStrokeType(StrokeType.OUTSIDE);

        this.x = x;
        this.y = y;

        x.bind(centerXProperty());
        y.bind(centerYProperty());
        enableDrag();
    }

    // make a node movable by dragging it around with the mouse.
    private void enableDrag() {
        final Delta dragDelta = new Delta();
        setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                // record a delta distance for the drag and drop operation.
                dragDelta.x = getCenterX() - mouseEvent.getX();
                dragDelta.y = getCenterY() - mouseEvent.getY();
                getScene().setCursor(Cursor.MOVE);
                System.out.println("dragDelta x: " + dragDelta.x + "dragDelta y: " + dragDelta.y);
            }
        });
        setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getScene().setCursor(Cursor.HAND);
            }
        });
        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                double newX = mouseEvent.getX() + dragDelta.x;
                double newY = mouseEvent.getY() + dragDelta.y;

                canvas.getGraphicsContext2D().setFill(Color.WHITE); // Fills the canvas with white color
                // Draws the canvas
                canvas.getGraphicsContext2D().fillRect(
                        canvas.getWidth(), // width of the rectangle
                        canvas.getHeight(),
                        mouseEvent.getX(),
                        mouseEvent.getY() // y of the upper left corner
                ); // height of the rectangle
                   // canvas.getGraphicsContext2D().fillRect(
                   // 0, //width of the rectangle
                   // 0,
                   // 1280,
                   // 800 //y of the upper left corner
                   // ); //height of the rectangle
                System.out.println("canvas width: " + canvas.getWidth() + "canvas height: " + canvas.getHeight());
                if (newX > 0 && newX < getScene().getWidth()) {
                    setCenterX(newX);
                }
                if (newY > 0 && newY < getScene().getHeight()) {
                    setCenterY(newY);
                }
                canvas.setWidth(newX);
                canvas.setHeight(newY);
                System.out.println("new x: " + newX + "new y: " + newY);
                System.out.println("mouse x: " + mouseEvent.getX() + "mouse y: " + mouseEvent.getY());

            }
        });
        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.HAND);
                }
                System.out.println("enter x: " + mouseEvent.getX() + "enter y: " + mouseEvent.getY());
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!mouseEvent.isPrimaryButtonDown()) {
                    getScene().setCursor(Cursor.DEFAULT);
                }
            }
        });
    }

    // records relative x and y co-ordinates.
    private class Delta {
        double x, y;
    }
}
