package tools;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import paint.EditTools;

public class PolygonTool {
	public double[] points;
	public double[] pointsY;
	int numberOfSides;
	int count = 0;
	GraphicsContext graphicsContext;
	
	public PolygonTool(GraphicsContext graphicsContext) {
		this.graphicsContext = graphicsContext;
        Stage textStage = new Stage();
        textStage.initModality(Modality.APPLICATION_MODAL);
        textStage.setTitle("Sides");
        VBox setTextBox = new VBox();
        Label infoLabel = new Label("Number of Sides:");
        TextField editText = new TextField();
        Button okay = new Button("Ok");
        setTextBox.setAlignment(Pos.CENTER);
        setTextBox.getChildren().addAll(infoLabel, editText, okay);      
        okay.setOnAction(f->{numberOfSides = Integer.parseInt(editText.getText());points = new double[numberOfSides];pointsY = new double[numberOfSides];textStage.close();});
        
        Scene textScene = new Scene(setTextBox, 300, 200);
        textStage.setScene(textScene);
        textStage.showAndWait(); 
	
	}
	
	public void mousePress(double x, double y) {
		 if(numberOfSides != 0){
           points[count] = x;

           pointsY[count] = y;
           count++;
           numberOfSides--;
           System.out.println(pointsY[count - 1]);
       }
       if(numberOfSides == 0){
           graphicsContext.setLineWidth(EditTools.lineWidth);
           graphicsContext.setStroke(EditTools.colorPicker.getValue());
           graphicsContext.setFill(EditTools.fillPicker.getValue());
           graphicsContext.strokePolygon(points, pointsY, points.length);
           graphicsContext.fillPolygon(points, pointsY, points.length);   
       }  
	}
	
	public void mouseRelease() {
        if(numberOfSides == 0){
		    graphicsContext.strokePolygon(points, pointsY, numberOfSides);
		    graphicsContext.fillPolygon(points, pointsY, numberOfSides);
        }
	}
}
