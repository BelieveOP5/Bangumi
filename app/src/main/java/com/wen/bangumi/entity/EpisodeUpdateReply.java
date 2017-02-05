package com.wen.bangumi.entity;

/**
 * Created by BelieveOP5 on 2017/2/5.
 */

public class EpisodeUpdateReply {


    /**
     * request :
     * code : 200
     * error : OK
     */

    private String request;
    private int code;
    private String error;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
