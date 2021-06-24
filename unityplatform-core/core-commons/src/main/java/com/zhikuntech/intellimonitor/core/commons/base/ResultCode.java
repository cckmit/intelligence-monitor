package com.zhikuntech.intellimonitor.core.commons.base;

import com.zhikuntech.intellimonitor.core.commons.constant.HttpConstant;

public enum ResultCode {

    /**
     * 响应状态枚举
     */
    SUCCESS(HttpConstant.HTTP_RES_CODE_200,"成功"),
    PERMISSION_DENIED(HttpConstant.HTTP_RES_CODE_500, "没有权限!"),
    INTERNAL_SERVER_ERROR(HttpConstant.HTTP_RES_CODE_500,"服务器内部异常!"),
    PARAMETER_ERROR(HttpConstant.HTTP_RES_CODE_500,"参数错误"),
    REQUEST_ERROR(HttpConstant.HTTP_RES_CODE_500,"请求错误，请检查请求信息!"),
    LOGIN_EXPIRED(HttpConstant.UPM_RESULT_ERROR,"登陆过期!"),
    NOT_LOGIN(HttpConstant.UPM_SESSION_ID_EXPIRE,"未登入!"),
    OPERATION_TOO_FREQUENT(HttpConstant.HTTP_RES_CODE_500,"操作频繁，请稍后再试!"),
    FEGIN_EXCEPTION(HttpConstant.HTTP_RES_CODE_500,  "服务器内部异常"),
    DATD_NOT_EXCEPTION(HttpConstant.HTTP_RES_CODE_500,  "数据查询为空"),
    DATD_FORMAT_EXCEPTION(HttpConstant.HTTP_RES_CODE_500,  "数据格式错误"),
    SERVER_EXCEPTION(HttpConstant.HTTP_RES_CODE_500,  "服务调用失败"),
    REDIS_LOCK_TIMEOUT(HttpConstant.HTTP_RES_CODE_500,  "获取锁超时"),
    TEMPLATE_INIT_ERROR(HttpConstant.HTTP_RES_CODE_500,  "模版初始化失败"),
    TEMPLATE_EMPTY(HttpConstant.HTTP_RES_CODE_500,  "模版为空"),
    TEMPLATE_PROCESS_FAIL(HttpConstant.HTTP_RES_CODE_500,  "模版加载失败"),
    PDF_CREAT_ERROR(HttpConstant.HTTP_RES_CODE_500,  "PDF生成失败"),
    LOG_BACK_IN(HttpConstant.UPM_SESSION_ID_EXPIRE,"重新登录"),
    FILE_UPLOAD_FAILED(HttpConstant.RPC_RESULT_SUCCESS, "文件上传失败"),
    GOLDEN_GETSNAPSHOTS_FAILED(HttpConstant.HTTP_RES_CODE_500,"获取指定id当前最新快照信息失败"),
    GOLDEN_SUBSCRIBESNAPSHOTS_FAILED(HttpConstant.HTTP_RES_CODE_500,"订阅庚顿推送快照(实时)数据失败"),
    USER_NOT_LOGIN_EXCEPTION(HttpConstant.HTTP_RES_CODE_500,"用户未登录"),
    SFTP_FAILURE(HttpConstant.SFTP_FAILURE,"sftp操作异常"),
    CREATE_AUTH_FAILED(HttpConstant.UPM_HTTP_CREATE_AUTH,"新增权限失败"),
    DELETE_AUTH_FAILED(HttpConstant.UPM_HTTP_DELETE_AUTH,"删除权限失败"),
    SELECT_AUTH_FAILED(HttpConstant.UPM_HTTP_SELECT_AUTH,"查询权限失败"),
    UPDATE_AUTH_FAILED(HttpConstant.UPM_HTTP_UPDATE_AUTH,"更新权限失败"),
    ROLLBACK_AUTH_FAILED(HttpConstant.UPM_HTTP_ROLLBACK_AUTH,"回滚权限失败"),
    AUTH_USER_NOT_LOGIN(HttpConstant.UPM_USER_NOT_LOGIN,"用户未登录"),
    USER_LOGIN_FAIL(HttpConstant.USER_LOGIN_FAIL,"用户登录失败"),
    USER_LOGOUT_FAIL(HttpConstant.USER_LOGOUT_FAIL,"用户登出失败"),
    REST_REQ_SEND_FAILED(HttpConstant.REST_REQ_SEND_FAILED,"restTemplate请求发送失败");

    private final Integer code;
    private String msg;

    ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
