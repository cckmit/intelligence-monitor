package com.zhikuntech.intellimonitor.core.commons.entity;

import com.zhikuntech.intellimonitor.core.commons.dto.AuthDTO;
import com.zhikuntech.intellimonitor.core.commons.valid.Add;
import com.zhikuntech.intellimonitor.core.commons.valid.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author 杨锦程
 * @Date 2021/6/23 17:09
 * @Description 权限
 * @Version 1.0
 */
@Data
public class Auth {
    /**
     * 权限编码，新增必填，唯一标识
     */
    private String authCode;

    /**
     * 权限Id
     */
    @NotBlank(
            message = "权限id不允许为空",
            groups = {Update.class}
    )
    private String authId;

    /**
     * 权限名，新增必填
     */
    @NotBlank(
            message = "权限名不允许为空",
            groups = {Add.class, Update.class}
    )
    @Length(
            max = 50,
            message = "权限名称不得超过50个字符"
    )
    private String authName;

    /**
     * 权限名，新增必填
     */
    @Length(
            max = 200,
            message = "权限描述不得超过200个字符"
    )
    private String description;

    /**
     * 系统权限时的子类型：0:系统权限，1:菜单权限，2：按钮权限
     */
    @NotNull(
            message = "权限类型不允许为空",
            groups = {Add.class, Update.class}
    )
    @Max(
            value = 4L,
            message = "权限类型最大值为4",
            groups = {Add.class, Update.class}
    )
    @Min(
            value = 0L,
            message = "权限类型最小值为0",
            groups = {Add.class, Update.class}
    )
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
