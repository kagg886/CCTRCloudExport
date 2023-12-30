package com.kagg886.utils;

import lombok.SneakyThrows;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class WebDriver {
    private static final WebDriver INSTANCE = new WebDriver();

    private final EdgeDriver driver;

    private WebDriver() {
        File f = new File("");
        System.out.println(f.getAbsolutePath());

        System.setProperty("webdriver.edge.verboseLogging", "true");

        driver = new EdgeDriver(EdgeDriverService.createDefaultService(), new EdgeOptions() {{
//            addArguments("headless");
            addArguments("lang=lang=zh_CN.UTF-8");
            setBinary(new File("msedge_beta_121.0.2277.4_driver_bin"));
        }});
    }

    public static WebDriver getInstance() {
        return INSTANCE;
    }

    @SneakyThrows
    public BufferedImage captchaPic(String html) {
        //创建tmp
        File tmp = new File(UUID.randomUUID() + ".html");
        if (!tmp.exists()) {
            tmp.createNewFile();
        }

        //写入tmp到html中
        FileOutputStream stream = new FileOutputStream(tmp);
        stream.write(html.getBytes(StandardCharsets.UTF_8));
        stream.close();

        //加载html
        driver.get("file://" + tmp.getAbsolutePath());
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(driver.getScreenshotAs(OutputType.BYTES)));

        //删除临时文件
        tmp.delete();
        //网页截图
        return image;
    }

    public void destroy() {
        driver.close();
    }
}
