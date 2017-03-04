package com.huobanplus.miniapp.web.model;

import com.huobanplus.miniapp.web.common.ArticleType;
import lombok.Data;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Data
public class ArticleSearch {

    private Long userId;

    /**
     * 文章状态
     */
    private ArticleType.ArticleStatus articleStatus;

    /**
     *  是否头条
     */
    private Boolean topHead;

    /**
     * 文章是否已删除
     */
    private Boolean enabled;

    /**
     * 笔记标题
     */
    private String title;

    /**
     * 布局类型
     */
    private Integer layoutType;

    /**
     * 创建时间 开始
     */
    private String beginCreateTime;

    /**
     * 创建时间 结束
     */
    private String endCreateTime;

    /**
     * 更新时间 开始
     */
    private String beginUpdateTime;

    /**
     * 更新时间 结束
     */
    private String endUpdateTime;
}
