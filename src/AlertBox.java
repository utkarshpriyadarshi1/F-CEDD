
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    public  void display(String title, String message) {
        System.out.println("I'm here : 1 AlertBox.display");
        Stage window = new Stage();
        window.setTitle(title);
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(400);
        window.setMinHeight(150);

        Label label = new Label();
        label.setText(message);

        Button btn = new Button("OK");
        btn.setOnAction(e -> window.close());
        //TODO: show alert box icons(in the titleBar, as well as in the body)
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, btn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
        //window.showAndWait();
        System.out.println("I'm here : 2 AlertBox.display");
    }
}

