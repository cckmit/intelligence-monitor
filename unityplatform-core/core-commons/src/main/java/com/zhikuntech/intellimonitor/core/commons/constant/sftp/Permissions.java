package com.zhikuntech.intellimonitor.core.commons.constant.sftp;

import lombok.Data;

/**
 * @Author 杨锦程
 * @Date 2021/6/18 11:49
 * @Description 用户操作文件的权限
 * @Version 1.0
 */
@Data
public class Permissions {
    /**
     * 文件类型
     */
    private ZKSftpATTRSFileType fileType;
    /**
     * 文件所有者读权限
     */
    private boolean userOfFileOwnerRead;
    /**
     * 文件所有者写权限
     */
    private boolean userOfFileOwnerWrite;
    /**
     * 文件所有者执行权限
     */
    private boolean userOfFileOwnerExecute;
    /**
     * 与文件所有者同一组的用户读权限
     */
    private boolean userOfSameGroupRead;
    /**
     * 与文件所有者同一组的用户写权限
     */
    private boolean userOfSameGroupWrite;
    /**
     * 与文件所有者同一组的用户执行权限
     */
    private boolean userOfSameGroupExecute;
    /**
     * 不与文件所有者同组的其他用户的读权限
     */
    private boolean userOfNotSameGroupRead;
    /**
     * 不与文件所有者同组的其他用户的写权限
     */
    private boolean userOfNotSameGroupWrite;
    /**
     * 不与文件所有者同组的其他用户的执行权限
     */
    private boolean userOfNotSameGroupExecute;
}
