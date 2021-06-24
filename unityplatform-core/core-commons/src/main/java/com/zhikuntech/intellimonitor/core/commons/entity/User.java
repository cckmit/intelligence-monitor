package com.zhikuntech.intellimonitor.core.commons.entity;

import com.zhikuntech.intellimonitor.core.commons.valid.Update;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author 杨锦程
 * @Date 2021/6/23 17:13
 * @Description 用户
 * @Version 1.0
 */
@Data
public class User {
    /**
     * 用户名
     */
    @NotBlank(
            message = "用户名不能为空",
            groups = {Update.class}
    )
    private String username;

    /**
     * 密码
     */
    @NotBlank(
            message = "密码不能为空",
            groups = {Update.class}
    )
    private String password;

    /**
     * 下次自动登录
     */
    private boolean remember;
}
