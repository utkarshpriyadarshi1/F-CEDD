
import javafx.concurrent.Task;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFormat /*extends Task<List<File>>*/
{

    public boolean compress(List<File> inputList, int bufferSize, String compressionFormat, String outputPath, String outputFile) throws Exception {

        System.out.println("I'm here : 1 compress");
        FileInputStream fis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(outputPath + "\\"+outputFile+ compressionFormat);
            zos = new ZipOutputStream(new BufferedOutputStream(fos));
            for (File f : inputList) {
                fis = new FileInputStream(f);
                ZipEntry ze = new ZipEntry(f.getName());
                /*this.updateMessage("Compressing: " + f.getAbsolutePath());*/
                zos.putNextEntry(ze);
                byte[] tmp = new byte[4 * 1024];
                int size = 0;
                while ((size = fis.read(tmp)) != -1) {
                    zos.write(tmp, 0, size);
                }
                zos.flush();
                fis.close();
            }
            zos.close();
        } catch (IOException e) {
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception ex) {
            }
        }
        System.out.println("I'm here : 2 compress");
        return true;
    }

    public boolean decompress(String inputFile, String decompressionFormat, String outputPath, String outputFile) {
        System.out.println("I'm here : 1 decompress");

        ZipInputStream zipInputStream = null;
        ZipEntry zipEntry = null;
        try {
            //TODO: Create multiple file decompression handler in future version
            zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                byte[] tmp = new byte[4 * 1024];
                FileOutputStream fileOutputStream = null;
                String outputFilePath = zipEntry.getName();
                fileOutputStream = new FileOutputStream(outputPath+ outputFilePath);
                int size = 0;
                while ((size = zipInputStream.read(tmp)) != -1) {
                    fileOutputStream.write(tmp, 0, size);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                zipInputStream.close();
                //this.updateMessage("Decompressing: " + outputFilePath.getRelativePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("I'm here : 2 decompress");
        return true;
    }

    public static void zip(String input, String output, String name) throws Exception {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(output));
        String[] paths = input.split("\\|");
        File[] files = new File[paths.length];
        byte[] buffer = new byte[1024];
        for (int i = 0; i < paths.length; i++) {
            files[i] = new File(paths[i]);
        }
        for (int i = 0; i < files.length; i++) {
            FileInputStream fis = new FileInputStream(files[i]);
            if (files.length == 1 && name != null) {
                out.putNextEntry(new ZipEntry(name));
            } else {
                out.putNextEntry(new ZipEntry(files[i].getName()));
            }
            int len;
            //  Read the contents of the file to download ， pack into zip file
            while ((len = fis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            out.closeEntry();
            fis.close();
        }
        out.close();
    }

    public static void unzip(String[] args) {
        final String OUTPUT_FOLDER = "C:/output";
        String FILE_PATH = "C:/test/datas.zip";

        // Create Output folder if it does not exists
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Create buffer
        byte[] buffer = new byte[1024];

        ZipInputStream zipIs = null;
        try {

            // Create ZipInputStream read a file from path.
            zipIs = new ZipInputStream(new FileInputStream(FILE_PATH));

            ZipEntry entry = null;

            // Read ever Entry (From top to bottom until the end)
            while ((entry = zipIs.getNextEntry()) != null) {
                String entryName = entry.getName();
                String outFileName = OUTPUT_FOLDER + File.separator + entryName;
                System.out.println("Unzip: " + outFileName);

                if (entry.isDirectory()) {

                    // Make directories
                    new File(outFileName).mkdirs();
                } else {

                    // Create Stream to write file.
                    FileOutputStream fos = new FileOutputStream(outFileName);

                    int len;

                    // Read the data on the current entry.
                    while ((len = zipIs.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                zipIs.close();
            } catch (Exception e) {
            }
        }
    }

/*    @Override
    protected List<File> call() throws Exception {
        return null;
    }*/
}
