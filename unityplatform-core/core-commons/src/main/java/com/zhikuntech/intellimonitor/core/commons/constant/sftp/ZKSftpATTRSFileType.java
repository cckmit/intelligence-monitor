package com.zhikuntech.intellimonitor.core.commons.constant.sftp;

/**
 * @Author 杨锦程
 * @Date 2021/6/17 19:00
 * @Description sftp服务器上文件类型
 * @Version 1.0
 */
public enum ZKSftpATTRSFileType {

    DIC("文件夹"),
    LINK("链接"),
    REG("文件"),
    CHR("外围设备"),
    BLK("块设备"),
    FIFO("fifo"),
    SOCK("管道通信");


    private String type;

    public String getType() {
        return type;
    }

    ZKSftpATTRSFileType(String type) {
        this.type = type;
    }
}
