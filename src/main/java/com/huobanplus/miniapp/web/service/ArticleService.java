package com.huobanplus.miniapp.web.service;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ArticleType;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleModel;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
public interface ArticleService {

    /**
     * 查找文章
     *
     * @param id
     * @return
     */
    Article findArticle(Long id);

    /**
     * 新增文章
     *
     * @param article
     * @return
     */
    ApiResult addArticle(Article article);

    /**
     * 新增文章
     *
     * @param user
     * @param title
     * @param summary
     * @param content
     * @return
     */
    ApiResult addArticle(User user, String title, String summary, String content, String author, String publicDate);

    /**
     * 删除文章（标记删除，不要真的删掉）
     *
     * @param userId
     * @param articleId
     * @return
     */
    ApiResult deleteArticle(Long userId, Long articleId);

    /**
     * 更新文章
     *
     * @param articleId
     * @param title
     * @param summary
     * @param content
     * @param rawContent
     * @return
     */
    ApiResult updateArticle(Long articleId, String title, String summary, String content, String rawContent);

    /**
     * 更新文章
     *
     * @param articleModel
     * @return
     */
    ApiResult updateArticle(ArticleModel articleModel);

    /**
     * 文章查询
     *
     * @param articleSearch
     * @param pageIndex
     * @param pageSize
     * @return
     */
    Page<Article> findAll(ArticleSearch articleSearch, int pageIndex, int pageSize, Sort sort);

    /**
     * 操作文章
     *
     * @param id
     * @param articleOperateEnum
     * @return
     */
    ApiResult operateArticle(Long id, ArticleType.ArticleOperateEnum articleOperateEnum);
}
