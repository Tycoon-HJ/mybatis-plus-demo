package org.example.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * oss上传
 * @author mr.ahai
 */
public class UploadByOssUtil {
    
    private static String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
    private static String accessKeyId = "LTAI5tE1thyNdeMSnuBcHr1j";
    private static String accessKeySecret = "5MVlHyZjPs8mrB7KWiIzgs0n2D0dt4";
    private static String bucketName = "yhj980325";
    
    public static String upload(MultipartFile file) throws IOException {
        /*
         * Constructs a client instance with your account for accessing OSS
         */
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName,file.getName()+".png",file.getInputStream());
        ossClient.shutdown();
        return "上传成功";
    }

}