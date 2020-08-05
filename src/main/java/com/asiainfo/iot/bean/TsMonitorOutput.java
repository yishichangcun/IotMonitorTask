package com.asiainfo.iot.bean;

public class TsMonitorOutput {
    private Integer id;
    private String ip;
    private String user_id;
    private Integer rule_id;
    private String output;
    private String status;
    private String updatetime;
    private Integer level;

    public TsMonitorOutput() {
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getRule_id() {
        return rule_id;
    }

    public void setRule_id(Integer rule_id) {
        this.rule_id = rule_id;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getupdatetime() {
        return updatetime;
    }

    public void setupdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
