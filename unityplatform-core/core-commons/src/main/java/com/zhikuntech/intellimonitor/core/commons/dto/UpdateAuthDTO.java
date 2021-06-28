package com.zhikuntech.intellimonitor.core.commons.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author 杨锦程
 * @Date 2021/6/23 15:10
 * @Description 更新权限后返回结果数据模型
 * @Version 1.0
 */
@Data
public class UpdateAuthDTO {
    /**
     * 新增部分
     */
    private List<CodeType> inserts;
    /**
     * 修改部分
     */
    private List<CodeType> updates;
    /**
     * 删除部分
     */
    private List<CodeType> deletions;
    /**
     * 回滚号
     */
    private String rollbackId;

    @Data
    public class CodeType{
        private Integer subType;
        private String authCode;
    }
}
