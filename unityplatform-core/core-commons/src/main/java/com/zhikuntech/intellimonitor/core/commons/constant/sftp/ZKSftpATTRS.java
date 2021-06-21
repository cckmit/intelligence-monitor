package com.zhikuntech.intellimonitor.core.commons.constant.sftp;

import lombok.Data;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/17 18:56
 * @Description 文件属性
 * @Version 1.0
 */
@Data
public class ZKSftpATTRS {
    /**
     * 标志指示哪些属性
     */
    private int flags = 0;
    /**
     * 文件大小
     */
    private long size;
    /**
     * 用户标识符
     */
    private int uid;
    /**
     * 组标识符
     */
    private int gid;
    /**
     * 文件权限
     */
    private Permissions permissions;
    /**
     * 最后一次访问文件或目录的时间(时间戳)
     */
    private int atime;
    /**
     * 最后一次修改文件或目录的时间(时间戳)
     */
    private int mtime;
    /**
     * 文件类型
     */
    private ZKSftpATTRSFileType fileType;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件路径(绝对路径)
     */
    private String path;
    /**
     * 子目录下文件或者文件夹
     */
    private List<ZKSftpATTRS> zkSftpATTRSList;
}
