package com.zhikuntech.intellimonitor.core.commons.constant;


public interface HttpConstant {
    // 响应请求成功code
    Integer HTTP_RES_CODE_200 = 200;
    // 系统错误
    Integer HTTP_RES_CODE_500 = 500;

    Integer RPC_RESULT_SUCCESS = 200;

    String UPM_RESULT_SUCCESS = "0";

    Integer UPM_RESULT_ERROR = -2;

    Integer UPM_SESSION_ID_EXPIRE = 100001;

    //upm 调用接口成功
    Integer UPM_HTTP_CODE_SUCCESS = 0;

    //upm查询权限失败
    Integer UPM_HTTP_CREATE_AUTH = 3001;
    //upm删除权限失败
    Integer UPM_HTTP_DELETE_AUTH = 3002;
    //upm查询权限失败
    Integer UPM_HTTP_SELECT_AUTH = 3003;
    //upm更新权限失败
    Integer UPM_HTTP_UPDATE_AUTH = 3004;
    //upm回滚权限失败
    Integer UPM_HTTP_ROLLBACK_AUTH = 3005;
    //upm用户未登录
    Integer UPM_USER_NOT_LOGIN = 100205;
    //登录失败
    Integer USER_LOGIN_FAIL = 100206;
    //登出失败
    Integer USER_LOGOUT_FAIL = 100207;
    //RestTemplate请求发送失败
    Integer REST_REQ_SEND_FAILED = 4000;

    //sftp异常
    Integer SFTP_FAILURE = 4100;
    //远程接口调用异常
    Integer REMOTE_INTERFACE_CALL_EXCEPTION = 4200;
}
