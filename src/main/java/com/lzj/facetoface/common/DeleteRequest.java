package com.lzj.facetoface.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用删除请求参数
 */
@Data
public class DeleteRequest implements Serializable {

    // 生成序列化id，使这个对象在序列化时能保持唯一
    private static final long serialVersionUID = -7886115165888702204L;

    private long id;

}
