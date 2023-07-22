package com.lzj.facetoface.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户创建队伍请求体
 */
@Data
public class TeamAddRequest implements Serializable {

    private static final long serialVersionUID = 1688618263965085766L;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * id
     */
    private Long userId;

    /**
     * 状态 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;
}
