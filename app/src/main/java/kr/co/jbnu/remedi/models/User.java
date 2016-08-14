package kr.co.jbnu.remedi.models;

import java.util.ArrayList;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class User {
    private String ID;
    private String Password;
    private String type;
    private String profile_imgname;
    private ArrayList<Board> userBoardList;

    public User(String ID, String password, String type, String profile_imgname, ArrayList<Board> userBoardList) {
        this.ID = ID;
        Password = password;
        this.type = type;
        this.profile_imgname = profile_imgname;
        this.userBoardList = userBoardList;
    }

    public ArrayList<Board> getUserBoardList() {
        return userBoardList;
    }

    public void setUserBoardList(ArrayList<Board> userBoardList) {
        this.userBoardList = userBoardList;
    }

    public User(String ID, String password, String type, String profile_imgname) {
        this.ID = ID;
        Password = password;
        this.type = type;
        this.profile_imgname = profile_imgname;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassWord() {
        return Password;
    }

    public void setPassWord(String passWord) {
        Password = passWord;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProfile_imgname() {
        return profile_imgname;
    }

    public void setProfile_imgname(String profile_imgname) {
        this.profile_imgname = profile_imgname;
    }
}
