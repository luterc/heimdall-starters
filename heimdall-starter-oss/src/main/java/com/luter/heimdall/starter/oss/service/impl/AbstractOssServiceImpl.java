/*
 *
 *  *
 *  *
 *  *      Copyright 2020-2021 Luter.me
 *  *
 *  *      Licensed under the Apache License, Version 2.0 (the "License");
 *  *      you may not use this file except in compliance with the License.
 *  *      You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *      Unless required by applicable law or agreed to in writing, software
 *  *      distributed under the License is distributed on an "AS IS" BASIS,
 *  *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *      See the License for the specific language governing permissions and
 *  *      limitations under the License.
 *  *
 *  *
 *
 */

package com.luter.heimdall.starter.oss.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.luter.heimdall.starter.oss.dto.OssFileInfo;
import com.luter.heimdall.starter.oss.config.OSSConfig;
import com.luter.heimdall.starter.utils.exception.LuterException;
import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class AbstractOssServiceImpl {
    private final OSSConfig ossConfig;

    protected AbstractOssServiceImpl(OSSConfig ossConfig) {
        this.ossConfig = ossConfig;
    }


    /**
     * 验证图片是否合法
     *
     * @param mFile 上传文件对象
     * @return 返回上传文件的新文件名
     */
    @SneakyThrows
    public OssFileInfo isValidImage(MultipartFile mFile) {
        if (null == mFile || mFile.isEmpty()) {
            throw new LuterException("请选择上传文件并确保参数正确");
        }
        //TIKA文件类型检测
        Tika tika = new Tika();
        String detectedType = tika.detect(mFile.getBytes());
        if (!detectedType.startsWith("image")) {
            throw new LuterException("请上传正确的图片文件.虽然看起来文件名像图片文件,但其实不是，它是这个类型的:" + detectedType);
        }
        long size = mFile.getSize();
        if (size > ossConfig.getImageMaxSize()) {
            throw new LuterException("文件大小不能超过:" + FileUtil.readableFileSize(ossConfig.getImageMaxSize()));
        }
        //校验通过，返回新文件的信息
        return new OssFileInfo().setName(randomFileName() + "." + detectedType.replace("image/", ""))
                .setMimeType(detectedType);
    }

    /**
     * 验证文件是否合法
     *
     * @param mFile 上传文件对象
     * @return 返回上传文件的新文件名
     */
    @SneakyThrows
    public OssFileInfo isValidFile(MultipartFile mFile) {
        if (null == mFile || mFile.isEmpty()) {
            throw new LuterException("请选择上传文件并确保参数正确");
        }
        String fileName = mFile.getOriginalFilename();
        if (StrUtil.isEmpty(fileName)) {
            throw new LuterException("上传文件名称不能为空");
        }
        long size = mFile.getSize();
        if (size > ossConfig.getFileMaxSize()) {
            throw new LuterException("文件大小不能超过:" + FileUtil.readableFileSize(ossConfig.getImageMaxSize()));
        }
        //TIKA文件类型检测
        Tika tika = new Tika();
        String detectedType = tika.detect(mFile.getBytes());
        //校验通过，返回新文件名称
        return new OssFileInfo().setName(fileName).setMimeType(detectedType);
    }


    /**
     * 按照当前时间戳重命名文件
     * @param name  原文件名
     */
    public static String rename(String name) {
        long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()));
        long random = (long) (Math.random() * now);
        String fileName = now + "" + random;
        if (name.contains(".")) {
            fileName += name.substring(name.lastIndexOf("."));
        }
        return fileName;
    }

    /**
     * 产生年月日字符串，如:20190101,用作目录名
     */
    public String getFolderName() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(new Date());
    }


    /**
     * 使用时间戳加随机数产生文件名
     *
     * @return 随机字符串
     */
    public static String randomFileName() {
        long now = Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()));
        long random = (long) (Math.random() * now);
        return now + "" + random;
    }
}
