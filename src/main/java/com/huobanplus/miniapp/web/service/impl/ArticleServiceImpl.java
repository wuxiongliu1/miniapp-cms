package com.huobanplus.miniapp.web.service.impl;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.repository.ArticleRepository;
import com.huobanplus.miniapp.web.service.ArticleService;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public Article findArticle(Long id) {
        Article article = articleRepository.findOne(id);
        if (article != null) {
            String[] previewImgUrlsArray = article.getPreviewImage().split("\\|");
            article.setPreviewImgArray(previewImgUrlsArray);
            return article;
        }
        return null;
    }

    @Override
    public ApiResult addArticle(Article article) {
        Article savedArticle = articleRepository.save(article);
        return ApiResult.resultWith(ResultCode.SUCCESS, savedArticle);
    }

    @Override
    public ApiResult addArticle(User user, String title, String summary, String content, String author, String publicDate) {
        Article article = new Article();
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setAuthor(author);
        article.setPublicDate(publicDate);
        article.setCreateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUpdateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUser(user);

        articleRepository.save(article);
        return ApiResult.resultWith(ResultCode.SUCCESS, article);
    }

    @Override
    public ApiResult deleteArticle(Long userId, Long articleId) {
        Article article = articleRepository.findOne(articleId);
        if (article.getUser().getId() == userId) {
            articleRepository.delete(articleId);
            return ApiResult.resultWith(ResultCode.SUCCESS, article);
        } else {
            return ApiResult.resultWith(ResultCode.NO_PERMISSTION);
        }
    }

    @Override
    public ApiResult updateArticle(Long articleId, String title, String summary, String content, String rawContent) {
        Article article = articleRepository.findOne(articleId);
        if (article == null) {
            return ApiResult.resultWith(ResultCode.NO_ARTICLE);
        }
        article.setTitle(title);
        article.setSummary(summary);
        article.setContent(content);
        article.setUpdateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article = articleRepository.save(article);
        return ApiResult.resultWith(ResultCode.SUCCESS, article);
    }

    @Override
    public Page<Article> findAll(ArticleSearch articleSearch, int pageIndex, int pageSize) {
        Specification<Article> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(articleSearch.getUserId())) {
                predicates.add(cb.equal(root.get("userId").as(Long.class), articleSearch.getUserId()));
            }
            if (!StringUtils.isEmpty(articleSearch.getTitle())) {
                predicates.add(cb.like(root.get("title").as(String.class), "%" + articleSearch.getTitle() + '%'));
            }
            if (!StringUtils.isEmpty(articleSearch.getBeginCreateTime())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), articleSearch.getBeginCreateTime()));
            }
            if (!StringUtils.isEmpty(articleSearch.getEndCreateTime())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createTime").as(String.class), articleSearch.getEndCreateTime()));
            }
            if (!StringUtils.isEmpty(articleSearch.getBeginUpdateTime())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(String.class), articleSearch.getBeginUpdateTime()));
            }
            if (!StringUtils.isEmpty(articleSearch.getEndUpdateTime())) {
                predicates.add(cb.lessThanOrEqualTo(root.get("updateTime").as(String.class), articleSearch.getEndUpdateTime()));
            }

            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        return articleRepository.findAll(specification, new PageRequest(pageIndex - 1, pageSize, new Sort(Sort.Direction.DESC, "updateTime")));
    }
}
