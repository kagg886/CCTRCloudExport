package com.kagg886;

import com.kagg886.exportexam.Library;
import com.kagg886.exportexam.Question;
import com.kagg886.exportexam.User;
import com.kagg886.utils.WebDriver;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws IOException {
        User u = new User();

        u.login("U101441", "2203050528", "123123qweqwe");

        Library l = u.getLibraries().stream().filter((v) -> v.getPracticename().contains("概率")).toList().getFirst();

        System.out.println(l);

        List<Question> questions = l.getAllQuestions();

        for (int i = 0; i < questions.size(); i++) {
            System.out.printf("第%d个，共%d个\n", i + 1, questions.size());
            Question a = questions.get(i);


            WebDriver driver = WebDriver.getInstance();
            BufferedImage i1 = driver.captchaPic(a.decode(a.getSubjecthtml_svg()));
            BufferedImage i2 = driver.captchaPic(a.decode(a.getAnswerhtml_svg()));

            BufferedImage i3 = new BufferedImage(i1.getWidth(), i1.getHeight() + i2.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = i3.createGraphics();
            g.drawImage(i1, 0, 0, i1.getWidth(), i1.getHeight(), null);
            g.drawImage(i2, 0, i1.getHeight(), i2.getWidth(), i2.getHeight(), null);
            g.dispose();

            File f = new File(new File("output"), a.getId() + ".png");
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            ImageIO.write(i3, "PNG", f);
        }
        WebDriver.getInstance().destroy();
    }
}