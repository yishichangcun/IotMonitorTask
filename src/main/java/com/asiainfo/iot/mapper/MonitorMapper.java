package com.asiainfo.iot.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface MonitorMapper {

    public Integer queryMonitotListPN(@Param("param") HashMap<String, Object> map);
    public List<HashMap<String,Object>> queryMonitotList(@Param("param") HashMap<String, Object> map);

//    public Integer queryStayListPN(@Param("param") HashMap<String, Object> map);
//    public List<HashMap<String,Object>> queryStayList(@Param("param") HashMap<String, Object> map);
//
//    public Integer queryFinishedListPN(@Param("param") HashMap<String, Object> map);
//    public List<HashMap<String,Object>> queryFinishedList(@Param("param") HashMap<String, Object> map);

    public List<HashMap<String,Object>> queryOutputList(@Param("param") HashMap<String, Object> map);

    public Integer updateMonitorStatus(@Param("param") HashMap<String, Object> map);
    public Integer updateByPrimaryKeySelective(@Param("param") HashMap<String, Object> map);
    public Integer insertSelective(@Param("param") HashMap<String, Object> map);
    public Integer deleteByTime(@Param("param") HashMap<String, Object> map);

}
