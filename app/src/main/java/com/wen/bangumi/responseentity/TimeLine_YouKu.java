package com.wen.bangumi.responseentity;

import java.util.List;

/**
 * Created by GsonFormat, don't Modify it.
 * Created by BelieveOP5 on 2017/1/13.
 */

public class TimeLine_YouKu {


    /**
     * big_data_sub_channel_id :
     * data_source : Retrofit2_RxJava2
     * is_include_cms_slider : false
     * p_ctrl_ret_boxes : []
     * sub_channel_type : editable_box
     * sub_channel_title : 追番表
     * brand_box : {}
     * boxes : []
     * has_brands_headline : Retrofit2_RxJava2
     * game_entrances : []
     */

    private String big_data_sub_channel_id;
    private int data_source;
    private boolean is_include_cms_slider;
    private String sub_channel_type;
    private String sub_channel_title;
    private BrandBoxBean brand_box;
    private int has_brands_headline;
    private List<?> p_ctrl_ret_boxes;
    private List<BoxesBean> boxes;
    private List<?> game_entrances;

    public String getBig_data_sub_channel_id() {
        return big_data_sub_channel_id;
    }

    public void setBig_data_sub_channel_id(String big_data_sub_channel_id) {
        this.big_data_sub_channel_id = big_data_sub_channel_id;
    }

    public int getData_source() {
        return data_source;
    }

    public void setData_source(int data_source) {
        this.data_source = data_source;
    }

    public boolean isIs_include_cms_slider() {
        return is_include_cms_slider;
    }

    public void setIs_include_cms_slider(boolean is_include_cms_slider) {
        this.is_include_cms_slider = is_include_cms_slider;
    }

    public String getSub_channel_type() {
        return sub_channel_type;
    }

    public void setSub_channel_type(String sub_channel_type) {
        this.sub_channel_type = sub_channel_type;
    }

    public String getSub_channel_title() {
        return sub_channel_title;
    }

    public void setSub_channel_title(String sub_channel_title) {
        this.sub_channel_title = sub_channel_title;
    }

    public BrandBoxBean getBrand_box() {
        return brand_box;
    }

    public void setBrand_box(BrandBoxBean brand_box) {
        this.brand_box = brand_box;
    }

    public int getHas_brands_headline() {
        return has_brands_headline;
    }

    public void setHas_brands_headline(int has_brands_headline) {
        this.has_brands_headline = has_brands_headline;
    }

    public List<?> getP_ctrl_ret_boxes() {
        return p_ctrl_ret_boxes;
    }

    public void setP_ctrl_ret_boxes(List<?> p_ctrl_ret_boxes) {
        this.p_ctrl_ret_boxes = p_ctrl_ret_boxes;
    }

