package com.mystic.skoolchat20;

import com.google.firebase.auth.FirebaseUser;

public class Chat {
    private String senderId;
    private String receiverId ;
    private String chatId;


    public Chat(){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private User user;

    public String getChatId() {
        return chatId;
    }

    public Chat(String senderId, String receiverId, String chatId,User user) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.user = user;
        this.chatId = chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
