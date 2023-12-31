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

    private final WebDriver driver = WebDriver.getInstance();
    @Test
    public void testCaptcha() throws IOException {
        BufferedImage image = driver.captchaPic("<p>abcdefg</p>");
        File f = new File("test.png");
        if (!f.exists()) {
            f.createNewFile();
        }
        ImageIO.write(image,"PNG",f);
//        driver.destroy();
    }
}
