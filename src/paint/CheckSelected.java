/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

/**
 *
 * @author shree
 */
public class CheckSelected {
    public String selectedTool;
    public EditTools editTool;
    
    public CheckSelected(EditTools editTool){
        this.editTool = editTool;
    }
    
    /**
     * Shows which tool is selected based on the toggle selects of the buttons.
     */
    public void checkTrue(){
//    	System.out.println(editTool.toggleGroup.getSelectedToggle().hashCode());
//    	System.out.println(editTool.toggleGroup.getSelectedToggle().equals(editTool.pencilTool));
    	
        if(editTool.pencilTool.isSelected()){
            selectedTool = "Free line selected";
        }else if(editTool.lineTool.isSelected()){
            selectedTool = "Line selected";
        }else if(editTool.rectangleTool.isSelected()){
            selectedTool = "Rectangle selected";
        }
        else if(editTool.squareTool.isSelected()){
            selectedTool = "Square selected";
        }
        else if(editTool.ellipseTool.isSelected()){
            selectedTool = "Ellipse selected";
        }
        else if(editTool.circleTool.isSelected()){
            selectedTool = "Circle selected";
        }else if(editTool.eraserTool.isSelected()){
            selectedTool = "Eraser selected";
        }else if(editTool.selectTool.isSelected()){
            selectedTool = "Select Tool selected";
        }else if(editTool.moveTool.isSelected()){
            selectedTool = "Move Tool selected";
        }else if(editTool.copyTool.isSelected()){
            selectedTool = "Copy Tool selected";
        }else if(editTool.triangleTool.isSelected()){
            selectedTool = "Triangle selected";
        }else if(editTool.polygonTool.isSelected()){
            selectedTool = "Polygon selected";
        }else{
            selectedTool = "No Tool selected";
        }
        editTool.toolInfo.setText(selectedTool);
        Log.tLogS(selectedTool, this);
    }
}
