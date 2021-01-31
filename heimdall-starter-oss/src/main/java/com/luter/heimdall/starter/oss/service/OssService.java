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

package com.luter.heimdall.starter.oss.service;

import com.luter.heimdall.starter.oss.dto.OssDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * The interface Oss service.
 */
public interface OssService {

    /**
     * 上传图片
     *
     * @param mFile      图片文件
     * @param folderName 保存的目录(桶)名称
     * @return oss dto
     */
    OssDTO uploadImage(MultipartFile mFile, String folderName);

    /**
     * 上传图片，支持缩放
     *
     * <p>
     * <p>
     * <p>
     * <p>
     * <p>size(width,height) 若图片横比width小，高比height小，不变
     * <p>若图片横比width小，高比height大，高缩小到height，图片比例不变 若图片横比width大，高比height小，横缩小到width，图片比例不变
     * <p>若图片横比width大，高比height大，图片按比例缩小，横为width或高为height
     *
     * @param mFile           图片文件
     * @param folderName      保存的目录(桶)名称
     * @param width           缩放到宽度
     * @param height          缩放到高度
     * @param keepAspectRatio 是否保持宽高比例
     * @return oss dto
     */
    OssDTO uploadImage(MultipartFile mFile, String folderName, int width, int height, boolean keepAspectRatio);


    /**
     * 上传图片，支持剪裁和缩放图片
     *
     * @param mFile           图片文件
     * @param folderName      the place name
     * @param w               宽
     * @param h               高
     * @param x               剪裁的X点坐标，从这个点往右W的部分
     * @param y               剪裁的Y点坐标，从这个点往下H的部分
     * @param keepAspectRatio 是否保持宽高比
     * @return 图片url oss dto
     */
    OssDTO uploadImage(MultipartFile mFile, String folderName, int w, int h, int x, int y, boolean keepAspectRatio);


    /**
     * 上传文件
     *
     * @param mFile      文件
     * @param folderName 保存的目录(桶),如果为空，则存储到默认位置:luter.oss.default-folder
     */
    OssDTO uploadFile(MultipartFile mFile, String folderName);


    /**
     * 批量上传文件
     *
     * @param mFiles     文件
     * @param folderName 保存的目录(桶)
     */
    List<OssDTO> uploadFile(MultipartFile[] mFiles, String folderName);


}
