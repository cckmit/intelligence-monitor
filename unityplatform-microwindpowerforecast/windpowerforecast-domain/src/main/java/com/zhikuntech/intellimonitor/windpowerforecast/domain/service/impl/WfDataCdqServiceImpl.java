package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataCdq;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfDataCdqMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.CdqBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.CdqHeaderParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataCdqService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.CdqAnd4CWindForSuperShortTimePatternUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ParseDataFileUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
            inputStream = obtainInputStream("浙江.普陀风电场_4Cwind_202103020115.rb");
            strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));

            CdqHeaderParse cdqHeaderParse = CdqAnd4CWindForSuperShortTimePatternUtils.processCdqHeader(strings);
            List<CdqBodyParse> cdqBodyParses = CdqAnd4CWindForSuperShortTimePatternUtils.processCdqBody(strings);
            if (CollectionUtils.isEmpty(cdqBodyParses) || Objects.isNull(cdqHeaderParse)) {
                return;
            }
            // construct data
            List<WfDataCdq> dataCdqs = new ArrayList<>();
            for (CdqBodyParse cdqBodyPars : cdqBodyParses) {
                WfDataCdq wfDataCdq = WfDataCdq.builder().orgId("333").build();
                dataCdqs.add(wfDataCdq);

                wfDataCdq.setOrderNum(cdqBodyPars.getOrderNum());
                wfDataCdq.setStationNumber(cdqBodyPars.getStationNumber());
                wfDataCdq.setBodyTime(Integer.valueOf(cdqBodyPars.getBodyTime()));
                wfDataCdq.setForecastProduce(new BigDecimal(cdqBodyPars.getUpProduce()));
                wfDataCdq.setSampleIds(cdqHeaderParse.getCdqSampleIds());
                wfDataCdq.setHeaderDate(LocalDateTime.parse(cdqHeaderParse.getCdqDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
            }

            if (CollectionUtils.isNotEmpty(dataCdqs)) {
                saveBatch(dataCdqs);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("ex occur: " + ex.getMessage());
        }
    }
}
