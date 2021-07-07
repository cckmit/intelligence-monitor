package com.zhikuntech.intellimonitor.demo.domain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhikuntech.intellimonitor.core.commons.utils.SFTPUtil;
import com.zhikuntech.intellimonitor.core.commons.constant.sftp.ZKSftpATTRS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/17 16:26
 * @Description sftp测试
 * @Version 1.0
 */
@Controller
@RequestMapping("/sftp")
public class SftpTestController {

    private SFTPUtil sftpUtil = new SFTPUtil();

    /**
     * 测试连接
     */
    @GetMapping("/createSession")
    public void testCreateSession() {
        sftpUtil.doConnect();
    }

    /**
     * 测试下载
     */
    @GetMapping("/download")
    public void testDownload() throws IOException {
        //如果是文件夹，会拒绝访问，必须是精确到文件
        OutputStream outputStream = new FileOutputStream("E:/zhikun/sftp/download/test1.txt");
        sftpUtil.download("", "/test.txt", outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 测试上传
     */
    @GetMapping("/upload")
    public void testUpload() throws IOException {
        System.out.println("开始上传");
        File file = new File("E:/zhikun/sftp/bg.jpg");
        InputStream inputStream = new FileInputStream(file);
        sftpUtil.upload("003", "bg.jpg", inputStream, file.length());
        inputStream.close();
        System.out.println("上传完成");
    }

    /**
     * 测试删除
     */
    @GetMapping("delete")
    public void testRm() {
//        sftpUtil.rm("/", "bg.jpg");
        //前面不能包含/upload,方法中已经包含了此文件夹
//        sftpUtil.delete("/001/word", "winform.docx");
//        sftpUtil.delete(ZKSftpATTRSFileType.REG,"001/word", "winform.docx");
//        sftpUtil.deleteFileOrDic("001", "word");
//        sftpUtil.deleteFileOrDic("/", "003");
        sftpUtil.deleteFileOrDic("", "test.txt");
    }

    /**
     * 测试目录是否存在
     */
    @GetMapping("/exist")
    public void testExist() {
        boolean exist = sftpUtil.isDirExist("001/word");
        if (exist) {
            System.out.println("存在");
        } else {
            System.out.println("不存在");
        }
    }

    /**
     * 列出所在目录下的文件列表
     */
    @GetMapping("list")
    public void testList() {
//        List<ZKSftpATTRS> zkSftpATTRSList = sftpUtil.ListChildGrandsonDicFile("/upload");
        List<ZKSftpATTRS> zkSftpATTRSList = sftpUtil.listDicFile("", true);
//        List<ZKSftpATTRS> zkSftpATTRSList = sftpUtil.listDicFile("/test.txt",true);
        //json转换
        ObjectMapper mapper = new ObjectMapper();
        try {
//            String zkSftpATTRSListJson = mapper.writeValueAsString(zkSftpATTRSList);
//            System.out.println(zkSftpATTRSListJson);
            //下载解析后的json文件
            File file = new File("E:\\zhikun\\sftp\\list\\list.json");
            OutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            mapper.writeValue(fileOutputStream, zkSftpATTRSList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            System.out.println("json转换异常!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建目录(可创建多级目录)
     *
     * @return
     */
    @GetMapping("/makeDirectory")
    public void makeDirectory() {
        boolean makeDirectory = sftpUtil.createDirectory("", "003");
        System.out.println(makeDirectory);
    }

    /**
     * 修改文件或者文件夹名
     */
    @GetMapping("/rename")
    private void rename() {
        boolean rename = sftpUtil.rename("", "004", "005");
        System.out.println("rename->" + rename);
    }


}
