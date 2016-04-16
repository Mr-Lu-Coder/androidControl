package com.example.test.app01;

import android.util.Log;

import java.io.Serializable;

/**
 * Computer类
 * 实现Serializable接口来实现活动之间对Computer对象的数据传递
 */
public class Computer implements Serializable{
    private String id = "";
    private String startTime = "";
    private String opentotaltime = "";
    private String mac = "";
    private String computename = "";
    private String ip = "";
    private String state = "";

    public Computer(){

    };


    public Computer(String computename, String ip, String state)
    {
        this.computename = computename;
        this.ip = ip;
        this.state = state;
    }


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getOpentotaltime() {
        return opentotaltime;
    }
    public void setOpentotaltime(String opentotaltime) {
        this.opentotaltime = opentotaltime;
    }

    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getComputename() {
        return computename;
    }
    public void setComputename(String computename) {
        this.computename = computename;
    }

    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 当两个Computer类的所有属性的值是相等时,返回true, 否则返回false
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof Computer) {
            if (((Computer) other).getId().equals(this.id) &&
                    ((Computer) other).getStartTime().equals(this.startTime) &&
                    ((Computer) other).getState().equals(this.state) &&
                    ((Computer) other).getComputename().equals(this.computename) &&
                    ((Computer) other).getOpentotaltime().equals(this.opentotaltime) &&
                            ((Computer) other).getIp().equals(this.ip) &&
                    ((Computer) other).getMac().equals(this.mac)) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }
}
