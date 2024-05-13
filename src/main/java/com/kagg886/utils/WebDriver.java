package com.kagg886.utils;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
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
    private final EdgeDriver driver;

    public WebDriver(File file,boolean headless) {
        System.setProperty("webdriver.edge.verboseLogging", "true");

        driver = new EdgeDriver(EdgeDriverService.createDefaultService(), new EdgeOptions() {{
            if (headless) {
                addArguments("headless");
            }
            addArguments("lang=lang=zh_CN.UTF-8");
            setBinary(file);
        }});
    }

    @SneakyThrows
    public BufferedImage captchaPic(String html) {
        //创建tmp
        File tmp = new File(UUID.randomUUID() + ".html");
        if (!tmp.exists()) {
            tmp.createNewFile();
        }

        try {
            //注入id
            Document doc = Jsoup.parse("<div>" + html + "</div>");
            doc.body().child(0).attr("id", "captcha");
            html = doc.html();

            //写入tmp到html中
            FileOutputStream stream = new FileOutputStream(tmp);
            stream.write(html.getBytes(StandardCharsets.UTF_8));
            stream.close();

            //加载html
            driver.get("file://" + tmp.getAbsolutePath());

            //获取矩形，准备裁剪
            WebElement rectangle = driver.findElement(new By.ById("captcha"));
            return ImageIO.read(new ByteArrayInputStream(rectangle.getScreenshotAs(OutputType.BYTES)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tmp.delete();
        }
        return null;
    }

    public void destroy() {
        driver.quit();
    }
}
