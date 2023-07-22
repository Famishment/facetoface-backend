package com.lzj.facetoface.utils;

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Auther: lzj
 * @Date: 2023/7/3-07-03-10:21
 * @Description: com.lzj.facetoface.utils
 */

public class ImportXingQiuUser {

    public static void main(String[] args) {
        String fileName = "E:\\星球项目\\user-center-backend\\src\\main\\resources\\testExcel.xlsx";
        // 这里需指定读用哪个class去读，然后读取第一个sheet  同步读取会自动finish
        List<XingQiuTableUserInfo> userInfoList = EasyExcel.read(fileName).head(XingQiuTableUserInfo.class).sheet().doReadSync();
        System.out.println("总数：" + userInfoList.size());

        Map<String, List<XingQiuTableUserInfo>> listMap = userInfoList.stream()
                .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername()))
                .collect(Collectors.groupingBy(XingQiuTableUserInfo::getUsername));
        for (String s : listMap.keySet()) {
            
        }
        System.out.println("不重复昵称数：" + listMap.keySet().size());
    }
}
