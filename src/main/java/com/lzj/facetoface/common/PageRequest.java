package com.lzj.facetoface.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {

    // 生成序列化id，使这个对象在序列化时能保持唯一
    private static final long serialVersionUID = 690581795461258737L;

    /**
     * 页面大小，默认大小为 10
     */
    protected int pageSize = 10;

    /**
     * 当前页数（第几页），默认是第 1 页
     */
    protected int pageNum = 1;

}
