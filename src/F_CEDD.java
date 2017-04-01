import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;


public class F_CEDD extends Application {

    private String compressFormat[] = {".zip", ".tar", ".tar.xz", ".tar.gz", ".7z"};

    private static ListView<File> compressInputList;
    private ArrayList<File> listFile = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) {
        Stage window;
        window = primaryStage;

        /* Title Box */
        Label title = new Label("F-CEDD: File manipulation tool ");
        title.getStyleClass().add("title");
        HBox titleBox = new HBox(title);
        titleBox.getStyleClass().add("titleBox");
      
        /* Button for ListView */
        Button clearBtn = new Button("Clear List");
        clearBtn.getStyleClass().add("viewBtn");
        Button removeBtn = new Button("Remove From List");
        removeBtn.getStyleClass().add("viewBtn");
        Button addBtn = new Button("Add To List");
        addBtn.getStyleClass().add("viewBtn");

        /* Button Box for ListView */
        VBox viewBtnBox = new VBox(10, addBtn, removeBtn, clearBtn);
        viewBtnBox.getStyleClass().add("viewBtnBox");

        /* Compress Input Box */
        Label compressInputLabel = new Label("Input");

        /*static ListView<File> */
        compressInputList = new ListView<>();
        compressInputList.getStyleClass().add("list");

        HBox compressInputListBox = new HBox(10, compressInputList, viewBtnBox);
        compressInputListBox.getStyleClass().add("subBox");

        VBox compressInput = new VBox(10, compressInputLabel, compressInputListBox);
        compressInput.getStyleClass().add("inputBox");

        /* Compreis Output Box */
        Label compressOutputLabel = new Label("Output");

        Label compressOutputFileLabel = new Label("File Name : ");
        TextField compressOutputFileTextField = new TextField();

        compressOutputFileTextField.setPromptText("Enter your comment.");
        Label compressOutputPathLabel = new Label("Path : ");

        TextField compressOutputPathTextField = new TextField();
        Button compressOutputSelectPathBtn = new Button("Select");

        ChoiceBox compressOutputFileFormat = new ChoiceBox();
        compressOutputFileFormat.getItems().addAll(compressFormat);

        /* Compress Output Path Box */
        HBox compressOutputPath = new HBox(10, compressOutputPathLabel, compressOutputPathTextField, compressOutputSelectPathBtn);
        compressOutputPath.getStyleClass().add("subBox");

        HBox compressOutputFile = new HBox(10, compressOutputFileLabel, compressOutputFileTextField, compressOutputFileFormat);
        compressOutputFile.getStyleClass().add("subBox");

        VBox compressOutput = new VBox(10, compressOutputLabel, compressOutputPath, compressOutputFile);
        compressOutput.getStyleClass().add("outputBox");

        /* Compress Box */
        Button compressBtn = new Button("Compress");
        VBox compressBox = new VBox(10, compressInput, compressOutput, compressBtn);
        compressBox.getStyleClass().add("main");

        /* Compression Tab */
        Tab compressTab = new Tab("Compress", compressBox);
        compressTab.setClosable(false);

        /* Decompress Input Box */
        Label decompressInputLabel = new Label("Input");

        Label decompressInputFileLabel = new Label("File : ");
        TextField decompressInputFileTextField = new TextField();
        Button decompressSelectFileBtn = new Button("Select");
        /* Decompress Input Box */
        HBox decompressInput = new HBox(10, decompressInputLabel, decompressInputFileLabel, decompressInputFileTextField, decompressSelectFileBtn);
        decompressInput.getStyleClass().add("inputBox");

        /* Decompress Output Label */
        Label decompressOutputLabel = new Label("Output");

        /* Decompress Output Path Component*/
        Label decompressOutputPathLabel = new Label("Path : ");
        TextField decompressOutputPathTextField = new TextField();
        Button decompressOutputSelectPathBtn = new Button("Select");

        /* Decompress Output Path Box */
        HBox decompressOutputPathBox = new HBox(10, decompressOutputPathLabel, decompressOutputPathTextField, decompressOutputSelectPathBtn );
        decompressOutputPathBox.getStyleClass().add("subBox");

        /* Decompress Output Box */
        HBox decompressOutput = new HBox(10, decompressOutputLabel,decompressOutputPathBox);
        decompressOutput.getStyleClass().add("outputBox");

        /* Decompress Box Component */
        Button decompressBtn = new Button("Decompress");

        /* Decompress Box */
        VBox decompressBox = new VBox(10, decompressInput, decompressOutput, decompressBtn);
        decompressBox.getStyleClass().add("main");

        //TODO: change box nesting for interactive display
        /* Decompression Tab */
        Tab decompressTab = new Tab("Decompress", decompressBox);
        decompressTab.setClosable(false);

        /* Encrypt Input Box */
        Label encryptInputLabel = new Label("Input");
        Label encryptInputFileLabel = new Label("File : ");
        TextField encryptInputFileTextField = new TextField();
        Button encryptSelectFileBtn = new Button("Select");
        Label encryptInputPasswordLabel = new Label("Password : ");
        PasswordField encryptInputPasswordField = new PasswordField();
        HBox encryptInputPassword = new HBox(10, encryptInputPasswordLabel, encryptInputPasswordField);
        HBox encryptInputFile = new HBox(10, encryptInputFileLabel, encryptInputFileTextField, encryptSelectFileBtn);
        VBox encryptInputBox = new VBox(10, encryptInputFile, encryptInputPassword);
        HBox encryptInput = new HBox(10, encryptInputLabel, encryptInputBox);
        encryptInput.getStyleClass().add("inputBox");

        /* Encrypt Output Box */
        Label encryptOutputLabel = new Label("Output");
        Label encryptOutputPathLabel = new Label("Path : ");
        TextField encryptOutputPathTextField = new TextField();
        Button encryptSelectPathBtn = new Button("Select");
        Label encryptOutputFileLabel = new Label("File Name : ");
        TextField encryptOutputFileTextField = new TextField();
        Button encryptBtn = new Button("Encrypt");
        HBox encryptOutputFile = new HBox(10, encryptOutputFileLabel, encryptOutputFileTextField);
        HBox encryptOutputPathBox = new HBox(10, encryptOutputPathLabel, encryptOutputPathTextField, encryptSelectPathBtn);
        VBox encryptOutputBox = new VBox(10, encryptOutputPathBox, encryptOutputFile);
        HBox encryptOutput = new HBox(10, encryptOutputLabel, encryptOutputBox);
        encryptOutput.getStyleClass().add("outputBox");

        /* Encrypt Box */
        VBox encryptBox = new VBox(10, encryptInput, encryptOutput, encryptBtn);
        encryptBox.getStyleClass().add("main");
        //TODO: change box nesting for interactive display
        /* Encrypt Tab */
        Tab encryptTab = new Tab("Encrypt", encryptBox);
        encryptTab.setClosable(false);

        /* Decrypt Input Box */
        Label decryptInputLabel = new Label("Input");
        Label decryptInputFileLabel = new Label("File : ");
        TextField decryptInputFileTextField = new TextField();
        Button decryptSelectFileBtn = new Button("Select");
        Label decryptInputPasswordLabel = new Label("Password : ");
        PasswordField decryptInputPasswordField = new PasswordField();
        HBox decryptInputPassword = new HBox(10, decryptInputPasswordLabel, decryptInputPasswordField);
        HBox decryptInputFile = new HBox(10, decryptInputFileLabel, decryptInputFileTextField, decryptSelectFileBtn);
        VBox decryptInputBox = new VBox(10, decryptInputFile, decryptInputPassword);
        HBox decryptInput = new HBox(10, decryptInputLabel, decryptInputBox);
        decryptInput.getStyleClass().add("inputBox");

        /* Decrypt Output Box */
        Label decryptOutputLabel = new Label("Output");
        Label decryptOutputPathLabel = new Label("Path : ");
        TextField decryptOutputPathTextField = new TextField();
        Button decryptSelectPathBtn = new Button("Select");
        Label decryptOutputFileLabel = new Label("File Name : ");
        TextField decryptOutputFileTextField = new TextField();
        Button decryptBtn = new Button("Decrypt");
        HBox decryptOutputFile = new HBox(10, decryptOutputFileLabel, decryptOutputFileTextField);
        HBox decryptOutputPathBox = new HBox(10, decryptOutputPathLabel, decryptOutputPathTextField, decryptSelectPathBtn);
        VBox decryptOutputBox = new VBox(10, decryptOutputPathBox, decryptOutputFile);
        HBox decryptOutput = new HBox(10, decryptOutputLabel, decryptOutputBox);
        decryptOutput.getStyleClass().add("outputBox");

        /* Decrypt Box */
        VBox decryptBox = new VBox(10, decryptInput, decryptOutput, decryptBtn);
        decryptBox.getStyleClass().add("main");
        //TODO: change box nesting for interactive display
        /* Decrypt Tab */
        Tab decryptTab = new Tab("Decrypt", decryptBox);
        decryptTab.setClosable(false);

        /* Tab Pane */
        TabPane tabPane = new TabPane(compressTab, decompressTab, encryptTab, decryptTab);

        /* Root Box */
        VBox root = new VBox(titleBox, tabPane);

        /* The Main Scene */
        Scene scene = new Scene(root);
        scene.getStylesheets().add("Style.css");

        /* The Main WIndow */
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
        addBtn.setOnAction(event -> compressInputList = new MyController().addToList(compressInputList));
        removeBtn.setOnAction(event -> compressInputList = new MyController().removeFromList(compressInputList));
        clearBtn.setOnAction(event -> compressInputList = new MyController().clearList(compressInputList));

        /* Compress Button Action*/
        compressBtn.setDefaultButton(true);

        compressBtn.setOnAction(event -> {
            listFile.addAll(compressInputList.getItems());
            String selectedFormat = ".zip"; //TODO: Get the user selected value here
            new MyController().doCompress(listFile, selectedFormat , compressOutputPathTextField.getText(), compressOutputFileTextField.getText());
        });

        /* Decompress Button Action*/
        decompressBtn.setOnAction(event -> new MyController().doDecompress(decompressInputFileTextField.getText(), decompressOutputPathTextField.getText()));

        /* Encrypt Button Action*/
        encryptBtn.setOnAction(event -> new MyController().doEncrypt(encryptInputFileTextField.getText(), encryptInputPasswordField.getText(), encryptOutputPathTextField.getText(), encryptOutputFileTextField.getText()));

        /* Decrypt Button Action*/
        decryptBtn.setOnAction(event -> new MyController().doEncrypt(decryptInputFileTextField.getText(),decryptInputPasswordField.getText(), decryptOutputPathTextField.getText(), decryptOutputFileTextField.getText()));
    }

    public static void main(String[] args) {
        launch(args);
    }

}