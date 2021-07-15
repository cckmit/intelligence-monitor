package com.zhikuntech.intellimonitor.structuredata.domain.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author tn
 * @since 2021-07-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ShakeData extends Model<ShakeData> {

    private static final long serialVersionUID = 1L;

    private String 时间;

    private Float 通道1峰峰值;

    private Float 通道2峰峰值;

    private Float 通道3峰峰值;

    private Float 通道4峰峰值;

    private Float 通道5峰峰值;

    private Float 通道6峰峰值;

    private Float 通道7峰峰值;

    private Float 通道8峰峰值;

    private Float 通道9峰峰值;

    private Float 通道10峰峰值;

    private Float 通道11峰峰值;

    private Float 通道12峰峰值;

    private Float 通道1有效值;

    private Float 通道2有效值;

    private Float 通道3有效值;

    private Float 通道4有效值;

    private Float 通道5有效值;

    private Float 通道6有效值;

    private Float 通道7有效值;

    private Float 通道8有效值;

    private Float 通道9有效值;

    private Float 通道10有效值;

    private Float 通道11有效值;

    private Float 通道12有效值;

    private Float 通道1均值;

    private Float 通道2均值;

    private Float 通道3均值;

    private Float 通道4均值;

    private Float 通道5均值;

    private Float 通道6均值;

    private Float 通道7均值;

    private Float 通道8均值;

    private Float 通道9均值;

    private Float 通道10均值;

    private Float 通道11均值;

    private Float 通道12均值;

    private Float 通道1主频率值;

    private Float 通道2主频率值;

    private Float 通道3主频率值;

    private Float 通道4主频率值;

    private Float 通道5主频率值;

    private Float 通道6主频率值;

    private Float 通道7主频率值;

    private Float 通道8主频率值;

    private Float 通道9主频率值;

    private Float 通道10主频率值;

    private Float 通道11主频率值;

    private Float 通道12主频率值;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
