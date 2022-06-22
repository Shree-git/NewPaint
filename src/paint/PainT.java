/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.Desktop;
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Stack;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.imageio.ImageIO;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;

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
    CheckSelected cS;
	public Canvas canvas;
	public GraphicsContext globalGC;
	public GraphicsContext graphicsContext;
	public double CANVAS_WIDTH = 1100;
	public double CANVAS_HEIGHT = 750;
	private Canvas layer = new Canvas();
	public Stack undoStack = new Stack();
	public Stack redoStack = new Stack();
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
        cS = new CheckSelected(editTool);
        canvasFunc();
        
        pane = new Pane(canvas);
        
        drawShapes = new DrawShapes(canvas, pane, undoStack);
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
                
        textTool();

        
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
    public void textTool(){
         editTool.textTool.setOnAction(e->{
            editTool.toolInfo.setText("Text Tool selected");
            canvas.setOnMouseClicked(point->{
                Stage textStage = new Stage();
                textStage.initModality(Modality.APPLICATION_MODAL);
                textStage.setTitle("Set Text");
                VBox setTextBox = new VBox();
                Label infoLabel = new Label("Text:");
                TextField editText = new TextField();
                Button okay = new Button("Ok");
                setTextBox.setAlignment(Pos.CENTER);
                setTextBox.getChildren().addAll(infoLabel, editText, okay);      
                okay.setOnAction(f->{graphicsContext.strokeText(editText.getText(), point.getX(), point.getY());textStage.close();});
                
                Scene textScene = new Scene(setTextBox, 300, 200);
                textStage.setScene(textScene);
                textStage.showAndWait();       
            });
        });
    }
    
    
    
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
    
    //Canvas
    public void canvasFunc(){
        createCanvas(CANVAS_WIDTH, CANVAS_HEIGHT); //Creates a canvas
        
        cS.checkTrue();
        editTool.pencilTool.setOnAction((ActionEvent event) -> {
        	System.out.println(event);
            cS.checkTrue();
        });
        
        editTool.lineTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
            
        });
        editTool.rectangleTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.squareTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.ellipseTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
        });
        editTool.circleTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.eraserTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.selectTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.moveTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.copyTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.triangleTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
        editTool.polygonTool.setOnAction((ActionEvent event)->{
            cS.checkTrue();
            
        });
       Image tempImge = canvas.snapshot(null, null);
            //Undo Stack
            undoStack.push(tempImge); 
            System.out.println("pushed");
        
        editTool.colorGrabberTool.setOnMouseClicked((MouseEvent event) ->{
            editTool.toolInfo.setText("Color Grabber selected");
            Image tempImage = canvas.snapshot(null, null);
            PixelReader pR = tempImage.getPixelReader();
            
            canvas.setOnMouseClicked(e ->{
                Color c = pR.getColor((int)e.getX(), (int)e.getY());
                editTool.colorPicker.setValue(c);
                canvas.setOnMouseClicked(null);
            });  
        });
        
        editTool.undoTool.setOnMouseClicked(e->{
            redoStack.push(undoStack.pop());
            Image tempImage = (Image)undoStack.peek();
            graphicsContext.drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("popped");
        });
        
        editTool.redoTool.setOnMouseClicked(e->{
            Image tempImage = (Image)redoStack.peek();
            undoStack.push(redoStack.pop());
            
            graphicsContext.drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("redone");
        });
    }

    /**
     * Takes in width and height and creates a canvas
     * @param width Canvas's width
     * @param height Canvas's height
     */
    //Canvas
    public void createCanvas(double width, double height){
        //Creates a canvas with certain width, height.
        canvas = new Canvas();
        canvas.setWidth(width);
        canvas.setHeight(height);
        globalGC = canvas.getGraphicsContext2D();
        globalGC.setFill(Color.WHITE); //Fills the canvas with white color
        //Draws the canvas
        globalGC.fillRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                width,    //width of the rectangle
                height);  //height of the rectangle
    }
    
    public void sThread(){
        autosave.interrupt();
        autosave = new Autosave(10, PainT.this);
        autosave.start();
    }
    
    /**
     *
     */
   
   
    /**
     *
     */
    public void colorGrabber(){
        
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
          canvas.setWidth(newX);
          if (newX > 0 && newX < getScene().getWidth()) {
            setCenterX(newX);
          }
          double newY = mouseEvent.getY() + dragDelta.y;
          canvas.setHeight(newY);
          if (newY > 0 && newY < getScene().getHeight()) {
            setCenterY(newY);
          }
//          globalGC.setFill(Color.WHITE); //Fills the canvas with white color
//          //Draws the canvas
//          globalGC.fillRect(
//        		  getCenterX(),              //x of the upper left corner
//        		  getCenterY(),              //y of the upper left corner
//                  newX,    //width of the rectangle
//                  newY);  //height of the rectangle
        }
      });
      setOnMouseEntered(new EventHandler<MouseEvent>() {
        @Override public void handle(MouseEvent mouseEvent) {
          if (!mouseEvent.isPrimaryButtonDown()) {
            getScene().setCursor(Cursor.HAND);
          }
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
