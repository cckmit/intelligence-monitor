package com.zhikuntech.intellimonitor.core.commons.utils;

import com.jcraft.jsch.*;
import com.zhikuntech.intellimonitor.core.commons.constant.sftp.Permissions;
import com.zhikuntech.intellimonitor.core.commons.constant.sftp.ZKSftpATTRS;
import com.zhikuntech.intellimonitor.core.commons.constant.sftp.ZKSftpATTRSFileType;
import com.zhikuntech.intellimonitor.core.commons.exception.CodeException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

import static com.zhikuntech.intellimonitor.core.commons.base.ResultCode.SFTP_FAILURE;

/**
 * @Author 杨锦程
 * @Date 2021/6/17 14:32
 * @Description SFTP操作工具类
 * @Version 1.0
 */
public class SFTPUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(SFTPUtil.class);
    /**
     * ip地址
     */
    private String ftpIP = "192.168.3.179";
    /**
     * 端口
     */
    private Integer ftpPort = 22;
    /**
     * 用户名
     */
    private String ftpUser = "mysftp";
    /**
     * 密码
     */
    private String ftpPwd = "sftp1234";
    /**
     * 文件存储位置
     */
    private String resourceLocation = "/upload";

    public SFTPUtil() {
    }

    public SFTPUtil(String ftpIP, Integer ftpPort, String ftpUser, String ftpPwd, String resourceLocation) {
        this.ftpIP = ftpIP;
        this.ftpPort = ftpPort;
        this.ftpUser = ftpUser;
        this.ftpPwd = ftpPwd;
        this.resourceLocation = resourceLocation;
    }

    private static Session session;

    public boolean isConnected() {
        return session != null && session.isConnected();
    }

    public void doConnect() {
        createSession();
        cd2DstRootDir();
    }

    private void cd2DstRootDir() {

        //去掉最后一个"/"
        if (!resourceLocation.equals("/") && resourceLocation.endsWith("/")) {
            resourceLocation = resourceLocation.substring(0, resourceLocation.length() - 1);
        }
        LOGGER.info("Sftp remote root dir is :{}.", resourceLocation);
        makeRootDirIfNeeded();
    }

    private void makeRootDirIfNeeded() {
        LOGGER.info("Create sftp root dir if not exist.");
        ChannelSftp channelSftp = null;
        try {
            channelSftp = this.open();
            mkdir(channelSftp, resourceLocation, null);
            LOGGER.info("Sftp pwd: {}.", channelSftp.pwd());
        } catch (Exception e) {
            LOGGER.error("Exception happen.", e);
            throw new CodeException(SFTP_FAILURE, e.getMessage());
        } finally {
            close(channelSftp);
        }
    }

    private void createSession() {
        try {
            if (session != null && session.isConnected()) {
                session.disconnect();
            }
            JSch jSch = new JSch();
            session = jSch.getSession(ftpUser, ftpIP, ftpPort);
            //第一次登陆时候，是否需要提示信息，value可以填写 yes，no或者是ask
            session.setConfig("StrictHostKeyChecking", "no");
            if (!StringUtils.isEmpty(ftpPwd)) {
                session.setPassword(ftpPwd);
            }
            // 设置超时时间为无穷大
            session.setTimeout(0);
            LOGGER.warn("Set Timeout to ZERO, never timeout.");
            session.connect();
        } catch (JSchException ex) {
            LOGGER.error("Create Jsch session failed.", ex);
            throw new CodeException("Create Jsch session failed.", ex);
        }
    }

    /**
     * 创建目录(可创建多级目录)
     *
     * @param parentDic 父目录
     * @param dic       待创建的目录
     * @return
     */
    public boolean createDirectory(String parentDic, String dic) {
        ChannelSftp channelSftp = null;
        boolean parentDirExist = isDirExist(parentDic);
        LOGGER.info("父目录是否已经存在->{}", parentDirExist);
        //前面拼接默认的路径
        parentDic = resourceLocation + parentDic;
        //拼接路径
        dic = "/" + dic;
        try {
            channelSftp = this.open();
            mkdir(channelSftp, dic, parentDic);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 上传
     *
     * @param dir    服务器目录
     * @param id     服务器目录下的文件
     * @param is     输入流对象
     * @param length 文件长度
     */
    public void upload(String dir, String id, InputStream is, Long length) {
        LOGGER.info("Start to upload, relative dir: {}, id, {}.", dir, id);
        ChannelSftp channelSftp = null;
        try (InputStream inputStream = is) {
            String path = concat(dir, id);
            channelSftp = this.open();
            mkdir(channelSftp, dir);
            channelSftp.put(inputStream, path, new SftpMonitor(length), ChannelSftp.OVERWRITE);
        } catch (Exception e) {
            LOGGER.error("Exception happen.", e);
            throw new CodeException(SFTP_FAILURE, e.getMessage());
        } finally {
            close(channelSftp);
            close();
        }
    }

    /**
     * 下载
     *
     * @param dir          服务器目录
     * @param id           服务器目录下的文件
     * @param outputStream 输出流对象
     */
    public void download(String dir, String id, OutputStream outputStream) {
        LOGGER.info("Start to download, relative dir: {}, id:{}.", dir, id);
        if (Objects.isNull(outputStream)) {
            throw new CodeException(SFTP_FAILURE, "Input stream handler is null.");
        }
        ChannelSftp channelSftp = null;
        try {
            String path = concat(dir, id);
            channelSftp = this.open();
            channelSftp.get(path, outputStream, new SftpMonitor());
        } catch (Exception e) {
            LOGGER.error("Exception happen.", e);
            throw new CodeException(SFTP_FAILURE, e.getMessage());
        } finally {
            close(channelSftp);
            close();
        }
    }

    private void delete(String dir, String id) {
        LOGGER.info("Start to delete, relative dir: {}, id, {}.", dir, id);
        ChannelSftp channelSftp = null;
        try {
            channelSftp = this.open();
            String path = concat(dir, id);
            channelSftp.rm(path);
        } catch (Exception e) {
            LOGGER.error("Exception happen.", e);
            throw new CodeException(SFTP_FAILURE, e.getMessage());
        } finally {
            close(channelSftp);
            close();
        }
    }

    /**
     * 删除文件或者文件夹
     * (如果文件夹内不为空,则会删除失败)
     *
     * @param dir 服务器目录
     * @param fd  服务器目录下文件或文件夹
     * @return
     */
    public boolean deleteFileOrDic(String dir, String fd) {
        ChannelSftp channelSftp = null;
        try {
            channelSftp = this.open();
            String path = concat(dir, fd);
            try {
                channelSftp.cd(path);
                //能进入表示是文件夹
                channelSftp.rmdir(path);
            } catch (SftpException e) {
                e.printStackTrace();
                //异常不能进入时是文件
                channelSftp.rm(path);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error("Exception happen.", e);
            return false;
        } finally {
            close(channelSftp);
            close();
        }
    }


    public void close() {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
        LOGGER.info("Session is closed.");
    }

    private String concat(String... dirs) {
        StringBuffer sb = new StringBuffer(resourceLocation);
        for (String dir : dirs) {
            String[] ss = dir.split("/");
            Arrays.stream(ss).forEach(s -> sb.append("/").append(s));
        }
        return sb.toString();
    }

    private boolean exist(ChannelSftp channelSftp, String dir) {
        try {
            channelSftp.cd(dir);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    /**
     * 判断目录是否存在
     *
     * @param dicrectory
     * @return
     */
    public boolean isDirExist(String dicrectory) {
        try {
            ChannelSftp channelSftp = this.open();
            //拼接默认/upload前缀
            String compDic = resourceLocation + "/" + dicrectory;
            channelSftp.cd(compDic);
        } catch (SftpException e) {
            return false;
        }
        return true;
    }

    /**
     * 创建单个目录
     */
    private void makeSingleDir(ChannelSftp channelSftp, String dir) {
        if (StringUtils.isEmpty(dir)) {
            return;
        }
        if (exist(channelSftp, dir)) {
            return;
        }
        try {
            channelSftp.mkdir(dir);
            LOGGER.info("Create dir:{} successfully.", dir);
        } catch (SftpException e) {
            LOGGER.error(String.format("Create remote dir:%s failed.", dir), e);
            throw new CodeException(SFTP_FAILURE, "Create remote dir failed.");
        }
    }

    private void mkdir(ChannelSftp channelSftp, String dir, String rootDir) {
        if (StringUtils.isEmpty(dir)) {
            return;
        }
        if (exist(channelSftp, dir)) {
            return;
        }
        String absolutePath = "";
        if (!StringUtils.isEmpty(rootDir)) {
            absolutePath = rootDir;
        }
        String[] name = dir.split("/");
        for (String s : name) {
            if (StringUtils.isEmpty(s)) {
                continue;
            }
            absolutePath += "/" + s;
            makeSingleDir(channelSftp, absolutePath);
        }
    }

    /**
     * 生成多级目录
     */
    private void mkdir(ChannelSftp channelSftp, String dir) {
        this.mkdir(channelSftp, dir, resourceLocation);
    }

    /**
     * 打开连接
     *
     * @return
     */
    public ChannelSftp open() {

        ChannelSftp channelSftp;
        try {
            if (session == null || !session.isConnected()) {
                doConnect();
            }
            channelSftp = (ChannelSftp) session.openChannel("sftp");
            if (Objects.isNull(channelSftp)) {
                LOGGER.error("Channel is null");
                throw new CodeException(SFTP_FAILURE, "channel is null.");
            }
            channelSftp.connect();
            return channelSftp;
        } catch (JSchException e) {
            LOGGER.error("Exception happen.", e);
            throw new CodeException(SFTP_FAILURE, e.getMessage());
        }
    }

    /**
     * 查询出文件夹下所有子(孙)文件夹或者文件
     *
     * @param path      路径
     * @param recursive 是否递归查询
     * @return
     */
    public List<ZKSftpATTRS> listDicFile(String path, boolean recursive) {
        //拼接默认目录,如果是"/"不拼接
        path = resourceLocation + path;
        if (recursive) {
            return listChildGrandsonDicFile(path);
        } else {
            return listDicFileNoRecursive(path);
        }
    }

    /**
     * 列出子目录下文件或者文件夹
     *
     * @param path 目录
     * @return
     */
    private List listChildDicFile(String path) {
        ChannelSftp channelSftp = this.open();
        List list = null;
        try {
            Vector vector = channelSftp.ls(path);
            //Vector转换程List
            list = vector;
        } catch (SftpException e) {
            LOGGER.error("当前目录下没有文件!" + e.getMessage());
            return null;
        }
        return list;
    }

    /**
     * 查询出文件夹下所有子文件夹或者文件(非递归)
     *
     * @param path 路径
     * @return
     */
    private List<ZKSftpATTRS> listDicFileNoRecursive(String path) {
        List dicList = listChildDicFile(path);
        if (StringUtils.isEmpty(dicList)) {
            return null;
        }
        List<ZKSftpATTRS> zkSftpATTRSList = new ArrayList<>();  //子文件夹或者文件
        for (Object dic : dicList) {
            //强转为ChannelSftp.LsEntry内部类
            ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) dic;
            //解析单个文件或文件夹
            ZKSftpATTRS childZKSftpATTRS = parseSingleFile(lsEntry, path, false);

            if (!StringUtils.isEmpty(childZKSftpATTRS)) {
                zkSftpATTRSList.add(childZKSftpATTRS);
            }
        }
        return zkSftpATTRSList;
    }

    /**
     * 查询出文件夹下所有子孙文件夹或者文件(递归)
     *
     * @param path 路径
     */
    private List<ZKSftpATTRS> listChildGrandsonDicFile(String path) {
        List dicList = listChildDicFile(path);
        if (StringUtils.isEmpty(dicList)) {
            return null;
        }
        List<ZKSftpATTRS> zkSftpATTRSList = new ArrayList<>();  //子文件夹或者文件
        for (Object dic : dicList) {
            //强转为ChannelSftp.LsEntry内部类
            ChannelSftp.LsEntry lsEntry = (ChannelSftp.LsEntry) dic;
            //解析单个文件或文件夹
            ZKSftpATTRS childZKSftpATTRS = parseSingleFile(lsEntry, path, true);

            if (!StringUtils.isEmpty(childZKSftpATTRS)) {
                zkSftpATTRSList.add(childZKSftpATTRS);
            }
        }
        return zkSftpATTRSList;
    }

    /**
     * 解析单个文件
     *
     * @param lsEntry    文件对象
     * @param parentPath 父节点所处sftp服务器上的绝对路径，用于拼接子文件(或目录)的完整绝对路径
     * @param recursive  是否需要递归查询子孙文件或文件夹，true:递归 false:不递归
     * @return
     */
    private ZKSftpATTRS parseSingleFile(ChannelSftp.LsEntry lsEntry, String parentPath, boolean recursive) {
        ZKSftpATTRS zkSftpATTRS = new ZKSftpATTRS();
        SftpATTRS attrs = lsEntry.getAttrs();
        String filename = lsEntry.getFilename();
        String longname = lsEntry.getLongname();
        String permissionsString = longname.substring(0, 10);
        //去除【.】和【..】的情况
        if (".".equals(filename) || "..".equals(filename)) {
            return null;
        }
        //属性赋值
        zkSftpATTRS.setAtime(attrs.getATime());
        zkSftpATTRS.setFlags(attrs.getFlags());
        zkSftpATTRS.setGid(attrs.getGId());
        zkSftpATTRS.setMtime(attrs.getMTime());
        zkSftpATTRS.setSize(attrs.getSize());
        zkSftpATTRS.setUid(attrs.getUId());
        zkSftpATTRS.setFilename(filename);
        //文件类型,此变量用于解析权限,文件类型只有3种(文件夹d,链接l,文件-)
        ZKSftpATTRSFileType fileType = null;
        //拼接绝对路径
        zkSftpATTRS.setPath(parentPath + (parentPath.equals("/") ? "" : "/") + filename);
        if (attrs.isDir()) {
            zkSftpATTRS.setFileType(ZKSftpATTRSFileType.DIC);
            fileType = ZKSftpATTRSFileType.DIC;
        } else if (attrs.isBlk()) {
            zkSftpATTRS.setFileType(ZKSftpATTRSFileType.BLK);
            fileType = ZKSftpATTRSFileType.REG;
        } else if (attrs.isChr()) {
            zkSftpATTRS.setFileType(ZKSftpATTRSFileType.CHR);
            fileType = ZKSftpATTRSFileType.REG;
        } else if (attrs.isFifo()) {
            zkSftpATTRS.setFileType(ZKSftpATTRSFileType.FIFO);
            fileType = ZKSftpATTRSFileType.REG;
        } else if (attrs.isLink()) {
            zkSftpATTRS.setFileType(ZKSftpATTRSFileType.LINK);
            fileType = ZKSftpATTRSFileType.LINK;
        } else if (attrs.isReg()) {
            zkSftpATTRS.setFileType(ZKSftpATTRSFileType.REG);
            fileType = ZKSftpATTRSFileType.REG;
        } else if (attrs.isSock()) {
            zkSftpATTRS.setFileType(ZKSftpATTRSFileType.SOCK);
            fileType = ZKSftpATTRSFileType.REG;
        }
        zkSftpATTRS.setPermissions(parsePermissionsString(fileType, permissionsString));

        //如果需要递归查询
        if (recursive) {
            //判断：如果是文件夹，递归查询子目录
            if (attrs.isDir()) {
                List<ZKSftpATTRS> zkSftpATTRSList = listChildGrandsonDicFile(parentPath + (parentPath.equals("/") ? "" : "/") + filename);
                if (!StringUtils.isEmpty(zkSftpATTRSList)) {
                    zkSftpATTRS.setZkSftpATTRSList(zkSftpATTRSList);
                }
            }
        }
        return zkSftpATTRS;
    }

    /**
     * 解析文件权限
     *
     * @param fileType          文件类型
     * @param permissionsString 文件权限字符串
     * @return
     */
    private Permissions parsePermissionsString(ZKSftpATTRSFileType fileType, String permissionsString) {
        Permissions permissions = new Permissions();
        permissions.setFileType(fileType);
        permissions.setUserOfFileOwnerRead(permissionsString.charAt(1) == 'r' ? true : false);
        permissions.setUserOfFileOwnerWrite(permissionsString.charAt(2) == 'w' ? true : false);
        permissions.setUserOfFileOwnerExecute(permissionsString.charAt(3) == 'x' ? true : false);
        permissions.setUserOfSameGroupRead(permissionsString.charAt(4) == 'r' ? true : false);
        permissions.setUserOfSameGroupWrite(permissionsString.charAt(5) == 'w' ? true : false);
        permissions.setUserOfSameGroupExecute(permissionsString.charAt(6) == 'x' ? true : false);
        permissions.setUserOfNotSameGroupRead(permissionsString.charAt(7) == 'r' ? true : false);
        permissions.setUserOfNotSameGroupWrite(permissionsString.charAt(8) == 'w' ? true : false);
        permissions.setUserOfNotSameGroupExecute(permissionsString.charAt(9) == 'x' ? true : false);
        return permissions;
    }

    /**
     * 修改文件或者文件夹名
     *
     * @param dic     路径位置
     * @param oldName 旧文件或者文件夹名
     * @param newName 新文件或者文件夹名
     * @return
     */
    public boolean rename(String dic, String oldName, String newName) {
        dic = resourceLocation + ("/".equals(dic) ? "" : dic);
        //拼接完整路径
        oldName = dic + "/" + oldName;
        newName = dic + "/" + newName;
        ChannelSftp channelSftp = this.open();
        try {
            channelSftp.rename(oldName, newName);
            return true;
        } catch (SftpException e) {
            LOGGER.error("修改文件或者文件夹名失败");
            return false;
        } finally {
            this.close();
        }
    }

//    public boolean

    private void close(ChannelSftp channelSftp) {
        if (Objects.isNull(channelSftp)) {
            LOGGER.error("Must not happen, channel is null.");
            return;
        }
        if (channelSftp.isConnected()) {
            channelSftp.exit();
        }
    }

    private static class SftpMonitor implements SftpProgressMonitor {
        private static final Logger LOGGER = LoggerFactory.getLogger(SftpMonitor.class);
        private Map<Integer, Status> statusMap;

        private Long length;

        SftpMonitor(Long length) {
            this.length = length;
        }

        SftpMonitor() {
            this(0L);
        }

        private void initStatus() {
            statusMap = new HashMap<>();
            for (int i = 0; i <= 10; ++i) {
                SftpMonitor.Status status = new SftpMonitor.Status();
                status.setIsPrinted(false);
                status.setMessage("[" + i * 10 + "%] transferred...");
                statusMap.put(i, status);
            }
        }

        @Data
        static class Status {
            String message;
            Boolean isPrinted;
        }

        private String name(int op) {
            switch (op) {
                case GET:
                    return "GET";
                case PUT:
                    return "PUT";
                default:
                    return "UNKNOWN";
            }
        }

        @Override
        public void init(int op, String src, String dest, long max) {
            LOGGER.info("Start a transfer[{}]: op:{}, src:{}, dest:{}, max:{}, length:{}.",
                    hashCode(), name(op), src, dest, max, length);
            if (max >= 0) {
                this.length = max;
            }
            initStatus();
        }

        @Override
        public boolean count(long count) {
            if (length <= 0L) {
                return true;
            }
            if (count >= 0L) {
                printStatus(count);
                return true;
            }
            LOGGER.error("Must not happen, count:{} is neg.", count);
            return false;
        }

        private void printStatus(long count) {
            Long percentage = count * 10 / length;
            SftpMonitor.Status status = statusMap.get(percentage.intValue());
            if (Objects.isNull(status)) {
                LOGGER.error("Must not happen, incorrect percentage:{}, count:{}, max:{}.",
                        percentage, count, length);
            } else if (!status.isPrinted) {
                status.setIsPrinted(true);
                LOGGER.info("[{}] {}", hashCode(), status.getMessage());
            }  // ignored

        }

        @Override
        public void end() {
            LOGGER.info("[{}] transfer finished.", hashCode());
        }
    }
}
