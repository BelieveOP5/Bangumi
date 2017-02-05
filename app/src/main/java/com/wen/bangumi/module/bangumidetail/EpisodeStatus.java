package com.wen.bangumi.module.bangumidetail;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by BelieveOP5 on 2017/2/5.
 */

public class EpisodeStatus {

    private static Map<String, String> actionMap = new HashMap<>();

    private static Map<String, String> statusMap = new HashMap<>();

    static {
        initActionMap();
        initStatusMap();
    }

    public static Map<String, String> getActionMap() {
        return actionMap;
    }

    public static Map<String, String> getStatusMap() {
        return statusMap;
    }

    private static void initStatusMap() {

        statusMap.put("queue", "statusQueue");
        statusMap.put("watched" ,"statusWatched");
        statusMap.put("drop", "statusDrop");
        statusMap.put("remove", "status");

    }

    private static void initActionMap() {

        actionMap.put("queue", "想看");
        actionMap.put("watched" ,"看过");
        actionMap.put("drop", "抛弃");
        actionMap.put("remove", "撤销");

    }


}
