package com.zhikuntech.intellimonitor.messagepush.facade.fegin;

import com.zhikuntech.intellimonitor.messagepush.prototype.dto.MessageBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 滕楠
 * @className MessageFegin
 * @create 2021/7/16 14:36
 **/
@FeignClient(name = "messagepush-app")
public interface MessageFegin {

    /**
     * 告警信息接收接口
     *
     * @return
     */
    @PostMapping("messagePush/sendMessage")
    Boolean sendMessage(@RequestBody MessageBody messageBody);

}