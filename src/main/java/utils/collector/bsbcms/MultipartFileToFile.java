package utils.collector.bsbcms;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author lk
 * @date 2021/10/15
 * 文件转化
 */
public class MultipartFileToFile {

    public static File multipartFileToFile(MultipartFile multipartFile) throws Exception{
        File toFile = null;
        if (multipartFile.equals("") || multipartFile.getSize() <= 0){
            multipartFile = null;
        }else {
            InputStream ins = null;
            ins = multipartFile.getInputStream();
            toFile = new File(multipartFile.getOriginalFilename());
            inputStreamToFile(ins, toFile);
            ins.close();
        }
        return toFile;
    }

    /**
     * 获取流文件
     * @param ins
     * @param toFile
     */
    private static void inputStreamToFile(InputStream ins, File toFile) {
        try {
            OutputStream os = new FileOutputStream(toFile);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0 , 8192)) != -1){
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 删除本地临时文件
     * @param file
     */
    public static void deleteTempFile(File file){
        if (file != null){
            File del = new File(file.toURI());
            del.delete();
        }
    }

}
