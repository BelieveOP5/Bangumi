package com.wen.bangumi.entity;

/**
 * Created by BelieveOP5 on 2017/1/24.
 */

public class Token {

    /**
     * id : 226970
     * url : http://bgm.tv/user/believeop5
     * username : believeop5
     * nickname : BelieveOP5
     * avatar : {"large":"http://lain.bgm.tv/pic/user/l/000/22/69/226970.jpg?r=1436162434","medium":"http://lain.bgm.tv/pic/user/m/000/22/69/226970.jpg?r=1436162434","small":"http://lain.bgm.tv/pic/user/s/000/22/69/226970.jpg?r=1436162434"}
     * sign : I‘m the wind that wavers.
     * auth : mf18jSi9CxX4jkV6Ne6BU5hEJJNBZY2hXKQI+iVL9FmdWKn/oBDz9pL0LugExBmBTQvN4WMibi8FzsCDvUhXMXdMZQfT0d1xf1Zf
     * auth_encode : mf18jSi9CxX4jkV6Ne6BU5hEJJNBZY2hXKQI%2BiVL9FmdWKn%2FoBDz9pL0LugExBmBTQvN4WMibi8FzsCDvUhXMXdMZQfT0d1xf1Zf
     */

    private int id;
    private String url;
    private String username;
    private String nickname;
    private AvatarBean avatar;
    private String sign;
    private String auth;
    private String auth_encode;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public AvatarBean getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarBean avatar) {
        this.avatar = avatar;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth_encode() {
        return auth_encode;
    }

    public void setAuth_encode(String auth_encode) {
        this.auth_encode = auth_encode;
    }

    public static class AvatarBean {
        /**
         * large : http://lain.bgm.tv/pic/user/l/000/22/69/226970.jpg?r=1436162434
         * medium : http://lain.bgm.tv/pic/user/m/000/22/69/226970.jpg?r=1436162434
         * small : http://lain.bgm.tv/pic/user/s/000/22/69/226970.jpg?r=1436162434
         */

        private String large;
        private String medium;
        private String small;

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
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
    }
}
