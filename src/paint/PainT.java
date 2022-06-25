/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import javafx.scene.image.Image;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author shree
 */
public class PainT extends Application{
    public PainT(){
        
    }
    
    //Stores the opened Image so that it can be used while saving.
    private Image tempImage;
    
    //Constants for screen width and height.
    final int SCREEN_WIDTH = 1200;
    final int SCREEN_HEIGHT = 800;
    
    //Layouts
    private VBox myLayout = new VBox(10);
    public ImageView iView;
    
    //FileChooser
    FileChooser fileChooser = new FileChooser();
    File outputFile;
 
    //ScrollBar
    private ScrollPane scroll = new ScrollPane();
    CreateMenuBar menu = new CreateMenuBar();
    public EditTools editTool = new EditTools();
    public StackPane canvasPane = new StackPane();
    Thread autosave;
    OpenSave os;
	public Canvas canvas;
//	public GraphicsContext globalGC;
//	public GraphicsContext graphicsContext;
	public double CANVAS_WIDTH = 1100;
	public double CANVAS_HEIGHT = 750;
	private Canvas layer = new Canvas();
	public static Stack undoStack = new Stack();
	public boolean freeLineTrue = false;
	public boolean lineTrue = false;
	public boolean rectangleTrue = false;
	public boolean squareTrue = false;
	public boolean ellipseTrue = false;
	public boolean circleTrue = false;
	public boolean eraserTrue = false;
	public boolean[] shapesBool = new boolean[7];
	public boolean selectTrue = false;
	public boolean moveTrue = false;
	public Pane pane;
	public String saveFileExtension;
	public Log log = new Log();
	public DrawShapes drawShapes;
	public PaintCanvas paintCanvas;

