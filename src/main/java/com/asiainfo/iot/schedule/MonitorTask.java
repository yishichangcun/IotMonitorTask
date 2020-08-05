package com.asiainfo.iot.schedule;

import com.asiainfo.iot.mapper.MachineStateMapper;
import com.asiainfo.iot.mapper.MonitorMapper;
import io.swagger.models.auth.In;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Configuration
@EnableScheduling
@CommonsLog
public class MonitorTask {

    @Autowired
    MachineStateMapper machineStateMapper;
    @Autowired
    MonitorMapper monitorMapper;

    /**
     * 每三分钟触发一次
     * 主机运行状态结果检测
     */
    @Scheduled(cron = "0 0/3 * * * ?")
    public void taskHostDetection() {
        Properties props = null;
        try {
            Resource resource = new ClassPathResource("/configInfo.properties");
            props = PropertiesLoaderUtils.loadProperties(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }

        float cpu_ratio = Float.parseFloat(props.getProperty("CPU_RATIO"));
        float mem_ratio = Float.parseFloat(props.getProperty("MEM_RATIO"));
        float disk_ratio = Float.parseFloat(props.getProperty("DISK_RATIO"));
        float iostate_ratio = Float.parseFloat(props.getProperty("IOSTATE_RATIO"));

        String cpu_ratio_desc = props.getProperty("CPU_RATIO_DESC");
        String mem_ratio_desc = props.getProperty("MEM_RATIO_DESC");
        String disk_ratio_desc = props.getProperty("DISK_RATIO_DESC");
        String iostate_ratio_desc = props.getProperty("IOSTATE_RATIO_DESC");

        List<HashMap<String, Object>> list = machineStateMapper.queryMonitorTaskList();
        if (list != null && list.size() > 0) {
            HashMap<String, Object> map = null;
            for (int i = 0; i < list.size(); i++) {
                HashMap<String, Object> taskMap = list.get(i);
                map = new HashMap<>();
                map.put("ip", taskMap.get("ip"));
                List<HashMap<String, Object>> listm = machineStateMapper.queryMachineStateList(map);
                if (listm != null && listm.size() > 0) {
                    HashMap<String, Object> hmap = listm.get(0);

                    Timestamp timestamp = (Timestamp) hmap.get("time");
                    long time = timestamp.getTime();
                    long system_time = System.currentTimeMillis();
                    long second = (system_time - time) / 1000;    //计算秒

                    float cpu_d = Float.parseFloat(StringUtils.isEmpty(hmap.get("cpu"))?"0":hmap.get("cpu") + "");
                    float iostat_d = Float.parseFloat(StringUtils.isEmpty(hmap.get("iostat"))?"0":hmap.get("iostat") + "");
                    int mem_d = Integer.parseInt(StringUtils.isEmpty(hmap.get("mem"))?"0":hmap.get("mem") + "");
                    int total_men_d = Integer.parseInt(StringUtils.isEmpty(hmap.get("total_men"))?"0":hmap.get("total_men") + "");
                    int disk_d = Integer.parseInt(StringUtils.isEmpty(hmap.get("disk"))?"0":hmap.get("disk") + "");
                    int total_disk_d = Integer.parseInt(StringUtils.isEmpty(hmap.get("total_disk"))?"0":hmap.get("total_disk") + "");

                    try {
                        //如果上次录入的数据大于十分钟
                        if (second > (10 * 60)) {
                            taskMap.put("rule_ids", "1");
                            insertSelective(taskMap, 1, "距离上次录入超过十分钟,请尽快查看!");

                        } else if (cpu_d > cpu_ratio) {
                            //如果cpu使用率超过阈值
                            taskMap.put("rule_ids", "5");
                            insertSelective(taskMap, 1, "cpu使用率超过"+ cpu_ratio_desc +"阈值,请尽快查看!");

                        } else if ((mem_d/total_men_d) > mem_ratio) {
                            //如果内存使用率超过阈值
                            taskMap.put("rule_ids", "2");
                            insertSelective(taskMap, 1, "内存占用:"+ mem_d +" 总内存大小:"+ total_men_d +"使用超过"+ mem_ratio_desc +",请尽快查看!");

                        } else if ((disk_d/total_disk_d) > disk_ratio) {
                            //如果磁盘使用率超过阈值
                            taskMap.put("rule_ids", "3");
                            insertSelective(taskMap, 1, "磁盘占用:"+disk_d+" 总磁盘大小:"+ total_disk_d +"使用超过"+ disk_ratio_desc +",请尽快查看!");

                        } else if (iostat_d > iostate_ratio) {
                            //如果I/O占比超过阈值
                            taskMap.put("rule_ids", "6");
                            insertSelective(taskMap, 1, "I/O 占比超过"+ iostate_ratio_desc +"阈值,请尽快查看!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    taskMap.put("rule_ids", "1");
                    insertSelective(taskMap, 1, "未检测到状态数据,请尽快查看!");
                }
            }
        }
    }


    /**
     * 每周一凌晨00:05触发
     * 清除七天前的数据
     */
    @Scheduled(cron = "0 05 00 ? * MON")
    public void taskClearExpiredData() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, -7);

        HashMap<String, Object> map = new HashMap<>();
        map.put("createtime", now.getTime());

        monitorMapper.deleteByTime(map);
    }


    public void insertSelective(HashMap<String, Object> taskMap, Integer level, String output) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("ip", taskMap.get("ip"));
        map.put("user_id", taskMap.get("user_id"));
        map.put("rule_id", taskMap.get("rule_ids"));
        map.put("output", output);
        map.put("status", "1,2");
        map.put("level", level);
        map.put("orderByClause", "updatetime");
        map.put("orderByClause2", "createtime");
        List<HashMap<String, Object>> list = monitorMapper.queryOutputList(map);

        //计算当前时间和上条告警的时间差
        long timeDifference = 0l;
        if (list != null && list.size() > 0) {
            Date nowDate = new Date();
            Date sqlDate = (Date) list.get(0).get("updatetime");
            if (sqlDate == null) {
                sqlDate = (Date) list.get(0).get("createtime");
            }
            timeDifference = (nowDate.getTime() - sqlDate.getTime()) / 1000;
        }

        //5分钟内（300秒）修改告警输出，超过5分钟新增
        if(list !=null && list.size() > 0 && timeDifference <= 300){
            map = new HashMap<>();
            map.put("id", list.get(0).get("id"));
            map.put("updatetime", new Date());
            map.put("cycle_num", Integer.parseInt(list.get(0).get("cycle_num")+"") + 1);
            monitorMapper.updateByPrimaryKeySelective(map);
        }else{
            map.put("cycle_num", 1);
            map.put("status", "1");
            map.put("createtime", new Date());
            monitorMapper.insertSelective(map);
        }
    }


}
