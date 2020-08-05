package com.asiainfo.iot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.iot.bean.Page;
import com.asiainfo.iot.bean.RespBean;
import com.asiainfo.iot.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/alarm/index")
public class IndexController {
    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Resource
    private MonitorService monitorService;

    /**
     * 页面查询待办列表
     * @return
     */
    @RequestMapping(value ="/queryStayList", method = RequestMethod.POST)
    public String queryStayList(@RequestBody String param){
        RespBean respBean= null;
        try {
            JSONObject json = JSON.parseObject(param);
            json.put("status","1,2");

            Page page = monitorService.queryMonitotList(json);
            respBean= RespBean.ok("请求成功", page);
        } catch (Exception e) {
            e.printStackTrace();
            respBean= RespBean.error("查询异常");
        }

        return JSONObject.toJSONString(respBean);
    }
    /**
     * 页面查询正在处理的列表
     * @return
     */
    @RequestMapping(value ="/queryDoingList", method = RequestMethod.POST)
    public String queryDoingList(@RequestBody String param){
        RespBean respBean= null;
        try {
            JSONObject json = JSON.parseObject(param);
            json.put("status","2");

            Page page = monitorService.queryMonitotList(json);
            respBean= RespBean.ok("请求成功", page);
        } catch (Exception e) {
            e.printStackTrace();
            respBean= RespBean.error("查询异常");
        }

        return JSONObject.toJSONString(respBean);
    }

    /**
     * 页面查询已办列表
     * @return
     */
    @RequestMapping(value ="/queryFinishedList", method = RequestMethod.POST)
    public String queryFinishedList(@RequestBody String param){
        RespBean respBean= null;
        try {
            JSONObject json = JSON.parseObject(param);
            json.put("status","3");

            Page page = monitorService.queryMonitotList(json);
            respBean= RespBean.ok("请求成功", page);
        } catch (Exception e) {
            e.printStackTrace();
            respBean= RespBean.error("查询异常");
        }

        return JSONObject.toJSONString(respBean);
    }

    /**
     * 发送完邮件更新任务输出表状态
     * @return
     */
    @RequestMapping(value ="/updateMonitorStatus", method = RequestMethod.POST)
    public String updateMonitorStatus(@RequestBody String param){
        RespBean respBean= null;
        try {
            JSONObject json = JSON.parseObject(param);
//            String sendTime = json.getString("sendTime");
//            String ids = json.getString("ids");
//            Map<Integer, String> map = (Map<Integer, String>)JSON.parse(ids);
//            String status = json.getString("status");

            monitorService.updateMonitorStatus(json);

            respBean= RespBean.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            respBean= RespBean.error("状态更新异常");
        }

        return JSONObject.toJSONString(respBean);
    }

}
