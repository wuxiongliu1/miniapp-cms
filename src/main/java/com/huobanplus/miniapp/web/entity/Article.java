package com.huobanplus.miniapp.web.entity;

import com.huobanplus.miniapp.web.common.ArticleType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by wuxiongliu on 2017-02-10.
 */

@Entity
@Table(name = "cms_article")
@Getter
@Setter
public class Article {

    /**
     * 自增id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 文章状态
     * * {@link com.huobanplus.miniapp.web.common.ArticleType.ArticleStatus}
     */
    @Column(name = "article_status")
    private ArticleType.ArticleStatus articleStatus = ArticleType.ArticleStatus.UNRELEASE;

    /**
     * 是否删除
     * true 未删除
     * false 已删除
     */
    @Column(name = "enabled")
    private Boolean enabled = true;

    /**
     * 文章标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 文章摘要
     */
    @Column(name = "summary")
    private String summary;

    /**
     * 文章内容
     */
    @Column(name = "content")
    @Lob
    private String content;


    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;


    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 发布时间
     */
    @Column(name = "public_date")
    private String publicDate;

    /**
     * 文章作者
     */
    @Column(name="author")
    private String author;


    /**
     * 布局类型
     * {@link ArticleType}
     */
    @Column(name = "layout_type")
    private ArticleType.LayoutEnum layoutType = ArticleType.LayoutEnum.NO_PIC;// 默认无图

    /**
     * 预览图片url数组
     */
    @Column(name = "preivew_img")
    private String[] previewImage;


    /**
     * 是否头条
     * true： 是 需要上传头条轮播图片[从预览图片的第一张获取]
     * false：否
     */
    private boolean topHead;


    /**
     * 关联用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
