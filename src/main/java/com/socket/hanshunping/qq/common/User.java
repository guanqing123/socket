package com.socket.hanshunping.qq.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 * @Author guanqing
 * @Date 2022/9/20 8:46
 **/
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 用户Id/用户名 */
    private String userId;
    /** 用户密码 */
    private String passwd;

    public User() {
    }

    public User(String userId, String passwd) {
        this.userId = userId;
        this.passwd = passwd;
    }
}
