import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

class AlertBox {
    private static String message;
    private static String title;
    private static Boolean status;

    AlertBox(String title, String msg, Boolean status) {
        AlertBox.message = msg;
        AlertBox.title = title;
        AlertBox.status = status;
    }

    void display() {
        Stage window = new Stage();
        String tempString;

        if (status)
            tempString = "icons/ok.png";
        else
            tempString = "icons/error.png";

        Image img = new Image(tempString);
        ImageView image = new ImageView(img);
        image.setFitWidth(60);
        image.setFitHeight(60);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font: 22 arial");

        Button btn = new Button("OK");
        btn.getStyleClass().add("okBtn");
        btn.setDefaultButton(true);
        btn.setOnAction(e -> window.close());

        HBox messageBox = new HBox(10, image, label);
        messageBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageBox, btn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 650, 150);
        scene.getStylesheets().add("DayStyle.css");

        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.setResizable(false);
        window.getIcons().add(img);
        window.show();
    }
}