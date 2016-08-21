package kr.co.jbnu.remedi.models;

import java.util.ArrayList;

/**
 * Created by seokhyeon on 2016-08-15.
 */


public class User {


    private int id;
    private String email;
    private String name;
    private String user_type;
    private String profile_imgname;
    private static User user;
    private ArrayList<Board> userBoardList;

    public static String PHARM = "pharm";
    public static String NORMAL = "normal";

    public User(int id, String email, String name, String user_type, String profile_imgname,ArrayList<Board> userBoardList) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.user_type = user_type;
        this.profile_imgname = profile_imgname;
        this.userBoardList = userBoardList;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Board> getUserBoardList() {
        return userBoardList;
    }

    public void setUserBoardList(ArrayList<Board> userBoardList) {
        this.userBoardList = userBoardList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getProfile_imgname() {
        return profile_imgname;
    }

    public void setProfile_imgname(String profile_imgname) {
        this.profile_imgname = profile_imgname;
    }

    public static synchronized User getInstance () {
        if (user==null){
            System.out.println("유저의 데이터가 넘어오지 않았음으로 종료해야합니다.");
        }
        return user;
    }

    public static void setUser(User given_user){
        user = given_user;
    }
}
