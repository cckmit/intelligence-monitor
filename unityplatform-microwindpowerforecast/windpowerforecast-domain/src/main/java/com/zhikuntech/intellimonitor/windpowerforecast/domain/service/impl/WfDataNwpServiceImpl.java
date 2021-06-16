package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfDataNwp;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfDataCfMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfDataNwpMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.NwpBodyParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.parsemodel.NwpHeaderParse;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfDataNwpService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.ParseDataFileUtil.obtainInputStream;

/**
 * <p>
 * 数值天气预报 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-10
 */
@Slf4j
@Service
public class WfDataNwpServiceImpl extends ServiceImpl<WfDataNwpMapper, WfDataNwp> implements IWfDataNwpService {

    @Resource
    private WfDataCfMapper wfDataCfMapper;


    @Override
    public List<WfDataNwp> queryBatch() {
//        getBaseMapper().selectPage();

        QueryWrapper<WfDataNwp> queryWrapper = new QueryWrapper<>();
        List<WfDataNwp> wfDataNwps = getBaseMapper().selectList(queryWrapper);
        log.info("query result: [{}]", wfDataNwps);

        IPage<WfDataNwp> page = new Page<>(2, 2);
        IPage<WfDataNwp> records = getBaseMapper().selectPage(page, queryWrapper);


        return wfDataNwps;
    }

    @Override
    public List<BigDecimal> queryHigh() {
        QueryWrapper<WfDataNwp> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("DISTINCT high_level").orderByAsc("high_level");
        List<WfDataNwp> wfDataNwps = getBaseMapper().selectList(queryWrapper);
        if (CollectionUtils.isEmpty(wfDataNwps)) {
            return new ArrayList<>();
        }
        return wfDataNwps.stream()
                .filter(Objects::nonNull).map(WfDataNwp::getHighLevel)
                .filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public void batchSave() {

        try {
            InputStream inputStream = null;
            List<String> strings = null;
            // TODO 此处替换为远程数据源
            inputStream = obtainInputStream("浙江.普陀风电场_72nwp_20210303.rb");
            strings = IOUtils.readLines(inputStream, Charset.forName("GBK"));

//            DqHeaderParse dqHeaderParse = DqAnd72windForShortTimePatternUtils.processDqHeader(strings);
//            List<DqBodyParse> dqBodyParses = DqAnd72windForShortTimePatternUtils.processDqBody(strings);
            NwpHeaderParse nwpHeaderParse = NwpAnd72nwpWeatherPatternUtils.parseNwpHeader(strings);
            List<NwpBodyParse> nwpBodyParses = NwpAnd72nwpWeatherPatternUtils.parseNwpBody(strings);
            if (Objects.isNull(nwpHeaderParse) || CollectionUtils.isEmpty(nwpBodyParses)) {
                return;
            }

            // construct data
            LocalDateTime headerDate = TimeProcessUtils.parseHeaderByPatternOrExcept(nwpHeaderParse.getNwpDate());
            LocalDateTime createTime = LocalDateTime.now();

            List<WfDataNwp> wfDataNwps = new ArrayList<>();
            for (NwpBodyParse nwpBodyParse : nwpBodyParses) {
                WfDataNwp wfDataNwp = WfDataNwp.builder().orgId(ConstantsOfWf.DEV_ORG_ID).build();
                wfDataNwps.add(wfDataNwp);

                int bodyTime = Integer.parseInt(nwpBodyParse.getBodyTime());
                LocalDateTime eventTime = headerDate.plusMinutes((bodyTime - 1) * 15);
                // body
                wfDataNwp.setOrderNum(nwpBodyParse.getOrderNum());
                wfDataNwp.setStationNumber(nwpBodyParse.getStationNumber());
                wfDataNwp.setBodyTime(bodyTime);
                wfDataNwp.setEventDateTime(eventTime);
                wfDataNwp.setWindSpeed(NumberProcessUtils.strToBigDecimal(nwpBodyParse.getWindSpeed()));
                wfDataNwp.setHighLevel(NumberProcessUtils.strToBigDecimal(nwpBodyParse.getHighLevel()));
                wfDataNwp.setWindDirection(NumberProcessUtils.strToBigDecimal(nwpBodyParse.getWindDirection()));
                wfDataNwp.setTemperature(NumberProcessUtils.strToBigDecimal(nwpBodyParse.getTemperature()));
                wfDataNwp.setHumidity(NumberProcessUtils.strToBigDecimal(nwpBodyParse.getHumidity()));
                wfDataNwp.setPressure(NumberProcessUtils.strToBigDecimal(nwpBodyParse.getPressure()));
                wfDataNwp.setCreateTime(createTime);
                // header
                wfDataNwp.setCoordinates(nwpHeaderParse.getNwpCoordinates());
                wfDataNwp.setTurbineHigh(NumberProcessUtils.strToBigDecimal(nwpHeaderParse.getNwpTurbinH()));
                wfDataNwp.setHeaderDate(headerDate);
            }

            if (CollectionUtils.isNotEmpty(wfDataNwps)) {
                saveBatch(wfDataNwps);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // TODO 记录解析文件失败的信息
            throw new RuntimeException("ex occur: " + ex.getMessage());
        }
    }
}
