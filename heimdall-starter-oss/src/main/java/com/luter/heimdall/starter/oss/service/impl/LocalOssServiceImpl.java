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


import com.luter.heimdall.starter.oss.dto.OssDTO;
import com.luter.heimdall.starter.oss.dto.OssFileInfo;
import com.luter.heimdall.starter.oss.service.OssService;
import com.luter.heimdall.starter.oss.config.OSSConfig;
import com.luter.heimdall.starter.utils.exception.LuterException;
import com.luter.heimdall.starter.utils.exception.LuterIllegalParameterException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LocalOssServiceImpl extends AbstractOssServiceImpl implements OssService {
    private final OSSConfig config;

    public LocalOssServiceImpl(OSSConfig config) {
        super(config);
        this.config = config;
    }

    @Override
    @SneakyThrows
    public OssDTO uploadImage(MultipartFile mFile, String folderName) {
        OssFileInfo fileInfo = isValidFile(mFile);
        File storeFolder = new File(config.getLocal().getSavePath() + File.separator + folderName);
        if (!storeFolder.exists()) {
            storeFolder.mkdirs();
        }
        File dest = new File(config.getLocal().getSavePath()
                + File.separator + folderName + File.separator + fileInfo.getName());
        String url = config.getLocal().getUrlPrefix() + "/" + folderName + "/" + fileInfo.getName();
        mFile.transferTo(dest);
        return new OssDTO()
                .setUri(url)
                .setName(fileInfo.getName())
                .setBucket(folderName)
                .setMimeType(fileInfo.getMimeType())
                .setPath(File.separator + folderName + File.separator + fileInfo.getName()).setSize(dest.length());

    }

    @Override
    @SneakyThrows
    public OssDTO uploadImage(MultipartFile mFile, String folderName, int width, int height, boolean keepAspectRatio) {
        if (width < 1 || height < 1) {
            throw new LuterIllegalParameterException("图片缩放尺寸错误，必须大于1");
        }
        OssFileInfo fileInfo = isValidImage(mFile);
        File storeFolder = new File(config.getLocal().getSavePath() + File.separator + folderName);
        if (!storeFolder.exists()) {
            storeFolder.mkdirs();
        }
        File dest = new File(config.getLocal().getSavePath()
                + File.separator + folderName + File.separator + fileInfo.getName());
        String url = config.getLocal().getUrlPrefix() + "/" + folderName + "/" + fileInfo.getName();
        FileOutputStream os = new FileOutputStream(dest);
        Thumbnails.of(mFile.getInputStream())
                .size(width, height)
                .keepAspectRatio(keepAspectRatio)
                .toOutputStream(os);
        return new OssDTO()
                .setUri(url)
                .setName(fileInfo.getName())
                .setBucket(folderName)
                .setMimeType(fileInfo.getMimeType())
                .setPath(File.separator + folderName + File.separator + fileInfo.getName()).setSize(dest.length());

    }


    @Override
    @SneakyThrows
    public OssDTO uploadImage(MultipartFile mFile, String folderName, int w, int h, int x, int y, boolean keepAspectRatio) {
        if (w < 1 || h < 1 || x < 1 || y < 1) {
            throw new LuterIllegalParameterException("图片缩放尺寸参数错误，必须大于1");
        }
        OssFileInfo fileInfo = isValidImage(mFile);
        File storeFolder = new File(config.getLocal().getSavePath() + File.separator + folderName);
        if (!storeFolder.exists()) {
            storeFolder.mkdirs();
        }
        File dest = new File(config.getLocal().getSavePath()
                + File.separator + folderName + File.separator + fileInfo.getName());
        String url = config.getLocal().getUrlPrefix() + "/" + folderName + "/" + fileInfo.getName();
        FileOutputStream os = new FileOutputStream(dest);
        Thumbnails.of(mFile.getInputStream())
                .sourceRegion(x, y, w, h)
                .size(w, h)
                .keepAspectRatio(keepAspectRatio)
                .toOutputStream(os);
        return new OssDTO()
                .setUri(url)
                .setName(fileInfo.getName())
                .setBucket(folderName)
                .setMimeType(fileInfo.getMimeType())
                .setPath(File.separator + folderName + File.separator + fileInfo.getName()).setSize(dest.length());

    }


    @Override
    @SneakyThrows
    public OssDTO uploadFile(MultipartFile mFile, String folderName) {
        File storeFolder = new File(config.getLocal().getSavePath() + File.separator + folderName);
        if (!storeFolder.exists()) {
            storeFolder.mkdirs();
        }
        OssFileInfo fileInfo = isValidFile(mFile);
        File dest = new File(config.getLocal().getSavePath()
                + File.separator + folderName + File.separator + fileInfo.getName());
        String url = config.getLocal().getUrlPrefix() + "/" + folderName + "/" + fileInfo.getName();
        mFile.transferTo(dest);
        return new OssDTO()
                .setUri(url)
                .setName(fileInfo.getName())
                .setBucket(folderName)
                .setMimeType(fileInfo.getMimeType())
                .setPath(File.separator + folderName + File.separator + fileInfo.getName()).setSize(dest.length());

    }


    @Override
    public List<OssDTO> uploadFile(MultipartFile[] mFiles, String folderName) {
        if (null == mFiles || mFiles.length <= 0) {
            throw new LuterException("上传失败，请选择文件");
        }
        List<OssDTO> result = new ArrayList<>();
        for (MultipartFile mFile : mFiles) {
            result.add(uploadFile(mFile, folderName));
        }
        return result;
    }


}
