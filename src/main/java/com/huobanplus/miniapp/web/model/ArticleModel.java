package com.huobanplus.miniapp.web.model;

import com.huobanplus.miniapp.web.common.ArticleType;
import lombok.Data;

/**
 * Created by wuxiongliu on 2017-02-20.
 */
@Data
public class ArticleModel {

    /**
     * 自增id
     */
    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章内容
     */
    private String content;

    /**
     * 发布时间
     */
    private String publicDate;

    /**
     * 文章作者
     */
    private String author;

    /**
     * 布局类型
     * {@link ArticleType}
     */
    private ArticleType.LayoutEnum layoutType = ArticleType.LayoutEnum.NO_PIC;// 默认无图

    /**
     * 预览图片url存
     */
    private String previewImages;

    private String[] oldImgs;
}
