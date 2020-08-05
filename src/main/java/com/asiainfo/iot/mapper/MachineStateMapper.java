package com.asiainfo.iot.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface MachineStateMapper {

    public List<HashMap<String,Object>> queryMonitorTaskList();

    public List<HashMap<String,Object>> queryMachineStateList(@Param("param") HashMap<String, Object> map);


}
