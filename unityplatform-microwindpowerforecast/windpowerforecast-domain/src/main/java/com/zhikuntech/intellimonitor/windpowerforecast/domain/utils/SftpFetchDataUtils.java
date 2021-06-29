package com.zhikuntech.intellimonitor.windpowerforecast.domain.utils;

import com.zhikuntech.intellimonitor.core.commons.utils.SFTPUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Sftp 获取数据
 *
 * @author liukai
 */
public class SftpFetchDataUtils {

    static SFTPUtil sftpUtil = new SFTPUtil();

    public static InputStream fetchRemoteData(String type, String fileName) {
        if (StringUtils.isBlank(type) || StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("类型和文件名称不能为空");
        }
        String path = null;
        switch (type) {
            case "dq":
                path = "wf/dq";
                break;
            case "cdq":
                path = "wf/cdq";
                break;
            case "nwp":
                path = "wf/nwp";
                break;
            default:
                throw new IllegalArgumentException("没有找到对应的type:[" + type + "]");
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        sftpUtil.download(path, fileName, bout);
        byte[] bytes = bout.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        assert bytes.length != 0;
        return inputStream;
    }
}
