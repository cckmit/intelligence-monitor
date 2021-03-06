package com.zhikuntech.intellimonitor.mainpage.domain.mapper.model;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGolden;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.mainpage.domain.model.BackendToGoldenQueryList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/15 10:53
 * @Description mysql数据库和golden数据库关于点位的映射关系
 * @Version 1.0
 */
@Mapper
public interface BackendToGoldenMapper extends BaseMapper<BackendToGolden> {
    /**
     * 根据数据库表中编号或者风机编号查询golden数据库表中id（单条）
     * @return
     */
    List<BackendToGolden> getGoldenIdByBackendIdOrNumber(@Param("backendToGoldenQuery") BackendToGoldenQuery backendToGoldenQuery);

    /**
     * 根据数据库表中编号或者风机编号查询golden数据库表中id（批量）
     * @param backendToGoldenQueryList
     * @return
     */
    List<Integer> listGoldenIdByBackendIdOrNumber(@Param("backendToGoldenQueryList") BackendToGoldenQueryList backendToGoldenQueryList);
}
