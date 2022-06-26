/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.io.File;
import java.io.IOException;
import java.awt.Desktop;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author shree
 */
public class Menu {
    // Menu
    public static MenuBar mBar;
    public static MenuItem openFile;
    public static MenuItem saveFile;
    public static MenuItem saveAsFile;
    public static MenuItem exitFile;
    public static MenuItem toolHelp;
    public static MenuItem about;
    public static VBox menuBox = new VBox();

    public static void createMenu() {
        mBar = new MenuBar();

        // All the menus, menuBar and menuItems
        javafx.scene.control.Menu file = new javafx.scene.control.Menu("_File");
        openFile = new MenuItem("Open");
        saveFile = new MenuItem("Save");
        saveAsFile = new MenuItem("Save As");
        exitFile = new MenuItem("Exit");

        // Shortcuts
        saveFile.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCodeCombination.CONTROL_DOWN));
        saveAsFile.setAccelerator(
                new KeyCodeCombination(KeyCode.S, KeyCodeCombination.SHIFT_DOWN, KeyCodeCombination.CONTROL_DOWN));
        openFile.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCodeCombination.CONTROL_DOWN));

        // Aidding menu Items
        file.getItems().add(openFile);
        file.getItems().add(saveFile);
        file.getItems().add(saveAsFile);
        file.getItems().add(new SeparatorMenuItem());
        file.getItems().add(exitFile);
        mBar.getMenus().add(file);

        javafx.scene.control.Menu edit = new javafx.scene.control.Menu("_Edit");
        mBar.getMenus().add(edit);

        javafx.scene.control.Menu tools = new javafx.scene.control.Menu("_Tools");
        mBar.getMenus().add(tools);

        javafx.scene.control.Menu help = new javafx.scene.control.Menu("_Help");
        toolHelp = new MenuItem("Tools Help");
        about = new MenuItem("About");
        help.getItems().add(toolHelp);
        help.getItems().add(about);
        mBar.getMenus().add(help);
        menuBox.getChildren().add(mBar);
    }

    public static void menuEventHandler(Stage stage) {

        Menu.about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("About"));
                dialogVbox.getChildren().add(new Text("\nv1.1\nPaint Application\nShree Software Solutions\n"));
                Button releaseNotes = new Button("Release Notes");
                Button ok = new Button("Ok");
                dialogVbox.getChildren().add(releaseNotes);
                dialogVbox.getChildren().add(ok);
                dialogVbox.setAlignment(Pos.CENTER);
                releaseNotes.setOnAction((ActionEvent e) -> {
                    File releaseFile = new File("D:\\NetBeans Projects\\PainT\\src\\paint\\Release Notes.txt");
                    try {
                        Desktop.getDesktop().open(releaseFile);
                    } catch (IOException ex) {
                        Logger.getLogger(PainT.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                ok.setOnAction((ActionEvent e) -> {
                    dialog.close();
                });
                Scene dialogScene = new Scene(dialogVbox, 400, 300);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });

        Menu.toolHelp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                VBox dialogVbox = new VBox(20);
                dialogVbox.getChildren().add(new Text("Tools Help:"));
                dialogVbox.getChildren().add(new Text(
                        "\nFree Line Tool -> Draw a line freely.\nLine Tool -> Draw a straight line.\nRectangle Tool -> Draw a rectangle\nSquare Tool: Draw a square.\nEllipse Tool: Draw an ellipse.\nCircle Tool: Draw a circle.\nEraser Tool: Erase the contents on the canvas freely.\nText Tool: Press anywhere on the canvas and fill out the text on the pop up window.\nColor Grabber: Sample any color from the canvas and changes the stroke to the newly sampled color.\n"));
                Button ok = new Button("Ok");
                dialogVbox.getChildren().add(ok);
                dialogVbox.setAlignment(Pos.CENTER);

                ok.setOnAction((ActionEvent e) -> {
                    dialog.close();
                });
                Scene dialogScene = new Scene(dialogVbox, 800, 500);
                dialog.setScene(dialogScene);
                dialog.show();
            }
        });
    }
}
