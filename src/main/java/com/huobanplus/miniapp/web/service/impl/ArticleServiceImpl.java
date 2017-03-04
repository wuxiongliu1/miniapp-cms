package com.huobanplus.miniapp.web.service.impl;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ArticleType;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleModel;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.repository.ArticleRepository;
import com.huobanplus.miniapp.web.service.ArticleService;
import com.huobanplus.miniapp.web.service.ResourceService;
import com.huobanplus.miniapp.web.util.EnumHelper;
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
    @Autowired
    private ResourceService resourceService;

    @Override
    public Article findArticle(Long id) {
        return articleRepository.findOne(id);
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
        article.setArticleStatus(ArticleType.ArticleStatus.UNRELEASE);
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
    public ApiResult addArticle(User user, ArticleModel articleModel) {
        Article article = new Article();
        article.setTitle(articleModel.getTitle());
        article.setArticleStatus(articleModel.getArticleStatus());
        article.setSummary(articleModel.getSummary());
        article.setContent(articleModel.getContent());
        article.setAuthor(articleModel.getAuthor());
        article.setTopHead(articleModel.isTopHead());
        article.setPublicDate(articleModel.getPublicDate());
        article.setLayoutType(articleModel.getLayoutType());
        article.setCreateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUpdateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUser(user);
        article.setPreviewImage(articleModel.getNewsFiles());

        articleRepository.save(article);
        return ApiResult.resultWith(ResultCode.SUCCESS, article);

    }

    @Override
    public ApiResult deleteArticle(Long userId, Long articleId) {
        Article article = articleRepository.findOne(articleId);
        if (article.getUser().getId() == userId) {
            article.setEnabled(false);
            articleRepository.save(article);
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
    public ApiResult updateArticle(ArticleModel articleModel) {
        Article article = articleRepository.findOne(articleModel.getId());

        if (article == null) {
            return ApiResult.resultWith(ResultCode.NO_ARTICLE);
        }
        String oldContent = article.getContent();
        String[] oldImgs = article.getPreviewImage();


        article.setTitle(articleModel.getTitle());
        article.setTopHead(articleModel.isTopHead());
        article.setSummary(articleModel.getSummary());
        article.setContent(articleModel.getContent());
        article.setUpdateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setPublicDate(articleModel.getPublicDate());
        article.setLayoutType(articleModel.getLayoutType());
        article.setAuthor(articleModel.getAuthor());
        article.setPreviewImage(articleModel.getNewsFiles());
//        article.setContentImage(ContentUtil.captureImgUrls(articleModel.getContent()));

        article = articleRepository.save(article);

        ArticleModel model = new ArticleModel();
        model.setNewsFiles(oldImgs);
        model.setContent(oldContent);
        return ApiResult.resultWith(ResultCode.SUCCESS, model);
    }

    @Override
    public Page<Article> findAll(ArticleSearch articleSearch, int pageIndex, int pageSize, Sort sort) {
        Specification<Article> specification = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(articleSearch.getUserId())) {
                predicates.add(cb.equal(root.get("user").get("id").as(Long.class), articleSearch.getUserId()));
            }
            if (articleSearch.getEnabled() != null) {
                predicates.add(cb.equal(root.get("enabled").as(Boolean.class), articleSearch.getEnabled()));
            }
            if (articleSearch.getTopHead() != null) {
                predicates.add(cb.equal(root.get("topHead").as(Boolean.class), articleSearch.getTopHead()));
            }
            if (!StringUtils.isEmpty(articleSearch.getArticleStatus())) {
                predicates.add(cb.equal(root.get("articleStatus").as(ArticleType.ArticleStatus.class), articleSearch.getArticleStatus()));
            }
            if (!StringUtils.isEmpty(articleSearch.getLayoutType())) {
                predicates.add(cb.equal(root.get("layoutType").as(ArticleType.LayoutEnum.class), EnumHelper.getEnumType(ArticleType.LayoutEnum.class, articleSearch.getLayoutType())));
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
        return articleRepository.findAll(specification, new PageRequest(pageIndex - 1, pageSize, sort));
    }

    @Override
    public synchronized ApiResult operateArticle(Long id, ArticleType.ArticleOperateEnum articleOperateEnum) {
        Article article = articleRepository.findOne(id);
        if (article == null) {
            return ApiResult.resultWith(ResultCode.NO_ARTICLE);
        }
        if (!article.getEnabled()) {
            return ApiResult.resultWith(ResultCode.ERROR, "该文章已删除,无法操作该文章");
        }
        switch (articleOperateEnum) {
            case DELETE: { // 删除
                if (article.getArticleStatus() == ArticleType.ArticleStatus.RELEASE) {
                    return ApiResult.resultWith(ResultCode.ERROR, "不能删除已发布的文章");
                }
                article.setEnabled(false);
                break;
            }
            case RELEASE: {// 发布
                article.setArticleStatus(ArticleType.ArticleStatus.RELEASE);
                break;
            }
            case OFFRELEASE: {// 取消发布
                article.setArticleStatus(ArticleType.ArticleStatus.UNRELEASE);
                break;
            }
            default: {
                return ApiResult.resultWith(ResultCode.ERROR, "不支持的操作类型");
            }
        }
        articleRepository.save(article);
        return ApiResult.resultWith(ResultCode.SUCCESS);
    }

    /**
     * 判断旧的图片url是否存在于新的url列表中
     *
     * @param olds
     * @param news
     * @return
     */
    private boolean containsImg(String olds, String[] news) {
        for (int i = 0; i < news.length; i++) {
            if (olds.equals(news[i])) {
                return true;
            }
        }
        return false;
    }
}
