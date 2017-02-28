package com.huobanplus.miniapp.web.controller.open;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.model.BannerArticle;
import com.huobanplus.miniapp.web.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuxiongliu on 2017-02-20.
 */
@Controller
@RequestMapping(value = "/open/article")
public class ArticleOpenController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/toUpload")
    public String toUpload() {
        return "test/upload";
    }

    /**
     * 文章列表
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ApiResult articleList(ArticleSearch articleSearch,
                                 @RequestParam(defaultValue = "1") int pageIndex,
                                 @RequestParam(defaultValue = "50") int pageSize,
                                 Model model) {
        articleSearch.setEnabled(true);
        Page<Article> articlePage = articleService.findAll(articleSearch, pageIndex, pageSize, new Sort("updateTime"));
        List<Article> articleList = articlePage.getContent();
        return ApiResult.resultWith(ResultCode.SUCCESS, articleList);
    }

    /**
     * banner列表
     *
     * @return
     */
    @RequestMapping(value = "/banners")
    @ResponseBody
    public ApiResult bannerList(ArticleSearch articleSearch) {
        articleSearch.setEnabled(true);
        Page<Article> articlePage = articleService.findAll(articleSearch, 1, 4, new Sort("updateTime"));// 取前面四篇的最新文章
        List<Article> articleList = articlePage.getContent();
        List<BannerArticle> bannerArticleList = new ArrayList<>();
        articleList.forEach(article -> {
            BannerArticle bannerArticle = new BannerArticle();
            bannerArticle.setId(article.getId());
            bannerArticle.setTitle(article.getTitle());
            bannerArticle.setLayoutType(article.getLayoutType().getCode());
            String[] previewImgs = article.getPreviewImage();
            if (previewImgs.length > 0) {
                bannerArticle.setImgSrc(previewImgs[0]);// 取第一张图
            } else {
                bannerArticle.setImgSrc("");// 照一张默认的？？
            }
            bannerArticleList.add(bannerArticle);
        });
        return ApiResult.resultWith(ResultCode.SUCCESS, bannerArticleList);
    }
}
