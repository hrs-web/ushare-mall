package com.youxiang.upload.service.impl;

import com.youxiang.upload.controller.UploadController;
import com.youxiang.upload.service.UploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadServiceImpl implements UploadService {
    // 定义支持的文件类型
    private static final List<String> CONTENT_TYPE = Arrays.asList("image/gif","image/jpeg","image/png");
    // 创建日志对象
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);
    @Override
    public String upload(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            // 1.效验文件类型
            String contentType = file.getContentType();
            if (!CONTENT_TYPE.contains(contentType)){
                LOGGER.info("文件上传失败：{}，文件类型不合法。",filename); // 拼接字符串效率低，logger提供占位符
                return null;
            }
            // 2.效验文件内容,ImageIO.read可以读取文件为图片的内容，如果内容不是图片则返回null
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage==null){
                LOGGER.info("文件上传失败：{}，文件内容不合法。",filename);
                return null;
            }
            // 3.保存图片
            File dir = new File("C:\\Download\\test");
            if (!dir.exists()){
                dir.mkdirs();
            }
            file.transferTo(new File(dir,filename));
            String url = "http://image.youxiang.com/"+filename;
            return url;
        }catch (Exception e){
            return null;
        }

    }
}
