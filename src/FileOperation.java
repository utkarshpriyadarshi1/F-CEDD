import javafx.collections.ObservableList;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
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
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.List;

public class FileOperation /*extends Task<List<File>>*/ {

    private static String algorithm;
    private static int BUFFER_SIZE;
    private byte[] keyValue;
    private String formatTypeArray[];
    private Boolean success;
    private static String[] inputFileList;
    private static List<String> stringList;
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private BufferedInputStream bufferedInputStream;
    private BufferedOutputStream bufferedOutputStream;
    private ArchiveInputStream archiveInputStream;
    private ArchiveOutputStream archiveOutputStream;
    private ArchiveEntry archiveEntry;
    private CompressorInputStream compressorInputStream;
    private CompressorOutputStream compressorOutputStream;

    //TODO: define the methods such that it handle both normal and advance mode operation for all format supported

    private void getAllFiles(File[] fileArray, String parentDirectory) {
        for (File tempFile : fileArray) {
            if (tempFile.isDirectory()) {
                getAllFiles(tempFile.listFiles(), parentDirectory);
            } else {
                // stringList.add(tempFile.relative);
            }
        }
    }

    private void getAndsetFileArray(ObservableList<File> listFile) {

        //setRelativePath(listFile);
        for (File tempFile : listFile) {

            if (tempFile.isDirectory()) {
                File[] list = tempFile.listFiles();
                System.out.println("original");
                listFile.addAll(list);
                System.out.println("updated");

            } else {
                System.out.println(tempFile.getName());
            }
        }
    }

    FileOperation() {
        this.algorithm = "AES";
        this.BUFFER_SIZE = 4 * 1024;
//        this.formatTypeArray[] =new String[".zip", ".tar", ".tgz", ".tar.gz", ".7z"]();
    }

    private void unpack(String inputPath, String inputFile, String extension, String outputPath, int buffer) {
        this.BUFFER_SIZE = buffer;
        unpack(inputPath, inputFile, extension, outputPath);
    }

    private void unpack(String inputPath, String inputFile, String extension, String outputPath) {
        try {

            fileInputStream = new FileInputStream(inputPath + inputFile + extension);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            fileOutputStream = new FileOutputStream(outputPath + inputFile);    // + ".tar"

            if (extension.equals(".gz") || extension.equals(".bz2") || extension.equals(".xz")) {
                if (extension.equals(".gz"))
                    compressorInputStream = new GzipCompressorInputStream(bufferedInputStream);
                else if (extension.equals(".bz"))
                    compressorInputStream = new BZip2CompressorInputStream(bufferedInputStream);
                else
                    compressorInputStream = new XZCompressorInputStream(bufferedInputStream);

                final byte[] buffer = new byte[BUFFER_SIZE];
                int n = 0;
                while (-1 != (n = compressorInputStream.read(buffer))) {
                    fileOutputStream.write(buffer, 0, n);
                }
                fileOutputStream.close();
                compressorInputStream.close();
            } else if (extension.equals(".tar") || extension.equals(".zip")) {
                if (extension.equals(".tar"))
                    archiveInputStream = new TarArchiveInputStream(bufferedInputStream);
                else
                    archiveInputStream = new ZipArchiveInputStream(bufferedInputStream);

                while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {    //Make sure archive inputStream is required

                    /* If the entry is a directory, create the directory */
                    if (archiveEntry.isDirectory()) {
                        File f = new File(archiveEntry.getName());
                        boolean created = f.mkdir();
                        if (!created) {
                            System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n", f.getAbsolutePath());
                        }
                    } else {
                        int count;
                        byte data[] = new byte[BUFFER_SIZE];
                        fileOutputStream = new FileOutputStream(archiveEntry.getName(), false);
                        try (BufferedOutputStream dest = new BufferedOutputStream(fileOutputStream, BUFFER_SIZE)) {
                            while ((count = archiveInputStream.read(data, 0, BUFFER_SIZE)) != -1) {
                                dest.write(data, 0, count);
                            }
                            dest.close();
                        }
                    }
                }
                archiveInputStream.close();
            } else {
//                if (extension == ".7z") {

                SevenZFile sevenZFile = new SevenZFile(new File(inputPath + inputFile + extension));
                SevenZArchiveEntry sevenZArchiveEntry;
                while ((sevenZArchiveEntry = sevenZFile.getNextEntry()) != null) {
                    if (sevenZArchiveEntry.isDirectory()) {
                        File f = new File(sevenZArchiveEntry.getName());
                        boolean created = f.mkdir();
                        if (!created) {
                            System.out.printf("Unable to create directory '%s', during extraction of archive contents.\n", f.getAbsolutePath());
                        } else {

                        }
                        byte[] content = new byte[Math.toIntExact(sevenZArchiveEntry.getSize())];
                        //LOOP UNTILL entry.getSize() HAS BEEN READ {
                        //   sevenZFile.read(content, offset, content.length - offset);
                        //}
                    }
                }
            }
        } catch (Exception e) {
            //TODO: handle exception here
        }

    }

