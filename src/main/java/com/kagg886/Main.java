package com.kagg886;

import com.alibaba.fastjson2.JSONObject;
import com.kagg886.exportexam.Library;
import com.kagg886.exportexam.Question;
import com.kagg886.exportexam.User;
import com.kagg886.utils.ImageUtil;
import com.kagg886.utils.WebDriver;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        User u = new User();

        u.login("U101441", "2203050528", "123123qweqwe");

        Library l = u.getLibraries().get(0);

        System.out.println(l);

        List<Question> questions = l.getAllQuestions();

        for (int i = 0; i < questions.size(); i++) {
            System.out.printf("第%d个，共%d个,题型:%s\n", i + 1, questions.size(),questions.get(i).getQuestiontypename());
            Question a = questions.get(i);


            WebDriver driver = WebDriver.getInstance();
            List<BufferedImage> i2 = new ArrayList<>() {{
                add(driver.captchaPic(a.decode(a.getSubjecthtml_svg())));
            }};

            switch (a.getQuestiontypename()) {
                case "单选题" -> {
                    for (JSONObject option : a.getOptions().stream().map((b) -> ((JSONObject) b)).toList()) {
//                        guid "bd0b496d633c42d7e963ef8513936f52"
//                        istrue "1"
//                        questionoptionhtml_svg
                        String h = a.decode(option.getString("questionoptionhtml_svg"));
                        if (option.getString("istrue").equals("1")) {
                            h = "<div style=\"background: red\">" + h + "</div>";
                        }
                        i2.add(driver.captchaPic(h));
                    }
                }
                default -> {
                    i2.add(driver.captchaPic(a.decode(a.getAnswerhtml_svg())));
                }
            }

            File f = new File(new File("output"), a.getId() + ".png");
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
            ImageIO.write(Objects.requireNonNull(ImageUtil.verticalImageGen(i2)), "PNG", f);
        }
        WebDriver.getInstance().destroy();
    }
}