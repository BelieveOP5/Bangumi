package com.wen.bangumi.util;

import com.wen.bangumi.greenDAO.BangumiItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BelieveOP5 on 2017/1/28.
 */

public class JsoupUtils {


    /**
     * 解析网页{@see <a href="http://bangumi.tv/subject/185762"></a>}
     * @param html
     * @return
     */
    public static BangumiItem parseBangumiHtml(String html) {
        return null;
    }

    /**
     * 解析网页{@see <a href="http://bangumi.tv/anime/list/believeop5/do"></a>}
     * @param html
     * @return
     */
    public static List<BangumiItem> parseCollHtml(String html) {
        Document doc = Jsoup.parse(html);

        Elements div = doc.select("div.column");
        Elements li = div.select("ul#browserItemList>li");

        List<BangumiItem> mList = new ArrayList<>();

        for (int i = 0; i < li.size(); i++) {
            Element element = li.get(i);

            BangumiItem entity = new BangumiItem();

            String id = element.select("a").attr("href").replace("/subject/", "").trim();
            String image = "https:" + element.select("a>span>img").attr("src");
            String name = element.select("div>h3>a").text();

            // 这个是自己做的，这张图不一定存在
            String largeImage = image.replace("/s/", "/l/");

            entity.setBangumi_id(Integer.valueOf(id));
            entity.setName_cn(name);
            entity.setAir_weekday(0);
            entity.setLarge_image(largeImage);

            mList.add(entity);
        }

        return mList;
    }

}