    private void pack(String inputFile, String extension, String outputPath, String outputFile) {
        try {
            fileInputStream = new FileInputStream(inputFile);
            fileOutputStream = new FileOutputStream(outputPath + outputFile + extension);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            switch (extension) {
                case ".gz":
                    compressorOutputStream = new GzipCompressorOutputStream(bufferedOutputStream);
                    break;
                case ".bz2":
                    compressorOutputStream = new BZip2CompressorOutputStream(bufferedOutputStream);
                    break;
                case ".xz":
                    compressorOutputStream = new XZCompressorOutputStream(bufferedOutputStream);
                    break;
                default:
                    break;
            }

            final byte[] buffer = new byte[BUFFER_SIZE];
            int n = 0;
            while (-1 != (n = fileInputStream.read(buffer))) {
                compressorOutputStream.write(buffer, 0, n);
            }
            compressorOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            //TODO: handle exception here
        }
    }

    private void pack(String[] inputFileList, String extension, String outputPath, String outputFile) {

        try {
            fileOutputStream = new FileOutputStream(outputPath + "\\" + outputFile + extension);
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            switch (extension) {
                case ".zip":
                    //ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(bufferedOutputStream);

                    archiveOutputStream = new ZipArchiveOutputStream(bufferedOutputStream);
                    int count = 0;
                    int total = inputFileList.length;
                    for (String tempFile : inputFileList) {

                        fileInputStream = new FileInputStream(tempFile);
                        tempFile = (new File(tempFile)).getName();
                        ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(tempFile);
                        System.out.println("Bug FIX ");
                        archiveOutputStream.putArchiveEntry(zipArchiveEntry);
                        System.out.println(" Archive Entry success");
                        byte[] tmp = new byte[1024];
                        System.out.println("byte[] tmp = new byte[BUFFER_SIZE];");
                        int size = 0;
                        while ((size = fileInputStream.read(tmp)) != -1) {
                            archiveOutputStream.write(tmp, 0, size);
                            System.out.println("archiveOutputStream.write(tmp, 0, size);");
                        }
                        archiveOutputStream.flush();
                        System.out.println(" archiveOutputStream.flush();");
                        fileInputStream.close();
                        System.out.println("fileInputStream.close();");
                        count++;

                    }
                    archiveOutputStream.closeArchiveEntry();
                    System.out.println("archiveOutputStream.close();");
                    archiveOutputStream.close();
                    System.out.println("archiveOutputStream.close();");
                    break;

                case ".tar":
                    TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(bufferedOutputStream);
                    for (String tempFile : inputFileList) {
                        fileInputStream = new FileInputStream(tempFile);
                        tempFile = (new File(tempFile)).getName();      //Can be simplified
                        System.out.println(tempFile);

                        TarArchiveEntry tarArchiveEntry = new TarArchiveEntry(tempFile);
                        System.out.println("Bug FIX ");

                        tarArchiveOutputStream.putArchiveEntry(tarArchiveEntry);
                        System.out.println(" Archive Entry success");

                        byte[] tmp = new byte[1024];
                        System.out.println("byte[] tmp = new byte[BUFFER_SIZE];");
                        int size = 0;
                        //Read from fileInputStream and write to tarArchiveOutputStream
                        while ((size = fileInputStream.read(tmp)) != -1) {
                            tarArchiveOutputStream.write(tmp, 0, size);
                            System.out.println("tarArchiveOutputStream.write(tmp, 0, size);");
                        }

                        tarArchiveOutputStream.flush();
                        System.out.println(" tarArchiveOutputStream.flush();");
                        fileInputStream.close();
                        System.out.println("fileInputStream.close();");
                    }
                    tarArchiveOutputStream.close();

                    break;
                case ".7z":/* SevenZOutputFile archiveOutput = new SevenZOutputFile(file);
                SevenZArchiveEntry archiveEntry = archiveOutput.createArchiveEntry(fileToArchive, name);
                archiveOutput.putArchiveEntry(archiveEntry);
                archiveOutput.write(contentOfEntry);
                archiveOutput.closeArchiveEntry();*/
                    break;

                default:
            }
                /*this.updateMessage("Compressing: " + f.getAbsolutePath());*/
        } catch (
                Exception e)

        {
            //TODO: handle exception here
        }

    }

