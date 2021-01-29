/*
 *
 *  *
 *  *  *    Copyright 2020-2021 Luter.me
 *  *  *
 *  *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *    you may not use this file except in compliance with the License.
 *  *  *    You may obtain a copy of the License at
 *  *  *
 *  *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *  *
 *  *  *    Unless required by applicable law or agreed to in writing, software
 *  *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *    See the License for the specific language governing permissions and
 *  *  *    limitations under the License.
 *  *
 *
 */

package com.luter.heimdall.starter.model.tree;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class TreeExtendDTO implements Serializable {
    private Long id;
    private String title;
    private String icon;
    private String uri;
    private String path;
    private String component;
    private String module;
    private Boolean leaf;
    private Boolean affix;
    private String target;

    private Boolean keepAlive;
    private String qtip;
    private Long pid;
    private boolean expand = true;
    List<TreeExtendDTO> children = new ArrayList<>();
    private boolean selected;
    private boolean checked;

}
