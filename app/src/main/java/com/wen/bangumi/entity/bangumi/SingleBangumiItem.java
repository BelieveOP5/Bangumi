package com.wen.bangumi.entity.bangumi;

import com.google.gson.annotations.SerializedName;

/**
 * 单个Bangumi的基本信息(从api返回的)
 * Created by BelieveOP5 on 2017/2/6.
 */

public class SingleBangumiItem {

    /**
     * id : 191956
     * url : http://bgm.tv/subject/191956
     * type : 1:漫画/小说 2:动画 3:音乐 4:游戏
     * name : 亜人ちゃんは語りたい
     * name_cn : 亚人酱有话要说
     * summary : 魅魔、无头骑士、雪女、以及吸血鬼——。与我们人类略有不同，他们就是“亚人”。在最近则是被称作Demi。
     * 这些有个性的“亚人”们，与对她们怀有深厚兴趣的高中教师·高桥铁男所展开的，稍有些刺激的心动学园亚人喜剧！
     * eps : 12
     * air_date : 2017-01-07
     * air_weekday : 6
     * rating : {"total":197,"count":{"10":4,"9":7,"8":45,"7":93,"6":36,"5":6,"4":2,"3":1,"2":0,"1":3},"score":7}
     * rank : 1739
     * images : {}
     * collection : {"wish":105,"collect":13,"doing":674,"on_hold":14,"dropped":51}
     */

    private int id;
    private String url;
    private int type;
    private String name;
    private String name_cn;
    private String summary;
    private int eps;
    private String air_date;
    private int air_weekday;
    private RatingBean rating;
    private int rank;
    private ImagesBean images;
    private CollectionBean collection;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getEps() {
        return eps;
    }

    public void setEps(int eps) {
        this.eps = eps;
    }

    public String getAir_date() {
        return air_date;
    }

    public void setAir_date(String air_date) {
        this.air_date = air_date;
    }

    public int getAir_weekday() {
        return air_weekday;
    }

    public void setAir_weekday(int air_weekday) {
        this.air_weekday = air_weekday;
    }

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public CollectionBean getCollection() {
        return collection;
    }

    public void setCollection(CollectionBean collection) {
        this.collection = collection;
    }

    public static class RatingBean {
        /**
         * total : 197
         * count : {"10":4,"9":7,"8":45,"7":93,"6":36,"5":6,"4":2,"3":1,"2":0,"1":3}
         * score : 7
         */

        private int total;
        private CountBean count;
        private int score;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public CountBean getCount() {
            return count;
        }

        public void setCount(CountBean count) {
            this.count = count;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public static class CountBean {
            /**
             * 10 : 4
             * 9 : 7
             * 8 : 45
             * 7 : 93
             * 6 : 36
             * 5 : 6
             * 4 : 2
             * 3 : 1
             * 2 : 0
             * 1 : 3
             */

            @SerializedName("10")
            private int _$10;
            @SerializedName("9")
            private int _$9;
            @SerializedName("8")
            private int _$8;
            @SerializedName("7")
            private int _$7;
            @SerializedName("6")
            private int _$6;
            @SerializedName("5")
            private int _$5;
            @SerializedName("4")
            private int _$4;
            @SerializedName("3")
            private int _$3;
            @SerializedName("2")
            private int _$2;
            @SerializedName("1")
            private int _$1;

            public int get_$10() {
                return _$10;
            }

            public void set_$10(int _$10) {
                this._$10 = _$10;
            }

            public int get_$9() {
                return _$9;
            }

            public void set_$9(int _$9) {
                this._$9 = _$9;
            }

            public int get_$8() {
                return _$8;
            }

            public void set_$8(int _$8) {
                this._$8 = _$8;
            }

            public int get_$7() {
                return _$7;
            }

            public void set_$7(int _$7) {
                this._$7 = _$7;
            }

            public int get_$6() {
                return _$6;
            }

            public void set_$6(int _$6) {
                this._$6 = _$6;
            }

            public int get_$5() {
                return _$5;
            }

            public void set_$5(int _$5) {
                this._$5 = _$5;
            }

            public int get_$4() {
                return _$4;
            }

            public void set_$4(int _$4) {
                this._$4 = _$4;
            }

            public int get_$3() {
                return _$3;
            }

            public void set_$3(int _$3) {
                this._$3 = _$3;
            }

            public int get_$2() {
                return _$2;
            }

            public void set_$2(int _$2) {
                this._$2 = _$2;
            }

            public int get_$1() {
                return _$1;
            }

            public void set_$1(int _$1) {
                this._$1 = _$1;
            }
        }
    }

    public static class ImagesBean {
        /**
         * large : http://lain.bgm.tv/pic/cover/l/ff/4a/191956_P7O5h.jpg
         * common : http://lain.bgm.tv/pic/cover/c/ff/4a/191956_P7O5h.jpg
         * medium : http://lain.bgm.tv/pic/cover/m/ff/4a/191956_P7O5h.jpg
         * small : http://lain.bgm.tv/pic/cover/s/ff/4a/191956_P7O5h.jpg
         * grid : http://lain.bgm.tv/pic/cover/g/ff/4a/191956_P7O5h.jpg
         */

        private String large;
        private String common;
        private String medium;
        private String small;
        private String grid;

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }

        public String getMedium() {
            return medium;
        }

        public void setMedium(String medium) {
            this.medium = medium;
        }

        public String getSmall() {
            return small;
        }

        public void setSmall(String small) {
            this.small = small;
        }

        public String getGrid() {
            return grid;
        }

        public void setGrid(String grid) {
            this.grid = grid;
        }
    }

    public static class CollectionBean {
        /**
         * wish : 105
         * collect : 13
         * doing : 674
         * on_hold : 14
         * dropped : 51
         */

        private int wish;
        private int collect;
        private int doing;
        private int on_hold;
        private int dropped;

        public int getWish() {
            return wish;
        }

        public void setWish(int wish) {
            this.wish = wish;
        }

        public int getCollect() {
            return collect;
        }

        public void setCollect(int collect) {
            this.collect = collect;
        }

        public int getDoing() {
            return doing;
        }

        public void setDoing(int doing) {
            this.doing = doing;
        }

        public int getOn_hold() {
            return on_hold;
        }

        public void setOn_hold(int on_hold) {
            this.on_hold = on_hold;
        }

        public int getDropped() {
            return dropped;
        }

        public void setDropped(int dropped) {
            this.dropped = dropped;
        }
    }
}
