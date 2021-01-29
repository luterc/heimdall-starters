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

package com.luter.heimdall.starter.utils.ipregion;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;

import java.io.File;
import java.io.InputStream;

@Slf4j
public final class Ip2RegionUtil {
    private static final String JAVA_TEMP_DIR = "java.io.tmpdir";
    static DbConfig config = null;
    static DbSearcher searcher = null;

    public static void main(String[] args) {
        System.out.println(getIpRegionInfo("49.2321.13.109"));
    }

    static {
        try {
            String dbPath = Ip2RegionUtil.class.getResource("/db/ip2region.db").getPath();
            File file = new File(dbPath);
            if (!file.exists()) {
                String tmpDir = System.getProperties().getProperty(JAVA_TEMP_DIR);
                dbPath = tmpDir + "ip2region.db";
                file = new File(dbPath);
                String classPath = "classpath:db/ip2region.db";
                InputStream resourceAsStream = ResourceUtil.getStreamSafe(classPath);
                if (resourceAsStream != null) {
                    FileUtil.writeFromStream(resourceAsStream, file);
                }
            }
            config = new DbConfig();
            searcher = new DbSearcher(config, dbPath);
        } catch (Exception e) {
            log.error("init ip region error:" + e.getMessage());
        }

    }

    public static String getIpRegionInfo(String ip) {
        try {
            if (searcher != null && !StrUtil.isEmpty(ip)) {
//                if (!Util.isIpAddress(ip)) {
//                    log.error("warning: Invalid ip address:" + ip);
//                    return "";
//                }
                return searcher.memorySearch(ip).getRegion();
            } else {
                log.error("DbSearcher is null:" + ip);
                return "";
            }
        } catch (Exception e) {
            log.error("error:" + e.getMessage());
            return "";
        }
    }

}
