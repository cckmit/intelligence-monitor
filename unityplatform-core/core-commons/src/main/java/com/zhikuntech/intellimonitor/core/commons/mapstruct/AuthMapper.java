package com.zhikuntech.intellimonitor.core.commons.mapstruct;

import com.zhikuntech.intellimonitor.core.commons.dto.AuthDTO;
import com.zhikuntech.intellimonitor.core.commons.entity.Auth;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Author 杨锦程
 * @Date 2021/6/23 17:47
 * @Description Auth属性映射到AuthDTO
 * @Version 1.0
 */
@Mapper
public interface AuthMapper {
    /**
     * 获取该类自动生成的实现类的实例
     */
    AuthMapper INSTANCES = Mappers.getMapper(AuthMapper.class);

    /**
     * 用于实现对象属性复制的方法
     * @param auth
     * @return
     */
    AuthDTO toAuthDTO(Auth auth);
}
