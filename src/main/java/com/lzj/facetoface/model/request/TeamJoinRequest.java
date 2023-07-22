package com.lzj.facetoface.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户加入队伍请求体
 */
@Data
public class TeamJoinRequest implements Serializable {

    private static final long serialVersionUID = -8464068243342447677L;

    /**
     * 队伍id
     */
    private Long teamId;

    /**
     * 队伍密码
     */
    private String password;
}
