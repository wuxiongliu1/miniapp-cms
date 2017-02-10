package com.huobanplus.miniapp.web.service;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import org.springframework.data.domain.Page;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
public interface ArticleService {

    Article findArticle(Long id);

    ApiResult addArticle(Article article);

    ApiResult addArticle(User user, String title, String summary, String content, String rawContent);

    ApiResult deleteArticle(Long userId, Long articleId);

    ApiResult updateArticle(Long articleId, String title, String summary, String content, String rawContent);

    Page<Article> findAll(ArticleSearch articleSearch, int pageIndex, int pageSize);
}
