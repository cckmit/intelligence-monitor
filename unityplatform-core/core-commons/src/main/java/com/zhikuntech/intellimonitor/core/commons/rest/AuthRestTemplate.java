package com.zhikuntech.intellimonitor.core.commons.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.dto.AuthDTO;
import com.zhikuntech.intellimonitor.core.commons.dto.HeaderParamDTO;
import com.zhikuntech.intellimonitor.core.commons.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author 杨锦程
 * @Date 2021/6/22 11:03
 * @Description 远程请求upm的接口
 * @Version 1.0
 */
@Component
@Slf4j
public class AuthRestTemplate {
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private ObjectMapper objectMapper;

    private static final String IP = "192.168.3.179";
    private static final Integer PORT = 18088;

    /**
     * 新增权限
     *
     * @param code    系统编码（唯一）
     * @param authDTO 权限模型
     * @return
     */
    public Map createAuth(String code, HeaderParamDTO headerParamDTO, AuthDTO authDTO) {
        log.info("新增权限 code->{},authDTO->{}", code, authDTO);
        String url = "http://" + IP + ":" + PORT + "/v2/auths/systems/" + code;
        String authDTOJsonStr = convertToJsonStr(authDTO);
        return sendRestRequest(url, authDTOJsonStr, HttpMethod.POST, headerParamDTO);
    }

    /**
     * 删除权限
     *
     * @param code
     * @return
     */
    public Map deleteAuth(String code, HeaderParamDTO headerParamDTO) {
        log.info("删除权限 code->{}", code);
        String url = "http://" + IP + ":" + PORT + "/v2/auths/systems/" + code;
        return sendRestRequest(url, null, HttpMethod.DELETE, headerParamDTO);
    }

    /**
     * 查询权限
     *
     * @param code
     * @return
     */
    public Map selectAuth(String code, HeaderParamDTO headerParamDTO) {
        log.info("查询权限 code->{}", code);
        String url = "http://" + IP + ":" + PORT + "/v2/auths/systems/" + code;
        return sendRestRequest(url, null, HttpMethod.GET, headerParamDTO);
    }

    /**
     * 修改权限
     *
     * @param authDTO
     * @return
     */
    public Map updateAuth(String code, HeaderParamDTO headerParamDTO, AuthDTO authDTO) {
        log.info("修改权限 code->{} authDTO->{}", code, authDTO);
        String url = "http://" + IP + ":" + PORT + "/v2/auths/systems/" + code;
        String authDTOJsonStr = convertToJsonStr(authDTO);
        return sendRestRequest(url, authDTOJsonStr, HttpMethod.PUT, headerParamDTO);
    }

    /**
     * 回滚权限
     *
     * @param rollbackId 修改权限时成功时返回的回滚编号
     * @return
     */
    public Map rollbacksAuth(String rollbackId, HeaderParamDTO headerParamDTO) {
        log.info("回滚权限 rollbackId-{}", rollbackId);
        String url = "http://" + IP + ":" + PORT + "/v2/auths/systems/rollbacks/" + rollbackId;
        return sendRestRequest(url, null, HttpMethod.PUT, headerParamDTO);
    }

    /**
     * 发送RestTemplate请求
     *
     * @param url            地址
     * @param data           携带数据
     * @param method         方法
     * @param headerParamDTO 请求头参数
     * @return 返回响应内容body部分
     */
    private Map sendRestRequest(String url, String data, HttpMethod method, HeaderParamDTO headerParamDTO) {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Application-Token", headerParamDTO.getApplicationToken());
        headers.add("xxl_sso_sessionid", headerParamDTO.getXxlSsoSessionid());
        //封装请求数据和请求头
        HttpEntity formEntity = new HttpEntity(data, headers);
        try {
            //发送请求
            ResponseEntity<Object> exchange = restTemplate.exchange(url, method, formEntity, Object.class);
            //解析响应
            log.info(exchange.toString());
            Map body = (Map) exchange.getBody();
            return body;
        } catch (RestClientException e) {
            log.error("发送RestTemplate请求失败{}", e);
            throw new AuthException(ResultCode.REST_REQ_SEND_FAILED, e.toString());
        }
    }

    /**
     * 转换成json字符串
     *
     * @param object
     * @return
     */
    private String convertToJsonStr(Object object) {
        String jsonStr = "";
        try {
            jsonStr = objectMapper.writeValueAsString(object);
            log.info("authDTOJsonStr->{}", jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("json转换失败");
        }
        return jsonStr;
    }

}
