package kr.co.jbnu.remedi.models;

import java.io.Serializable;
import java.sql.Date;

public class Board implements Serializable{
    private String img_url;
    private String content;
    private Answer answer;
    private Date date;
    private int board_serialnumber;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getBoard_serialnumber() {
        return board_serialnumber;
    }

    public void setBoard_serialnumber(int board_serialnumber) {
        this.board_serialnumber = board_serialnumber;
    }

    public Board(String img_url, String content, Answer answer) {
        this.img_url = img_url;
        this.content = content;
        this.answer = answer;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_name(String img_url) {
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
}
