package com.wen.bangumi.responseentity;

import java.util.List;

/**
 * 存储用户看的番剧的章节的状态
 * api返回的只有看过和抛弃状态
 * Created by BelieveOP5 on 2017/1/30.
 */

public class EpisodeStatus {

    /**
     * subject_id : 975
     * eps : []
     */

    private int subject_id;
    private List<EpsBean> eps;

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public List<EpsBean> getEps() {
        return eps;
    }

    public void setEps(List<EpsBean> eps) {
        this.eps = eps;
    }

    public static class EpsBean {
        /**
         * id : 1807
         * status : {"id":2,"css_name":"Watched","url_name":"watched","cn_name":"看过"}
         */

        private int id;
        private StatusBean status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public StatusBean getStatus() {
            return status;
        }

        public void setStatus(StatusBean status) {
            this.status = status;
        }

        public static class StatusBean {
            /**
             * id : 2
             * css_name : Watched
             * url_name : watched
             * cn_name : 看过
             */

            private int id;
            private String css_name;
            private String url_name;
            private String cn_name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCss_name() {
                return css_name;
            }

            public void setCss_name(String css_name) {
                this.css_name = css_name;
            }

            public String getUrl_name() {
                return url_name;
            }

            public void setUrl_name(String url_name) {
                this.url_name = url_name;
            }

            public String getCn_name() {
                return cn_name;
            }

            public void setCn_name(String cn_name) {
                this.cn_name = cn_name;
            }
        }
    }
}
