package com.lzj.facetoface.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户退出队伍请求体
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = -3724766103919022347L;

    /**
     * 队伍id
     */
    private Long teamId;

}
