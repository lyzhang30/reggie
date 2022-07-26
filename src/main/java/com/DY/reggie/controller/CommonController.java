package com.DY.reggie.controller;

import com.DY.reggie.common.CustomException;
import com.DY.reggie.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * 通过控制类
 *@author zhanglianyong
 *@date 2022/8/5
 */
@RestController
@Slf4j
@RequestMapping("/common")
@Api("通用控制类")
public class CommonController {
    /**
     * 静态图片保存路径
     */
    @Value("${reggie.path}")
    private String basePath;

    /**
     * 文件上传方法
     *
     * @author zhanglianyong
     * @date 2022/8/5 20:06
     * @param file  文件
     * @return 返回是否成功
     **/
    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public R<String> upload(MultipartFile file) {
        // file是一个临时文件
        log.info(file.toString());

        // 获取原文件名
        String originalFilename = file.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            throw new CustomException("获取不到原图片路径");
        }
        String suffix = originalFilename.substring(originalFilename.lastIndexOf('.'));

        // 生成UUID，防止文件名称重复文件覆盖
        String fileName = UUID.randomUUID() + suffix;

        // 创建一个目录
        File dir = new File(basePath);
        if (!dir.exists()) {
            // 创建目录
            dir.mkdirs();
        }

        try {
            // 将文件转存到指定位置
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 下载文件
     *
     * @author zhanglianyong
     * @date 2022/8/5 20:08
     * @param name 文件名
     * @param response  返回流
     **/
    @GetMapping("/download")
    @ApiOperation("下载文件")
    public void download(@ApiParam("文件名") String name, HttpServletResponse response) {
        try {
            // 输入流，通过输入流读取文件
            FileInputStream fileInputStream = new FileInputStream(basePath + name);
            // 输出流，通过输出流将文件写回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            // 关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 下载文件
     *
     * @author zhanglianyong
     * @date 2022/8/5 20:10
     * @param file  文件对象
     * @return 返回一个下载的实体
     **/
    private ResponseEntity<FileSystemResource> export(File file) {
        if (file == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + file.getName());
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(new FileSystemResource(file));
    }

}
