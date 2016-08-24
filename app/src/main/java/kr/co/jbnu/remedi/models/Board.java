package kr.co.jbnu.remedi.models;

import java.io.Serializable;
import java.sql.Date;

public class Board implements Serializable{
    private String img_url;
    private String content;
    private Answer answer;
    private Date updated_at;
    private int id;
    private int user_id;

    //더미 보드
    public Board(String img_url,String content,Answer answer){
        this.img_url = img_url;
        this.content = content;
        this.answer = answer;
    }

    public Board(String img_url, String content, Answer answer, Date updated_at, int id, int user_id) {
        this.img_url = img_url;
        this.content = content;
        this.answer = answer;
        this.updated_at = updated_at;
        this.id = id;
        this.user_id = user_id;
    }

    public String getImg_url() {
        return "http://kossi.iptime.org:2000/viate/getimg?imgname="+img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Answer getAnswer() {
        return answer;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
