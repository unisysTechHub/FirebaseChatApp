package com.uisys.firebasechatapp;

/**
 * Created by Admin on 3/23/17.
 */

public class ChatMessage {

    String name;
    String message;
    ChatMessage()
    {


    }
     ChatMessage(String name,String message)
     {
         this.name=name;
         this.message=message;


     }

    public String getMessage() {
        return message;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
