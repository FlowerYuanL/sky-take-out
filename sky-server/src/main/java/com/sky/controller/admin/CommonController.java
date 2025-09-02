package com.sky.controller.admin;

import com.sky.config.OSSConfiguration;
import com.sky.constant.MessageConstant;
import com.sky.properties.AliOssProperties;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Api(tags = "通用接口接口")
@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;


    /**
     * 文件上传的接口
     * @param file 上传的文件
     * @return 返回上传到阿里云OSS的路径
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public Result<String> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            InputStream inputStream = file.getInputStream();
            String originalFileName = file.getOriginalFilename();
            //获取当前系统日期的字符串，格式为yyyy/MM
            String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/"));
            //使用UUID生成一个新的文件名（不包含扩展名的部分）
            String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
            String objectName = dir + newFileName;
            String url = aliOssUtil.upload(inputStream, objectName);
            return Result.success(url);
        } catch (IOException e) {
            log.error("文件上传失败:{}",e.getMessage(),e);
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

}