import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CompressionChoiceBox {
    private String formatArray[] = {"zip", "tar", "tar.xz", "tar.gz", "7z"};
    private static String formatCmprs;// = null;

    public String display() {
        System.out.println("I'm here : 1 CompressionChoiceBox.display");
        Stage window = new Stage();
        window.setTitle("Select Compression Format");
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(400);
        window.setMinHeight(150);

        ChoiceBox<String> chBox = new ChoiceBox<>();
        chBox.getItems().addAll(formatArray);
        // chBox.setValue("zip");

        Button btn = new Button("OK");
        btn.setOnAction(event -> {
                    formatCmprs = chBox.getValue();
                    //getChoice(chBox);
                    window.close();
                    //return formatCmprs;
                }
        );

        HBox layout = new HBox(10);
        layout.getChildren().addAll(chBox, btn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();
        System.out.println("I'm here : 2 CompressionChoiceBox.display");
        System.out.println("Format Compress = " + formatCmprs);
        return formatCmprs;
    }

    private void getChoice(ChoiceBox<String> chBox) {

        System.out.println("I'm here : 1 getChoice");
        formatCmprs = chBox.getValue();
        System.out.println("I'm here : 2 getChoice");
    }
}
