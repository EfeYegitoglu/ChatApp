package com.efeyegitoglu.sohbet_uygulamasi;

public class MessageModel {

    String from,text,time,type;
    Boolean seen;

    public MessageModel(String from, Boolean seen, String text, String time, String type) {
        this.from = from;
        this.seen = seen;
        this.text = text;
        this.time = time;
        this.type = type;
    }

    public MessageModel(){}

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
