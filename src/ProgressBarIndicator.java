import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class ProgressBarIndicator extends Application {

    public void start(Stage primaryStage) {

        Label topLabel = new Label("Compressing"+" from "+"Input Directiory"+" to "+"Output Directory");
        Label mainLabel = new Label("Paused - 0% Complete");
        Label fileLabel = new Label("File Name: "+"filename");
        fileLabel.setTextFill(Color.GREEN);
        Label bottomLabel = new Label("Items Remaining: "+"12,223");

        final ProgressBar progressBar = new ProgressBar(0);
        progressBar.setMinWidth(400);
        progressBar.setMinHeight(30);

        //Image cancelImage = new Image(getClass().getResourceAsStream("cancelBtn.png"));
        Button stopBtn = new Button();
        //stopBtn.setGraphic(new ImageView(cancelImage));
        stopBtn.setCancelButton(true);
        stopBtn.setMinHeight(30);
        stopBtn.setMinWidth(30);
        stopBtn.setOnAction(event ->
        {
            stopBtn.setDisable(true);
            //taskProgress.cancel(true);
            progressBar.progressProperty().unbind();
   //         statusLabel.textProperty().unbind();
            primaryStage.close();
        });

        Button pauseResumeBtn = new Button();
        pauseResumeBtn.setDefaultButton(true);
        pauseResumeBtn.setMinHeight(30);
        pauseResumeBtn.setMinWidth(30);
        pauseResumeBtn.setOnAction(event -> {
            pauseResumeBtn.setText("|>");
            progressBar.setProgress(0);
            stopBtn.setDisable(false);
            // Create a Task.
            //taskProgress = new CopyTask();
            // Unbind progress property
            progressBar.progressProperty().unbind();
            // Bind progress property
  //          progressBar.progressProperty().bind(taskProgress.progressProperty());
            
            // Unbind text property for Label.
            //statusLabel.textProperty().unbind();
            
            // Bind the text property of Label
            // with message property of Task
            //statusLabel.textProperty().bind(taskProgress.messageProperty());
            // When completed tasks
            //taskProgress.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, new EventHandler<WorkerStateEvent>() {
             //   @Override
             //   public void handle(WorkerStateEvent t) {
            //        List<File> copied = taskProgress.getValue();
              //      statusLabel.textProperty().unbind();
             //       statusLabel.setText("Copied: " + copied.size());
            //          primaryStage.close();
             //   }
            //});
            // Start the Task.
            //new Thread(taskProgress).start();
        });

        HBox singleTask = new HBox(10);
        singleTask.getChildren().addAll(progressBar, pauseResumeBtn, stopBtn);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(topLabel, mainLabel, singleTask, fileLabel, bottomLabel);

        Scene scene = new Scene(root, 510, 160, Color.WHITE);
        primaryStage.setTitle(mainLabel.getText());
        //primaryStage.getIcons();
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}