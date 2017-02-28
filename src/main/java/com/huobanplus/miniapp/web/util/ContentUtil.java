package com.huobanplus.miniapp.web.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by wuxiongliu on 2017-02-17.
 */
public class ContentUtil {

    /**
     * 从html中取出所有的img标签
     *
     * @param html
     * @return
     */
    public static String[] captureImgUrls(String html) {
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.select("img");
        int imgNums = imgs.size();
        String[] imgSrcs = new String[imgNums];
        for (int i = 0; i < imgNums; i++) {
            if (imgs.get(i).attr("src") != null) {
                imgSrcs[i] = imgs.get(i).attr("src");
            }
        }
        return imgSrcs;
    }


    public static void main(String[] args) {
        String html = "<img src=\"http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg\"/>" +
                "<img src=\"http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg\"/>" +
                "<img data-src=\"http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg\"/>" +
                "<img src=\"http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg\"/>" +
                "<img src=\"http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg\"/>";
        String nonImgHtml = "fsadffasdfasdfads";

        String[] imgSrcs = captureImgUrls(nonImgHtml);
        for (String imgSrc : imgSrcs) {
            System.out.println(imgSrc);
        }
        System.out.println(imgSrcs.length);

        String res = "http://img06.tooopen.com/images/20160727/tooopen_sl_172639819999.jpg|http://img06.tooopen.com/images/20160724/tooopen_sl_171557864411.jpg|http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg|";
        System.out.println(res.split("\\|").length);
    }
}
