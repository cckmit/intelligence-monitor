package com.zhikuntech.intellimonitor.windpowerforecast.domain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.entity.WfBasicParseResult;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.mapper.WfBasicParseResultMapper;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.IWfBasicParseResultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataCdqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataDqService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.service.data.IWfDataNwpService;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.SftpFetchDataUtils;
import com.zhikuntech.intellimonitor.windpowerforecast.domain.utils.TimeProcessUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 文件解析结果 服务实现类
 * </p>
 *
 * @author liukai
 * @since 2021-06-28
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WfBasicParseResultServiceImpl extends ServiceImpl<WfBasicParseResultMapper, WfBasicParseResult> implements IWfBasicParseResultService {

    private final IWfDataDqService dqService;

    private final IWfDataNwpService nwpService;

    private final IWfDataCdqService cdqService;

    //# TODO 补发之前日期 (success_mark = 2)


    /*
        获取数据
     */

    public void fetchDqWithPointDate(LocalDateTime dateTime, /*dq|cdq|nwp*/ String type) {
        // 短期数据获取
        QueryWrapper<WfBasicParseResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("data_gen_date", TimeProcessUtils.formatLocalDateTimeWithSecondPattern(dateTime));
        queryWrapper.eq("file_type", "dq");
        queryWrapper.eq("success_mark", 2);

        WfBasicParseResult parseResult = getBaseMapper().selectOne(queryWrapper);
        if (Objects.nonNull(parseResult)) {
            String fileType = parseResult.getFileType();
            String fileName = parseResult.getFileName();
            // 需要判断是否为null
            InputStream inputStream = SftpFetchDataUtils.fetchRemoteData(fileType, fileName);
            if (Objects.isNull(inputStream)) {
                throw new IllegalStateException(
                        "sftp未获取到数据:[" + fileType + "], [" + fileName + "]"
                );
            }
            try {
                List<String> list = IOUtils.readLines(inputStream, "GBK");
                switch (type) {
                    case "dq":
                        dqService.batchProcessDqData(list);
                        break;
                    case "cdq":
                        cdqService.batchProcessCdqData(list);
                        break;
                    case "nwp":
                        nwpService.batchProcessNwpData(list);
                        break;
                    default:
                        throw new IllegalArgumentException("无法处理的类别:" + type);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        }
    }

    public void fetchNwpWithPointDate(LocalDate localDate) {
        // 气象数据获取

    }

    public void fetchCdqWithPointDate(LocalDateTime dateTime) {
        // 超短期数据

    }

    /*
        获取数据
     */


    @Override public void genDqDataNeedFetch(LocalDate localDate) {
        // 后缀
        String yyyyMMdd = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        // 浙江.普陀风电场
        //_
        //72wind
        //_
        //20210303
        //.rb


        // 生成需要获取的短期数据
        WfBasicParseResult parseResult = WfBasicParseResult.builder()
                .version(0)
                .fileType("dq")
                .successMark(2)
                .filePrefix("72wind")
                .fileSuffix(yyyyMMdd)
                .windFarmName("浙江.普陀风电场")
                .dataGenDate(LocalDateTime.of(localDate, LocalTime.MIN))
                .build();
        String fileName = parseResult.getWindFarmName() + "_" + parseResult.getFilePrefix()
                + "_" + parseResult.getFileSuffix() + ".rb";
        parseResult.setFileName(fileName);

        getBaseMapper().insert(parseResult);
    }

    @Override public void genCdqDataNeedFetch(LocalDate localDate) {
        // 生成需要获取的超短期数据

        // 浙江.普陀风电场
        //_
        //4Cwind
        //_
        //202103020115
        //.rb

        LocalDateTime dateTime = LocalDateTime.of(localDate, LocalTime.MIN).plusMinutes(15);
        List<WfBasicParseResult> resultList = new ArrayList<>();
        int dayOfMonth = dateTime.getDayOfMonth();


        LocalDateTime compTime = dateTime;
        for (;dayOfMonth == compTime.getDayOfMonth();) {
            String yyyyMMddHHmm = dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            WfBasicParseResult parseResult = WfBasicParseResult.builder()
                    .version(0)
                    .fileType("cdq")
                    .successMark(2)
                    .filePrefix("4Cwind")
                    .fileSuffix(yyyyMMddHHmm)
                    .windFarmName("浙江.普陀风电场")
                    .dataGenDate(dateTime)
                    .build();
            String fileName = parseResult.getWindFarmName() + "_" + parseResult.getFilePrefix()
                    + "_" + parseResult.getFileSuffix() + ".rb";
            parseResult.setFileName(fileName);

            resultList.add(parseResult);

            dateTime = dateTime.plusMinutes(15);
            compTime = dateTime.minusMinutes(15);
        }

        // save
        saveBatch(resultList);
    }

    @Override public void genNwpDataNeedFetch(LocalDate localDate) {
        // 生成需要获取的气象数据

        String yyyyMMdd = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // 浙江.普陀风电场
        //_
        //72nwp
        //_
        //20210304
        //.rb

        // 生成需要获取的短期数据
        WfBasicParseResult parseResult = WfBasicParseResult.builder()
                .version(0)
                .fileType("nwp")
                .successMark(2)
                .filePrefix("72nwp")
                .fileSuffix(yyyyMMdd)
                .windFarmName("浙江.普陀风电场")
                .dataGenDate(LocalDateTime.of(localDate, LocalTime.MIN))
                .build();
        String fileName = parseResult.getWindFarmName() + "_" + parseResult.getFilePrefix()
                + "_" + parseResult.getFileSuffix() + ".rb";
        parseResult.setFileName(fileName);

        getBaseMapper().insert(parseResult);
    }

}
