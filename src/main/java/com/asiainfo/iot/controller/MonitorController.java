package com.asiainfo.iot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.asiainfo.iot.bean.RespBean;
import com.asiainfo.iot.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/alarm/monitor")
public class MonitorController {
    Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Resource
    private MonitorService monitorService;

    /**
     * 查询当前需要发送邮件的任务输出列表
     * @return
     */
    @RequestMapping(value ="/queryOutputList", method = RequestMethod.POST)
    public String queryOutputList(@RequestBody String param){
        RespBean respBean= null;
        try {
            JSONObject json = JSON.parseObject(param);
            String sendTime = json.getString("sendTime");
            json.put("status","1");
            List<HashMap<String,Object>> list = monitorService.queryOutputList(json);

            respBean= RespBean.ok("请求成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            respBean= RespBean.error("查询异常");
        }

        return JSONObject.toJSONString(respBean);
    }




//    public static void main(String[] args) {
//        Map<String,String> map = new HashMap<>();
//        map.put("1","3");
//        map.put("2","3");
//        map.put("3","3");
//        map.put("4","3");
//        JSONObject js = new JSONObject();
//        js.put("qwe","111");
//        js.put("ids", map);
//
//        JSONObject ids = js.getJSONObject("ids");
//
//        Iterator it =ids.entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();
//            System.out.println(entry.getKey()+"--"+ entry.getValue());
//        }
//
//    }
}
