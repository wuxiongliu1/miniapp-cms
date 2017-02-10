package com.huobanplus.miniapp.web.entity;

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
     * 文章正文
     */
    @Column(name = "content")
    private String content;

    /**
     * 富文本编辑得到的文章正文
     */
    @Column(name = "raw_content")
    private String rawContent;

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
     * 关联用户
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
