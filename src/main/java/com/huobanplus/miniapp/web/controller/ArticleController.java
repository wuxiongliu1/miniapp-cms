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
    @RequestMapping(method = RequestMethod.POST)
    public String articleList(ArticleSearch articleSearch,
                              @RequestParam(defaultValue = "1") int pageIndex,
                              @RequestParam(defaultValue = "5") int pageSize,
                              Model model) {
        Page<Article> articlePage = articleService.findAll(articleSearch, pageIndex, pageSize);
        List<Article> articleList = articlePage.getContent();

        model.addAttribute("articleList", articleList);
        model.addAttribute("articleSearch", articleSearch);
        model.addAttribute("hasPrev", articlePage.hasPrevious());
        model.addAttribute("hasNext", articlePage.hasNext());
        model.addAttribute("pageNum", articlePage.getTotalPages());
        model.addAttribute("pageIndex", pageIndex);
        model.addAttribute("pageSize", pageSize);

        return "index";
    }

    /**
     * 跳转到编辑文章页面
     *
     * @param articleId
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String toEditArticle(@PathVariable(value = "id") Long articleId, Model model) throws Exception {
        Article article = articleService.findArticle(articleId);
        if (article == null) {
            throw new Exception("未找到该文章");
        }
        model.addAttribute("article", article);
        return "ArticleEdit";
    }

    /**
     * 跳转到新增文章页面
     *
     * @param user
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAddArticle(@UserAuthenticationPrincipal(value = "user") User user, Model model) throws Exception {
//        model.addAttribute("user",user);
        return "addNews";
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
        articleList.forEach(article -> {
            if (article.getPreviewImage() != null) {
                article.setPreviewImgArray(article.getPreviewImage().split("\\|"));
            } else {
                article.setPreviewImgArray(new String[]{});
            }
        });
        return ApiResult.resultWith(ResultCode.SUCCESS, articleList);
    }

    /**
     * 新增文章
     *
     * @param user
     * @param newsTittle
     * @param newsSummary
     * @param newsAuthor
     * @param editorValue
     * @param date
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    public ApiResult addArticle(@UserAuthenticationPrincipal User user, String newsTittle, String newsSummary, String newsAuthor, String editorValue, String date) {
        return articleService.addArticle(user, newsTittle, newsSummary, editorValue, newsAuthor, date);
    }

    /**
     * 文章详情
     *
     * @param articleId
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String articleDetail(@PathVariable(value = "id") Long articleId, Model model) throws Exception {
        Article article = articleService.findArticle(articleId);
        if (article == null) {
            throw new Exception("未找到该文章");
        }
        model.addAttribute("article", article);
        return "ArticleDetail";
    }

    /**
     * 删除文章
     *
     * @param user
     * @param articleId
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ApiResult deleteArticle(@UserAuthenticationPrincipal(value = "user") User user, @PathVariable(value = "id") Long articleId) {
        return articleService.deleteArticle(user.getId(), articleId);
    }

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
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResult updateArticle(@PathVariable(value = "id") Long articleId, String title, String summary, String content, String rawContent) {
        return articleService.updateArticle(articleId, title, summary, content, rawContent);
    }
}