    public void compress(ObservableList<File> listFile, String extension, String outputPath, String outputFileName) {
        //listFile.addAll(compressInputList.getItems());
        String[] inputFileList = new String[listFile.size()];
        int i = 0;
        for (File x : listFile) {
            inputFileList[i] = x.toString();
            i++;
        }
        //getAndsetFileArray(listFile);

        switch (extension) {
            case ".7z":
                break;
            case ".zip":
            case ".tar":
                pack(inputFileList, extension, outputPath, outputFileName);
                break;
            case ".tar.bz2":
            case ".tar.gz":
            case ".tar.xz":
                System.out.println(extension);
                pack(inputFileList, extension, outputPath, outputFileName);
                extension = extension.substring(4);
                System.out.println(extension);
                pack(outputFileName, extension, outputPath, outputFileName);
                break;
            default:
        }
    }


    public void encrypt(String inputFile, String password, String outputPath, String outputFile) {
        try {

            //TODO: do encryption here
            FileInputStream inFile = new FileInputStream(inputFile);
            FileOutputStream outFile = new FileOutputStream(outputPath + "\\" + outputFile);

            // password to encrypt the file
            // password, iv and salt should be transferred to the other end
            // in a secure manner

            // salt is used for encoding
            // writing it to a file
            // salt should be transferred to the recipient securely
            // for decryption
            byte[] salt = new byte[8];
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.nextBytes(salt);
            FileOutputStream saltOutFile = new FileOutputStream("salt.enc");
            saltOutFile.write(salt);
            saltOutFile.close();

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKey secretKey = factory.generateSecret(keySpec);
            SecretKey secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            AlgorithmParameters params = cipher.getParameters();

            // iv adds randomness to the text and just makes the mechanism more
            // secure
            // used while initializing the cipher
            // file to store the iv
            FileOutputStream ivOutFile = new FileOutputStream("iv.enc");
            byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
            ivOutFile.write(iv);
            ivOutFile.close();

            //file encryption
            byte[] input = new byte[64];
            int bytesRead;

            while ((bytesRead = inFile.read(input)) != -1) {
                byte[] output = cipher.update(input, 0, bytesRead);
                if (output != null)
                    outFile.write(output);
            }

            byte[] output = cipher.doFinal();
            if (output != null)
                outFile.write(output);

            inFile.close();
            outFile.flush();
            outFile.close();

            System.out.println("File Encrypted.");

//            return true;
        } catch (Exception e) {

        }
    }


    //public void decrypt(String inputFile, String password, String outputPath, String outputFile) {
    //TODO: do decryption here
    //TODO: handle progress bar, and alert or notification, if required

    // reading the salt
    // user should have secure mechanism to transfer the
    // salt, iv and password to the recipient
//        FileInputStream saltFis = new FileInputStream("salt.enc");
//        byte[] salt = new byte[8];
//        saltFis.read(salt);
//        saltFis.close();
//
//        // reading the iv
//        FileInputStream ivFis = new FileInputStream("iv.enc");
//        byte[] iv = new byte[16];
//        ivFis.read(iv);
//        ivFis.close();
//
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
//                256);
//        SecretKey tmp = factory.generateSecret(keySpec);
//        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
//        FileInputStream fis = new FileInputStream("encryptedfile.des");
//        FileOutputStream fos = new FileOutputStream("plainfile_decrypted.txt");
//        byte[] in = new byte[64];
//        int read;
//        while ((read = fis.read(in)) != -1) {
//            byte[] output = cipher.update(in, 0, read);
//            if (output != null)
//                fos.write(output);
//        }
//
//        byte[] output = cipher.doFinal();
//        if (output != null)
//            fos.write(output);
//        fis.close();
//        fos.flush();
//        fos.close();
//        System.out.println("File Decrypted.");
//
//    }
}