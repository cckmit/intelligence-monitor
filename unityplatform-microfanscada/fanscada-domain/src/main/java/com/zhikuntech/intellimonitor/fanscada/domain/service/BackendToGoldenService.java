package com.zhikuntech.intellimonitor.fanscada.domain.service;


import com.zhikuntech.intellimonitor.fanscada.domain.golden.annotation.GoldenId;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQuery;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.BackendToGoldenQueryList;
import com.zhikuntech.intellimonitor.fanscada.domain.pojo.GoldenIdQuery;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/15 11:09
 * @Description mysql数据库和golden数据库关于点位的映射关系
 * @Version 1.0
 */
@Service
public interface BackendToGoldenService {
    /**
     * 根据数据库表中编号或者风机编号查询golden数据库表中id（单条）
     * @return
     */
    List<Integer> getGoldenIdByBackendIdOrNumber(BackendToGoldenQuery backendToGoldenQuery);

    /**
     * 根据数据库表中编号或者风机编号查询golden数据库表中id（批量）
     * @param backendToGoldenQueryList
     * @return
     */
    List<Integer> listGoldenIdByBackendIdOrNumber(BackendToGoldenQueryList backendToGoldenQueryList);

    /**
     *
     * @param goldenIdQuery
     * @return
     */
    List<Integer> getGoldenIdByNumberAndId(GoldenIdQuery goldenIdQuery);
}
