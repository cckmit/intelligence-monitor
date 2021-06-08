package com.zhikuntech.intellimonitor.mainpage.domain.exception;//package com.monitor.aop.exception;
//
//import com.alibaba.fastjson.JSONObject;
//import com.monitor.base.ResultCode;
//import com.monitor.base.BaseResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.validation.BindException;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import javax.servlet.ServletException;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestControllerAdvice
//@Slf4j
//public class ExceptionAdvice {
//
//    @ExceptionHandler(BaseException.class)
//    public BaseResponse<?> handleCodeException(BaseException ex) {
//        produceErrorLog(ex);
//        return BaseResponse.exception(ex);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public BaseResponse<?> handleException(Exception ex) {
//        produceErrorLog(ex);
//        System.out.println(ex);
//        return BaseResponse.failure(ResultCode.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(ServletException.class)
//    public BaseResponse<?> handleServletException(ServletException ex) {
//        produceErrorLog(ex);
//        return BaseResponse.failure(ResultCode.REQUEST_ERROR);
//    }
//
//    /**
//     * 处理Get请求中 入参自定义异常
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(BindException.class)
//    public BaseResponse<?> handleBindException(Exception ex) {
//        produceErrorLog(ex);
//        //封装报错信息
//        StringBuilder sb = new StringBuilder("参数错误：[");
//        List<ObjectError> list = ((BindException) ex).getAllErrors();
//        for (ObjectError item : list) {
//            sb.append(item.getDefaultMessage()).append(',');
//        }
//        sb.deleteCharAt(sb.length() - 1);
//        sb.append(']');
//        String msg = sb.toString();
//        msg = StringUtils.isEmpty(msg) ? ResultCode.INTERNAL_SERVER_ERROR.msg() : msg;
//        return BaseResponse.failure(ResultCode.INTERNAL_SERVER_ERROR,msg);
//    }
//
//    /**
//     * 处理Post请求中 入参自定义异常
//     * @param ex
//     * @return
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public BaseResponse<?> handleArgumentException(Exception ex) {
//        produceErrorLog(ex);
//        String msg = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError().getDefaultMessage();
//        msg = StringUtils.isEmpty(msg) ? ResultCode.INTERNAL_SERVER_ERROR.msg() : "参数错误：["+msg+"]";
//        return BaseResponse.failure(ResultCode.INTERNAL_SERVER_ERROR,msg);
//    }
//
//    private void produceErrorLog(Exception ex){
//        Arrays.stream(ex.getStackTrace())
//            .filter(item -> item.getClassName().contains("com.monitor") && item.getLineNumber() > 0)
//            .collect(Collectors.toList()).forEach(item -> {
//                JSONObject jsonObject = new JSONObject();
//                jsonObject.put("className", item.getClassName());
//                jsonObject.put("methodName", item.getMethodName());
//                jsonObject.put("lineNumber", item.getLineNumber());
//                jsonObject.put("logContent", ex instanceof BaseException ? ((BaseException) ex).getCode().msg() : StringUtils.isBlank(ex.getMessage()) ? ex.getCause() : ex.getMessage());
//                log.error(jsonObject.toJSONString());
//            });
//    }
//}
