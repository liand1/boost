package com.boost.core.java.jdk8.domain;

import lombok.Data;

import java.util.List;

/**
 * 专辑，由若干曲目组成。
 */
@Data
public class Album {
    private String name;
    private List<Track> tracks;
    private Artist musician;
}
