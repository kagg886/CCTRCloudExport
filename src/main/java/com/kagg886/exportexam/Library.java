package com.kagg886.exportexam;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Library {
    @JSONField(serialize = false,deserialize = false)
    private User binder;
    private LocalDateTime finishtime;
    private String practiseid;
    private String teacherid;
    private String practicename;
    private String teachername;
    private String questioncount;
    private String lessonid;
    private String lessonname;
    private String id;
    private LocalDateTime starttime;
    private String courseid;
    private String teachernumber;

    @SneakyThrows
    public List<Question> getAllQuestions() {
        //key: XwUh+gifttACgeKKtbrCTaQDm+7Zl0EC30THw5R6+8L1ed2mZCxkmvLouSVjNc5ixdS9Z3i5g6Ofd/ay8LZMl5hRPGD8hL2GeEIvZr0wKa+GscqYEJ4nK9igQgvtaV0zbiCAd6ZT++oAbFl3oNa+7fQVWmrdkCI3+nXNy87S+oo=
        //courseid: 50794
        //practiseid: 18237
        //studentpractiseid: 1077517
        //teacherid: 26155
        //pagecount: 15
        //pageindex: 1
        //practisetype: 0
        //statenum: 0
        //id:
        //list: true
        //studentpractisequestioncount: 53

        return new ArrayList<>() {{
            addAll(getQuestionsByType(0));
            addAll(getQuestionsByType(1));
        }};
    }

    @SneakyThrows
    public List<Question> getQuestionsByType(int i) {
        String body = Jsoup.newSession()
                .url("https://api.cctrcloud.net/mobile/index.php?act=studentpracticeapi&op=getStudentPractiseQuestionList")
                .method(Connection.Method.POST)
                .data("key",binder.getTokens().getString("key"))
                .data("courseid",courseid)
                .data("practiseid",practiseid)
                .data("studentpractiseid",id)
                .data("teacherid",teacherid)
                .data("pagecount","15000")
                .data("pageindex","1")
                .data("practisetype",String.valueOf(i))
                .data("statenum","0")
//                .data("id","")
                .data("list","true")
                .data("studentpractisequestioncount","53")
                .execute().body();
        return JSONObject.parse(body).getJSONObject("datas").getList("questionlist", Question.class);
    }
}
