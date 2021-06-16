package com.zhikuntech.intellimonitor.fanscada.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGolden;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQueryList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.annotation.Resource;
import java.util.List;

@Mapper
@Resource
public interface BackendToGoldenMapper extends BaseMapper<BackendToGolden> {
    /**
     * 根据数据库表中编号或者风机编号查询golden数据库表中id（单条）
     * @return
     */
    List<Integer> getGoldenIdByBackendIdOrNumber(@Param("backendToGoldenQuery") BackendToGoldenQuery backendToGoldenQuery);

    /**
     * 根据数据库表中编号或者风机编号查询golden数据库表中id（批量）
     * @param backendToGoldenQueryList
     * @return
     */
    List<Integer> listGoldenIdByBackendIdOrNumber(@Param("backendToGoldenQueryList") BackendToGoldenQueryList backendToGoldenQueryList);
}