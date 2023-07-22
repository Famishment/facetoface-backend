package com.lzj.facetoface.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户更新队伍请求体
 */
@Data
public class TeamUpdateRequest implements Serializable {

    private static final long serialVersionUID = -6787267718386398140L;

    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 状态 0 - 公开，1 - 私有，2 - 加密
     */
    private Integer status;

    /**
     * 队伍密码
     */
    private String password;
}
