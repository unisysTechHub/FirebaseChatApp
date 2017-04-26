package com.uisys.firebasechatapp;

/**
 * Created by Admin on 3/31/17.
 */

public class BktappUserInfo
{
    String name;
    String emailId;
    String uid;
    BktappUserInfo()
    {}
    BktappUserInfo(String name, String emailId)
    {
        this.name=name;
        this.emailId=emailId;


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