    @Override
    public void start(Stage stage) throws IOException { 
        log.LoggerFunc();
        //Setting up the scene
        Scene scene = new Scene(myLayout, SCREEN_WIDTH, SCREEN_HEIGHT);
//        scene.getStylesheets().add((PainT.class.getResource("stylesheet.css").toExternalForm())); //
        
        os = new OpenSave(this);
        
        //Application functionality and tools
        menu.createMenu(); //Creates the Menu Bar
        editTool.editTool(); //Contains tools such as color picker and brush width
        
        os.eventHandler(stage); //Handles the Menu Item clicks
       
        ///SmartSaves
        stage.setOnCloseRequest(e->{
            e.consume();
            closeProgram(stage, ConfirmExit.display("PainT", "Do you want to save changes?"));
        });
        
        paintCanvas = new PaintCanvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        canvas = paintCanvas.canvas;
        
        pane = new Pane(canvas);
        
        drawShapes = new DrawShapes(canvas, pane);
        drawShapes.drawShape();
        
        editTool.autosaveBox.setOnAction(e->{
	         if(editTool.autosaveBox.isSelected()){
	             autosave = new Autosave(10, this);
	             autosave.setDaemon(true);
	             autosave.start();
	         }
         });
             
        canvasPane.setMargin(canvas , new Insets(20,20,20,20));
        canvasPane.getChildren().add(pane);
       
        ObservableList<Double> canvasPoints = FXCollections.observableArrayList();
        canvasPoints.addAll(canvas.getWidth(), canvas.getHeight());
 
        editTool.selectTool.setOnAction(e->{
            selectTrue = !selectTrue;      
        });
     
        zoomInOut();
        //Setting the basic layout 
        myLayout.getChildren().add(0,menu.menuBox); 
        myLayout.getChildren().add(1,editTool.editToolsBox);
        
        myLayout.getChildren().add(2,canvasPane);
        myLayout.getChildren().add(3,scroll);
        myLayout.getChildren().add(4, editTool.infoBar);
        pane.getChildren().addAll(createControlAnchorsFor(canvasPoints));
        //ScrollBar
        scroll.setStyle("-fx-background-insets: 0;");
        scroll.setContent(pane);
    
        stage.setTitle("PainT");
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     *
     */
    
    
    /**
     *
     */
    public void zoomInOut(){
        editTool.zoomIn.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX() * 2);
            canvas.setScaleY(canvas.getScaleY() * 2);
        });
        
        editTool.zoomOut.setOnAction(e->{
            canvas.setScaleX(canvas.getScaleX() / 2);
            canvas.setScaleY(canvas.getScaleY() / 2);
        });    
    }
    
    public void sThread(){
        autosave.interrupt();
        autosave = new Autosave(10, PainT.this);
        autosave.start();
    }
 
    // @return a list of anchors which can be dragged around to modify points in the format [x1, y1, x2, y2...]

    /**
     *
     *
     */
    public ObservableList<Anchor> createControlAnchorsFor(final ObservableList<Double> points) {
    ObservableList<Anchor> anchors = FXCollections.observableArrayList();

    for (int i = 0; i < points.size(); i+=2) {
      final int idx = i;

      DoubleProperty xProperty = new SimpleDoubleProperty(points.get(i));
      DoubleProperty yProperty = new SimpleDoubleProperty(points.get(i + 1));

      xProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldX, Number x) {
          points.set(idx, (double) x);
        }
      });

      yProperty.addListener(new ChangeListener<Number>() {
        @Override public void changed(ObservableValue<? extends Number> ov, Number oldY, Number y) {
          points.set(idx + 1, (double) y);
        }
      });

      anchors.add(new Anchor(Color.GOLD, xProperty, yProperty));
    }

    return anchors;
  }

  // a draggable anchor displayed around a point.
  class Anchor extends Circle {
    private final DoubleProperty x, y;

    Anchor(Color color, DoubleProperty x, DoubleProperty y) {
      super(x.get(), y.get(), 10);
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
        @Override public void handle(MouseEvent mouseEvent) {
          // record a delta distance for the drag and drop operation.
          dragDelta.x = getCenterX() - mouseEvent.getX();
          dragDelta.y = getCenterY() - mouseEvent.getY();
          getScene().setCursor(Cursor.MOVE);
          System.out.println("dragDelta x: " + dragDelta.x + "dragDelta y: " + dragDelta.y);
        }
      });
      setOnMouseReleased(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          getScene().setCursor(Cursor.HAND);
        }
      });
      setOnMouseDragged(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
        	 

          double newX = mouseEvent.getX() + dragDelta.x;
          double newY = mouseEvent.getY() + dragDelta.y;
          
          canvas.getGraphicsContext2D().setFill(Color.WHITE); //Fills the canvas with white color
          //Draws the canvas
          canvas.getGraphicsContext2D().fillRect(
         		 canvas.getWidth(),    //width of the rectangle
         		 canvas.getHeight(),
         		 mouseEvent.getX(),
         		 mouseEvent.getY() //y of the upper left corner
                  );  //height of the rectangle
//          canvas.getGraphicsContext2D().fillRect(
//          		 0,    //width of the rectangle
//          		 0,
//          		 1280,
//          		 800 //y of the upper left corner
//                   );  //height of the rectangle
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
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
          System.out.println("enter x: " + mouseEvent.getX() + "enter y: " + mouseEvent.getY());
        }
      });
      setOnMouseExited(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.DEFAULT);
          }
        }
      });
    }

    // records relative x and y co-ordinates.
    private class Delta { double x, y; }
  }
  
    /**
     *
     * @param stage
     * @param display
     */
    public void closeProgram(Stage stage, String display){
        String answer = display;
        switch(answer){
            case "save":
                if(outputFile != null){
                WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                canvas.snapshot(null, writableImage);
                
                try{
                    ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);
                }catch(IOException ex){
                    System.out.println("Error in saving");
                }
                }else{                  
                    FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG File (*.png)", "*.png");
                    fileChooser.getExtensionFilters().add(extPNG);
                    outputFile = fileChooser.showSaveDialog(stage);
                    WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                    canvas.snapshot(null, writableImage);

                    try {


                            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);
                            

                    } catch (IOException ex) {
                        System.out.println("Error in saving as");
                    }
                    
                }
                if(autosave != null){
                    autosave.interrupt();
                }
                stage.close();
                break;
            case "dontSave":
                if(autosave != null){
                    autosave.interrupt();
                }
                stage.close();
                break;
            case "cancel":
                break;
        }
                
    }

    /**
     *
     * @param stage
     * @param display
     */
    public void saveWarning(Stage stage, String display){
        String answer = display;
        switch(answer){
            case "save":
                    WritableImage writableImage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
                    canvas.snapshot(null, writableImage);

                    try {
                       ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), saveFileExtension, outputFile);      
                    } catch (IOException ex) {
                       System.out.println("Error in saving as");
                    }

//                stage.close();
                break;
            case "dontSave":
//                stage.close();
            
                break;
            case "cancel":
                break;
        }
                
    }
 
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
//        PainT paint = new PainT();
        launch(args);
    }
    
}
