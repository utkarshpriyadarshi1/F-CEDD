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

public class AlertBox {

    public void display(Boolean status, String operation) {

        Stage window = new Stage();

        Image img;
        String message;
        String title;

        if (status) {
            img = new Image("icons/success4.png");
            title = "Operation successful";
            message = "File(s) " + operation + " has been successfully completed!!";
        } else {
            img = new Image("icons/error1.png");
            message = "File(s) " + operation + " has failed!!";
            title = "Operation unsuccessful";
        }
        ImageView image = new ImageView(img);
        image.setFitWidth(60);
        image.setFitHeight(60);

        Label label = new Label();
        label.setText(message);
        label.setStyle("-fx-font: 22 arial");

        Button btn = new Button("OK");
        btn.setStyle("-fx-font: 16 arial;-fx-label-padding: 1 20 1 20");
        btn.setDefaultButton(true);
        btn.setOnAction(e -> window.close());

        HBox messageBox = new HBox(10, image, label);
        messageBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(messageBox, btn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 650, 150);

        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setScene(scene);
        window.setResizable(false);
        window.getIcons().add(img);
        window.show();
    }
}

