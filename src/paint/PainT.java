/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.scene.image.Image;
import java.io.IOException;
import java.util.Stack;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author shree
 */
public class PainT extends Application {

  // Stores the opened Image so that it can be used while saving.
  private Image tempImage;

  // Constants for screen width and height.
  final int SCREEN_WIDTH = 1200;
  final int SCREEN_HEIGHT = 800;

  // Layouts
  private VBox myLayout = new VBox(10);

  // ScrollBar
  private ScrollPane scroll = new ScrollPane();
  public EditTools editTool = new EditTools();
  public static StackPane canvasPane = new StackPane();
  OpenSave os;
  public Canvas canvas;
  // public GraphicsContext globalGC;
  // public GraphicsContext graphicsContext;
  public double CANVAS_WIDTH = 1100;
  public double CANVAS_HEIGHT = 750;
  public static Stack<Image> undoStack = new Stack<Image>();
  public static Pane pane;
  public String saveFileExtension;
  public Log log = new Log();
  public DrawShapes drawShapes;
  public PaintCanvas paintCanvas;

  @Override
  public void start(Stage stage) throws IOException {
    log.LoggerFunc();
    // Setting up the scene
    Scene scene = new Scene(myLayout, SCREEN_WIDTH, SCREEN_HEIGHT);
    // scene.getStylesheets().add((PainT.class.getResource("stylesheet.css").toExternalForm()));

    // Application functionality and tools
    Menu.createMenu(); // Creates the Menu Bar
    Menu.menuEventHandler(stage);
    editTool.editTool(); // Contains tools such as color picker and brush width

    /// SmartSaves
    stage.setOnCloseRequest(e -> {
      e.consume();
      os.closeProgram(stage, ConfirmExit.display("PainT", "Do you want to save changes?"));
    });

    paintCanvas = new PaintCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
    canvas = paintCanvas.canvas;

    os = new OpenSave(canvas);
    os.menuEventHandler(stage); // Handles the Menu Item clicks

    pane = new Pane(canvas);

    pane.setMinSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    pane.setPrefSize(canvas.getWidth(), canvas.getHeight());

    drawShapes = new DrawShapes(canvas, pane);
    drawShapes.drawShape();
    canvasPane.setMargin(canvas, new Insets(20, 20, 20, 20));
    canvasPane.setStyle("-fx-background-color: black;");
    canvasPane.getChildren().add(pane);
    buildStage(stage, scene);
  }

  public void buildStage(Stage stage, Scene scene) {
    // Setting the basic layout
    myLayout.getChildren().add(0, Menu.menuBox);
    myLayout.getChildren().add(1, EditTools.editToolsBox);

    myLayout.getChildren().add(2, canvasPane);
    myLayout.getChildren().add(3, scroll);
    myLayout.getChildren().add(4, editTool.infoBar);

    ObservableList<Double> canvasPoints = FXCollections.observableArrayList();
    canvasPoints.addAll(canvas.getWidth(), canvas.getHeight());

    pane.getChildren().addAll(createControlAnchorsFor(canvasPoints));
    // ScrollBar
    scroll.setStyle("-fx-background-insets: 0;");
    scroll.setContent(pane);

    stage.setTitle("PainT");
    stage.setScene(scene);
    stage.show();
  }

  // @return a list of anchors which can be dragged around to modify points in the
  // format [x1, y1, x2, y2...]
  public ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    for (int i = 0; i < points.size(); i += 2) {
      final int idx = i;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
          points.set(idx, (double) x);
        }
      });

      yProperty.addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
          points.set(idx + 1, (double) y);
        }
      });

      anchors.add(new Anchor(Color.GOLD, xProperty, yProperty, canvas));
    }

    return anchors;
  }

  /**
   *
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }

}
