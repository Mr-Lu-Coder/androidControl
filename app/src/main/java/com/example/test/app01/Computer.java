package com.example.test.app01;

import java.io.Serializable;

/**
 * Created by lushangqi on 2016/3/26.
 */
public class Computer implements Serializable{
    private String id;
    private String startTime;
    private String opentotaltime;
    private String mac;
    private String computename;
    private String ip;
    private String state;

    public Computer(){};
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


}
