package kr.co.jbnu.remedi.models;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class Answer {
    private String medi_name;
    private String answer_content;
    private String medi_element;
    private String medi_company;
    private String medi_serialnumber;
    private String medi_category;
    private String medi_effect;
    private String pharm_id;
    private String pharm_imgname;
    private ArrayList<Reply> repliesList;
    private Date date;
    private int Answer_serialnumber;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAnswer_serialnumber() {
        return Answer_serialnumber;
    }

    public void setAnswer_serialnumber(int answer_serialnumber) {
        Answer_serialnumber = answer_serialnumber;
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

    public ArrayList<Reply> getRepliesList() {
        return repliesList;
    }

    public void setRepliesList(ArrayList<Reply> repliesList) {
        this.repliesList = repliesList;
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
}
