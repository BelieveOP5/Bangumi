package com.wen.bangumi.util;

import com.wen.bangumi.greenDAO.BangumiItem;
import com.wen.bangumi.entity.calendar.EpisodesEntity;

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

        for (int i = 0; i < li.size(); ++i) {
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

    /**
     * 解析网页{@see <a href="http://bangumi.tv/subject/975/ep"></a>}
     * @param html
     */
    public static List<EpisodesEntity> parseBangumiEpi(String html) {
        Document doc = Jsoup.parse(html);

        Elements div = doc.select("div.line_detail");
        Elements li = div.select("ul.line_list>li");

        List<EpisodesEntity> episodesEntityList = new ArrayList<>();

        for(int i = 0; i < li.size(); ++i) {
            /**
             * 先将本篇，特别篇等章节种类存储下来
             */
            Element element = li.get(i);
            if (element.hasClass("cat")) {
                EpisodesEntity episodesEntity = new EpisodesEntity();
                episodesEntity.setType(element.text());
                episodesEntity.setEpisodeList(new ArrayList<EpisodesEntity.Episode>());
                episodesEntityList.add(episodesEntity);
            }
        }

        for(int i = 0, k = -1; i < li.size(); ++i) {
            Element element = li.get(i);

            if (element.hasClass("cat")) {
                k++;
                continue;
            }

            EpisodesEntity.Episode episode = new EpisodesEntity.Episode();

            episode.setId(Long.valueOf(element.select("h6>a").attr("href").replace("/ep/", "")));
            episode.setStatus(element.select("h6>span.epAirStatus>span").attr("class"));
            // FIXME: 2017/2/2 在该函数中没有初始化章节中的用户状态，因为在网页中无法获取到，所以在该函数调用之后，需要调用api函数来初始化该状态
            episode.setMy_status("status");
            episode.setName(element.select("h6>a").text());
            episode.setName_cn(element.select("h6>span.tip").text());
            episode.setInfo(element.select("small.grey").get(0).text());

            episodesEntityList.get(k).getEpisodeList().add(episode);

        }

        return episodesEntityList;

    }

}
