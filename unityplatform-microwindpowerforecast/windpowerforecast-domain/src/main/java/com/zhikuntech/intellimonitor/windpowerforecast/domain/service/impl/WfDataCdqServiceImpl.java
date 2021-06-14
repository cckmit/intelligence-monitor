package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCdq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfDataCdqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.CdqBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.CdqHeaderParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataCdqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.CdqAnd4CWindForSuperShortTimePatternUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ConstantsOfWf;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.NumberProcessUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ParseDataFileUtil.obtainInputStream;

/**
 * <p>
 * 超短期功率预测 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
@Service
public class WfDataCdqServiceImpl extends ServiceImpl<WfDataCdqMapper, WfDataCdq> implements IWfDataCdqService {


    @Override public void batchSave() {

        try {
            InputStream inputStream = null;
            List<String> strings = null;
            // TODO 此处替换为远程数据源
            inputStream = obtainInputStream("浙江.普陀风电场_4Cwind_202103020115.rb");
            strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));

            CdqHeaderParse cdqHeaderParse = CdqAnd4CWindForSuperShortTimePatternUtils.processCdqHeader(strings);
            List<CdqBodyParse> cdqBodyParses = CdqAnd4CWindForSuperShortTimePatternUtils.processCdqBody(strings);
            if (Objects.isNull(cdqHeaderParse) || CollectionUtils.isEmpty(cdqBodyParses)) {
                return;
            }
            // construct data
            LocalDateTime headerDate = TimeProcessUtils.parseHeaderByPatternOrExcept(cdqHeaderParse.getCdqDate());
            LocalDateTime createTime = LocalDateTime.now();

            List<WfDataCdq> dataCdqs = new ArrayList<>();
            for (CdqBodyParse cdqBodyPars : cdqBodyParses) {
                WfDataCdq wfDataCdq = WfDataCdq.builder().orgId(ConstantsOfWf.DEV_ORG_ID).build();
                dataCdqs.add(wfDataCdq);

                int bodyTime = Integer.parseInt(cdqBodyPars.getBodyTime());
                /*
                    根据bodyTime计算eventTime
                    以date标识的起始时刻开始为第一点，其后每行以时间排序，时间顺序列标识的是距离起始点的时间点数。例中：行#16中时间顺序为16
                 */

                LocalDateTime eventTime = headerDate.plusMinutes((bodyTime - 1) * 15);
                // body
                wfDataCdq.setEventDateTime(eventTime);
                wfDataCdq.setCreateTime(createTime);
                wfDataCdq.setBodyTime(bodyTime);
                wfDataCdq.setOrderNum(cdqBodyPars.getOrderNum());
                wfDataCdq.setStationNumber(cdqBodyPars.getStationNumber());
                wfDataCdq.setForecastProduce(NumberProcessUtils.strToBigDecimal(cdqBodyPars.getUpProduce()));
                // header
                wfDataCdq.setSampleCap(NumberProcessUtils.strToBigDecimal(cdqHeaderParse.getCdqSampleCap()));
                wfDataCdq.setCap(NumberProcessUtils.strToBigDecimal(cdqHeaderParse.getCdqCap()));
                wfDataCdq.setRunningCap(NumberProcessUtils.strToBigDecimal(cdqHeaderParse.getCdqRunningCap()));
                wfDataCdq.setSampleIds(cdqHeaderParse.getCdqSampleIds());
                wfDataCdq.setHeaderDate(headerDate);
            }

            if (CollectionUtils.isNotEmpty(dataCdqs)) {
                // TODO 记录解析文件成功信息
                saveBatch(dataCdqs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // TODO 记录解析文件失败的信息
            throw new RuntimeException("ex occur: " + ex.getMessage());
        }
    }
}
