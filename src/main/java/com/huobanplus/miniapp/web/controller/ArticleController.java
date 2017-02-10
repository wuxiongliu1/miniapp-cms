package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章列表
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public String articleList(ArticleSearch articleSearch, int pageIndex, int pageSize, Model model) {
        Page<Article> articlePage = articleService.findAll(articleSearch, pageIndex, pageSize);
        List<Article> articleList = articlePage.getContent();
        model.addAttribute("articleList", articleList);
        return "articleList";
    }

    /**
     * 开放接口
     *
     * @param articleSearch
     * @param pageIndex
     * @param pageSize
     * @param model
     * @return
     */
    @RequestMapping(value = "/open", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult articleList2(ArticleSearch articleSearch,
                                  @RequestParam(defaultValue = "1") int pageIndex,
                                  @RequestParam(defaultValue = "50") int pageSize,
                                  Model model) {
        Page<Article> articlePage = articleService.findAll(articleSearch, pageIndex, pageSize);
        List<Article> articleList = articlePage.getContent();
        model.addAttribute("articleList", articleList);
        return ApiResult.resultWith(ResultCode.SUCCESS, articleList);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public ApiResult addArticle(@UserAuthenticationPrincipal User user, String title, String summary, String content, String rawContent) {
        return articleService.addArticle(user, title, summary, content, rawContent);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String articleDetail(@PathVariable(value = "id") Long articleId) {
        Article article = articleService.findArticle(articleId);
        return "";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResult deleteArticle(@UserAuthenticationPrincipal(value = "user") User user, @PathVariable(value = "id") Long articleId) {
        return articleService.deleteArticle(user.getId(), articleId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResult updateArticle(@PathVariable(value = "id") Long articleId, String title, String summary, String content, String rawContent) {
        return articleService.updateArticle(articleId, title, summary, content, rawContent);
    }
}
