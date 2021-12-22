package utils.collector.bsbcms;

import com.xueliman.iov.cloud.framework.web.util.Asserts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lk
 * @date 2021/10/14
 * ftp服务工具类
 */
@Slf4j
public class FtpFileUtil {

    /** 本地字符编码 */
    private static String LOCAL_CHARSET = "GBK";

    private FTPClient ftpClient;

    private String ftpUrl;
    private String ftpAccount;
    private String ftpPassword;
    private String ftpAddressPath;

    public FtpFileUtil(String ftpUrl, String ftpAccount, String ftpPassword, String ftpAddressPath) {
        this.ftpUrl = ftpUrl;
        this.ftpAccount = ftpAccount;
        this.ftpPassword = ftpPassword;
        this.ftpAddressPath = ftpAddressPath;
    }

    /**
     * 初始化ftp客户端
     */
    private boolean initFtp(){
        int replyCode = 0;
        try {
            ftpClient = new FTPClient();
//            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(ftpUrl);
            ftpClient.login(ftpAccount, ftpPassword);
//            if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))){
//                LOCAL_CHARSET = "UTF-8";
//            }
            ftpClient.setControlEncoding(LOCAL_CHARSET);
            replyCode = ftpClient.getReplyCode();
        }catch (Exception e){
            e.printStackTrace();
            Asserts.fail("ftp初始化异常");
        }
        boolean positiveCompletion = FTPReply.isPositiveCompletion(replyCode);

        if (!positiveCompletion){
            closeConnect();
            Asserts.fail("ftp连接失败,请确连接信息是否正确");
        }

        return positiveCompletion;

    }

