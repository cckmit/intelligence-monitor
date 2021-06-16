package com.zhikuntech.intellimonitor.mainpage.domain.utils;

import com.alibaba.excel.EasyExcel;
import com.zhikuntech.intellimonitor.mainpage.domain.dto.FanRuntimeDTO;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 代志豪
 * 2021/6/16 14:42
 */
public class EasyExcelUtil {

    public static <T> void export(OutputStream outputStream, String sheetName, List<T> list){
        EasyExcel.write(outputStream,list.get(0).getClass()).sheet(sheetName).doWrite(list);
    }

    public static void main(String[] args) {

        //实现excel写的操作
        //1 设置写入文件夹地址和excel文件名称
        String filename = "C:\\Users\\zhikun\\Desktop\\a\\a.xlsx";
        // 2 调用easyexcel里面的方法实现写操作
        // write方法两个参数：第一个参数文件路径名称，第二个参数实体类class
        EasyExcel.write(filename, FanRuntimeDTO.class).sheet("学生列表").doWrite(getData());

    }

    //创建方法返回list集合
    private static List<FanRuntimeDTO> getData() {
        List<FanRuntimeDTO> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            FanRuntimeDTO data = new FanRuntimeDTO();
            data.setNumber(i);
            data.setWindVelocity(2.5);
            list.add(data);
        }
        return list;
    }
}
