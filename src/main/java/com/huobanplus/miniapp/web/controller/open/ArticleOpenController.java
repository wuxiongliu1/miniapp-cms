package com.huobanplus.miniapp.web.controller.open;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ArticleType;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.model.ArticleModel;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.model.BannerArticle;
import com.huobanplus.miniapp.web.service.ArticleService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "testurl")
    @ResponseBody
    public String testUrl(HttpServletRequest request) {
        String contentPath = request.getServletContext().getContextPath();
        String realPath = request.getServletContext().getRealPath("/");
        System.out.println("contentpath:" + contentPath);
        System.out.println("realpath:" + realPath);
        return contentPath + "|" + realPath;
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
                                 Model model, HttpServletRequest request) {
        String prefixUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        articleSearch.setEnabled(true);// 未删除
        articleSearch.setArticleStatus(ArticleType.ArticleStatus.RELEASE);// 已发布
        Page<Article> articlePage = articleService.findAll(articleSearch, pageIndex, pageSize, new Sort(Sort.Direction.DESC, "updateTime"));
        List<Article> articleList = articlePage.getContent();
        List<ArticleModel> articleModelList = new ArrayList<>();
        for (Article article : articleList) {
            ArticleModel articleModel = new ArticleModel();
            articleModel.setId(article.getId());
            articleModel.setTitle(article.getTitle());
            articleModel.setAuthor(article.getAuthor());
//            articleModel.setContent(article.getContent());

            String[] completeImgUrl = new String[article.getPreviewImage().length];
            String[] previewImage = article.getPreviewImage();
            for (int i = 0; i < previewImage.length; i++) {
                completeImgUrl[i] = prefixUrl + previewImage[i];
            }
            articleModel.setNewsFiles(completeImgUrl);
            articleModel.setTopHead(article.isTopHead());
//            articleModel.setLayoutType(article.getLayoutType());
            articleModel.setLayoutCode(article.getLayoutType().getCode());
            articleModel.setSummary(article.getSummary());
            articleModel.setCreateTime(article.getCreateTime());
            articleModel.setUpdateTime(article.getUpdateTime());
            articleModelList.add(articleModel);
        }
        return ApiResult.resultWith(ResultCode.SUCCESS, articleModelList);
    }

    /**
     * 文章详情
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult articleDetail(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        String prefixUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        Article article = articleService.findArticle(id);// todo 判断文章状态
        if (article == null) {
            return ApiResult.resultWith(ResultCode.NO_ARTICLE);
        }

        if (!article.getEnabled()) {
            return ApiResult.resultWith(ResultCode.NO_ARTICLE);
        }

        article.setUser(null);
        // 需要将文章中的所有图片地址设置为url
        String content = article.getContent();
        Document document = Jsoup.parseBodyFragment(content);
        Elements nodes = document.select("img");
        int nodeLenth = nodes.size();
        for (int i = 0; i < nodeLenth; i++) {
            Element e = nodes.get(i);
            String dataSrc = e.attr("src");
            if (!dataSrc.startsWith("http")) {
                e.attr("src", prefixUrl + dataSrc);
            }
        }
        article.setContent(document.toString());

        return ApiResult.resultWith(ResultCode.SUCCESS, article);
    }

    /**
     * banner列表
     *
     * @return
     */
    @RequestMapping(value = "/banners")
    @ResponseBody
    public ApiResult bannerList(ArticleSearch articleSearch, HttpServletRequest request) {
        String prefixUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        articleSearch.setEnabled(true);// 未删除
        articleSearch.setArticleStatus(ArticleType.ArticleStatus.RELEASE);// 已发布
        Page<Article> articlePage = articleService.findAll(articleSearch, 1, 4, new Sort(Sort.Direction.DESC, "updateTime"));// 取前面四篇的最新文章
        List<Article> articleList = articlePage.getContent();
        List<BannerArticle> bannerArticleList = new ArrayList<>();
        articleList.forEach(article -> {
            BannerArticle bannerArticle = new BannerArticle();
            bannerArticle.setId(article.getId());
            bannerArticle.setTitle(article.getTitle());
            bannerArticle.setLayoutType(article.getLayoutType().getCode());
            String[] previewImgs = article.getPreviewImage();
            if (previewImgs != null && previewImgs.length > 0) {
                bannerArticle.setImgSrc(prefixUrl + previewImgs[0]);// 取第一张图
            } else {
                bannerArticle.setImgSrc("");// 照一张默认的？？
            }
            bannerArticleList.add(bannerArticle);
        });
        return ApiResult.resultWith(ResultCode.SUCCESS, bannerArticleList);
    }
}
