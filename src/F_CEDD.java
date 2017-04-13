import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class F_CEDD extends Application {

    private int buffer = 1024;
    private Logger logger = Logger.getLogger("MyLog");
    private Stage window;
    private List<File> listOfFiles;
    private String outputFileName = "PSPCrypt";
    private String outputDir = "/";
    private ObservableList<File> items;
    private ObservableList<File> selectedFiles;
    private final String[] compressFormat = {".7z", ".tar", ".tar.bz2", ".tar.gz", ".tar.xz", ".zip"};
    private static ListView<File> compressInputList;
    private final ArrayList<File> listFile = new ArrayList<>();

    private ListView<File> addToList(List<File> lost1, ListView<File> listView) {
        items = listView.getItems();
        items.addAll(lost1);
        listView.setItems(items);
        return listView;
    }

    private ListView<File> addToList(ListView<File> listView, Boolean isFile) {
        items = listView.getItems();
        if (isFile)
            items.addAll(FXCollections.observableArrayList(new FileChooser().showOpenMultipleDialog(window)));
        else
            items.add((new DirectoryChooser()).showDialog(window));//.showOpenMultipleDialog(window);
        //this.items = FXCollections.observableArrayList(listOfFiles);
        listView.setItems(items);
        return listView;
    }

    private ListView<File> removeFromList(ListView<File> listView) {
        items = listView.getItems();
        selectedFiles = listView.getSelectionModel().getSelectedItems();
        items.removeAll(FXCollections.observableArrayList(selectedFiles));
        //listOfFiles.remove(selectedFiles);
        //System.out.println("list:"+listOfFiles);
        //items = FXCollections.observableArrayList(listOfFiles);
        listView.setItems(items);
        return listView;
    }

    private ListView<File> clearList(ListView<File> listView) {
        listView.setItems(null);
        return listView;
    }

    @Override
    public void start(Stage primaryStage) {
        Stage window;
        window = primaryStage;

        Label title = new Label("F-CEDD: File manipulation tool ");
        title.getStyleClass().add("title");
        HBox titleBox = new HBox(title);
        titleBox.getStyleClass().add("titleBox");
      
        /* Button for ListView */
        Button clearBtn = new Button("Remove all");
        clearBtn.getStyleClass().add("viewBtn");
        Button removeBtn = new Button("Remove");
        removeBtn.getStyleClass().add("viewBtn");
        Button addFBtn = new Button("Add File(s)");
        addFBtn.getStyleClass().add("viewBtn");
        Button addDBtn = new Button("Add Folder");
        addDBtn.getStyleClass().add("viewBtn");


        /* Button Box for ListView */
        HBox viewBtnBox = new HBox(10, addFBtn, addDBtn, removeBtn, clearBtn);
        viewBtnBox.getStyleClass().add("viewBtnBox");
        viewBtnBox.setAlignment(Pos.CENTER_RIGHT);

        Label compressInputLabel = new Label("Input");

        /* ListView<File> */
        compressInputList = new ListView<>();
        compressInputList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        compressInputList.getStyleClass().add("list");

        VBox compressInputListBox = new VBox(10, compressInputList, viewBtnBox);
        compressInputListBox.getStyleClass().add("subBox");

        VBox compressInput = new VBox(10, compressInputLabel, compressInputListBox);
        compressInput.getStyleClass().add("inputBox");

        /* Compress Output Box */
        Label compressOutputLabel = new Label("Output");

        Label compressOutputPathLabel = new Label("         Path : ");
        TextField compressOutputPathTextField = new TextField();
        Button compressOutputSelectPathBtn = new Button("Select Path");
        HBox compressOutputPath = new HBox(10, compressOutputPathLabel, compressOutputPathTextField, compressOutputSelectPathBtn);
        compressOutputPath.getStyleClass().add("subBox");

        Label compressOutputFileLabel = new Label("File Name : ");
        TextField compressOutputFileTextField = new TextField();
        ChoiceBox<String> compressOutputFileFormat = new ChoiceBox<>(FXCollections.observableArrayList(compressFormat));
        compressOutputFileFormat.getSelectionModel().selectFirst();
        HBox compressOutputFile = new HBox(10, compressOutputFileLabel, compressOutputFileTextField, compressOutputFileFormat);
        compressOutputFile.getStyleClass().add("subBox");

        VBox compressOutput = new VBox(10, compressOutputLabel, compressOutputPath, compressOutputFile);
        compressOutput.getStyleClass().add("outputBox");

        Button compressBtn = new Button("Compress");
        compressBtn.getStyleClass().add("mainBtn");

        VBox compressBtnBox = new VBox(compressBtn);
        compressBtnBox.setAlignment(Pos.CENTER_RIGHT);

        VBox compressBox = new VBox(10, compressInput, compressOutput, compressBtnBox);
        compressBox.getStyleClass().add("main");

        Tab compressTab = new Tab("Compress", compressBox);
        compressTab.setClosable(false);

        Label decompressInputLabel = new Label("Input");

        //Label decompressInputFileLabel = new Label("File : ");
        TextField decompressInputFileTextField = new TextField();
        Button decompressSelectFileBtn = new Button("Select File");

        HBox decompressInputFile = new HBox(10, decompressInputFileTextField, decompressSelectFileBtn);
        decompressInputFile.getStyleClass().add("subBox");

        VBox decompressInput = new VBox(10, decompressInputLabel, decompressInputFile);
        decompressInput.getStyleClass().add("inputBox");

        Label decompressOutputLabel = new Label("Output");

        //Label decompressOutputPathLabel = new Label("Path : ");
        TextField decompressOutputPathTextField = new TextField();
        Button decompressOutputSelectPathBtn = new Button("Select Path");

        HBox decompressOutputPathBox = new HBox(10, decompressOutputPathTextField, decompressOutputSelectPathBtn);
        decompressOutputPathBox.getStyleClass().add("subBox");

        VBox decompressOutput = new VBox(10, decompressOutputLabel, decompressOutputPathBox);
        decompressOutput.getStyleClass().add("outputBox");

        Button decompressBtn = new Button("Decompress");
        decompressBtn.getStyleClass().add("mainBtn");

        VBox decompressBtnBox = new VBox(decompressBtn);
        decompressBtnBox.setAlignment(Pos.BOTTOM_RIGHT);

        VBox decompressBox = new VBox(10, decompressInput, decompressOutput, decompressBtnBox);
        decompressBox.getStyleClass().add("main");

        Tab decompressTab = new Tab("Decompress", decompressBox);
        decompressTab.setClosable(false);


        Label encryptInputLabel = new Label("Input");

        Label encryptInputFileLabel = new Label("File : ");
        TextField encryptInputFileTextField = new TextField();
        Button encryptSelectFileBtn = new Button("Select File");
        HBox encryptInputFile = new HBox(10, encryptInputFileLabel, encryptInputFileTextField, encryptSelectFileBtn);
        encryptInputFile.getStyleClass().add("subBox");

        Label encryptInputPasswordLabel = new Label("Password : ");
        PasswordField encryptInputPasswordField = new PasswordField();
        Region space4 = new Region();
        space4.setMinWidth(100);
        HBox encryptInputPassword = new HBox(10, encryptInputPasswordLabel, encryptInputPasswordField, space4);
        encryptInputPassword.getStyleClass().add("subBox");


        VBox encryptInput = new VBox(10, encryptInputLabel, encryptInputFile, encryptInputPassword);
        encryptInput.getStyleClass().add("inputBox");

        Label encryptOutputLabel = new Label("Output");

        Label encryptOutputPathLabel = new Label("Path : ");
        TextField encryptOutputPathTextField = new TextField();
        Button encryptSelectPathBtn = new Button("Select Path");
        HBox encryptOutputPathBox = new HBox(10, encryptOutputPathLabel, encryptOutputPathTextField, encryptSelectPathBtn);
        encryptOutputPathBox.getStyleClass().add("subBox");

        Label encryptOutputFileLabel = new Label("File Name : ");
        TextField encryptOutputFileTextField = new TextField();
        Region space3 = new Region();
        space3.setMinWidth(100);
        HBox encryptOutputFile = new HBox(10, encryptOutputFileLabel, encryptOutputFileTextField, space3);
        encryptOutputFile.getStyleClass().add("subBox");

        Button encryptBtn = new Button("Encrypt");
        encryptBtn.getStyleClass().add("mainBtn");
        VBox encryptBtnBox = new VBox(encryptBtn);
        encryptBtnBox.setAlignment(Pos.CENTER_RIGHT);


        VBox encryptOutput = new VBox(10, encryptOutputLabel, encryptOutputPathBox, encryptOutputFile);
        encryptOutput.getStyleClass().add("outputBox");

        VBox encryptBox = new VBox(10, encryptInput, encryptOutput, encryptBtnBox);
        encryptBox.getStyleClass().add("main");

        Tab encryptTab = new Tab("Encrypt", encryptBox);
        encryptTab.setClosable(false);


        Label decryptInputLabel = new Label("Input");

        Label decryptInputFileLabel = new Label("File : ");
        TextField decryptInputFileTextField = new TextField();
        Button decryptSelectFileBtn = new Button("Select File");
        HBox decryptInputFile = new HBox(10, decryptInputFileLabel, decryptInputFileTextField, decryptSelectFileBtn);
        decryptInputFile.getStyleClass().add("subBox");

        Label decryptInputPasswordLabel = new Label("Password : ");
        PasswordField decryptInputPasswordField = new PasswordField();

        Region space2 = new Region();
        space2.setMinWidth(100);

        HBox decryptInputPassword = new HBox(10, decryptInputPasswordLabel, decryptInputPasswordField, space2);
        decryptInputPassword.getStyleClass().add("subBox");


        VBox decryptInput = new VBox(10, decryptInputLabel, decryptInputFile, decryptInputPassword);
        decryptInput.getStyleClass().add("inputBox");

        Label decryptOutputLabel = new Label("Output");

        Label decryptOutputPathLabel = new Label("Path : ");
        TextField decryptOutputPathTextField = new TextField();
        Button decryptSelectPathBtn = new Button("Select Path");
        HBox decryptOutputPathBox = new HBox(10, decryptOutputPathLabel, decryptOutputPathTextField, decryptSelectPathBtn);
        decryptOutputPathBox.getStyleClass().add("subBox");

        Label decryptOutputFileLabel = new Label("File Name : ");
        TextField decryptOutputFileTextField = new TextField();

        Region space1 = new Region();
        space1.setMinWidth(100);

        HBox decryptOutputFile = new HBox(10, decryptOutputFileLabel, decryptOutputFileTextField, space1);
        decryptOutputFile.getStyleClass().add("subBox");

        Button decryptBtn = new Button("Decrypt");
        decryptBtn.getStyleClass().add("mainBtn");
        VBox decryptBtnBox = new VBox(decryptBtn);
        decryptBtnBox.setAlignment(Pos.CENTER_RIGHT);

        VBox decryptOutput = new VBox(10, decryptOutputLabel, decryptOutputPathBox, decryptOutputFile);
        decryptOutput.getStyleClass().add("outputBox");

        VBox decryptBox = new VBox(10, decryptInput, decryptOutput, decryptBtnBox);
        decryptBox.getStyleClass().add("main");

        Tab decryptTab = new Tab("Decrypt", decryptBox);
        decryptTab.setClosable(false);

        TabPane tabPane = new TabPane(compressTab, decompressTab, encryptTab, decryptTab);

        VBox root = new VBox(titleBox, tabPane);

        Scene scene = new Scene(root, 720, 640);
        scene.getStylesheets().add("Style.css");

        window.setTitle("F-CEDD");
        window.setScene(scene);
        window.show();

        /* Path Input Actions */
        compressOutputSelectPathBtn.setOnAction(event -> compressOutputPathTextField.setText((new DirectoryChooser().showDialog(window)).toString()));
        decompressOutputSelectPathBtn.setOnAction(event -> decompressOutputPathTextField.setText((new DirectoryChooser().showDialog(window)).toString()));
        encryptSelectPathBtn.setOnAction(event -> encryptOutputPathTextField.setText((new DirectoryChooser().showDialog(window)).toString()));
        decryptSelectPathBtn.setOnAction(event -> decryptOutputPathTextField.setText((new DirectoryChooser().showDialog(window)).toString()));

        /* File Input Actions */
        decompressSelectFileBtn.setOnAction(event -> decompressInputFileTextField.setText((new FileChooser().showOpenDialog(window)).toString()));
        encryptSelectFileBtn.setOnAction(event -> encryptInputFileTextField.setText((new FileChooser().showOpenDialog(window)).toString()));
        decryptSelectFileBtn.setOnAction(event -> decryptInputFileTextField.setText((new FileChooser().showOpenDialog(window)).toString()));

        /* List Button Actions */
        addFBtn.setOnAction(event -> compressInputList = addToList(compressInputList, true));
        addDBtn.setOnAction(event -> compressInputList = addToList(compressInputList, false));
        removeBtn.setOnAction(event -> compressInputList = removeFromList(compressInputList));
        clearBtn.setOnAction(event -> compressInputList = clearList(compressInputList));

        compressInputList.setOnDragOver(event -> {
            if (event.getGestureSource() != compressInputList
                    && event.getDragboard().hasString()) {
                /* allow for both copying and moving, whatever user chooses */
                event.acceptTransferModes(TransferMode.ANY);
            }
            event.consume();
        });

        compressInputList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasString()) {
                addToList(db.getFiles(), compressInputList);
                success = true;
            }
            /* let the source know whether the string was successfully
             * transferred and used */
            event.setDropCompleted(success);

            event.consume();
        });
        
        /* Compress Button Action*/
        compressBtn.setDefaultButton(true);

        compressBtn.setOnAction(event -> {

            try {
                (new FileOperation()).compress(compressInputList.getItems(), compressOutputFileFormat.getValue(), compressOutputPathTextField.getText(), compressOutputFileTextField.getText());
                (new AlertBox()).display(true, "Compression");
            } catch (Exception eb) {
                (new AlertBox()).display(false, "Compression");
            }
        });

        /* Decompress Button Action*/
        decompressBtn.setOnAction(event -> {
            //new MyController().doDecompress(decompressInputFileTextField.getText(), decompressOutputPathTextField.getText());
        });

        /* Encrypt Button Action*/
        encryptBtn.setOnAction(event -> {
            //new MyController().doEncrypt(encryptInputFileTextField.getText(), encryptInputPasswordField.getText(), encryptOutputPathTextField.getText(), encryptOutputFileTextField.getText());
        });

        /* Decrypt Button Action*/
        decryptBtn.setOnAction(event -> {
            try {
                // if (new FileOperation().encrypt(decryptInputFileTextField.getText(), decryptInputPasswordField.getText(), decryptOutputPathTextField.getText(), decryptOutputFileTextField.getText()))
                (new AlertBox()).display(true, "Decryption");
                ;
            } catch (Exception eb) {
                (new AlertBox()).display(false, "Decryption");
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }

}