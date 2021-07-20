package com.zhikuntech.intellimonitor.cable.domain.base;

import lombok.Data;

import java.util.Date;

@Data
public class IdSegment {
    private String bizTag;

    private Long minId;

    private Long maxId;

    private Long step;

    private Long middleId;

    private Date lastUpdateTime;

    private Date currentUpdateTime;

    public Long getMiddleId() {

        if (this.middleId == null) {
            this.middleId = this.maxId - (long) Math.round(step / 2);
        }
        return middleId;
    }

    public Long getMinId() {
        if (this.minId == null) {
            if (this.maxId != null && this.step != null) {
                this.minId = this.maxId - this.step;
            } else {
                throw new RuntimeException("maxid or step is null");
            }
        }

        return minId;
    }

    public IdSegment(){}

    public IdSegment(String bizTag, Long middleId) {
        this.bizTag = bizTag;
        this.middleId = middleId;
        this.maxId = 0L;
        this.step = 1L;
        this.minId = 1L;

    }
}
