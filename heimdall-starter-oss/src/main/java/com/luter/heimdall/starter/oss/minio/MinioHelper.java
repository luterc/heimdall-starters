
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

package com.luter.heimdall.starter.oss.minio;


import com.luter.heimdall.starter.oss.config.OSSConfig;
import com.luter.heimdall.starter.utils.exception.LuterException;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
public class MinioHelper {
    private final OSSConfig ossConfig;
    private final MinioClient minioClient;

    /**
     * 列出bucket根目录下所有文件，不会递归查找
     *
     * @return List of items
     */
    public List<Item> list() {
        Iterable<Result<Item>> myObjects = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(ossConfig.getMinio().getBucket())
                        .prefix("")
                        .recursive(false)
                        .build()
        );
        return getItems(myObjects);
    }

    /**
     * 列出bucket根目录下所有文件，会递归查找，谨慎使用
     *
     * @return List of items
     */
    public List<Item> fullList() {
        Iterable<Result<Item>> myObjects = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(ossConfig.getMinio().getBucket()).build()
        );
        return getItems(myObjects);

    }

    /**
     * 列出bucket内指定前缀(目录)下的文件
     * Simulate a folder hierarchy. Objects within folders (i.e. all objects which match the pattern {@code {prefix}/{objectName}/...}) are not returned
     *
     * @param path Prefix of seeked list of object
     * @return List of items
     */
    public List<Item> list(Path path) {
        Iterable<Result<Item>> myObjects = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(ossConfig.getMinio().getBucket())
                .prefix(path.toString()).recursive(false)
                .build());
        return getItems(myObjects);
    }

    /**
     * 列出bucket内指定前缀(目录)下的文件
     * <p>
     * All objects, even those which are in a folder are returned.
     *
     * @param path Prefix of seeked list of object
     * @return List of items
     */
    public List<Item> getFullList(Path path) {
        Iterable<Result<Item>> myObjects = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(ossConfig.getMinio().getBucket()).prefix(path.toString()).build()
        );
        return getItems(myObjects);

    }

    /**
     * 转换查询结果到list
     *
     * @param myObjects Iterable of results
     * @return List of items
     */
    private List<Item> getItems(Iterable<Result<Item>> myObjects) {
        return StreamSupport
                .stream(myObjects.spliterator(), true)
                .map(itemResult -> {
                    try {
                        return itemResult.get();
                    } catch (InvalidBucketNameException
                            | NoSuchAlgorithmException
                            | InsufficientDataException
                            | IOException
                            | InvalidKeyException
                            | XmlParserException
                            | InvalidResponseException
                            | ErrorResponseException
                            | InternalException
                            | ServerException e) {
                        throw new LuterException("Error while parsing list of objects", e);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 读取文件到流
     *
     * @param path Path with prefix to the object. Object name must be included.
     * @return The object as an InputStream
     */
    public InputStream get(Path path) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(ossConfig.getMinio().getBucket())
                            .object(path.toString())
                            .build()
            );
        } catch (XmlParserException
                | InvalidBucketNameException
                | NoSuchAlgorithmException
                | InsufficientDataException
                | IOException
                | InvalidKeyException
                | ErrorResponseException
                | InternalException
                | ServerException
                | InvalidResponseException e) {
            throw new LuterException("Error while fetching files in Minio", e);
        }
    }

    /**
     * 读取文件的metadata信息
     *
     * @param path Path with prefix to the object. Object name must be included.
     * @return Metadata of the  object
     */
    public ObjectStat getMetadata(Path path) {
        try {
            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(ossConfig.getMinio().getBucket())
                            .object(path.toString())
                            .build()
            );
        } catch (
                XmlParserException
                        | InvalidBucketNameException
                        | NoSuchAlgorithmException
                        | InsufficientDataException
                        | IOException
                        | InvalidKeyException
                        | ErrorResponseException
                        | InternalException
                        | ServerException
                        | InvalidResponseException e) {
            throw new LuterException("Error while fetching files in Minio", e);
        }
    }

    /**
     * 批量读取文件的metadata信息
     *
     * @param paths Paths of all objects with prefix. Objects names must be included.
     * @return A map where all paths are keys and metadatas are values
     */
    public Map<Path, ObjectStat> getMetadata(Iterable<Path> paths) {
        return StreamSupport.stream(paths.spliterator(), false)
                .map(path -> {
                    try {
                        return new HashMap.SimpleEntry<>(path, minioClient.statObject(
                                StatObjectArgs.builder()
                                        .bucket(ossConfig.getMinio().getBucket())
                                        .object(path.toString())
                                        .build()
                        ));
                    } catch (XmlParserException
                            | InvalidBucketNameException
                            | NoSuchAlgorithmException
                            | InsufficientDataException
                            | IOException
                            | InvalidKeyException
                            | ErrorResponseException
                            | InternalException
                            | ServerException
                            | InvalidResponseException e) {
                        throw new LuterException("Error while parsing list of objects", e);
                    }
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * 上传文件
     *
     * @param source  Path with prefix to the object. Object name must be included.
     * @param file    File as an inputstream
     * @param headers Additional headers to put on the file.
     *                The map MUST be mutable.
     *                All custom headers will start with 'x-amz-meta-' prefix when fetched with {@code getMetadata()} method.
     */
    public void upload(Path source, InputStream file, Map<String, String> headers) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(ossConfig.getMinio().getBucket())
                    .object(source.toString())
                    .stream(file, file.available(), -1)
                    .headers(headers)
                    .build());
        } catch (XmlParserException
                | InvalidBucketNameException
                | NoSuchAlgorithmException
                | InsufficientDataException
                | IOException
                | InvalidKeyException
                | ErrorResponseException
                | InternalException
                | ServerException
                | InvalidResponseException e) {
            throw new LuterException("Error while fetching files in Minio", e);
        }
    }

    /**
     * 上传文件
     *
     * @param source Path with prefix to the object. Object name must be included.
     * @param file   File as an inputstream
     * @throws LuterException if an error occur while uploading object
     */
    public void upload(Path source, InputStream file) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(ossConfig.getMinio().getBucket())
                    .object(source.toString())
                    .stream(file, file.available(), -1)
                    .build());
        } catch (XmlParserException
                | InvalidBucketNameException
                | NoSuchAlgorithmException
                | InsufficientDataException
                | IOException
                | InvalidKeyException
                | ErrorResponseException
                | InternalException
                | ServerException
                | InvalidResponseException e) {
            throw new LuterException("Error while fetching files in Minio", e);
        }
    }

    /**
     * 上传文件
     *
     * @param source      Path with prefix to the object. Object name must be included.
     * @param file        File as an inputstream
     * @param contentType MIME type for the object
     * @param headers     Additional headers to put on the file. The map MUST be mutable
     */
    public void upload(Path source, InputStream file, String contentType, Map<String, String> headers) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(ossConfig.getMinio().getBucket())
                    .object(source.toString())
                    .headers(headers)
                    .stream(file, file.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (XmlParserException
                | InvalidBucketNameException
                | NoSuchAlgorithmException
                | InsufficientDataException
                | IOException
                | InvalidKeyException
                | ErrorResponseException
                | InternalException
                | ServerException
                | InvalidResponseException e) {
            throw new LuterException("Error while fetching files in Minio", e);
        }
    }

    /**
     * 上传文件
     *
     * @param source      Path with prefix to the object. Object name must be included.
     * @param file        File as an inputstream
     * @param contentType MIME type for the object
     * @throws LuterException if an error occur while uploading object
     */
    public void upload(Path source, InputStream file, String contentType) {

        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(ossConfig.getMinio().getBucket())
                    .object(source.toString())
                    .stream(file, file.available(), -1)
                    .contentType(contentType)
                    .build());
        } catch (
                XmlParserException
                        | InvalidBucketNameException
                        | NoSuchAlgorithmException
                        | InsufficientDataException
                        | IOException
                        | InvalidKeyException
                        | ErrorResponseException
                        | InternalException
                        | ServerException
                        | InvalidResponseException e
        ) {
            throw new LuterException("Error while fetching files in Minio", e);
        }
    }


    /**
     * 删除文件
     *
     * @param source Path with prefix to the object. Object name must be included.
     * @throws LuterException if an error occur while removing object
     */
    public void remove(Path source) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(ossConfig.getMinio().getBucket())
                            .object(source.toString())
                            .build()
            );
        } catch (XmlParserException
                | InvalidBucketNameException
                | NoSuchAlgorithmException
                | InsufficientDataException
                | IOException
                | InvalidKeyException
                | ErrorResponseException
                | InternalException
                | ServerException
                | InvalidResponseException e) {
            throw new LuterException("Error while fetching files in Minio", e);
        }
    }

}
