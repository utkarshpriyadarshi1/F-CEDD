import java.io.File;

public class LocalFile {
    private File typeFile;
    private String typeString;

    LocalFile(File file, String localPathString){
        typeFile = file;
        typeString = localPathString;
    }

    public File getFile(){
        return typeFile;
    }

    public String getDirectory(){
        return typeString;
    }

    public String getName(){
        return typeFile.getName();
    }
}