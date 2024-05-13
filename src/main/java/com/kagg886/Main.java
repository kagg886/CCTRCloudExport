package com.kagg886;

import com.alibaba.fastjson2.JSONObject;
import com.kagg886.exportexam.Library;
import com.kagg886.exportexam.Question;
import com.kagg886.exportexam.User;
import com.kagg886.utils.ImageUtil;
import com.kagg886.utils.MsEdgeUtils;
import com.kagg886.utils.WebDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        User u = new User();
        //账号2205010316
        //密码148218
        //材料力学性能
        //金属热处理
        u.login("U101441", "2205010316", "148218");
        System.out.println("登录成功，准备初始化Web Driver");

        //下载链接：https://msedgedriver.azureedge.net/124.0.2478.97/edgedriver_linux64.zip
//        MsEdgeUtils.download(MsEdgeUtils.getFullDownloadUrl(), (d) -> {
//            System.out.printf("下载中...%2f%n",d);
//        });

        String targetFileName = MsEdgeUtils.getPlatform() == MsEdgeUtils.Platform.LINUX ? "" : ".exe";

        WebDriver driver = new WebDriver(new File("msedgedriver/msedgedriver" + targetFileName), false);
        for (Library l : u.getLibraries()) {

            if (!(l.getPracticename().contains("金属热处理"))) {
                continue;
            }

            System.out.println("正在导出:" + l.getPracticename());
            List<Question> questions = l.getAllQuestions();
            for (int i = 0; i < questions.size(); i++) {
                System.out.printf("第%d个，共%d个,题型:%s\n", i + 1, questions.size(), questions.get(i).getQuestiontypename());
                Question a = questions.get(i);


                List<BufferedImage> i2 = new ArrayList<>() {{
                    add(driver.captchaPic(a.decode(a.getSubjecthtml_svg())));
                }};

                if (a.getOptions() != null) {
                    for (JSONObject option : a.getOptions().stream().map((b) -> ((JSONObject) b)).toList()) {
                        String h = a.decode(option.getString("questionoptionhtml_svg"));
                        if (option.getString("istrue").equals("1")) {
                            h = "<div style=\"background: red\">" + h + "</div>";
                        }
                        i2.add(driver.captchaPic(h));
                    }
                } else {
                    i2.add(driver.captchaPic(a.decode(a.getAnswerhtml_svg().isEmpty() ? a.getAnswer() : a.getAnswerhtml_svg())));
                }
                File f = Paths.get(new File("").getAbsolutePath(), "output", l.getPracticename(), a.getQuestiontypename(), a.getId() + ".png").toFile();
                if (!f.exists()) {
                    f.getParentFile().mkdirs();
                    f.createNewFile();
                }
                ImageIO.write(Objects.requireNonNull(ImageUtil.verticalImageGen(i2)), "PNG", f);
            }
        }


        driver.destroy();
    }
}