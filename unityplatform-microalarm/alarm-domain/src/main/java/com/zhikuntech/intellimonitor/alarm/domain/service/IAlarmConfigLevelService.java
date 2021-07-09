package com.zhikuntech.intellimonitor.alarm.domain.service;

import com.zhikuntech.intellimonitor.alarm.domain.dto.AlarmLevelDTO;
import com.zhikuntech.intellimonitor.alarm.domain.entity.AlarmConfigLevel;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AddNewAlarmLevelQuery;
import com.zhikuntech.intellimonitor.alarm.domain.query.alarmlevel.AlarmLevelSimpleQuery;
import com.zhikuntech.intellimonitor.core.commons.base.Pager;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 告警等级表 服务类
 * </p>
 *
 * @author liukai
 * @since 2021-07-06
 */
public interface IAlarmConfigLevelService extends IService<AlarmConfigLevel> {

    /**
     * 新增告警等级
     * @param query 新增内容
     * @return true 新增成功 false 新增失败
     */
    boolean addNewAlarmLevel(AddNewAlarmLevelQuery query);

    /**
     * 查询告警等级-分页
     * @param query 查询(页码+每页数量)
     * @return 分页后的数据
     */
    Pager<AlarmLevelDTO> queryAlarmLevel(AlarmLevelSimpleQuery query);

    /**
     * 删除数据,删除前判断
     * <p>
     *     在告警规则表是否存在对该id的引用
     * </p>
     *
     * @param levelNo    标识id
     * @return      AlarmLevelDTO
     */
    AlarmLevelDTO deleteByLevelNo(String levelNo);

    /**
     * 批量删除数据
     * @param levelNos  需要删除的levelNos
     * @return          删除的数据
     */
    List<AlarmLevelDTO> batchDelete(List<String> levelNos);


    /**
     * 根据id修改内容
     *
     * @param dto   待修改的数据
     * @return      修改后的值
     */
    AlarmLevelDTO updateById(AlarmLevelDTO dto);

    /**
     * map映射等级信息
     *
     * @return  map映射
     */
    Map<String, AlarmLevelDTO> queryLevelMapAll();
}
