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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PaintCanvas {
	public static String selectedToolText;
	
	public Canvas canvas;
	public static GraphicsContext globalGC;
	public static GraphicsContext graphicsContext;
	
	public Stack redoStack = new Stack();
	
	public PaintCanvas(double width, double height) {
		createCanvas(width, height);
		canvasTools();
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
	
	//Canvas
    public void canvasTools(){
        checkSelectedTool("No Tool Selected");
        EditTools.pencilTool.setOnAction((ActionEvent event) -> {
            checkSelectedTool("Pencil Tool Selected");
        });
        
        EditTools.lineTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Line Tool Selected");
        });
        EditTools.rectangleTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Rectangle Tool Selected");
            
        });
        EditTools.squareTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Square Tool Selected");
            
        });
        EditTools.ellipseTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Ellipse Tool Selected");
        });
        EditTools.circleTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Circle Tool Selected");
            
        });
        EditTools.eraserTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Eraser Tool Selected");
            
        });
        EditTools.selectTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Select Tool Selected");
            
        });
        EditTools.moveTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Move Tool Selected");
            
        });
        EditTools.copyTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Copy Tool Selected");
            
        });
        EditTools.triangleTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Triangle Tool Selected");
            
        });
        EditTools.polygonTool.setOnAction((ActionEvent event)->{
            checkSelectedTool("Polygon Tool Selected");
            
        });
       Image tempImge = canvas.snapshot(null, null);
            //Undo Stack
            PainT.undoStack.push(tempImge); 
            System.out.println("pushed");
        
        EditTools.colorGrabberTool.setOnMouseClicked((MouseEvent event) ->{
        	checkSelectedTool("Color Grabber Tool Selected");
            Image tempImage = canvas.snapshot(null, null);
            PixelReader pR = tempImage.getPixelReader();
            
            canvas.setOnMouseClicked(e ->{
                Color c = pR.getColor((int)e.getX(), (int)e.getY());
                EditTools.colorPicker.setValue(c);
                canvas.setOnMouseClicked(null);
            });  
        });
        
        EditTools.undoTool.setOnMouseClicked(e->{
            redoStack.push(PainT.undoStack.pop());
            Image tempImage = (Image)PainT.undoStack.peek();
            canvas.getGraphicsContext2D().drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("popped");
        });
        
        EditTools.redoTool.setOnMouseClicked(e->{
            Image tempImage = (Image)redoStack.peek();
            PainT.undoStack.push(redoStack.pop());
            
            canvas.getGraphicsContext2D().drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
            System.out.println("redone");
        });
        
  
        EditTools.textTool.setOnAction(e->{
           checkSelectedTool("Text Tool selected");
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
               okay.setOnAction(f->{canvas.getGraphicsContext2D().strokeText(editText.getText(), point.getX(), point.getY());textStage.close();});
               
               Scene textScene = new Scene(setTextBox, 300, 200);
               textStage.setScene(textScene);
               textStage.showAndWait();       
           });
       });
    
    }
    
    public void checkSelectedTool(String selectedToolTxt) {
    	selectedToolText = selectedToolTxt;
    	EditTools.toolInfo.setText(selectedToolTxt);
    	Log.tLogS(selectedToolTxt);
    }
}
