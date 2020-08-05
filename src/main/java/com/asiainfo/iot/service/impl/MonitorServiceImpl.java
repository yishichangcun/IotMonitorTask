package com.asiainfo.iot.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.asiainfo.iot.bean.Page;
import com.asiainfo.iot.bean.TsMonitorUser;
import com.asiainfo.iot.mapper.MonitorMapper;
import com.asiainfo.iot.service.MonitorService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
public class MonitorServiceImpl implements MonitorService {
    private static Logger logger= LogManager.getLogger(MonitorServiceImpl.class);

    @Autowired
    MonitorMapper monitorMapper;


    @Override
    public Page queryMonitotList(JSONObject json) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        HashMap<String,Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(json.getString("id"))){
            map.put("id", json.getString("id"));
        }
        if(!StringUtils.isEmpty(json.getString("ip"))){
            map.put("ip", json.getString("ip"));
        }
        if(authenticationToken != null && authenticationToken.getPrincipal() != null){
            TsMonitorUser user = (TsMonitorUser) authenticationToken.getPrincipal();
            if(user!=null){
                map.put("user_id", user.getUsername());
            }
        }
        if(!StringUtils.isEmpty(json.getString("rule_id"))){
            map.put("rule_id", json.getString("rule_id"));
        }
        if(!StringUtils.isEmpty(json.getString("level"))){
            map.put("level", json.getString("level"));
        }
        if(!StringUtils.isEmpty(json.getString("status"))){
            map.put("status", json.getString("status"));
        }
        map.put("orderByClause", "updatetime");

        int countNum = Integer.parseInt(json.getString("countNum"));
        map.put("countNum", countNum);

        int currNum =1;
        String currNumStr = json.getString("currNum");
        if(!StringUtils.isEmpty(currNumStr) && Integer.parseInt(currNumStr)>1){
            currNum = Integer.parseInt(currNumStr);
        }
        map.put("beginNum", (currNum -1) * countNum);


        Page page = new Page();
        List<HashMap<String, Object>> list = null;
        try {
            Integer totalRecord = 0;
            String totalNum = json.getString("totalRecord");
            if(StringUtils.isEmpty(totalNum) || "0".equals(totalNum)){
                totalRecord = monitorMapper.queryMonitotListPN(map);
            }else{
                totalRecord = Integer.parseInt(totalNum);
            }

            if(totalRecord > 0){
                list = monitorMapper.queryMonitotList(map);
                page = new Page(totalRecord,currNum,countNum,list);
            }else{
                page.setTotalRecord(0);
                page.setTotalPages(0);
                page.setCurrNum(1);
                page.setCountNum(countNum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return page;
    }


    @Override
    public List<HashMap<String, Object>> queryOutputList(JSONObject json) {
        HashMap<String,Object> map = new HashMap<>();
        if(!StringUtils.isEmpty(json.getString("id"))){
            map.put("id", json.getString("id"));
        }
        if(!StringUtils.isEmpty(json.getString("ip"))){
            map.put("ip", json.getString("ip"));
        }
        if(!StringUtils.isEmpty(json.getString("rule_id"))){
            map.put("rule_id", json.getString("rule_id"));
        }
        if(!StringUtils.isEmpty(json.getString("level"))){
            map.put("level", json.getString("level"));
        }
        if(!StringUtils.isEmpty(json.getString("status"))){
            map.put("status", json.getString("status"));
        }
        map.put("orderByClause", "createtime");
        List<HashMap<String, Object>> list = monitorMapper.queryMonitotList(map);
        if(list!=null && list.size()>0){
            String ids = "";
            for(int i=0;i<list.size();i++){
                if(i==0){
                    ids += list.get(i).get("id");
                }else{
                    ids += "," + list.get(i).get("id");
                }
            }
            map = new HashMap<>();
            map.put("status","2");
            map.put("ids", ids);
            int tag = monitorMapper.updateMonitorStatus(map);
        }

        return list;
    }

    @Override
    public String updateMonitorStatus(JSONObject json){
        HashMap<String,Object> map = new HashMap<>();
        map.put("status", json.getString("status"));
        map.put("ids", json.getString("ids"));
        if("3".equals(map.get("status"))){
            map.put("confirmtime", new Date());
        }
        int tag = monitorMapper.updateMonitorStatus(map);
        return null;
    }
}
