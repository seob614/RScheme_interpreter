package com.example.rscheme_interpreter.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

    public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.close();
    }

    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment)
            throws IOException {
        ZipOutputStream zipout = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(
                zipFile), BUFF_SIZE));
        for (File resFile : resFileList) {
            zipFile(resFile, zipout, "");
        }
        zipout.setComment(comment);
        zipout.close();
    }

    public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
            ZipEntry entry = ((ZipEntry)entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int realLength;
            while ((realLength = in.read(buffer)) > 0) {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
    }



    public static void upZipFile(InputStream is, String dir) throws IOException {
        File desDir = new File(dir);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        File dest = desDir;
        if ( !dest.isDirectory())
            throw new IOException("Invalid Unzip destination " + dest);

        if(null == is){
            throw new IOException("InputStream is null");
        }
        ZipInputStream zip = new ZipInputStream(is);

        ZipEntry ze;
        while ( (ze = zip.getNextEntry()) != null ) {
            final String path = dest.getAbsolutePath()
                    + File.separator + ze.getName();

            // Create any entry folders
            String zeName = ze.getName();
            char cTail = zeName.charAt(zeName.length() - 1);
            if ( cTail == File.separatorChar)
            {
                File file = new File(path);
                if(!file.exists()){
                    if ( !file.mkdirs() ){
                        throw new IOException("Unable to create folder " + file);
                    }
                }
                continue;
            }

            FileOutputStream fout = new FileOutputStream(path);
            byte[] bytes = new byte[1024];
            int c;
            while((c = zip.read(bytes)) != -1){
                fout.write(bytes,0, c);
            }

            zip.closeEntry();
            fout.close();
        }

    }

    public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath,
                                                    String nameContains) throws ZipException, IOException {
        ArrayList<File> fileList = new ArrayList<File>();

        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdir();
        }

        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
            ZipEntry entry = ((ZipEntry)entries.nextElement());
            if (entry.getName().contains(nameContains)) {
                InputStream in = zf.getInputStream(entry);
                String str = folderPath + File.separator + entry.getName();
                str = new String(str.getBytes("8859_1"), "GB2312");
                // str.getBytes("GB2312"),"8859_1" 输出
                // str.getBytes("8859_1"),"GB2312" 输入
                File desFile = new File(str);
                if (!desFile.exists()) {
                    File fileParentDir = desFile.getParentFile();
                    if (!fileParentDir.exists()) {
                        fileParentDir.mkdirs();
                    }
                    desFile.createNewFile();
                }
                OutputStream out = new FileOutputStream(desFile);
                byte buffer[] = new byte[BUFF_SIZE];
                int realLength;
                while ((realLength = in.read(buffer)) > 0) {
                    out.write(buffer, 0, realLength);
                }
                in.close();
                out.close();
                fileList.add(desFile);
            }
        }
        return fileList;
    }

    public static ArrayList<String> getEntriesNames(File zipFile) throws ZipException, IOException {
        ArrayList<String> entryNames = new ArrayList<String>();
        Enumeration<?> entries = getEntriesEnumeration(zipFile);
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry)entries.nextElement());
            entryNames.add(new String(getEntryName(entry).getBytes("GB2312"), "8859_1"));
        }
        return entryNames;
    }

    public static Enumeration<?> getEntriesEnumeration(File zipFile) throws ZipException,
            IOException {
        ZipFile zf = new ZipFile(zipFile);
        return zf.entries();

    }

    public static String getEntryComment(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getComment().getBytes("GB2312"), "8859_1");
    }

    public static String getEntryName(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getName().getBytes("GB2312"), "8859_1");
    }

    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath)
            throws FileNotFoundException, IOException {
        rootpath = rootpath + (rootpath.trim().length() == 0 ? "" : File.separator)
                + resFile.getName();
        rootpath = new String(rootpath.getBytes("8859_1"), "GB2312");
        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();
            for (File file : fileList) {
                zipFile(file, zipout, rootpath);
            }
        } else {
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(resFile),
                    BUFF_SIZE);
            zipout.putNextEntry(new ZipEntry(rootpath));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipout.write(buffer, 0, realLength);
            }
            in.close();
            zipout.flush();
            zipout.closeEntry();
        }
    }
}
