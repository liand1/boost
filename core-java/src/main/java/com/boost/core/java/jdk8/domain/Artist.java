package com.boost.core.java.jdk8.domain;

import lombok.Data;
import java.util.List;

@Data
public class Artist {

    private String name;
    // 乐队成员
    private List<String> mmbers;
    // 乐队来自哪里
    private String origin;
}
