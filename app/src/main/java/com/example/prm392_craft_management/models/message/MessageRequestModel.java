package com.example.prm392_craft_management.models.message;

public class MessageRequestModel {
    private int reciever_id, sender_id;
    private String message;

    public MessageRequestModel(int sender_id, int reciever_id, String message) {
        this.reciever_id = reciever_id;
        this.sender_id = sender_id;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getReceiver_id() {
        return reciever_id;
    }

    public void setReceiver_id(int reciever_id) {
        this.reciever_id = reciever_id;
    }

    public int getSender_id() {
        return sender_id;
    }

    public void setSender_id(int sender_id) {
        this.sender_id = sender_id;
    }
}
