import com.kagg886.utils.WebDriver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.edge.EdgeOptions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class EdgeTest {

    private static EdgeDriver driver;

    @BeforeAll
    public static void be() {
        File f = new File("");
        System.out.println(f.getAbsolutePath());

        System.setProperty("webdriver.edge.verboseLogging", "true");

        driver = new EdgeDriver(EdgeDriverService.createDefaultService(), new EdgeOptions() {{
//            addArguments("headless");
            setBinary(new File("msedge_beta_121.0.2277.4_driver_bin"));
        }});
    }

    @Test
    public void testEdgeDriver() {
        driver.get("data:text/html,<html>...</html>");
        String title = driver.getTitle();
        System.out.println(title);
        driver.close();
    }
}
