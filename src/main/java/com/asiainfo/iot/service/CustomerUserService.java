package com.asiainfo.iot.service;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.iot.bean.TsMonitorUser;
import com.asiainfo.iot.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(value = "customerUserService")
public class CustomerUserService implements UserDetailsService{

    @Autowired
    private UserDao userDao;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        TsMonitorUser user = userDao.findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        String pwd = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(pwd);
        return user;
    }


}
