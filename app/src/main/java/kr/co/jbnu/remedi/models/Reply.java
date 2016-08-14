package kr.co.jbnu.remedi.models;

/**
 * Created by seokhyeon on 2016-08-15.
 */
public class Reply {
    private String content;
    private String UserId;
    private int reply_serialnumber;

    public Reply(String content, String userId) {
        this.content = content;
        UserId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getReply_serialnumber() {
        return reply_serialnumber;
    }

    public void setReply_serialnumber(int reply_serialnumber) {
        this.reply_serialnumber = reply_serialnumber;
    }
}
