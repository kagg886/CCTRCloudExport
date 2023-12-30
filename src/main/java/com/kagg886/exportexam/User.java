package com.kagg886.exportexam;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.kagg886.utils.RSA;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
public class User {
    private JSONObject tokens;

    protected JSONObject getTokens() {
        return tokens;
    }

    @SneakyThrows
    public List<Library> getLibraries() {
        String body = Jsoup.newSession().url("https://api.cctrcloud.net/mobile/index.php?act=studentpracticeapi&op=getPractiseCourseList")
                .method(Connection.Method.POST)
                .data("key", tokens.getString("key"))
                .execute().body();
        //{
        //    "code": 200,
        //    "datas": [
        //        {
        //            "finishtime": "2024-01-06 09:00:00",
        //            "practiseid": "18848",
        //            "teacherid": "27023",
        //            "practicename": "马\u539f\u7ec3\u4e605",
        //            "teachername": "1603005",
        //            "questioncount": "114",
        //            "lessonid": "196016",
        //            "lessonname": "\u9a6c\u514b\u601d\u4e3b\u4e49\u57fa\u672c\u539f\u7406",
        //            "id": 1291217,
        //            "starttime": "2023-12-23 22:46:00",
        //            "courseid": "41658",
        //            "teachernumber": "\u738b\u4e3d\u5a1c"
        //        },
        //    ]
        //}
        List<Library> l = JSON.parseObject(body).getList("datas", Library.class);
        l.forEach((v) -> v.setBinder(this));
        return l;
    }

    @SneakyThrows
    public void login(String stu, String user, String pass) {
        //第一步：获取真正的学校代码
        //act: login
        //op: getschoolData
        //number: U101441

        String res = Jsoup.newSession()
                .url("https://api.cctrcloud.net/mobile/index.php?act=login&op=getschoolData")
                .method(Connection.Method.POST)
                .data("number", stu).execute().body();
        JSONObject obj = JSON.parseObject(res);
        //rtn: {"code":200,"datas":{"ID":"9887","Name":"\u6c88\u9633\u7406\u5de5\u5927\u5b66","Number":"U101441","State":"1","IsPrivate":"0","PrivateUri":""}}
        String id = obj.getJSONObject("datas").getString("ID");


        //username: 2203050528
        //password: TObMGhFQiNogEyaD4ixikSOLg7wdeKB7UN4G8RYTDKCRU+GXS3n7BBWfR5WL/35eusll9YWqOhgD0gW+y5/ONEZnV31h4hVrvRE3lnttXift8Gv/Ioj0IHb0G7q5Q+J7otsZCIJezqsBeewE+ioSKQr6QN7S7FUFTSnvR0m2eBQ=
        //schoolid: 9887
        //client: web

        String result = Jsoup.newSession()
                .url("https://api.cctrcloud.net/mobile/index.php?act=login")
                .method(Connection.Method.POST)
                .data("username", user)
                .data("schoolid", id)
                .data("client", "web")
                .data("password", RSA.encrypt(pass))
                .execute().body();


//        {"code":400,"datas":{"error":"\u5b66\u6821,用户\u540d\u6216\u5bc6\u7801\u9519\u8bef"}}
//        {"code":200,"datas":{"username":"2203050528","userid":"948830","usertruename":"","key":"","userclazzname":"22030505","userimg":"","membersex":"1","schoolid":"9887","admissionnumber":"2203050528","access_key":"","access_key_secret":""}}

        JSONObject err = JSONObject.parseObject(result);

        if (err.getInteger("code") != 200) {
            throw new IllegalStateException(err.getJSONObject("datas").getString("error"));
        }
        log.info("login: {}-{}, result: {}", stu, user, result);

        tokens = err.getJSONObject("datas");
    }
}
