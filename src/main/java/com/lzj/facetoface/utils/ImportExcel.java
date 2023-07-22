package com.lzj.facetoface.utils;

import com.alibaba.excel.EasyExcel;

import java.util.List;

/**
 * @Auther: lzj
 * @Date: 2023/7/1-07-01-19:18
 * @Description: com.lzj.facetoface.utils
 */

public class ImportExcel {
    /**
     * 读取数据
     */
    //程序入口
    public static void main(String[] args) {
        String fileName = "E:\\星球项目\\user-center-backend\\src\\main\\resources\\prodExcel.xlsx";
        //readByListener(fileName);
        synchronousRead(fileName);
    }

    /**
     * 监听器读取
     * @param fileName
     */
    private static void readByListener(String fileName) {
        // 写法1：
        EasyExcel.read(fileName, XingQiuTableUserInfo.class, new TableListener()).sheet().doRead();
    }

    /**
     * 同步读取
     * @param fileName
     */
    private static void synchronousRead(String fileName) {
        // 写法2；
        List<XingQiuTableUserInfo> totalDataList = EasyExcel.read(fileName).head(XingQiuTableUserInfo.class).sheet().doReadSync();
        for (XingQiuTableUserInfo xingQiuTableUserInfo : totalDataList) {
            System.out.println(xingQiuTableUserInfo);
        }
    }
}
