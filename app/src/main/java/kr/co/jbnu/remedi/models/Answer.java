package kr.co.jbnu.remedi.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class Answer implements Serializable {
    private int id;
    private String medi_name;
    private String answer_content;
    private String medi_element;
    private String medi_company;
    private String medi_serialnumber;
    private String medi_category;
    private String medi_effect;
    private String pharm_id;
    private String pharm_imgname;
    private String pharm_name;
    private int board_id;
    private ArrayList<Reply> replies;
    private Date updated_at;

    public Answer(int id, String medi_name, String answer_content, String medi_element, String medi_company, String medi_serialnumber, String medi_category, String medi_effect, String pharm_id, String pharm_imgname, int board_id, ArrayList<Reply> repliesList, Date updated_at) {
        this.id = id;
        this.medi_name = medi_name;
        this.answer_content = answer_content;
        this.medi_element = medi_element;
        this.medi_company = medi_company;
        this.medi_serialnumber = medi_serialnumber;
        this.medi_category = medi_category;
        this.medi_effect = medi_effect;
        this.pharm_id = pharm_id;
        this.pharm_imgname = pharm_imgname;
        this.board_id = board_id;
        this.replies = repliesList;
        this.updated_at = updated_at;
    }

    public Answer(String medi_name, String answer_content, String medi_element, String medi_company, String medi_serialnumber, String medi_category, String medi_effect, String pharm_id, String pharm_imgname) {
        this.medi_name = medi_name;
        this.answer_content = answer_content;
        this.medi_element = medi_element;
        this.medi_company = medi_company;
        this.medi_serialnumber = medi_serialnumber;
        this.medi_category = medi_category;
        this.medi_effect = medi_effect;
        this.pharm_id = pharm_id;
        this.pharm_imgname = pharm_imgname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMedi_name() {
        return medi_name;
    }

    public void setMedi_name(String medi_name) {
        this.medi_name = medi_name;
    }

    public String getAnswer_content() {
        return answer_content;
    }

    public void setAnswer_content(String answer_content) {
        this.answer_content = answer_content;
    }

    public String getMedi_element() {
        return medi_element;
    }

    public void setMedi_element(String medi_element) {
        this.medi_element = medi_element;
    }

    public String getMedi_company() {
        return medi_company;
    }

    public void setMedi_company(String medi_company) {
        this.medi_company = medi_company;
    }

    public String getMedi_serialnumber() {
        return medi_serialnumber;
    }

    public void setMedi_serialnumber(String medi_serialnumber) {
        this.medi_serialnumber = medi_serialnumber;
    }

    public String getMedi_category() {
        return medi_category;
    }

    public void setMedi_category(String medi_category) {
        this.medi_category = medi_category;
    }

    public String getMedi_effect() {
        return medi_effect;
    }

    public void setMedi_effect(String medi_effect) {
        this.medi_effect = medi_effect;
    }

    public String getPharm_id() {
        return pharm_id;
    }

    public void setPharm_id(String pharm_id) {
        this.pharm_id = pharm_id;
    }

    public String getPharm_imgname() {
        return pharm_imgname;
    }

    public void setPharm_imgname(String pharm_imgname) {
        this.pharm_imgname = pharm_imgname;
    }

    public int getBoard_id() {
        return board_id;
    }

    public void setBoard_id(int board_id) {
        this.board_id = board_id;
    }

    public ArrayList<Reply> getRepliesList() {
        return replies;
    }

    public void setRepliesList(ArrayList<Reply> repliesList) {
        this.replies = repliesList;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public String getAnswoer_content() {
        return answer_content;
    }

    public void setAnswoer_content(String answoer_content) {
        this.answer_content = answoer_content;
    }

    public String getPharm_name() {
        return pharm_name;
    }

    public void setPharm_name(String pharm_name) {
        this.pharm_name = pharm_name;
    }

    public ArrayList<Reply> getReplies() {
        return replies;
    }

    public void setReplies(ArrayList<Reply> replies) {
        this.replies = replies;
    }
}
