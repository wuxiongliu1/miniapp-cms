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
     * @param num  要抓取的数量
     * @return
     */
    public static String captureImgUrls(String html, int num) {
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.select("img");

        StringBuffer resultImages = new StringBuffer();

        int imgNums = imgs.size();

        if (imgNums >= 3) {
            // 取前三条
            resultImages.append(imgs.get(0).attr("src")).append("|")
                    .append(imgs.get(1).attr("src")).append("|")
                    .append(imgs.get(2).attr("src"));

        } else if (imgNums >= 1 && imgNums < 3) {
            // 取第一条
            resultImages.append(imgs.get(0).attr("src")).append("|");
        } else {
            // 一条也没有
        }

        return resultImages.toString();
    }


    public static void main(String[] args) {
        String html = "<img src=\"http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg\" onerror=\"javascript:this.src='http://resource.tooopen.com/image/no-img-192.gif';this.onerror=null;\" alt=\"美丽的海边岩石景色摄影大图\" class=\"imgItem\"/>\n" + "<img data-src=\"http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg\" onerror=\"javascript:this.src='http://resource.tooopen.com/image/no-img-192.gif';this.onerror=null;\" alt=\"美丽的海边岩石景色摄影大图\" class=\"imgItem\"/>\n";


        System.out.println(captureImgUrls(html, 5));

        String res = "http://img06.tooopen.com/images/20160727/tooopen_sl_172639819999.jpg|http://img06.tooopen.com/images/20160724/tooopen_sl_171557864411.jpg|http://img06.tooopen.com/images/20160725/tooopen_sl_171743228656.jpg|";
        System.out.println(res.split("\\|").length);
    }
}
