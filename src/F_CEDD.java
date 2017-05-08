import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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

    private Stage window;
    private Logger logger = Logger.getLogger("MyLog");
    private ObservableList<File> items;
    private ObservableList<File> selectedFiles;
    private final String[] compressFormat = {".tar", ".tar.bz2", ".tar.gz", ".zip"};
    private static ListView<File> compressInputList;
    private final ArrayList<File> listFile = new ArrayList<>();
    private String alertMsg;
    private String alertTitle;
    private Boolean alertSuccess = false;

    private ListView<File> addToList(List<File> lost1, ListView<File> listView) {
        items = listView.getItems();
        items.addAll(lost1);
        listView.setItems(items);
        return listView;
    }

    private ListView<File> addToList(ListView<File> listView, Boolean isFile) {
        items = listView.getItems();
        if (isFile) {
            List<File> tempFile = new FileChooser().showOpenMultipleDialog(window);
            if (tempFile != null)
                items.addAll(FXCollections.observableArrayList(tempFile));
        } else {
            File tempDir = new DirectoryChooser().showDialog(window);
            if (tempDir != null)
                items.add(tempDir);
        }

        listView.setItems(items);
        return listView;
    }

    private ListView<File> removeFromList(ListView<File> listView) {
        items = listView.getItems();
        selectedFiles = listView.getSelectionModel().getSelectedItems();
        if (!selectedFiles.isEmpty())
            items.removeAll(FXCollections.observableArrayList(selectedFiles));
        listView.setItems(items);
        return listView;
    }

    private ListView<File> clearList(ListView<File> listView) {
        listView.setItems(null);
        return listView;
    }

    @Override
    public void start(Stage primaryStage) {

        window = primaryStage;

        Label title = new Label("F-CEDD: File Manipulation Tool");
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
        compressInputList.placeholderProperty().setValue(new Label("Drag and drop items here"));
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
        compressOutputSelectPathBtn.getStyleClass().add("viewBtn");

        HBox compressOutputPath = new HBox(10, compressOutputPathLabel, compressOutputPathTextField, compressOutputSelectPathBtn);
        compressOutputPath.getStyleClass().add("subBox");

        Label compressOutputFileLabel = new Label("File Name : ");
        TextField compressOutputFileTextField = new TextField();

        ChoiceBox<String> compressOutputFileFormat = new ChoiceBox<>(FXCollections.observableArrayList(compressFormat));
        compressOutputFileFormat.getSelectionModel().select(1);
        compressOutputFileFormat.getStyleClass().add("choiceBox");

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

        Label decompressInputFileLabel = new Label("File : ");
        TextField decompressInputFileTextField = new TextField();
        Button decompressSelectFileBtn = new Button("Select File");
        decompressSelectFileBtn.getStyleClass().add("viewBtn");

        HBox decompressInputFile = new HBox(10, decompressInputFileLabel, decompressInputFileTextField, decompressSelectFileBtn);
        decompressInputFile.getStyleClass().add("subBox");

        VBox decompressInput = new VBox(10, decompressInputLabel, decompressInputFile);
        decompressInput.getStyleClass().add("inputBox");

        Label decompressOutputLabel = new Label("Output");

        Label decompressOutputPathLabel = new Label("Path : ");
        TextField decompressOutputPathTextField = new TextField();
        Button decompressOutputSelectPathBtn = new Button("Select Path");
        decompressOutputSelectPathBtn.getStyleClass().add("viewBtn");

        HBox decompressOutputPathBox = new HBox(10, decompressOutputPathLabel, decompressOutputPathTextField, decompressOutputSelectPathBtn);
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
        encryptSelectFileBtn.getStyleClass().add("viewBtn");

        HBox encryptInputFile = new HBox(10, encryptInputFileLabel, encryptInputFileTextField, encryptSelectFileBtn);
        encryptInputFile.getStyleClass().add("subBox");

        Label encryptInputPasswordLabel = new Label("Password : ");
        PasswordField encryptInputPasswordField = new PasswordField();
        Region space4 = new Region();
        space4.setMinWidth(130);
        HBox encryptInputPassword = new HBox(10, encryptInputPasswordLabel, encryptInputPasswordField, space4);
        encryptInputPassword.getStyleClass().add("subBox");

        VBox encryptInput = new VBox(10, encryptInputLabel, encryptInputFile, encryptInputPassword);
        encryptInput.getStyleClass().add("inputBox");

        Label encryptOutputLabel = new Label("Output");

        Label encryptOutputPathLabel = new Label("Path : ");
        TextField encryptOutputPathTextField = new TextField();
        Button encryptSelectPathBtn = new Button("Select Path");
        encryptSelectPathBtn.getStyleClass().add("viewBtn");

        HBox encryptOutputPathBox = new HBox(10, encryptOutputPathLabel, encryptOutputPathTextField, encryptSelectPathBtn);
        encryptOutputPathBox.getStyleClass().add("subBox");

        Button encryptBtn = new Button("Encrypt");
        encryptBtn.getStyleClass().add("mainBtn");

        VBox encryptBtnBox = new VBox(encryptBtn);
        encryptBtnBox.setAlignment(Pos.CENTER_RIGHT);

        VBox encryptOutput = new VBox(10, encryptOutputLabel, encryptOutputPathBox);
        encryptOutput.getStyleClass().add("outputBox");

        VBox encryptBox = new VBox(10, encryptInput, encryptOutput, encryptBtnBox);
        encryptBox.getStyleClass().add("main");

        Tab encryptTab = new Tab("Encrypt", encryptBox);
        encryptTab.setClosable(false);

        Label decryptInputLabel = new Label("Input");

        Label decryptInputFileLabel = new Label("File : ");
        TextField decryptInputFileTextField = new TextField();
        Button decryptSelectFileBtn = new Button("Select File");
        decryptSelectFileBtn.getStyleClass().add("viewBtn");
        HBox decryptInputFile = new HBox(10, decryptInputFileLabel, decryptInputFileTextField, decryptSelectFileBtn);
        decryptInputFile.getStyleClass().add("subBox");

        Label decryptInputPasswordLabel = new Label("Password : ");
        PasswordField decryptInputPasswordField = new PasswordField();

        Region space2 = new Region();
        space2.setMinWidth(130);

        HBox decryptInputPassword = new HBox(10, decryptInputPasswordLabel, decryptInputPasswordField, space2);
        decryptInputPassword.getStyleClass().add("subBox");

        VBox decryptInput = new VBox(10, decryptInputLabel, decryptInputFile, decryptInputPassword);
        decryptInput.getStyleClass().add("inputBox");

        Label decryptOutputLabel = new Label("Output");

        Label decryptOutputPathLabel = new Label("Path : ");
        TextField decryptOutputPathTextField = new TextField();
        Button decryptSelectPathBtn = new Button("Select Path");
        decryptSelectPathBtn.getStyleClass().add("viewBtn");
        HBox decryptOutputPathBox = new HBox(10, decryptOutputPathLabel, decryptOutputPathTextField, decryptSelectPathBtn);
        decryptOutputPathBox.getStyleClass().add("subBox");

        Button decryptBtn = new Button("Decrypt");
        decryptBtn.getStyleClass().add("mainBtn");

        VBox decryptBtnBox = new VBox(decryptBtn);
        decryptBtnBox.setAlignment(Pos.CENTER_RIGHT);

        VBox decryptOutput = new VBox(10, decryptOutputLabel, decryptOutputPathBox);
        decryptOutput.getStyleClass().add("outputBox");

        VBox decryptBox = new VBox(10, decryptInput, decryptOutput, decryptBtnBox);
        decryptBox.getStyleClass().add("main");

        Tab decryptTab = new Tab("Decrypt", decryptBox);
        decryptTab.setClosable(false);

        TabPane tabPane = new TabPane(compressTab, decompressTab, encryptTab, decryptTab);

        VBox root = new VBox(titleBox, tabPane);

        Scene scene = new Scene(root, 720, 640);
        scene.getStylesheets().add("DayStyle.css");

        window.setTitle("F-CEDD");
        window.getIcons().add(new Image("icons/fcedd.png"));
        window.setScene(scene);
        window.show();

        /* Path Input Actions */
        compressOutputSelectPathBtn.setOnAction(event -> {
            File tempDir = new DirectoryChooser().showDialog(window);
            if (tempDir != null)
                compressOutputPathTextField.setText(tempDir.toString());
        });

        decompressOutputSelectPathBtn.setOnAction(event -> {
            File tempDir = new DirectoryChooser().showDialog(window);
            if (tempDir != null)
                decompressOutputPathTextField.setText(tempDir.toString());
        });

        encryptSelectPathBtn.setOnAction(event -> {
            File tempDir = new DirectoryChooser().showDialog(window);
            if (tempDir != null)
                encryptOutputPathTextField.setText(tempDir.toString());
        });

        decryptSelectPathBtn.setOnAction(event -> {
            File tempDir = new DirectoryChooser().showDialog(window);
            if (tempDir != null)
                decryptOutputPathTextField.setText(tempDir.toString());
        });

        /* File Input Actions */
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Compressed File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All", "*.zip", "*.tar", "*.tar.bz2", "*.tar.gz", "*.tgz"),
                new FileChooser.ExtensionFilter("ZIP", "*.zip"),
                new FileChooser.ExtensionFilter("TAR", "*.tar"),
                new FileChooser.ExtensionFilter("TAR.BZIP2", "*.tar.bz2"),
                new FileChooser.ExtensionFilter("TAR.GZIP", "*.tar.gz", "*.tgz")
        );

        decompressSelectFileBtn.setOnAction(event -> {
            File tempFile = fileChooser.showOpenDialog(window);
            if (tempFile != null)
                decompressInputFileTextField.setText(tempFile.toString());
        });

        encryptSelectFileBtn.setOnAction(event -> {
            File tempFile = new FileChooser().showOpenDialog(window);
            if (tempFile != null)
                encryptInputFileTextField.setText(tempFile.toString());
        });

        decryptSelectFileBtn.setOnAction(event -> {
            File tempFile = new FileChooser().showOpenDialog(window);
            if (tempFile != null)
                decryptInputFileTextField.setText(tempFile.toString());
        });

        /* List Button Actions */
        addFBtn.setOnAction(event -> {
            compressInputList = addToList(compressInputList, true);
        });

        addDBtn.setOnAction(event -> {
            compressInputList = addToList(compressInputList, false);
        });

        removeBtn.setOnAction(event -> {
            compressInputList = removeFromList(compressInputList);
        });

        clearBtn.setOnAction(event -> {
            compressInputList = clearList(compressInputList);
        });

        compressInputList.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            addToList(db.getFiles(), compressInputList);
            event.consume();
        });

        compressInputList.setOnDragOver(event -> {
            Dragboard dbd = event.getDragboard();
            event.acceptTransferModes(TransferMode.LINK);
            event.consume();
        });

        /* Compress Button Action */
        compressBtn.setOnAction(event -> {
            try {
                if (compressInputList.getItems().size() == 0) {
                    alertTitle = "File(s) Required";
                    alertMsg = "Input file(s) must be provided !!";
                    alertSuccess = false;
                } else if (compressOutputPathTextField.getText().isEmpty()) {
                    alertTitle = "Path Required";
                    alertMsg = "Output Directory path must be provided !!";
                    alertSuccess = false;
                } else if (compressOutputFileTextField.getText().isEmpty()) {
                    alertTitle = "File Name Required";
                    alertMsg = "Output File Name must be provided !!";
                    alertSuccess = false;
                }else if (!new File(compressOutputPathTextField.getText()).exists()) {
                    alertTitle = "Output Path Required";
                    alertMsg = "Output Directory path must be valid !!";
                    alertSuccess = false;
                } else {
                    new FileOperation().compress(compressInputList.getItems(), compressOutputFileFormat.getValue(), compressOutputPathTextField.getText(), compressOutputFileTextField.getText());
                    alertTitle = "Compression successful";
                    alertMsg = "File(s) compression is done successfully !!";
                    alertSuccess = true;
                }
            } catch (Exception e) {
                alertTitle = "Compression failed";
                alertMsg = "File(s) compression has failed !!";
                alertSuccess = false;
            } finally {
                new AlertBox(alertTitle, alertMsg, alertSuccess).display();
            }
        });

        /* Decompress Button Action */
        decompressBtn.setOnAction(event -> {
            try {
                if (decompressInputFileTextField.getText().isEmpty()) {
                    alertTitle = "File path Required";
                    alertMsg = "Input File path must be provided !!";
                    alertSuccess = false;
                } else if (decompressOutputPathTextField.getText().isEmpty()) {
                    alertTitle = "Path Required";
                    alertMsg = "Output Directory path must be provided !!";
                    alertSuccess = false;
                }else if (!new File(decompressInputFileTextField.getText()).exists()) {
                    alertTitle = "Input File Required";
                    alertMsg = "Input File path must be valid !!";
                    alertSuccess = false;
                }else if (!new File(decompressOutputPathTextField.getText()).exists()) {
                    alertTitle = "Output Path Required";
                    alertMsg = "Output Directory path must be valid !!";
                    alertSuccess = false;
                } else {
                    new FileOperation().decompress(decompressInputFileTextField.getText(), decompressOutputPathTextField.getText(), new ProgressStatus());
                    alertTitle = "Decompression successful";
                    alertMsg = "File(s) decompression is done successfully !!";
                    alertSuccess = true;
                }
            } catch (Exception e) {
                alertTitle = "Decompression failed";
                alertMsg = "File(s) decompression has failed !!";
                alertSuccess = false;
            } finally {
                new AlertBox(alertTitle, alertMsg, alertSuccess).display();
            }
        });

        /* Encrypt Button Action*/
        encryptBtn.setOnAction(event -> {
            try {
                if (encryptInputFileTextField.getText().isEmpty()) {
                    alertTitle = "File Name Required";
                    alertMsg = "Input File Name must be provided !!";
                    alertSuccess = false;
                } else if (encryptInputPasswordField.getText().isEmpty()) {
                    alertTitle = "Password Required";
                    alertMsg = "Password must be provided !!";
                    alertSuccess = false;
                } else if (encryptOutputPathTextField.getText().isEmpty()) {
                    alertTitle = "Path Required";
                    alertMsg = "Output Directory path must be provided !!";
                    alertSuccess = false;
                }else if (!new File(encryptInputFileTextField.getText()).exists()) {
                    alertTitle = "Input File path Required";
                    alertMsg = "Input File path must be valid !!";
                    alertSuccess = false;
                }else if (!new File(encryptOutputPathTextField.getText()).exists()) {
                    alertTitle = "Output Path Required";
                    alertMsg = "Output Directory path must be valid !!";
                    alertSuccess = false;
                } else {
                    new FileOperation().encrypt(encryptInputFileTextField.getText(), encryptInputPasswordField.getText(), encryptOutputPathTextField.getText());
                    alertTitle = "Encryption successful";
                    alertMsg = "File encryption is done successfully !!";
                    alertSuccess = true;
                }
            } catch (Exception e) {
                alertTitle = "Encryption failed";
                alertMsg = "File encryption has failed !!";
                alertSuccess = false;
            } finally {
                new AlertBox(alertTitle, alertMsg, alertSuccess).display();
            }
        });

        /* Decrypt Button Action*/
        decryptBtn.setOnAction(event -> {
            try {
                if (decryptInputFileTextField.getText().isEmpty()) {
                    alertTitle = "Input File path Required";
                    alertMsg = "Input File path must be provided !!";
                    alertSuccess = false;
                } else if (decryptInputPasswordField.getText().isEmpty()) {
                    alertTitle = "Password Required";
                    alertMsg = "Password must be provided !!";
                    alertSuccess = false;
                } else if (decryptOutputPathTextField.getText().isEmpty()) {
                    alertTitle = "Path Required";
                    alertMsg = "Output Directory path must be provided !!";
                    alertSuccess = false;
                } else if (!new File(decryptOutputPathTextField.getText()).exists()) {
                    alertTitle = "Path Required";
                    alertMsg = "Output Directory path must be valid !!";
                    alertSuccess = false;
                } else if (!new File(decryptInputFileTextField.getText()).exists()) {
                    alertTitle = "Input File path Required";
                    alertMsg = "Input File path must be valid !!";
                    alertSuccess = false;
                } else {
                    new FileOperation().decrypt(decryptInputFileTextField.getText(), decryptInputPasswordField.getText(), decryptOutputPathTextField.getText());
                    alertTitle = "Decryption successful";
                    alertMsg = "File decryption is done successfully !!";
                    alertSuccess = true;
                }
            } catch (Exception e) {
                alertTitle = "Decryption failed";
                alertMsg = "File decryption has failed !!";
                alertSuccess = false;
            } finally {
                new AlertBox(alertTitle, alertMsg, alertSuccess).display();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}