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
public class TreeDTO implements Serializable {
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
    List<TreeDTO> children = new ArrayList<>();


}
