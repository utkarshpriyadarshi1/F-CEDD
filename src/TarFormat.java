import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import java.io.*;

public class TarFormat {
    public void compress(String files[], String outputFileName) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        TarArchiveOutputStream tarOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(outputFileName + ".tar");
            tarOutputStream = new TarArchiveOutputStream(new BufferedOutputStream(fileOutputStream));
            for (String filePath : files) {
                File f = new File(filePath);
                fileInputStream = new FileInputStream(f);
                TarArchiveEntry ze = new TarArchiveEntry(f.getName());
                tarOutputStream.putArchiveEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fileInputStream.read(tmp)) != -1) {
                    tarOutputStream.write(tmp, 0, size);
                }
                tarOutputStream.flush();
                fileInputStream.close();
            }
            tarOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void decompress(String filePath) {
        FileInputStream fileInputStream = null;
        TarArchiveInputStream tarInputStream = null;
        TarArchiveEntry tarEntry = null;
        try {
            fileInputStream = new FileInputStream(filePath);
            tarInputStream = new TarArchiveInputStream(new BufferedInputStream(fileInputStream));
            while ((tarEntry = (TarArchiveEntry) tarInputStream.getNextEntry()) != null) {
                try {
                    byte[] tmp = new byte[4 * 1024];
                    FileOutputStream fileOutputStream = null;
                    String opFilePath = tarEntry.getName();
                    fileOutputStream = new FileOutputStream(opFilePath);
                    int size = 0;
                    while ((size = tarInputStream.read(tmp)) != -1) {
                        fileOutputStream.write(tmp, 0, size);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception ex) {
                }
            }
            tarInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}