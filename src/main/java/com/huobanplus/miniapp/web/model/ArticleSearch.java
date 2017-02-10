package com.huobanplus.miniapp.web.model;

import lombok.Data;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Data
public class ArticleSearch {

    private Long userId;

    /**
     * 笔记标题
     */
    private String title;

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
