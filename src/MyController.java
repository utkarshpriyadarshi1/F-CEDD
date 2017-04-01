import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class MyController {

    private int buffer = 1024;
    // private Logger logger = Logger.getLogger("MyLog");
    private Stage window;
    private List<File> listOfFiles;
    private String ouputFilename = "PSPCrypt";
    private String outputDir = "/";
    private ObservableList<File> items;
    private ObservableList<File> selectedfiles;

    public  File selectFile(){
        return new FileChooser().showOpenDialog(window);
    }

    public ListView<File> addToList(ListView<File>  listView ) {
        items = listView.getItems();
        listOfFiles = (new FileChooser()).showOpenMultipleDialog(window);
        //this.items = FXCollections.observableArrayList(listOfFiles);
        items.addAll(FXCollections.observableArrayList(listOfFiles));
        listView.setItems(items);
        return listView;
    }

    public ListView<File> removeFromList(ListView<File> listView) {
        System.out.println("I'm here : 1 removeFromList");
        items = listView.getItems();
        selectedfiles = listView.getSelectionModel().getSelectedItems();
        items.removeAll(FXCollections.observableArrayList(selectedfiles));
        //listOfFiles.remove(selectedfiles);
        //System.out.println("list:"+listOfFiles);
        //items = FXCollections.observableArrayList(listOfFiles);
        System.out.println("I'm here : 2 removeFromList");
        listView.setItems(items);
        return listView;
    }

    public ListView<File> clearList(ListView<File> listView) {
        System.out.println("I'm here : 1 clearList");
        //listOfFiles.clear();
        //items.clear();
        listView.setItems(null);
        System.out.println("I'm here : 2 clearList");
        return listView;

    }

    public void doCompress(List<File> listOfFiles1, String format, String outputPath, String outputFile ) {
        //TODO: handle calling method for both normal and advance mode
        //TODO: check for the arguments valid or not, set if required, before calling
        //TODO: show alert, if required

        //String formatCmprs = (new CompressionChoiceBox()).display();
        //if (formatCmprs == "zip") {
            try {
                (new ZipFormat()).compress(listOfFiles1, buffer, format, outputPath, outputFile);
                (new AlertBox()).display("Compression successful", "Files have been successfullly compressed!!");
            } catch (Exception eb) {
                (new AlertBox()).display("Compression unsuccessful", "Files have NOT been compressed!!");
            }
        //}
        //TODO: handle other compression format here
        //else {
          //  (new AlertBox()).display("Compression unsuccessful", "Compression format not selected!!");
        //}
    }

    public void doDecompress(String inputFile, String outputPath) {
        //TODO: handle calling method for both normal and advance mode
        //TODO: check for the arguments valid or not, set if required, before calling
        //TODO: show alert, if required
    }

    public void doEncrypt(String inputFile, String password, String outputPath, String outputFile) {
        //TODO: handle calling method for both normal and advance mode
        //TODO: check for the arguments valid or not, set if required, before calling
        //TODO: show alert, if required
        try {
            if(new AESFormat().encrypt( inputFile, password, outputPath, outputFile));
                (new AlertBox()).display("Encryption successful", inputFile+" has been successfully encrypted!!");
        } catch (Exception eb) {
            (new AlertBox()).display("Encryption unsuccessful", inputFile+" has NOT been encrypted!!");
        }
    }

    public void doDecrypt(String inputFile, String password, String outputPath, String outputFile) {
        //TODO: handle calling method for both normal and advance mode
        //TODO: check for the arguments valid or not, set if required, before calling
        //TODO: show alert, if required
    }

}
