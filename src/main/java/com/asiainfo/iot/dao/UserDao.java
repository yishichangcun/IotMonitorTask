package com.asiainfo.iot.dao;

import com.asiainfo.iot.bean.TsMonitorUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDao extends JpaRepository<TsMonitorUser,String> {



   TsMonitorUser findByUsername(String userName);

}
