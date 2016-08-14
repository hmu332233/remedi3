package kr.co.jbnu.remedi.models;

import java.util.Date;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class Board {
    private String img_name;
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

    public Board(String img_name, String content, Answer answer) {
        this.img_name = img_name;
        this.content = content;
        this.answer = answer;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
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
