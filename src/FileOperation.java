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

    private static String algorithm = "AES";
    private static int BUFFER_SIZE = 4 * 1024;
    private byte[] keyValue;
    private CompressorInputStream compressorInputStream;
    private CompressorOutputStream compressorOutputStream;
    private static FileInputStream fileInputStream = null;
    private static FileOutputStream fileOutputStream = null;
    private static ArchiveOutputStream archiveOutputStream = null;
    private static ArchiveInputStream archiveInputStream = null;
    private static ArchiveEntry archiveEntry = null;
    private String formatTypeArray[] = {".zip", ".tar", ".tgz", ".tar.gz", ".7z"};

    //TODO: define the methods such that it handle both normal and advance mode operation for all format supported

    private boolean typeChecker(List<File> inputFilepath) {
        Boolean flag = true;
        for (File x : inputFilepath)
            for (String type : formatTypeArray) {
                if (!(x.toString().toLowerCase()).endsWith(type))
                    flag = false;
            }
        return flag;
    }

    private String typeChecker(String file) {
        for (String type : formatTypeArray)
            if ((file.toLowerCase()).endsWith(type))
                return type;
        return null;
    }

    /*
    private boolean fileAvailable(List<File> inputFilepath) {
        for (File x : inputFilepath)
            if (!x.exists())
                flag = false;
        return flag;
    }
    */

    /*
    public boolean compress(String[] inputFileList, String compressionFormat, String outputPath, String outputFile, int buffer){

        return compress();
    }
    */

    private CompressorInputStream decompressOnly(String compressFormat, FileInputStream inputFileStream) {

        try {
            switch (compressFormat) {
                case ".gz":
                    compressorInputStream = new GzipCompressorInputStream(inputFileStream);
                    break;
                case ".bz2":
                case ".bz":
                    compressorInputStream = new BZip2CompressorInputStream(inputFileStream);
                    break;
                case ".xz":
                    compressorInputStream = new XZCompressorInputStream(inputFileStream);
                    break;
                default:
                    System.out.println("Error occurred during decompressing stream");
            }
        } catch (Exception e) {
            //TODO: handle exception here
        }
        return compressorInputStream;
    }

    private CompressorOutputStream compressOnly(String compressFormat, FileOutputStream outputStream) {

        try {
            switch (compressFormat) {
                case ".gz":
                    compressorOutputStream = new GzipCompressorOutputStream(outputStream);
                    break;
                case ".bz2":
                case ".bz":
                    compressorOutputStream = new BZip2CompressorOutputStream(outputStream);
                    break;
                case ".xz":
                    compressorOutputStream = new XZCompressorOutputStream(outputStream);
                    break;
                default:
                    System.out.println("Error occurred during decompressing stream");
            }
        } catch (Exception e) {
            //TODO: handle exception here
        }
        return compressorOutputStream;
    }

    private boolean archiveOnly(String[] inputFileList, String compressFormat, String outputPath, String outputFile) {

        try {
            fileOutputStream = new FileOutputStream(outputPath + "\\" + outputFile + compressFormat);

            switch (compressFormat) {
                case ".zip":
                    archiveOutputStream = new ZipArchiveOutputStream(new BufferedOutputStream(fileOutputStream));
                    break;
                case ".tar":
                    archiveOutputStream = new TarArchiveOutputStream(new BufferedOutputStream(fileOutputStream));
                    break;
                default:
                    System.out.println("archiveEntry switch failed !!");
            }

                /*this.updateMessage("Compressing: " + f.getAbsolutePath());*/

            for (String tempFile : inputFileList) {
                fileInputStream = new FileInputStream(tempFile);
                tempFile = (new File(tempFile)).getName();

                archiveOutputStream.putArchiveEntry(archiveEntry);

                byte[] tmp = new byte[BUFFER_SIZE];
                int size = 0;

                while ((size = fileInputStream.read(tmp)) != -1) {
                    archiveOutputStream.write(tmp, 0, size);
                }

                archiveOutputStream.flush();
                fileInputStream.close();
            }
            archiveOutputStream.close();
        } catch (Exception e) {
            //TODO: handle exception here
        }
        return true;
    }

    private Boolean unarchiveOnly(String compressFormat, String inputFile) {

        try {
            switch (compressFormat) {
                case ".tar":
                    archiveInputStream = new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
                    break;
                case ".zip":
                    archiveInputStream = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(inputFile)));
                    break;
                default:
                    System.out.println("Archive Input Stream creation failed");
            }

            while ((archiveEntry = (TarArchiveEntry) archiveInputStream.getNextEntry()) != null) {

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
        } catch (Exception e) {
            //TODO: handle exception here
        }
        return true;
    }

    public boolean compress(String[] inputFileList, String compressionFormat, String outputPath, String outputFile) throws Exception {

        try {
            fileOutputStream = new FileOutputStream(outputPath + "\\" + outputFile + compressionFormat);
            switch (compressionFormat) {
                case ".zip":
                    archiveOutputStream = new ZipArchiveOutputStream(new BufferedOutputStream(fileOutputStream));
                    break;
                case ".tar.gz":
                case ".tgz":
                    break;
                case ".tar":
                    archiveOutputStream = new TarArchiveOutputStream(new BufferedOutputStream(fileOutputStream));
                    break;
                case ".7z":
                    break;
                default:
                    System.out.println("outputStream switch failed !!");
            }

            for (String tempFile : inputFileList) {
                fileInputStream = new FileInputStream(tempFile);


                tempFile = (new File(tempFile)).getName();


                switch (compressionFormat) {
                    case ".zip":
                        System.out.println("here 2 ");
                        archiveEntry = new ZipArchiveEntry(tempFile);
                        break;
                    case ".tar.gz":
                    case ".tgz":
                        break;
                    case ".tar":
                        archiveEntry = new TarArchiveEntry(tempFile);
                        break;
                    case ".7z":
/*
                        archiveEntry = new SevenZArchiveEntry().setName(tempFile);
                        archiveEntry.setName(tempFile);
                        //TODO: add name to SevenZ Archive*/
                        break;
                }
            }
        } catch (Exception e) {

        }
        return true;
    }

    public boolean decompress(String inputFile, String decompressionFormat, String outputPath, String
            outputFile) {

        try {
            switch (decompressionFormat) {
                case ".zip":
                    break;
                case ".tar.bz2":
                    break;
                case ".tar.gz":
                case ".tgz":
                    break;
                case ".tar.xz":
                    break;
                case ".tar":
                    break;
                case ".7z":
                        /*archiveEntry = new SevenZArchiveEntry().setName(tempFile);
                        archiveEntry.setName(tempFile);
                        */
                    //TODO: add name to SevenZ Archive
                    break;
                default:
                    System.out.println("archiveEntry switch failed !!");
            }

            while ((archiveEntry = archiveInputStream.getNextEntry()) != null) {
                byte[] tmp = new byte[4 * 1024];
                String outputFilePath = archiveEntry.getName();
                fileOutputStream = new FileOutputStream(outputPath + outputFilePath);

                int size = 0;
                while ((size = archiveInputStream.read(tmp)) != -1) {
                    fileOutputStream.write(tmp, 0, size);
                }

                fileOutputStream.flush();

                fileOutputStream.close();
                //this.updateMessage("Decompressing: " + outputFilePath.getRelativePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean encrypt(String inputFile, String password, String outputPath, String outputFile) throws
            Exception {
        //TODO: do encryption here
        //TODO: handle progress bar, and alert or notification, if required

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

        return true;
    }

    public boolean decrypt(String inputFile, String password, String outputPath, String outputFile) throws
            Exception {
        //TODO: do decryption here
        //TODO: handle progress bar, and alert or notification, if required

        // reading the salt
        // user should have secure mechanism to transfer the
        // salt, iv and password to the recipient
        FileInputStream saltFis = new FileInputStream("salt.enc");
        byte[] salt = new byte[8];
        saltFis.read(salt);
        saltFis.close();

        // reading the iv
        FileInputStream ivFis = new FileInputStream("iv.enc");
        byte[] iv = new byte[16];
        ivFis.read(iv);
        ivFis.close();

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, 65536,
                256);
        SecretKey tmp = factory.generateSecret(keySpec);
        SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        FileInputStream fis = new FileInputStream("encryptedfile.des");
        FileOutputStream fos = new FileOutputStream("plainfile_decrypted.txt");
        byte[] in = new byte[64];
        int read;
        while ((read = fis.read(in)) != -1) {
            byte[] output = cipher.update(in, 0, read);
            if (output != null)
                fos.write(output);
        }

        byte[] output = cipher.doFinal();
        if (output != null)
            fos.write(output);
        fis.close();
        fos.flush();
        fos.close();
        System.out.println("File Decrypted.");
        return true;
    }

}