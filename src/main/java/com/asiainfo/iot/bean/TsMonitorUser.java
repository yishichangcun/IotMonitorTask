package com.asiainfo.iot.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "ts_monitor_user")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "entityCache")
@ToString(includeFieldNames = true)
@ApiModel(value="ts_monitor_user", description="登录用户对象")
public class TsMonitorUser implements UserDetails {

    @Id
    @Column(name = "name")
    @ApiModelProperty(value="用户名称",name="name",example="测试")
    private String username;

    @Column(name = "password")
    @ApiModelProperty(value="登录密码",name="password",example="admin")
    private String password;

    @Column(name = "phone")
    @ApiModelProperty(value="手机号",name="phone",example="test")
    private String phone;

    @Column(name = "email")
    @ApiModelProperty(value="邮箱",name="email",example="test")
    private String email;

    public TsMonitorUser() {
    }

    public TsMonitorUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public TsMonitorUser(String username, String phone, String email){
        this.username = username;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auths = new ArrayList<>();

        return auths;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }


}
