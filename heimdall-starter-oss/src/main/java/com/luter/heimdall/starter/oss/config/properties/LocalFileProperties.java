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

package com.luter.heimdall.starter.oss.config.properties;

import lombok.Data;

@Data
public class LocalFileProperties {
    /**
     * 上传文件保存的根目录，绝对路径，默认jar 所在目录:System.getProperty("user.dir") + "/macaw"
     */
    //System.getProperty("user.dir") 这种方式在哪里执行java -jar命令，目录就在哪里
    private String savePath = System.getProperty("user.dir") + "/heimdall";
    //jar包在哪个目录，就在哪里，如果是开发环境，则直接在target/classes下
//    private String savePath = new ApplicationHome(this.getClass()).getDir().getAbsolutePath() + "/files";
    /**
     * 前端访问静态文件的url前缀,不能与系统中controller的路径冲突
     * <p>
     * eg:  http://主机地址:服务端口/{url-prefix}/xxx/bbb.jpg
     */
    private String urlPrefix = "uploads";


}
