package com.huobanplus.miniapp.web.model;

import lombok.Data;

/**
 * Created by wuxiongliu on 2017-02-20.
 * 上banner的文章
 */
@Data
public class BannerArticle {

    /**
     * 文章id
     */
    private Long id;

    /**
     * 图片地址
     */
    private String imgSrc;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 布局类型
     */
    private int layoutType;
}
