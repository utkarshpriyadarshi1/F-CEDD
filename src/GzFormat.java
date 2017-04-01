import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class GzFormat {
    public  void gzip(String input, String output, String name) throws Exception {
        String compress_name = null;
        if (name != null) {
            compress_name = name;
        } else {
            compress_name = new File(input).getName();
        }
        byte[] buffer = new byte[1024];
        try {
            GzipParameters gp = new GzipParameters();  // Set file name in compressed file
            gp.setFilename(compress_name);
            GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(new FileOutputStream(output), gp);
            FileInputStream fis = new FileInputStream(input);
            int length;
            while ((length = fis.read(buffer)) > 0) {
                gcos.write(buffer, 0, length);
            }
            fis.close();
            gcos.finish();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
