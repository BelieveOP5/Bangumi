package com.wen.bangumi.entity.bangumi;

import java.util.List;

/**
 * 存储从http://bangumi.tv/subject/147068/ep网页中获取到的章节信息
 * Created by BelieveOP5 on 2017/2/1.
 */

public class EpisodesEntity {

    /**
     * type:本篇还是番外等、
     * episodeList:章节列表
     */

    private String type;
    private List<Episode> episodeList;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Episode> getEpisodeList() {
        return episodeList;
    }

    public void setEpisodeList(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public static class Episode {

        /**
         * 一集的信息
         * id:该章节在Bangumi中对应的ID
         * status:Air, Today, NA
         * status:已放送，正在放送，未放送
         * my_status:status, statusQueue，statusWatched，statusDrop
         * my_status:无状态，想看，看过，抛弃
         * name 日文名
         * name_cn 中文名
         * info 该集的时长和上映时间
         */

        private int id;
        private String status;
        private String my_status;
        private String name;
        private String name_cn;
        private String info;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMy_status() {
            return my_status;
        }

        public void setMy_status(String my_status) {
            this.my_status = my_status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName_cn() {
            return name_cn;
        }

        public void setName_cn(String name_cn) {
            this.name_cn = name_cn;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

    }
}
