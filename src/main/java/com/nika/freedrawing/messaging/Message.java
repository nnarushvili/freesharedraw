package com.nika.freedrawing.messaging;

import java.io.Serializable;

public class Message implements Serializable{

    private String from;
    private int prevX;
    private int prevY;
    private int currX;
    private int currY;
    private String res;
    private String color;

    public int getPrevX() {
        return prevX;
    }

    public void setPrevX(int prevX) {
        this.prevX = prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setPrevY(int prevY) {
        this.prevY = prevY;
    }

    public int getCurrX() {
        return currX;
    }

    public void setCurrX(int currX) {
        this.currX = currX;
    }

    public int getCurrY() {
        return currY;
    }

    public void setCurrY(int currY) {
        this.currY = currY;
    }

    public String getRes() {
        return res;
    }

    public void setRes(String res) {
        this.res = res;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "From : " + from + ", prevX : " + prevX + ", prevY : " + prevY + ", currX : " + currX + ", currY : " + currY + ", res : " + res + ", color : " + color;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

}
