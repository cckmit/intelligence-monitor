package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCdq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataDq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfDataDqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.DqBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.DqHeaderParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataDqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.DqAnd72windForShortTimePatternUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ParseDataFileUtil.obtainInputStream;

/**
 * <p>
 * 短期功率预测 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
@Service
public class WfDataDqServiceImpl extends ServiceImpl<WfDataDqMapper, WfDataDq> implements IWfDataDqService {


    @Override
    public void batchSave() {

        try {
            InputStream inputStream = null;
            List<String> strings = null;
            // TODO 此处替换为远程数据源
            inputStream = obtainInputStream("浙江.普陀风电场_72wind_20210303.rb");
            strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));

            DqHeaderParse dqHeaderParse = DqAnd72windForShortTimePatternUtils.processDqHeader(strings);
            List<DqBodyParse> dqBodyParses = DqAnd72windForShortTimePatternUtils.processDqBody(strings);
            if (Objects.isNull(dqHeaderParse) || CollectionUtils.isEmpty(dqBodyParses)) {
                return;
            }

            // construct data
            LocalDateTime headerDate = TimeProcessUtils.parseHeaderByPatternOrExcept(dqHeaderParse.getDqDate());
            LocalDateTime createTime = LocalDateTime.now();

            List<WfDataDq> wfDataDqs = new ArrayList<>();
            for (DqBodyParse dqBodyPars : dqBodyParses) {
                WfDataDq wfDataCdq = WfDataDq.builder().orgId("333").build();
                wfDataDqs.add(wfDataCdq);

                int bodyTime = Integer.parseInt(dqBodyPars.getBodyTime());
                LocalDateTime eventTime = headerDate.plusMinutes((bodyTime - 1) * 15);
                // body
                wfDataCdq.setEventDateTime(eventTime);
                wfDataCdq.setCreateTime(createTime);
                wfDataCdq.setBodyTime(bodyTime);
                wfDataCdq.setOrderNum(dqBodyPars.getOrderNum());
                wfDataCdq.setStationNumber(dqBodyPars.getStationNumber());
                wfDataCdq.setForecastProduce(new BigDecimal(dqBodyPars.getUpProduce()));
                // header

            }

            if (CollectionUtils.isNotEmpty(dqBodyParses)) {
                saveBatch(wfDataDqs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // TODO 记录解析文件失败的信息
            throw new RuntimeException("ex occur: " + ex.getMessage());
        }
    }
}
