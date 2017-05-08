import javafx.collections.ObservableList;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;

class FileOperation {

    private static int BUFFER_SIZE;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private ArchiveEntry archiveEntry;
    private ArchiveOutputStream archiveOutputStream;
    private ArchiveInputStream archiveInputStream;
    private CompressorInputStream compressorInputStream;
    private CompressorOutputStream compressorOutputStream;
    private ArrayList<LocalFile> globalFileList = new ArrayList<LocalFile>();
    private static int i;
    private ProgressStatus progressStatus;
    private int countTask = 1;

    FileOperation() {
        BUFFER_SIZE = 4 * 1024;
    }

    private void getAllFiles(File[] fileArray, String getDirectory) {
        for (File tempFile : fileArray) {
            if (tempFile.isDirectory()) {
                getAllFiles(tempFile.listFiles(), getDirectory + tempFile.getName() + File.separator);
            } else {
                globalFileList.add(i, new LocalFile(tempFile, getDirectory + tempFile.getName()));
                i++;
            }
        }
    }

    private void getRelativeFileName(ObservableList<File> listFile) {
        File[] newArray = new File[listFile.size()];
        i = 0;
        for (File tempFile : listFile) {
            newArray[i] = tempFile;
            i++;
        }
        getAllFiles(newArray, "");
    }

    private String getExtension(String inputFile) {
        String extension;
        if (inputFile.endsWith(".tar"))
            extension = ".tar";
        else if (inputFile.endsWith(".tar.bz2"))
            extension = ".tar.bz2";
        else if (inputFile.endsWith(".tgz") || inputFile.endsWith("tar.gz"))
            extension = ".tar.gz";
        else
            extension = ".zip";
        return extension;
    }

