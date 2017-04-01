import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;

import java.io.*;

public class SevenZFormat {
    public void compress(String files[], String outputFileName) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        ArchiveOutputStream sevenZOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(outputFileName + ".tar");
            //TODO: Find a fix for the below code
            // sevenZOutputStream  = new ArchiveOutputStream(new BufferedOutputStream(fileOutputStream));
            for (String filePath : files) {
                File f = new File(filePath);
                fileInputStream = new FileInputStream(f);
                //    SevenZArchiveEntry ze = new SevenZArchiveEntry(f.getName());
                //    sevenZOutputStream .putArchiveEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fileInputStream.read(tmp)) != -1) {
                    sevenZOutputStream.write(tmp, 0, size);
                }
                sevenZOutputStream.flush();
                fileInputStream.close();
            }
            sevenZOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void z7z(String input, String output, String name) throws Exception {
        try {
            SevenZOutputFile sevenZOutput = new SevenZOutputFile(new File(output));
            SevenZArchiveEntry entry = null;

            String[] paths = input.split("\\|");
            File[] files = new File[paths.length];
            for (int i = 0; i < paths.length; i++) {
                files[i] = new File(paths[i].trim());
            }
            for (int i = 0; i < files.length; i++) {
                BufferedInputStream instream = null;
                instream = new BufferedInputStream(new FileInputStream(paths[i]));
                if (name != null) {
                    entry = sevenZOutput.createArchiveEntry(new File(paths[i]), name);
                } else {
                    entry = sevenZOutput.createArchiveEntry(new File(paths[i]), new File(paths[i]).getName());
                }
                sevenZOutput.putArchiveEntry(entry);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = instream.read(buffer)) > 0) {
                    sevenZOutput.write(buffer, 0, len);
                }
                instream.close();
                sevenZOutput.closeArchiveEntry();
            }
            sevenZOutput.close();
        } catch (IOException ioe) {
            System.out.println(ioe.toString() + " " + input);
        }
    }
}
