/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paint;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author shree
 */
public class OpenSave {
    public String fileName;
    public String fileExtension;
    public String saveFileName;
    public String saveFileExtension;
    public EventHandler<ActionEvent> saveClick;
    public PainT p;
    public Canvas canvas;
    public Thread autosave;
    public ImageView iView;

    // FileChooser
    public FileChooser fileChooser = new FileChooser();
    public static File outputFile;

    public OpenSave(Canvas canvas) {
        this.canvas = canvas;
    }

    /**
     * Handles all the events for open, save and save as.
     * 
     * @param stage Takes in input the current stage.
     */
    // EventHandler for menu
    public void menuEventHandler(Stage stage) {
        EditTools.autosaveBox.setOnAction(e -> {
            if (EditTools.autosaveBox.isSelected()) {
                autosave = new Autosave(10, canvas);
                autosave.setDaemon(true);
                autosave.start();
            }
        });

        FileChooser.ExtensionFilter extJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        FileChooser.ExtensionFilter extGIF = new FileChooser.ExtensionFilter("Bmp files (*.bmp)", "*.bmp");
        FileChooser.ExtensionFilter allFilter = new FileChooser.ExtensionFilter("All files", "*.*");
        fileChooser.getExtensionFilters().addAll(allFilter, extJPG, extPNG, extGIF);
        // Handles the event when the user presses Open File.
        EventHandler<ActionEvent> openClick = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {

                try {
                    File file = fileChooser.showOpenDialog(stage);

                    BufferedImage bImage = ImageIO.read(file); // Reading the file

                    Image image = SwingFXUtils.toFXImage(bImage, null);
                    fileName = file.getName();
                    fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
                    System.out.println(">> fileExtension " + fileExtension);
                    Log.oThread(fileName);

                    // tempImage = image;
                    iView = new ImageView();
                    iView.setImage(image);
                    iView.setFitWidth(200);

                    iView.setPreserveRatio(true);

                    Image tempImage = iView.getImage();

                    PaintCanvas.globalGC.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                    PaintCanvas.globalGC.fillRect(0, 0, tempImage.getWidth(), tempImage.getHeight());
                    canvas.setWidth(tempImage.getWidth());
                    canvas.setHeight(tempImage.getHeight());
                    PaintCanvas.globalGC.drawImage(tempImage, 0, 0, tempImage.getWidth(), tempImage.getHeight());
                    System.out.println("Image opened successfully!");
                } catch (IOException ex) {
                    System.out.println("Error in opening file");
                } catch (IllegalArgumentException ex) {
                    System.out.println("File not opened");
                }
            }
        };

        // saveClick
        saveClick = new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent e) {
                if (outputFile != null) {
                    save();
                    if (autosave != null) {
                        sThread();
                    }

                } else {
                    outputFile = fileChooser.showSaveDialog(stage);

                    saveFileName = outputFile.getName();
                    saveFileExtension = saveFileName.substring(saveFileName.lastIndexOf(".") + 1,
                            outputFile.getName().length());

                    if (saveFileExtension.equals(fileExtension)) {
                        save();
                        System.out.println(">> SavefileExtension " + saveFileExtension);
                    } else {
                        saveWarning(stage, ConfirmExit.display("WARNING",
                                "WARNING:\nData loss can happen\nDo you want to save?"));
                        System.out.println(">> SavefileExtension " + saveFileExtension);
                    }

                    if (autosave != null) {
                        sThread();
                    }

                }
                Log.slThread(outputFile);
            }
        };

        // When User presses Save File
        EventHandler<ActionEvent> saveAsClick = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                outputFile = fileChooser.showSaveDialog(stage);

                saveFileName = outputFile.getName();
                saveFileExtension = saveFileName.substring(saveFileName.lastIndexOf(".") + 1,
                        outputFile.getName().length());

                if (saveFileExtension.equals(fileExtension)) {
                    save();
                    System.out.println(">> SavefileExtension " + saveFileExtension);
                } else {
                    saveWarning(stage,
                            ConfirmExit.display("WARNING", "WARNING:\nData loss can happen\nDo you want to save?"));
                    System.out.println(">> SavefileExtension " + saveFileExtension);
                }
                if (autosave != null) {
                    sThread();
                }
                Log.slThread(outputFile);
            }
        };

        // When User presses Exit.
        EventHandler<ActionEvent> exitClick = new EventHandler<ActionEvent>() {
            @Override
            public void handle(final ActionEvent e) {
                closeProgram(stage, ConfirmExit.display("PainT", "Do you want to save changes?"));
            }
        };

        // Listeners for opening, saving and exiting
        Menu.openFile.setOnAction(openClick);
        Menu.saveFile.setOnAction(saveClick);
        Menu.saveAsFile.setOnAction(saveAsClick);
        Menu.exitFile.setOnAction(exitClick);
    }

    public void save() {

        try {
            WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
            canvas.snapshot(null, writableImage);
            ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);
        } catch (IOException ex) {
            System.out.println("Error in saving");
        }

    }

    public void sThread() {
        autosave.interrupt();
        autosave = new Autosave(10, canvas);
        autosave.start();
    }

    /**
     *
     * @param stage
     * @param display
     */
    public void saveWarning(Stage stage, String display) {
        String answer = display;
        switch (answer) {
            case "save":
                save();
                break;
            case "dontSave":
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
    public void closeProgram(Stage stage, String display) {
        String answer = display;
        switch (answer) {
            case "save":
                if (outputFile != null) {
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);

                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);
                    } catch (IOException ex) {
                        System.out.println("Error in saving");
                    }
                } else {
                    FileChooser.ExtensionFilter extPNG = new FileChooser.ExtensionFilter("PNG File (*.png)", "*.png");
                    fileChooser.getExtensionFilters().add(extPNG);
                    outputFile = fileChooser.showSaveDialog(stage);
                    WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
                    canvas.snapshot(null, writableImage);

                    try {

                        ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", outputFile);

                    } catch (IOException ex) {
                        System.out.println("Error in saving as");
                    }

                }
                if (autosave != null) {
                    autosave.interrupt();
                }
                stage.close();
                break;
            case "dontSave":
                if (autosave != null) {
                    autosave.interrupt();
                }
                stage.close();
                break;
            case "cancel":
                break;
        }

    }

}
