package com.lzj.facetoface.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 * @Auther: lzj
 * @Date: 2022/11/2-11-02-22:59
 * @description: com.lzj.facetoface.model.domain.request
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 5181985051858471034L;

    private String userAccount;
    private String userPassword;
}
