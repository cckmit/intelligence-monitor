package com.zhikuntech.intellimonitor.core.commons.utils;

import com.zhikuntech.intellimonitor.core.commons.base.BaseResponse;
import com.zhikuntech.intellimonitor.core.commons.base.ResultCode;
import com.zhikuntech.intellimonitor.core.commons.dto.AuthDTO;
import com.zhikuntech.intellimonitor.core.commons.dto.HeaderParamDTO;
import com.zhikuntech.intellimonitor.core.commons.dto.UpdateAuthDTO;
import com.zhikuntech.intellimonitor.core.commons.rest.AuthRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @Author 杨锦程
 * @Date 2021/6/22 10:18
 * @Description 权限管理工具类
 * @Version 1.0
 */
@Component
@Slf4j
public class AuthUtil {
    /**
     * 新增权限
     * @param code
     * @param authDTO
     * @return
     */
    public BaseResponse createAuth(String code, HeaderParamDTO headerParamDTO, AuthDTO authDTO){
        Map body = AuthRestTemplate.createAuth(code,headerParamDTO, authDTO);
        String upmStatus = body.get("code").toString();
        if(upmStatus.equals("0")){
            log.info("新增权限成功!");
            return BaseResponse.success(body.get("data"));
        }else {
            log.info("新增权限失败!");
            return BaseResponse.failure(ResultCode.CREATE_AUTH_FAILED, body.get("message").toString());
        }
    }

    /**
     * 删除权限
     * @param code
     * @return
     */
    public BaseResponse deleteAuth(String code,HeaderParamDTO headerParamDTO){
        Map body = AuthRestTemplate.deleteAuth(code,headerParamDTO);
        String upmStatus = body.get("code").toString();
        if(upmStatus.equals("0")){
            log.info("删除权限成功");
            return BaseResponse.success(body.get("data"));
        }else {
            log.info("删除权限失败!");
            return BaseResponse.failure(ResultCode.DELETE_AUTH_FAILED, body.get("message").toString());
        }
    }

    /**
     * 查询权限
     * @param code
     * @return
     */
    public BaseResponse selectAuth(String code,HeaderParamDTO headerParamDTO){
        Map body = AuthRestTemplate.selectAuth(code,headerParamDTO);
        String upmStatus = body.get("code").toString();
        if(upmStatus.equals("0")){
            log.info("查询权限成功");
            //解析响应结果到权限数据模型
            Object data = body.get("data");
            if(!ObjectUtils.isEmpty(data)){
                AuthDTO authDTO = parseAuthDTO((Map)data);
                log.info("解析响应结果 authDTO->{}",authDTO);
                return BaseResponse.success(authDTO);
            }
            return BaseResponse.success("");
        }else {
            log.info("查询权限失败!");
            return BaseResponse.failure(ResultCode.SELECT_AUTH_FAILED, body.get("message").toString());
        }
    }

    /**
     * 修改权限
     * @param authDTO
     * @return
     */
    public BaseResponse updateAuth(String code,HeaderParamDTO headerParamDTO,AuthDTO authDTO){
//        AuthDTO authDTO = AuthMapper.INSTANCES.toAuthDTO(auth);
        Map body = AuthRestTemplate.updateAuth(code,headerParamDTO, authDTO);
        String upmStatus = body.get("code").toString();
        if(upmStatus.equals("0")){
            log.info("修改权限成功");
            Object data = body.get("data");
            if(!ObjectUtils.isEmpty(data)){
                //解析响应结果
                UpdateAuthDTO updateAuthDTO = parseToUpdateAuthDTO((Map) data);
                return BaseResponse.success(updateAuthDTO);
            }
            return BaseResponse.success("");
        }else {
            log.info("修改权限失败!");
            return BaseResponse.failure(ResultCode.UPDATE_AUTH_FAILED, body.get("message").toString());
        }
    }

    /**
     * 回滚权限
     * @param rollbackId
     * @return
     */
    public BaseResponse rollbacksAuth(String rollbackId,HeaderParamDTO headerParamDTO){
        Map body = AuthRestTemplate.rollbacksAuth(rollbackId,headerParamDTO);
        String upmStatus = body.get("code").toString();
        if(upmStatus.equals("0")){
            log.info("回滚权限成功");
            return BaseResponse.success(body.get("data"));
        }else {
            log.info("回滚权限失败!");
            return BaseResponse.failure(ResultCode.ROLLBACK_AUTH_FAILED, body.get("message").toString());
        }
    }

    /**
     * 解析响应结果到权限数据模型
     * @param map
     * @return
     */
    private AuthDTO parseAuthDTO(Map map){
        AuthDTO authDTO = new AuthDTO();
        authDTO.setAuthId(map.get("authId").toString());
        authDTO.setAuthCode(map.get("authCode").toString());
        authDTO.setAuthName(map.get("authName").toString());
        authDTO.setDescription(map.get("description").toString());
        try {
            authDTO.setCreateTime(TimeUtil.parseDate(map.get("createTime").toString()));
            authDTO.setUpdateTime(TimeUtil.parseDate(map.get("updateTime").toString()));
        } catch (ParseException e) {
            log.error("时间格式转换异常 {}",e);
        }
        authDTO.setSubType(Integer.parseInt(map.get("subType").toString()));
        Object childObj = map.get("children");
        if(childObj!=null){
            //强转成List<Map>类型
            List<Map> child = (List<Map>) childObj;
            AuthDTO[] childAuth = new AuthDTO[child.size()];
            for(int index = 0;index < child.size();index++){
                AuthDTO subAuthDTO = parseAuthDTO(child.get(index));
                childAuth[index] = (subAuthDTO);
                authDTO.setChildren(childAuth);
            }
        }
        return authDTO;
    }

    /**
     * 解析修改权限响应结果到数据模型
     * @param map
     * @return
     */
    private UpdateAuthDTO parseToUpdateAuthDTO(Map map){
        UpdateAuthDTO updateAuthDTO = new UpdateAuthDTO();
        updateAuthDTO.setRollbackId(map.get("rollbackId").toString());
        updateAuthDTO.setInserts((List<UpdateAuthDTO.CodeType>)map.get("inserts"));
        updateAuthDTO.setUpdates((List<UpdateAuthDTO.CodeType>)map.get("updates"));
        updateAuthDTO.setDeletions((List<UpdateAuthDTO.CodeType>)map.get("deletions"));
        return updateAuthDTO;
    }
}
