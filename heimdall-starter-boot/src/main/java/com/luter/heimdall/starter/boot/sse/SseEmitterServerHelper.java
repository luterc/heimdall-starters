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

package com.luter.heimdall.starter.boot.sse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Slf4j
public class SseEmitterServerHelper {
    private static final AtomicInteger count = new AtomicInteger(0);

    private static final Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    public static SseEmitter connect(String userId) {
        // 设置超时时间，0表示不过期。默认30秒，超过时间未完成会抛出异常：AsyncRequestTimeoutException
        SseEmitter sseEmitter = new SseEmitter(0L);
        // 注册回调
        sseEmitter.onCompletion(completionCallBack(userId));
        sseEmitter.onError(errorCallBack(userId));
        sseEmitter.onTimeout(timeoutCallBack(userId));
        sseEmitterMap.put(userId, sseEmitter);
        // 数量+1
        count.getAndIncrement();
        log.info("创建新的sse连接，当前客户端ID：{}", userId);
        return sseEmitter;
    }

    public static void sendMessage(String userId, String message) {
        if (sseEmitterMap.containsKey(userId)) {
            try {
                sseEmitterMap.get(userId).send(message);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", userId, e.getMessage());
                removeUser(userId);
            }
        }
    }

    public static void batchSendMessage(String wsInfo, List<String> ids) {
        ids.forEach(userId -> sendMessage(wsInfo, userId));
    }

    public static void batchSendMessage(String wsInfo) {
        sseEmitterMap.forEach((k, v) -> {
            try {
                v.send(wsInfo, MediaType.APPLICATION_JSON);
                log.info("客户端消息推送:{}", wsInfo);
            } catch (IOException e) {
                log.error("用户[{}]推送异常:{}", k, e.getMessage());
                removeUser(k);
            }
        });
    }

    public static void removeUser(String userId) {
        sseEmitterMap.remove(userId);
        // 数量-1
        count.getAndDecrement();
        log.info("移除用户：{}", userId);
    }

    public static List<String> getIds() {
        return new ArrayList<>(sseEmitterMap.keySet());
    }

    public static int getUserCount() {
        return count.intValue();
    }

    private static Runnable completionCallBack(String userId) {
        return () -> {
            log.info("结束连接：{}", userId);
            removeUser(userId);
        };
    }

    private static Runnable timeoutCallBack(String userId) {
        return () -> {
            log.info("连接超时：{}", userId);
            removeUser(userId);
        };
    }

    private static Consumer<Throwable> errorCallBack(String userId) {
        return throwable -> {
            log.info("连接异常：{}", userId);
            removeUser(userId);
        };
    }

}