    private void onlyDecompress(String inputPath, String inputFile, String extension, String outputPath, ProgressStatus obj) {
        try {
            switch (extension) {
                case ".bz2":
                    fileOutputStream = new FileOutputStream(outputPath + File.separator + inputFile.replace(extension, ""));
                    compressorInputStream = new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(inputPath + inputFile)));
                    final byte[] buffer1 = new byte[BUFFER_SIZE];
                    i = 0;
                    while ((i = compressorInputStream.read(buffer1)) != -1) {
                        fileOutputStream.write(buffer1, 0, i);
                    }
                    break;

                case ".gz":
                    fileOutputStream = new FileOutputStream(outputPath + File.separator + inputFile.replace(extension, ""));
                    compressorInputStream = new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(inputPath + inputFile)));
                    final byte[] buffer = new byte[BUFFER_SIZE];
                    i = 0;
                    while ((i = compressorInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, i);
                    }
                    break;

                case ".tar":
                    int task2 = 0;

                    fileInputStream = new FileInputStream(inputPath + inputFile);
                    archiveInputStream = new TarArchiveInputStream(new BufferedInputStream(fileInputStream));
                    while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
                        obj.display(archiveEntry.toString(), task2++);

                        byte[] tmp = new byte[4 * 1024];
                        String outputFile = archiveEntry.getName();
                        fileOutputStream = new FileOutputStream(outputPath + File.separator + outputFile);
                        i = 0;
                        while ((i = archiveInputStream.read(tmp)) != -1) {
                            fileOutputStream.write(tmp, 0, i);
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    break;

                case ".zip":

                    fileInputStream = new FileInputStream(inputPath + inputFile);
                    archiveInputStream = new ZipArchiveInputStream(new BufferedInputStream(fileInputStream));

                    while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
                        byte[] tmp = new byte[4 * 1024];
                        String outputFile = archiveEntry.getName();

                        if (archiveEntry.isDirectory()) {
                            if (new File(outputPath + File.separator + outputFile).mkdirs()) {
                                System.out.println("created");
                            } else {
                                System.out.println(" not created");
                            }
                        } else {
                            fileOutputStream = new FileOutputStream(outputPath + File.separator + outputFile);
                            i = 0;
                            while ((i = archiveInputStream.read(tmp)) != -1) {
                                fileOutputStream.write(tmp, 0, i);
                            }
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
                compressorInputStream.close();
                archiveInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onlyCompress(String inputFile, String extension) {
        try {
            fileInputStream = new FileInputStream(inputFile);
            fileOutputStream = new FileOutputStream(inputFile + extension);

            switch (extension) {
                case ".bz2":
                    compressorOutputStream = new BZip2CompressorOutputStream(new BufferedOutputStream(fileOutputStream));
                    break;
                case ".gz":
                    compressorOutputStream = new GzipCompressorOutputStream(new BufferedOutputStream(fileOutputStream));
                    break;
                default:
            }

            final byte[] buffer = new byte[BUFFER_SIZE];
            i = 0;
            while ((i = fileInputStream.read(buffer)) != -1) {
                compressorOutputStream.write(buffer, 0, i);
            }
            compressorOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            new File(inputFile).delete();
        }
    }

    private void onlyArchive(String extension, String outputPath, String outputFile) {
        try {
            switch (extension) {
                case ".zip":
                    fileOutputStream = new FileOutputStream(outputPath + File.separator + outputFile + extension);
                    archiveOutputStream = new ZipArchiveOutputStream(new BufferedOutputStream(fileOutputStream));

                    for (LocalFile tempFile : globalFileList) {
                        fileInputStream = new FileInputStream(tempFile.getFile());
                        archiveEntry = new ZipArchiveEntry(tempFile.getFile(), tempFile.getDirectory());
                        archiveOutputStream.putArchiveEntry(archiveEntry);
                        byte[] tmp = new byte[1024];
                        i = 0;
                        while ((i = fileInputStream.read(tmp)) != -1) {
                            archiveOutputStream.write(tmp, 0, i);
                        }
                        archiveOutputStream.flush();
                        fileInputStream.close();
                        archiveOutputStream.closeArchiveEntry();
                    }
                    break;

                case ".tar":
                    fileOutputStream = new FileOutputStream(outputPath + File.separator + outputFile + extension);
                    archiveOutputStream = new TarArchiveOutputStream(new BufferedOutputStream(fileOutputStream));

                    for (LocalFile tempFile : globalFileList) {
                        fileInputStream = new FileInputStream(tempFile.getFile());
                        archiveEntry = new TarArchiveEntry(tempFile.getFile(), tempFile.getDirectory());
                        archiveOutputStream.putArchiveEntry(archiveEntry);
                        byte[] tmp = new byte[4096];
                        i = 0;
                        while ((i = fileInputStream.read(tmp)) != -1) {
                            archiveOutputStream.write(tmp, 0, i);
                        }
                        archiveOutputStream.flush();
                        fileInputStream.close();
                        archiveOutputStream.closeArchiveEntry();
                    }
                    break;

                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                archiveOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void compress(ObservableList<File> listFile, String extension, String outputPath, String outputFileName) {
        getRelativeFileName(listFile);
        switch (extension) {
            case ".zip":
            case ".tar":
                onlyArchive(extension, outputPath, outputFileName);
                break;
            case ".tar.bz2":
            case ".tar.gz":
                onlyArchive(extension.substring(0, 4), outputPath, outputFileName);
                onlyCompress(outputPath + File.separator + outputFileName + extension.substring(0, 4), extension.substring(4));
                break;
            default:
        }
    }

    void decompress(String inputFile, String outputPath, ProgressStatus obj) {
        String extension = getExtension(inputFile);
        String inputPath = new File(inputFile).getAbsolutePath();
        inputFile = new File(inputFile).getName();
        inputPath = inputPath.replace(inputFile, "");

        switch (extension) {
            case ".tar":
            case ".zip":
                onlyDecompress(inputPath, inputFile, extension, outputPath, obj);
                break;
            case ".tar.gz":
            case ".tar.bz2":
                onlyDecompress(inputPath, inputFile, extension.substring(4), outputPath, obj);
                onlyDecompress(outputPath + File.separator, inputFile.substring(0, inputFile.lastIndexOf(".")), extension.substring(0, 4), outputPath, obj);
                new File(outputPath + File.separator + inputFile.substring(0, inputFile.lastIndexOf("."))).delete();
                break;
            default:
        }
    }

    void encrypt(String inputFile, String password, String outputPath) {
        try {
            fileInputStream = new FileInputStream(inputFile);
            String outputFile = new File(inputFile).getName();
            fileOutputStream = new FileOutputStream(outputPath + File.separator + outputFile + ".aes");

            byte[] key = password.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKey secret = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);

            byte[] input = new byte[64];

            i = 0;
            while ((i = fileInputStream.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, i);
                if (output != null)
                    fileOutputStream.write(output);
            }

            byte[] output = cipher.doFinal();

            if (output != null)
                fileOutputStream.write(output);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void decrypt(String inputFile, String password, String outputPath) {
        try {
            fileInputStream = new FileInputStream(inputFile);
            String outputFile = new File(inputFile).getName();
            outputFile = outputFile.substring(0, outputFile.lastIndexOf("."));
            fileOutputStream = new FileOutputStream(outputPath + File.separator + outputFile);

            byte[] key = password.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            SecretKey secret = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

            cipher.init(Cipher.DECRYPT_MODE, secret);

            byte[] in = new byte[64];
            i = 0;
            while ((i = fileInputStream.read(in)) != -1) {
                byte[] output = cipher.update(in, 0, i);
                if (output != null)
                    fileOutputStream.write(output);
            }

            byte[] output = cipher.doFinal();

            if (output != null)
                fileOutputStream.write(output);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}