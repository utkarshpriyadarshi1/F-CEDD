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
import javafx.stage.Modality;
import javafx.stage.Stage;

class ProgressStatus {

    private static Label topLabel, mainLabel, fileLabel, bottomLabel;
    private static int totalTask;
    private static ProgressBar progressBar;

    ProgressStatus() {

    }

    ProgressStatus(String operation, String inputDir, String outputDir, int totalTask) {
        topLabel = new Label(operation + " from " + inputDir + " to " + outputDir);
        ProgressStatus.totalTask = totalTask;
    }

    void display(String currentFile, int currentTask) {
        Stage window = new Stage();

        int taskProgress = currentTask * 100 / totalTask;
        float taskPrgss = taskProgress / 100;
        mainLabel = new Label("Paused - " + (taskProgress) + " % Completed");

        fileLabel = new Label("File Name : " + currentFile);
        fileLabel.setTextFill(Color.GREEN);

        bottomLabel = new Label("Items Remaining: " + (totalTask - currentTask));

        progressBar.setProgress(taskPrgss);
        progressBar.setMinWidth(400);
        progressBar.setMinHeight(30);

        // progressBar.progressProperty().bind(taskPrgss);
        Image cancelImage = new Image(getClass().getResourceAsStream("icons/error.png"));
        Button stopBtn = new Button();
        stopBtn.setGraphic(new ImageView(cancelImage));

        stopBtn.setCancelButton(true);
        stopBtn.setMinHeight(30);
        stopBtn.setMinWidth(30);

        stopBtn.setOnAction(event -> {
            stopBtn.setDisable(true);
            window.close();
        });

        Button pauseResumeBtn = new Button();
        pauseResumeBtn.setGraphic(new ImageView(new Image("icons/play.png")));
        pauseResumeBtn.setDefaultButton(true);
        pauseResumeBtn.setMinHeight(30);
        pauseResumeBtn.setMinWidth(30);
        pauseResumeBtn.setOnAction(event -> {
            pauseResumeBtn.setGraphic(new ImageView(new Image("icons/pause.png")));
            progressBar.setProgress(0);
            stopBtn.setDisable(false);
            progressBar.progressProperty().unbind();
            fileLabel.textProperty().unbind();
            progressBar.progressProperty().unbind();
        });

        HBox singleTask = new HBox(10);
        singleTask.getChildren().addAll(progressBar, pauseResumeBtn, stopBtn);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(topLabel, mainLabel, singleTask, fileLabel, bottomLabel);

        Scene scene = new Scene(root, 510, 160, Color.WHITE);

        window.setTitle(mainLabel.getText());
        window.getIcons().add(new Image("icons/fcedd.png"));
        window.setResizable(false);
        window.setScene(scene);
        window.initModality(Modality.APPLICATION_MODAL);
        window.show();
    }
}