    public List<BoxesBean> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<BoxesBean> boxes) {
        this.boxes = boxes;
    }

    public List<?> getGame_entrances() {
        return game_entrances;
    }

    public void setGame_entrances(List<?> game_entrances) {
        this.game_entrances = game_entrances;
    }

    public static class BrandBoxBean {
    }

    public static class BoxesBean {
        /**
         * data_source : Retrofit2_RxJava2
         * header : {"jump_info":{"type":"no_jump"},"title":"周一","tags":[]}
         * is_for_user_channel : Retrofit2_RxJava2
         * image :
         * is_phone_use_only_one_unit : Retrofit2_RxJava2
         * image_link :
         * hidden_header : Retrofit2_RxJava2
         * module_type : normal
         * module_id : 743
         * sub_channel_id_for_link :
         * cells : []
         */

        private int data_source;
        private HeaderBean header;
        private int is_for_user_channel;
        private String image;
        private int is_phone_use_only_one_unit;
        private String image_link;
        private int hidden_header;
        private String module_type;
        private int module_id;
        private String sub_channel_id_for_link;
        private List<CellsBean> cells;

        public int getData_source() {
            return data_source;
        }

        public void setData_source(int data_source) {
            this.data_source = data_source;
        }

        public HeaderBean getHeader() {
            return header;
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public int getIs_for_user_channel() {
            return is_for_user_channel;
        }

        public void setIs_for_user_channel(int is_for_user_channel) {
            this.is_for_user_channel = is_for_user_channel;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getIs_phone_use_only_one_unit() {
            return is_phone_use_only_one_unit;
        }

        public void setIs_phone_use_only_one_unit(int is_phone_use_only_one_unit) {
            this.is_phone_use_only_one_unit = is_phone_use_only_one_unit;
        }

        public String getImage_link() {
            return image_link;
        }

        public void setImage_link(String image_link) {
            this.image_link = image_link;
        }

        public int getHidden_header() {
            return hidden_header;
        }

        public void setHidden_header(int hidden_header) {
            this.hidden_header = hidden_header;
        }

        public String getModule_type() {
            return module_type;
        }

        public void setModule_type(String module_type) {
            this.module_type = module_type;
        }

        public int getModule_id() {
            return module_id;
        }

        public void setModule_id(int module_id) {
            this.module_id = module_id;
        }

        public String getSub_channel_id_for_link() {
            return sub_channel_id_for_link;
        }

        public void setSub_channel_id_for_link(String sub_channel_id_for_link) {
            this.sub_channel_id_for_link = sub_channel_id_for_link;
        }

        public List<CellsBean> getCells() {
            return cells;
        }

        public void setCells(List<CellsBean> cells) {
            this.cells = cells;
        }

        public static class HeaderBean {
            /**
             * jump_info : {"type":"no_jump"}
             * title : 周一
             * tags : []
             */

            private JumpInfoBean jump_info;
            private String title;
            private List<?> tags;

            public JumpInfoBean getJump_info() {
                return jump_info;
            }

            public void setJump_info(JumpInfoBean jump_info) {
                this.jump_info = jump_info;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public List<?> getTags() {
                return tags;
            }

            public void setTags(List<?> tags) {
                this.tags = tags;
            }

            public static class JumpInfoBean {
                /**
                 * type : no_jump
                 */

                private String type;

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }

        public static class CellsBean {
            /**
             * layout : 5
             * contents : []
             */

            private int layout;
            private List<ContentsBean> contents;

            public int getLayout() {
                return layout;
            }

            public void setLayout(int layout) {
                this.layout = layout;
            }

            public List<ContentsBean> getContents() {
                return contents;
            }

            public void setContents(List<ContentsBean> contents) {
                this.contents = contents;
            }

            public static class ContentsBean {
                /**
                 * pk_odshow : 19510
                 * subtitle : 9.9
                 * img : http://r1.ykimg.com/05100000586E440967BC3D5A5605B1C4
                 * title : 银魂 (01:35)
                 * url :
                 * paid : Retrofit2_RxJava2
                 * stripe : 更新至318集
                 * tid : cc0026e0962411de83b1
                 * is_vv : Retrofit2_RxJava2
                 * type : 2
                 */

                private String pk_odshow;
                private String subtitle;
                private String img;
                private String title;
                private String url;
                private int paid;
                private String stripe;
                private String tid;
                private int is_vv;
                private String type;

                public String getPk_odshow() {
                    return pk_odshow;
                }

                public void setPk_odshow(String pk_odshow) {
                    this.pk_odshow = pk_odshow;
                }

                public String getSubtitle() {
                    return subtitle;
                }

                public void setSubtitle(String subtitle) {
                    this.subtitle = subtitle;
                }

                public String getImg() {
                    return img;
                }

                public void setImg(String img) {
                    this.img = img;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public int getPaid() {
                    return paid;
                }

                public void setPaid(int paid) {
                    this.paid = paid;
                }

                public String getStripe() {
                    return stripe;
                }

                public void setStripe(String stripe) {
                    this.stripe = stripe;
                }

                public String getTid() {
                    return tid;
                }

                public void setTid(String tid) {
                    this.tid = tid;
                }

                public int getIs_vv() {
                    return is_vv;
                }

                public void setIs_vv(int is_vv) {
                    this.is_vv = is_vv;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
    }
}
