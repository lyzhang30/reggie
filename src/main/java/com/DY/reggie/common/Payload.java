package com.DY.reggie.common;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 为了方便后期获取token中的用户信息，将token中载荷部分单独封装成一个对象
 *
 * @author CaoChenLei
 */
@Data
public class Payload<T> implements Serializable {
    private String id;
    private T userInfo;
    private Date expiration;

    public Payload(String id, T userInfo, Date expiration) {
        this.id = id;
        this.userInfo = userInfo;
        this.expiration = expiration;
    }

    public Payload() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(T userInfo) {
        this.userInfo = userInfo;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
