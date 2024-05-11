package com.kagg886.utils;

import lombok.Getter;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Objects;
import java.util.function.Consumer;

public class MsEdgeUtils {
    @SneakyThrows
    private static String exec(String... cmd) {
        Process p = Runtime.getRuntime().exec(cmd);

        SequenceInputStream stream = new SequenceInputStream(p.getInputStream(), p.getErrorStream());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] byt = new byte[1024];
        int len;
        while ((len = stream.read(byt)) != -1) {
            outputStream.write(byt, 0, len);
        }
        stream.close();
        outputStream.close();
        p.destroy();
        return new String(outputStream.toByteArray());
    }

    public static Platform getPlatform() {
        String name = System.getProperty("os.name");

        if (name.equals("Linux")) {
            return Platform.LINUX;
        }
        return Platform.WINDOWS;
    }

    public static String getEdgeVersion(Platform platform) {
        return switch (platform) {
            case LINUX -> exec("microsoft-edge", "--version").split(" ")[2];
            case WINDOWS ->
                    exec("wmic", "datafile", "where", "'name=\"C:\\\\Program Files (x86)\\\\Microsoft\\\\Edge\\\\Application\\\\msedge.exe\"'", "get", "version").split("\n")[1];
        };
    }

    public static String getFullDownloadUrl() {
        Platform platform = getPlatform();
        String version = getEdgeVersion(platform);
        return "https://msedgedriver.azureedge.net/" + version + "/edgedriver_" + platform.name + ".zip";
    }

    @SneakyThrows
    public static void download(String url, Consumer<Double> downloadListener) {
        Connection.Response resp = Jsoup.connect(url).ignoreContentType(true).execute();
        int cLen = Integer.parseInt(Objects.requireNonNull(resp.header("Content-Length")));

        InputStream stream = resp.bodyStream();
        FileOutputStream outputStream = new FileOutputStream("msedgedriver.zip");
        byte[] byt = new byte[8192];
        double dLen = 0;
        int len;
        while ((len = stream.read(byt)) != -1) {
            outputStream.write(byt, 0, len);
            dLen += len;
            downloadListener.accept(dLen / cLen);
        }
        ZipFileUtil.unzip("msedgedriver.zip");
    }


    @Getter
    public enum Platform {
        WINDOWS("win64"), LINUX("linux64");

        private final String name;

        Platform(String name) {
            this.name = name;
        }
    }
}
