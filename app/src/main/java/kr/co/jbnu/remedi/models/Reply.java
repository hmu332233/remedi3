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
    private String profile_imgname;
    private Date updated_at;

    public Reply(int id, String content, String userid, int answer_id, Date updated_at,String user_name,String profile_imgname) {
        this.id = id;
        this.content = content;
        this.userid = userid;
        this.answer_id = answer_id;
        this.updated_at = updated_at;
        this.user_name = user_name;
        this.profile_imgname = profile_imgname;
    }

    public Reply( String content, String username,String profile_imgname) {

        this.content = content;
        this.user_name = username;
        this.profile_imgname = profile_imgname;

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

    public String getProfile_imgname() {
        return profile_imgname;
    }

    public void setProfile_imgname(String profile_imgname) {
        this.profile_imgname = profile_imgname;
    }

    public String getProfileUril(){
        return "http://kossi.iptime.org:2000/viate/getimg?imgname="+profile_imgname;
    }
}
