package com.asiainfo.iot.service;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.iot.bean.Page;

import java.util.HashMap;
import java.util.List;


public interface MonitorService {

    public Page queryMonitotList(JSONObject json);
//    public Page queryStayList(JSONObject json);
//    public Page queryFinishedList(JSONObject json);
    public List<HashMap<String,Object>> queryOutputList(JSONObject json);
    public String updateMonitorStatus(JSONObject json);


}
