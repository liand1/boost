package com.boost.core.java.jdk8.stream;

import com.boost.core.java.jdk8.domain.Album;
import com.boost.core.java.jdk8.domain.Artist;
import com.boost.core.java.jdk8.domain.StringCollector;
import com.boost.core.java.jdk8.domain.StringCombiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Case12 {

    // 使用 for 循环格式化艺术家姓名
    private void origin_1() {
        List<Artist> artists = new ArrayList<>();
        StringBuilder builder = new StringBuilder("[");

        for (Artist artist : artists) {
            if(builder.length()>1) {
                builder.append(",");
            }
            builder.append(artist.getName());
        }
        builder.append("]");
        System.out.println(builder.toString());
    }

    private void refactor_1() {
        List<Artist> artists = new ArrayList<>();
        artists.stream().map(Artist::getName).collect(Collectors.joining(",", "[", "]"));
    }

    // 计算一个艺术家的专辑数量
    private void origin_2() {
        List<Album> albums = new ArrayList<>();
        Map<Artist, List<Album>> albumsByArtist = albums.stream().collect(groupingBy(Album::getMusician));

        Map<Artist, Integer> numberOfAlbums = new HashMap<>();
        for(Map.Entry<Artist, List<Album>> entry : albumsByArtist.entrySet()) {
            numberOfAlbums.put(entry.getKey(), entry.getValue().size());
        }
    }

    private void refactor_2() {
        List<Album> albums = new ArrayList<>();
        Map<Artist, Long> collect = albums.stream().collect(groupingBy(Album::getMusician, counting()));
    }

    // 求每个艺术家的专辑名
    private void origin_3() {
        List<Album> albums = new ArrayList<>();
        Map<Artist, List<Album>> albumsByArtist =
                albums.stream().collect(groupingBy(Album::getMusician));
        Map<Artist, List<String>> nameOfAlbums = new HashMap<>();
        for(Map.Entry<Artist, List<Album>> entry : albumsByArtist.entrySet()) {
            nameOfAlbums.put(entry.getKey(), entry.getValue()
                    .stream()
                    .map(Album::getName)
                    .collect(toList()));
        }
    }

    private void refactor_3() {
        List<Album> albums = new ArrayList<>();
        Map<Artist, List<String>> artistListMap = albums.stream().collect(groupingBy(Album::getMusician, mapping(Album::getName, toList())));
    }


    /**
     * 使用 stream 格式化艺术家姓名
     * 首先创建一
     * 个 StringBuilder 对象，该对象是 reduce 操作的初始状态，然后使用 Lambda 表达式将
     * 姓名连接到 builder 上。reduce 操作的第三个参数也是一个 Lambda 表达式，接受两个
     * StringBuilder 对象做参数，将两者连接起来。最后添加前缀和后缀。
     */
    private void origin_4() {
        List<Artist> artists = new ArrayList<>();
        StringBuilder reduced =
                artists.stream()
                        .map(Artist::getName)
                        .reduce(new StringBuilder(), (builder, name) -> {
                            if (builder.length() > 0)
                                builder.append(", ");
                            builder.append(name);
                            return builder;
                        }, (left, right) -> left.append(right));
        reduced.insert(0, "[");
        reduced.append("]");
        String result = reduced.toString();
    }

    private void refactor_4() {
        List<Artist> artists = new ArrayList<>();
        StringCombiner combined =
                artists.stream()
                        .map(Artist::getName)
                        .reduce(new StringCombiner(", ", "[", "]"),
                                StringCombiner::add,
                                StringCombiner::merge);
        String result = combined.toString();
    }

    private void refactor_4_2() {
        List<Artist> artists = new ArrayList<>();

        String result = artists.stream()
                .map(Artist::getName)
                .collect(new StringCollector(", ", "[", "]"));
    }

}
