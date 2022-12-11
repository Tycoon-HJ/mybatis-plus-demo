package org.example.resource;

import org.example.util.UploadByOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author mr.ahai
 */
@RestController
public class UploadResource {
    @PostMapping("/xx")
    public String upload(MultipartFile file) throws IOException {
        return UploadByOssUtil.upload(file);
    }
}
