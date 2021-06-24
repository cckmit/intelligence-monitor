package com.zhikuntech.intellimonitor.core.commons.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/22 10:23
 * @Description 权限数据模型
 * @Version 1.0
 */
@Data
public class AuthDTO {
    /**
     * 权限编码，新增必填，唯一标识
     */
    private String authCode;
    /**
     * 权限Id
     */
    private String authId;
    /**
     * 权限名，新增必填
     */
    private String authName;
    /**
     * 权限名，新增必填
     */
    private String description;
    /**
     * 系统权限时的子类型：0:系统权限，1:菜单权限，2：按钮权限
     */
    private Integer subType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 子权限(数组形式)
     */
    private AuthDTO[] children;
}
