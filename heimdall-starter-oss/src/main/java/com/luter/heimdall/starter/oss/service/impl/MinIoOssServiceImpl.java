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
import com.luter.heimdall.starter.utils.exception.LuterIllegalParameterException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
public class MinIoOssServiceImpl extends AbstractOssServiceImpl implements OssService {
    private final OSSConfig config;
    private final MinioClient minioClient;

    public MinIoOssServiceImpl(OSSConfig config, MinioClient minioClient) {
        super(config);
        this.config = config;
        this.minioClient = minioClient;
    }

    @Override
    @SneakyThrows
    public OssDTO uploadImage(MultipartFile mFile, String folderName) {
        OssFileInfo fileInfo = isValidImage(mFile);
        InputStream inputStream = mFile.getInputStream();
        minioClient.putObject(
                PutObjectArgs.builder().bucket(config.getMinio().getBucket())
                        .object(folderName + "/" + fileInfo.getName()).stream(
                        inputStream, inputStream.available(), -1)
                        .contentType(fileInfo.getMimeType())
                        .build());
        inputStream.close();
        return new OssDTO()
                .setName(fileInfo.getName())
                .setSize(mFile.getSize())
                .setBucket(folderName)
                .setMimeType(fileInfo.getMimeType())
                .setPath("/" + folderName + "/" + fileInfo.getName())
                .setUri(minioClient.getObjectUrl(config.getMinio().getBucket(), folderName + "/" + fileInfo.getName()));

    }

    @Override
    @SneakyThrows
    public OssDTO uploadImage(MultipartFile mFile, String folderName, int width, int height, boolean keepAspectRatio) {
        if (width < 1 || height < 1) {
            throw new LuterIllegalParameterException("图片缩放宽、高错误，必须大于1");
        }
        OssFileInfo fileInfo = isValidImage(mFile);
        File tempFile = File.createTempFile("temp", fileInfo.getName());
        Thumbnails.of(mFile.getInputStream())
                .size(width, height)
                .keepAspectRatio(keepAspectRatio)
                .toFile(tempFile);
        FileInputStream fileInputStream = new FileInputStream(tempFile);
        minioClient.putObject(
                PutObjectArgs.builder().bucket(config.getMinio().getBucket())
                        .object(folderName + "/" + fileInfo.getName()).stream(
                        fileInputStream, fileInputStream.available(), -1)
                        .contentType(fileInfo.getMimeType())
                        .build());
        fileInputStream.close();
        final OssDTO ossDTO = new OssDTO()
                .setName(fileInfo.getName())
                .setSize(tempFile.length())
                .setBucket(folderName)
                .setMimeType(fileInfo.getMimeType())
                .setPath("/" + folderName + "/" + fileInfo.getName())
                .setUri(minioClient.getObjectUrl(config.getMinio().getBucket(), folderName + "/" + fileInfo.getName()));
        //删除临时文件
        tempFile.delete();
        return ossDTO;

    }

    @Override
    public OssDTO uploadImage(MultipartFile mFile, String folderName, int w, int h, int x, int y, boolean keepAspectRatio) {
        return null;
    }

    @Override
    @SneakyThrows
    public OssDTO uploadFile(MultipartFile mFile, String folderName) {
        OssFileInfo fileInfo = isValidFile(mFile);
        return uploadOneFile(mFile, folderName, fileInfo.getName());
    }

    /**
     * 上传文件
     *
     * @param mFile      文件对象
     * @param folderName 桶的名字
     * @param fileName   文件名称
     */
    @SneakyThrows
    private OssDTO uploadOneFile(MultipartFile mFile, String folderName, String fileName) {
        InputStream inputStream = mFile.getInputStream();
        final OssFileInfo fileInfo = isValidFile(mFile);
        minioClient.putObject(
                PutObjectArgs.builder().bucket(config.getMinio().getBucket())
                        .object(folderName + "/" + fileName).stream(
                        inputStream, inputStream.available(), -1)
                        .contentType(fileInfo.getMimeType())
                        .build());
        inputStream.close();
        return new OssDTO()
                .setName(fileName)
                .setSize(mFile.getSize())
                .setMimeType(fileInfo.getMimeType())
                .setBucket(config.getMinio().getBucket())
                .setPath("/" + folderName + "/" + fileName)
                .setUri(minioClient.getObjectUrl(config.getMinio().getBucket(), folderName + "/" + fileInfo.getName()));
    }

    @SneakyThrows
    private OssDTO uploadOneFile(InputStream inputStream, String folderName, String fileName) {
        Tika tika = new Tika();
        String detectedType = tika.detect(inputStream);
        minioClient.putObject(
                PutObjectArgs.builder().bucket(config.getMinio().getBucket())
                        .object(folderName + "/" + fileName).stream(
                        inputStream, inputStream.available(), -1)
                        .contentType(detectedType)
                        .build());
        inputStream.close();
        return new OssDTO()
                .setName(fileName)
                .setSize((long) inputStream.available())
                .setBucket(config.getMinio().getBucket())
                .setMimeType(detectedType)
                .setPath("/" + folderName + "/" + fileName)
                .setUri(minioClient.getObjectUrl(config.getMinio().getBucket(), folderName + "/" + fileName));
    }

    @Override
    public List<OssDTO> uploadFile(MultipartFile[] mFiles, String folderName) {
        return null;
    }


}
