import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
    private final String[] compressFormat = {".zip", ".tar", ".tar.gz", ".tgz", ".7z"};
    private static ListView<File> compressInputList;
    private final ArrayList<File> listFile = new ArrayList<>();

    private ListView<File> addToList(ListView<File> listView) {
        items = listView.getItems();
        listOfFiles = (new FileChooser()).showOpenMultipleDialog(window);
        //this.items = FXCollections.observableArrayList(listOfFiles);
        items.addAll(FXCollections.observableArrayList(listOfFiles));
        listView.setItems(items);
        return listView;
    }

    private ListView<File> removeFromList(ListView<File> listView) {
        System.out.println("I'm here : 1 removeFromList");
        items = listView.getItems();
        selectedFiles = listView.getSelectionModel().getSelectedItems();
        items.removeAll(FXCollections.observableArrayList(selectedFiles));
        //listOfFiles.remove(selectedFiles);
        //System.out.println("list:"+listOfFiles);
        //items = FXCollections.observableArrayList(listOfFiles);
        System.out.println("I'm here : 2 removeFromList");
        listView.setItems(items);
        return listView;
    }

    private ListView<File> clearList(ListView<File> listView) {
        System.out.println("I'm here : 1 clearList");
        //listOfFiles.clear();
        //items.clear();
        listView.setItems(null);
        System.out.println("I'm here : 2 clearList");
        return listView;
    }

    //TODO: create class for the standard mode view in the future version

    @Override
    public void start(Stage primaryStage) {
        Stage window;
        window = primaryStage;

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

        Label compressInputLabel = new Label("Input");

        /* ListView<File> */
        compressInputList = new ListView<>();
        compressInputList.getStyleClass().add("list");

        HBox compressInputListBox = new HBox(10, compressInputList, viewBtnBox);
        compressInputListBox.getStyleClass().add("subBox");

        VBox compressInput = new VBox(10, compressInputLabel, compressInputListBox);
        compressInput.getStyleClass().add("inputBox");

        /* Compress Output Box */
        Label compressOutputLabel = new Label("Output");

        Label compressOutputFileLabel = new Label("File Name : ");
        TextField compressOutputFileTextField = new TextField();

        compressOutputFileTextField.setPromptText("Enter your comment.");
        Label compressOutputPathLabel = new Label("Path : ");

        TextField compressOutputPathTextField = new TextField();
        Button compressOutputSelectPathBtn = new Button("Select");

        ChoiceBox<String> compressOutputFileFormat = new ChoiceBox<>(FXCollections.observableArrayList(compressFormat));
        compressOutputFileFormat.getSelectionModel().selectFirst();

        HBox compressOutputPath = new HBox(10, compressOutputPathLabel, compressOutputPathTextField, compressOutputSelectPathBtn);
        compressOutputPath.getStyleClass().add("subBox");

        HBox compressOutputFile = new HBox(10, compressOutputFileLabel, compressOutputFileTextField, compressOutputFileFormat);
        compressOutputFile.getStyleClass().add("subBox");

        VBox compressOutput = new VBox(10, compressOutputLabel, compressOutputPath, compressOutputFile);
        compressOutput.getStyleClass().add("outputBox");

        Button compressBtn = new Button("Compress");
        VBox compressBox = new VBox(10, compressInput, compressOutput, compressBtn);
        compressBox.getStyleClass().add("main");

        Tab compressTab = new Tab("Compress", compressBox);
        compressTab.setClosable(false);

        Label decompressInputLabel = new Label("Input");

        Label decompressInputFileLabel = new Label("File : ");
        TextField decompressInputFileTextField = new TextField();
        Button decompressSelectFileBtn = new Button("Select");

        HBox decompressInputFile = new HBox(10, decompressInputFileLabel, decompressInputFileTextField, decompressSelectFileBtn);
        decompressInputFile.getStyleClass().add("subBox");

        VBox decompressInput = new VBox(10, decompressInputLabel, decompressInputFile);
        decompressInput.getStyleClass().add("inputBox");

        Label decompressOutputLabel = new Label("Output");

        Label decompressOutputPathLabel = new Label("Path : ");
        TextField decompressOutputPathTextField = new TextField();
        Button decompressOutputSelectPathBtn = new Button("Select");

        HBox decompressOutputPathBox = new HBox(10, decompressOutputPathLabel, decompressOutputPathTextField, decompressOutputSelectPathBtn);
        decompressOutputPathBox.getStyleClass().add("subBox");

        VBox decompressOutput = new VBox(10, decompressOutputLabel, decompressOutputPathBox);
        decompressOutput.getStyleClass().add("outputBox");

        Button decompressBtn = new Button("Decompress");

        VBox decompressBox = new VBox(10, decompressInput, decompressOutput, decompressBtn);
        decompressBox.getStyleClass().add("main");

        Tab decompressTab = new Tab("Decompress", decompressBox);
        decompressTab.setClosable(false);


        Label encryptInputLabel = new Label("Input");

        Label encryptInputFileLabel = new Label("File : ");
        TextField encryptInputFileTextField = new TextField();
        Button encryptSelectFileBtn = new Button("Select");

        Label encryptInputPasswordLabel = new Label("Password : ");
        PasswordField encryptInputPasswordField = new PasswordField();

        HBox encryptInputPassword = new HBox(10, encryptInputPasswordLabel, encryptInputPasswordField);
        encryptInputPassword.getStyleClass().add("subBox");
        HBox encryptInputFile = new HBox(10, encryptInputFileLabel, encryptInputFileTextField, encryptSelectFileBtn);
        encryptInputFile.getStyleClass().add("subBox");

        VBox encryptInput = new VBox(10, encryptInputLabel, encryptInputFile, encryptInputPassword);
        encryptInput.getStyleClass().add("inputBox");

        Label encryptOutputLabel = new Label("Output");

        Label encryptOutputPathLabel = new Label("Path : ");
        TextField encryptOutputPathTextField = new TextField();
        Button encryptSelectPathBtn = new Button("Select");

        Label encryptOutputFileLabel = new Label("File Name : ");
        TextField encryptOutputFileTextField = new TextField();

        Button encryptBtn = new Button("Encrypt");

        HBox encryptOutputFile = new HBox(10, encryptOutputFileLabel, encryptOutputFileTextField);
        encryptOutputFile.getStyleClass().add("subBox");
        HBox encryptOutputPathBox = new HBox(10, encryptOutputPathLabel, encryptOutputPathTextField, encryptSelectPathBtn);
        encryptOutputPathBox.getStyleClass().add("subBox");

        VBox encryptOutput = new VBox(10, encryptOutputLabel, encryptOutputPathBox, encryptOutputFile);
        encryptOutput.getStyleClass().add("outputBox");

        VBox encryptBox = new VBox(10, encryptInput, encryptOutput, encryptBtn);
        encryptBox.getStyleClass().add("main");

        Tab encryptTab = new Tab("Encrypt", encryptBox);
        encryptTab.setClosable(false);


        Label decryptInputLabel = new Label("Input");

        Label decryptInputFileLabel = new Label("File : ");
        TextField decryptInputFileTextField = new TextField();
        Button decryptSelectFileBtn = new Button("Select");

        Label decryptInputPasswordLabel = new Label("Password : ");
        PasswordField decryptInputPasswordField = new PasswordField();

        HBox decryptInputPassword = new HBox(10, decryptInputPasswordLabel, decryptInputPasswordField);
        decryptInputPassword.getStyleClass().add("subBox");
        HBox decryptInputFile = new HBox(10, decryptInputFileLabel, decryptInputFileTextField, decryptSelectFileBtn);
        decryptInputFile.getStyleClass().add("subBox");

        VBox decryptInput = new VBox(10, decryptInputLabel, decryptInputFile, decryptInputPassword);
        decryptInput.getStyleClass().add("inputBox");

        Label decryptOutputLabel = new Label("Output");

        Label decryptOutputPathLabel = new Label("Path : ");
        TextField decryptOutputPathTextField = new TextField();
        Button decryptSelectPathBtn = new Button("Select");

        Label decryptOutputFileLabel = new Label("File Name : ");
        TextField decryptOutputFileTextField = new TextField();

        Button decryptBtn = new Button("Decrypt");

        HBox decryptOutputFile = new HBox(10, decryptOutputFileLabel, decryptOutputFileTextField);
        decryptOutputFile.getStyleClass().add("subBox");
        HBox decryptOutputPathBox = new HBox(10, decryptOutputPathLabel, decryptOutputPathTextField, decryptSelectPathBtn);
        decryptOutputPathBox.getStyleClass().add("subBox");

        VBox decryptOutput = new VBox(10, decryptOutputLabel, decryptOutputPathBox, decryptOutputFile);
        decryptOutput.getStyleClass().add("outputBox");

        VBox decryptBox = new VBox(10, decryptInput, decryptOutput, decryptBtn);
        decryptBox.getStyleClass().add("main");

        Tab decryptTab = new Tab("Decrypt", decryptBox);
        decryptTab.setClosable(false);

        TabPane tabPane = new TabPane(compressTab, decompressTab, encryptTab, decryptTab);

        VBox root = new VBox(titleBox, tabPane);

        Scene scene = new Scene(root, 600, 500);
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
        addBtn.setOnAction(event -> compressInputList = addToList(compressInputList));
        removeBtn.setOnAction(event -> compressInputList = removeFromList(compressInputList));
        clearBtn.setOnAction(event -> compressInputList = clearList(compressInputList));

        /* Compress Button Action*/
        compressBtn.setDefaultButton(true);

        compressBtn.setOnAction(event -> {
            listFile.addAll(compressInputList.getItems());
            String[] inputFileList = new String[listFile.size()];
            int i = 0;
            for (File x : listFile) {
                inputFileList[i] = x.toString();
                i++;
            }

            try {
                (new FileOperation()).compress(inputFileList, compressOutputFileFormat.getValue(), compressOutputPathTextField.getText(), compressOutputFileTextField.getText());
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
                if (new FileOperation().encrypt(decryptInputFileTextField.getText(), decryptInputPasswordField.getText(), decryptOutputPathTextField.getText(), decryptOutputFileTextField.getText()))
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