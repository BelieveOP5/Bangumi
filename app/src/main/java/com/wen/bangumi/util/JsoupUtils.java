package com.wen.bangumi.util;

import android.text.TextUtils;

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

//    /** 解析网页动画概览 */
//    private Items parseAnimeDetail(String html) {
//        LogUtil.d(LogUtil.ZUBIN, "parseDetail thread = " + Thread.currentThread());
//        Items items = new Items();
//
////        AnimeDetailEntity animeDetailEntity = new AnimeDetailEntity();
//        Document document = Jsoup.parse(html);
//
//        // subjectID
//        String subjectId = document.select("h1.nameSingle>a").attr("href").replace("/subject/", "").trim();
//
//        // 顶上名字旁边的灰色小字，一般是类别
//        String small_type = document.select("h1.nameSingle>small").text();
//        if (!TextUtils.isEmpty(small_type)) {
//            items.add(new TitleItem(BangumiApp.sAppCtx.getString(R.string.bangumi_type), R.drawable.ic_widgets_24dp));
//            items.add(new TextItem(small_type));
//        }
//
//        // 剧情简介
//        String summary = document.select("div#subject_summary").text().trim();
//        summaryStr = summary;
//        if (!TextUtils.isEmpty(summary)) {
//            items.add(new TitleItem(BangumiApp.sAppCtx.getString(R.string.bangumi_detail_summary), R.drawable.ic_graphic_eq_24dp));
//            items.add(new TextItem(summary));
//        }
//
//        // 左侧列表, 外面只有简介，点击进入后还有制作人员
//        Elements li = document.select("div.infobox>ul#infobox>li");
//        String showInfo = "";
//        String extra = "";
//        for (int i = 0; i < li.size(); i++) {
//            Element element = li.get(i);
//            String left_intro = element.text();
//
//            if (i < 6) {
//                showInfo += left_intro + "\n";
//            }
//            extra += left_intro + "\n";
//
//        }
//
//        if (li.size() > 0) {
//            TitleMoreItem titleMoreItem = new TitleMoreItem(BangumiApp.sAppCtx.getString(R.string.bangumi_detail_content)
//                    , R.drawable.ic_weekend_24dp, subjectId, MyConstants.DES_PERSON);
//            titleMoreItem.setExtra(extra);
//            items.add(titleMoreItem);
//            items.add(new TextItem(showInfo));
//        }
//
//        // 角色介绍, 可能不全
//        Elements subject_clearit = document.select("ul#browserItemList>li");
//        List<AnimeCharacterEntity> characterList = new ArrayList<>();
//        for (int clearItIndex = 0; clearItIndex < subject_clearit.size(); clearItIndex++) {
//            Element element = subject_clearit.get(clearItIndex);
//
//            AnimeCharacterEntity entity = new AnimeCharacterEntity();
//
//            // 角色小头像图片
//            String role_image_url = "https:" + element.select("div>strong>a>span>img").attr("src");
//            entity.setRoleImageUrl(role_image_url);
//            // 下列链接为脑补，不一定有
//            String large_mage_url = role_image_url.replace("/s/", "/l/");
//            entity.setRoleLargeImageUrl(large_mage_url);
//
//            // 日文名字
//            String role_name_jp = element.select("div>strong>a").text();
//            entity.setRoleNameJp(role_name_jp);
//            // 角色链接
//            String role_url = element.select("div>strong>a").attr("href");
//            entity.setRoleUrl(role_url);
//
//            // 角色类型， 主角
//            String role_type = element.select("div>div>span>small").text();
//            entity.setRoleType(role_type);
//            // 角色中文名
//            String role_name_cn = element.select("div>div>span>span.tip").text();
//            entity.setRoleNameCn(role_name_cn);
//            // 声优
//            String cv_name = element.select("div>div>span>a").text();
//            entity.setCvName(cv_name);
//            // 声优链接
//            String cv_url = element.select("div>div>span>a").attr("href");
//            entity.setCvUrl(cv_url);
//
//            characterList.add(entity);
//        }
//        if (subject_clearit.size() > 0) {
//            items.add(new TitleMoreItem(BangumiApp.sAppCtx.getString(R.string.bangumi_detail_character), R.drawable.ic_whatshot_24dp, subjectId, MyConstants.DES_CHARACTER));
//            items.add(new DetailCharacterList(characterList));
//        }
//
//
//        // 右侧收藏盒
//        String global_score = document.select("div.global_score").text();
//        scoreStr = global_score;
//
//        // 标签栏
//        Elements tag = document.select("div.subject_tag_section>div.inner>a");
//        for (int tagIndex = 0; tagIndex < tag.size(); tagIndex++) {
//            Element element = tag.get(tagIndex);
//
//            String tag_name = element.text();
//            String tag_src = element.attr("href");
//
//            if (tagIndex < tag.size() - 1) {
//                tagStr += tag_name + "、";
//            } else {
//                tagStr += tag_name;
//            }
//        }
//
//        // 观看进度, 点击会跳转到新的详细页面
//        Elements prg_list = document.select("ul.prg_list>li");
//        List<AnimeEpEntity> epList = new ArrayList<>();
//        for (int i = 0; i < prg_list.size(); i++) {
//            Element element = prg_list.get(i);
//            // /ep/638065
////            String url = element.select("a").attr("href");
////            // load-epinfo epBtnWatched
////            String state = element.select("a").attr("class");
////            //  ep.5 OCHIMUSHA ～超能力と僕～
////            String name = element.select("a").attr("title");
//
//            // 显示的名字
//            String displayName = element.select("a").text().trim();
//            if(!TextUtils.isEmpty(displayName) && epList.size() < 12) {
//                AnimeEpEntity entity = new AnimeEpEntity(displayName, subjectId);
//                epList.add(entity);
//            }
//        }
//        if (prg_list.size() > 0) {
//            items.add(new TitleMoreItem(BangumiApp.sAppCtx.getString(R.string.watch_progress), R.drawable.ic_timeline_24dp, subjectId, MyConstants.DES_EP));
//            items.add(new DetailEpList(epList));
//        }
//
//        // 最顶上的名称， 一般是日文名
//        String title = document.select("h1.nameSingle").text();
//
//        // 图片large 地址, 可能为空
//        String large_image_url = "https:" + document.select("div.infobox>div>a").attr("href");
//
//        // 图片cover 地址, 可能为空
//        String cover_image_url = "https:" + document.select("div.infobox>div>a>img").attr("src");
//
//        // 吐槽箱
//        Elements comments = document.select("div#comment_box>div");
//        List<CommentEntity> commentList = new ArrayList<>();
//        for (int commentIndex = 0; commentIndex < comments.size(); commentIndex++) {
//            Element element = comments.get(commentIndex);
//            CommentEntity entity = new CommentEntity();
//
//            String userLink = element.select("a").attr("href");
//            //background-image:url('//lain.bgm.tv/pic/user/s/000/30/28/302862.jpg?r=1472368595')
//            String originImage = element.select("a>span").attr("style").replace("\'", "");
//            String userAvatar = originImage.substring(originImage.indexOf("(") + 1, originImage.indexOf(")"));
//            String userName = element.select("div>a").text();
//            String userComment = element.select("div>p").text();
//            String commentDate = element.select("div>small").text();
//
//            entity.setUserLink(userLink);
//            entity.setUserAvatar(userAvatar);
//            entity.setUserName(userName);
//            entity.setUserComment(userComment);
//            entity.setCommentDate(commentDate);
//
//            commentList.add(entity);
//        }
//
//        return items;
//    }

}
