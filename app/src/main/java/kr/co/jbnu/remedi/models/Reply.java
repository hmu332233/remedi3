package kr.co.jbnu.remedi.models;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class Reply implements Serializable {
    private int id;
    private String content;
    private String userid;
    private String user_name;
    private int answer_id;
    private Date updated_at;

    public Reply(int id, String content, String userid, int answer_id, Date updated_at) {
        this.id = id;
        this.content = content;
        this.userid = userid;
        this.answer_id = answer_id;
        this.updated_at = updated_at;
    }

    public Reply( String content, String userId) {

        this.content = content;
        this.userid = userId;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userid;
    }

    public void setUserid(String userid) {
        userid = userid;
    }

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getUserid() {
        return userid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
