package com.zhikuntech.intellimonitor.structuredata.domain.mapper;

import com.zhikuntech.intellimonitor.structuredata.domain.entity.ShakeData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author tn
 * @since 2021-07-15
 */
@Mapper
public interface ShakeDataMapper extends BaseMapper<ShakeData> {

    ShakeData getFanData(@Param("fanNumber") String fanNumber);
}
