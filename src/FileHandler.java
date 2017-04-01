
import java.io.File;
import java.util.List;

public class FileHandler {
    boolean flag = true;

    public boolean typeChecker(List<File> inputFilepath) {
        String formatTypeArray[] = {".zip", ".tar", ".xz", ".gz", ".7z"};
        for (File x : inputFilepath)
            for (String type : formatTypeArray) {
                if (!(x.toString().toLowerCase()).endsWith(type))
                    flag = false;
            }
        return flag;
    }

    public boolean fileAvailable(List<File> inputFilepath) {
        for (File x : inputFilepath)
            if (!x.exists())
                flag = false;
        return flag;
    }
}
