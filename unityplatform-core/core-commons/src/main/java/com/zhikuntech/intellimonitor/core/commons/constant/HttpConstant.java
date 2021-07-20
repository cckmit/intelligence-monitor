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

    //空指针异常
    Integer NULL_POINTER_EXCEPTION = 9001;
    //数组角标越界异常
    Integer ARRAY_INDEX_OUT_OF_BOUNDS_EXCEPTION = 9002;
    //类转换异常
    Integer CLASS_CAST_EXCEPTION = 9003;
    //数据库异常
    Integer SQL_EXCEPTION = 9004;
    //算数计算异常
    Integer ARITHMETIC_EXCEPTION = 9005;
    //非法参数异常
    Integer ILLEGAL_ARGUMENT_EXCEPTION = 9006;
    //非法权限异常
    Integer ILLEGAL_ACCESS_EXCEPTION = 9007;
    //栈溢出
    Integer STACK_OVER_FLOW_ERROR = 9008;
    //非法状态异常
    Integer ILLEGAL_STATE_EXCEPTION = 9009;
    //参数校验失败
    Integer PARAMETERS_CALIBRATION_FAILURE = 9010;
}
