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

package com.luter.heimdall.starter.oss.dto;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 上传文件信息DTO
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "上传文件信息DTO")
public class OssDTO {
    /**
     * 文件大小
     */
    @ApiModelProperty("文件大小")
    private Long size = 0L;
    /**
     * 文件大小文字，用作显示
     */
    @ApiModelProperty("文件大小文字,会进行单位转换")
    private String sizeStr;
    /**
     * 文件最终存储的名称
     */
    @ApiModelProperty("文件最终存储的名称")
    private String name;
    /**
     * 文件MimeType类型，通过TIKA检测获得
     */
    @ApiModelProperty("文件MimeType类型")
    private String mimeType;
    /**
     * 资源访问url，可通过此url访问资源
     * 如果是本项目服务，可通过本项目运行ip:port+uri访问资源
     * 如果是第三方存储，则是访问完整路径
     */
    @ApiModelProperty(value = "资源访问url", notes = "如果是本项目服务，可通过本项目运行ip:port+uri访问资源" +
            "如果是第三方存储，则是访问完整路径")
    private String uri;
    /**
     * 文件存储path，可作为Url的后半部分进行资源访问，第三方存储此信息即可。
     * 文件访问可通过如下形式：http://ip:port/{url-prefix}/{path}
     */
    @ApiModelProperty(value = "文件存储path", notes = "可作为Url的后半部分进行资源访问，" +
            "第三方存储此信息即可。文件访问可通过如下形式：http://ip:port/{url-prefix}/{path}")
    private String path;
    /**
     * minio的桶名称
     */
    @ApiModelProperty(value = "minio的桶名称", notes = "只有minio的时候配置")
    private String bucket;
    /**
     * 返回消息
     */
    @ApiModelProperty(value = "成功或者失败的提示信息")
    private String msg;

    public String getSizeStr() {
        return FileUtil.readableFileSize(this.size);
    }

}