    /**
     * 上传至ftp
     * @param fileName 上传文件名
     * @param file 上传文件绝对路径
     * @param ftpAddress ftp目标地址
     */
    public boolean uploadToFtp(String fileName, File file, String ftpAddress) {
        //初始化
        initFtp();
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            ftpClient.setFileType(2);
            //设置ftp目标存储地址
            this.prepareStorePath(ftpClient, new String(ftpAddress.getBytes("GBK"), StandardCharsets.ISO_8859_1));
            //上传文件
            boolean b = ftpClient.storeFile(new String(fileName.getBytes("GBK"), StandardCharsets.ISO_8859_1), stream);
            return b;
        }catch (Exception e){
            Asserts.fail("ftp上传文件:" + fileName + "失败:" + e);
            e.printStackTrace();
        }finally {
            try {
                if (stream != null){
                    stream.close();
                }
            closeConnect();
            } catch (IOException e) {
                Asserts.fail("IO流关闭失败" + e);
            }
        }
        return false;
    }

    /**
     * 下载链接配置
     * @param localBaseDir 本地目录
     * @param remoteBaseDir 远程目录
     * @return
     */
    public boolean startDown(String localBaseDir, String remoteBaseDir){
        //初始化
        initFtp();
        FTPFile[] files = null;
        try{
            boolean changeDir = ftpClient.changeWorkingDirectory(remoteBaseDir);
            if (changeDir){
                files = ftpClient.listFiles();
                for (int i = 0; i < files.length; i++) {
                    try {
                        downLoadFile(files[i], localBaseDir, remoteBaseDir);
                    }catch (Exception e){
                         log.error("<" + files[i].getName() + ">文件下载失败");
                    }
                }
                return true;
            }
        }catch (IOException e){
            Asserts.fail("下载过程出现异常" + e.getMessage());
        }finally {
            closeConnect();
        }
        return false;
    }

    /**
     * 下载ftp文件
     * @param ftpFile
     * @param relativeLocalPath 本地地址
     * @param relativeRemotePath 远程地址
     */
    public void downLoadFile(FTPFile ftpFile, String relativeLocalPath, String relativeRemotePath){
        if (ftpFile.isFile()){
            if (!ftpFile.getName().contains("?")){
                OutputStream outputStream = null;
                try{
                    File localFile = new File(relativeLocalPath + ftpFile.getName());
                    //判断文件是否存在，存在则返回
                    if (localFile.exists()){
                        return;
                    }else {
                        outputStream = new FileOutputStream(relativeLocalPath + ftpFile.getName());
                        ftpClient.retrieveFile(ftpFile.getName(), outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                }finally {
                    try {
                        if (outputStream != null){
                            outputStream.close();
                        }
                    }catch (IOException e){
                        log.error("IO输出文件流异常");
                    }
                }
            }else {
                String newLocalRelatePath = relativeLocalPath + ftpFile.getName();
                String newRemote = new String(relativeRemotePath+ ftpFile.getName().toString());
                File fl = new File(newLocalRelatePath);
                if (!fl.exists()){
                    fl.mkdirs();
                }
                try {
                    newLocalRelatePath = newLocalRelatePath + '/';
                    newRemote = newRemote + "/";
                    String currentWorkDir = ftpFile.getName().toString();
                    boolean changeDir = ftpClient.changeWorkingDirectory(currentWorkDir);
                    if (changeDir){
                        FTPFile[] files = null;
                        files = ftpClient.listFiles();
                        for (int i = 0; i < files.length; i++) {
                            downLoadFile(files[i], newLocalRelatePath, newRemote);
                        }
                    }
                    if (changeDir){
                        ftpClient.changeToParentDirectory();
                    }
                }catch (Exception e){
                    log.error(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        String a = "F:\\download\\";
        FtpFileUtil ftpFileUtil = new FtpFileUtil("192.168.1.70", "Administrator", "xueliman", "");
        boolean b = ftpFileUtil.startDown(a, "/mp3");
        System.out.println(b);
    }

    /**
     * 上传文件夹内的所有文件
     * @param fileAddress 本地文件夹绝对路径
     * @param ftpAddress 上传到FTP的路径
     * @return
     */
    public Map<String, Object> uploadManyFile(String fileAddress, String ftpAddress) {

        //初始化
        initFtp();

        boolean flag = true;
        Map<String, Object> result = new HashMap<>();
        StringBuffer strBuf = new StringBuffer();
        // 上传失败的文件个数
        int failCount = 0;
        // 上传成功的文件个数
        int m = 0;
        try {
//            ftpClient.changeWorkingDirectory("/");
            File file = new File(fileAddress);
            File fileList[] = file.listFiles();
            for (File upFile : fileList) {
                if (upFile.isDirectory()) {
                    uploadManyFile(upFile.getAbsoluteFile().toString(), ftpAddress);
                } else {
                    //flag = uploadToFtp(upFile.getName(), fileAddress + File.separator + upFile.getName(), ftpAddress);
//                    ftpClient.changeWorkingDirectory("/");
                }
                if (!flag) {
                    failCount++;
                    strBuf.append(upFile.getName() + ",");
                    log.info("File［" + upFile.getName() + "］upload failed");
                } else {
                    m++;
                }
            }
            result.put("文件上传成功的个数", m);
            result.put("上传失败的个数", failCount);
            result.put("上传失败的文件名", strBuf.toString());
        } catch (NullPointerException e) {
            e.printStackTrace();
            log.error("local file upload failed, the file not found！" + e);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("local file upload failed ！" + e);
        }
        return result;
    }


    /**
     * 文件重命名/文件移动
     * @param oldFile ftp中原文件 路径/文件名
     * @param newFile 新文件 路径/文件名
     * @return
     */
    public boolean renameFile(String oldFile, String newFile){
        boolean flag = false;
        try {
            //初始化
            initFtp();
            ftpClient.changeWorkingDirectory(oldFile.substring(0, oldFile.lastIndexOf("/")));
            ftpClient.rename(oldFile, newFile);
            flag = true;
        } catch (Exception e) {
            Asserts.fail(oldFile + "文件重命名失败");
            e.printStackTrace();
        } finally {
            closeConnect();
        }
        return flag;
    }

    /**
     * 删除ftp文件
     * @param ftpAddress ftp文件的绝对路径目录
     * @param fileName 文件名
     */
    public boolean deleteFile(String ftpAddress, String fileName){
        boolean flag = false;
        try {
            //初始化
            initFtp();
            ftpClient.changeWorkingDirectory(ftpAddress);
            ftpClient.dele(fileName);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnect();
        }
        return flag;
    }

    /**
     * 关闭FTP连接
     */
    private void closeConnect() {
        if (ftpClient != null && ftpClient.isConnected()) {
            try {
                ftpClient.logout();
                ftpClient.disconnect();
            } catch (IOException e) {
                Asserts.fail("关闭FTP连接失败" + e.getMessage());
            }
        }
    }

    /**
     * 获取ftp下地址位置，不存在则新增地址
     * @param client ftp客户端
     * @param storePath ftp目标地址，不包含文件名
     */
    private void prepareStorePath(FTPClient client, String storePath) throws Exception{
        //按目标地址路径分割
        String[] split = storePath.split("/");

        //循环判断路径是否存在
        for (String str : split) {
            if (StringUtils.isBlank(str)){
                continue;
            }

            if (!client.changeWorkingDirectory(str)) {
                client.makeDirectory(str);
                client.changeWorkingDirectory(str);
            }
        }
    }
}
