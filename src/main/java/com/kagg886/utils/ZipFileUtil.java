package com.kagg886.utils;

import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFileUtil {
    @SneakyThrows
    public static void unzip(String filePath) {
        FileInputStream fileInputStream = new FileInputStream(filePath);
        CheckedInputStream checkedInputStream = new CheckedInputStream(fileInputStream, new Adler32());
        ZipInputStream zipInputStream = new ZipInputStream(checkedInputStream);
        ZipEntry zipEntry;

        FileOutputStream fileOutputStream;
        File savePath = new File(filePath.replace(".zip", ""));
        if (!savePath.exists()) {
            savePath.mkdir();
        }
        BufferedInputStream bufferedInputStream = new BufferedInputStream(zipInputStream);
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            File file = new File(savePath.getName() + "/" + zipEntry.getName());
            file.getParentFile().mkdirs();
            file.createNewFile();
            fileOutputStream = new FileOutputStream(savePath.getName() + "/" + zipEntry.getName());
            int x;
            byte[] bytes = new byte[1024];
            while ((x = bufferedInputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes);
            }
            fileOutputStream.close();
        }
        zipInputStream.close();
        fileInputStream.close();
    }
}
