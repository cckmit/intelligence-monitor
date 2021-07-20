package com.zhikuntech.intellimonitor.messagepush.domain.controller;

import com.zhikuntech.intellimonitor.messagepush.domain.service.MessageService;
import com.zhikuntech.intellimonitor.messagepush.prototype.dto.MessageBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author 滕楠
 * @className MessageController
 * @create 2021/7/16 14:39
 **/
@RestController
@RequestMapping("/messagePush")
public class MessageController {

    @Resource
    private MessageService messageService;

    @PostMapping("/sendMessage")
    public Boolean sendMessage(@RequestBody MessageBody messageBody) {
        Optional<MessageBody> optional = Optional.ofNullable(messageBody);
        if (optional.isPresent()) {
            MessageBody body = optional.get();
            String message = body.getObject().toString();
            Integer type = body.getType();
            messageService.messagePush(message, type);
            return true;
        }
        return false;
    }
}