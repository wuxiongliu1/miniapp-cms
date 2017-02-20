package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleModel;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.service.ArticleService;
import com.huobanplus.miniapp.web.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ResourceService resourceService;

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
        Page<Article> articlePage = articleService.findAll(articleSearch, pageIndex, pageSize, new Sort("updateTime"));
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
        ArticleModel articleModel = new ArticleModel();
        articleModel.setId(article.getId());
        articleModel.setTitle(article.getTitle());
        articleModel.setSummary(article.getSummary());
        articleModel.setContent(article.getContent());
        articleModel.setPublicDate(article.getPublicDate());
        articleModel.setAuthor(article.getAuthor());
        articleModel.setLayoutType(article.getLayoutType());
        articleModel.setPreviewImages(article.getPreviewImage());
        articleModel.setOldImgs(article.getPreviewImage().split("\\|"));
        model.addAttribute("articleModel", articleModel);
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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
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

    /**
     * 更新文章
     *
     * @param articleId
     * @param articleModel
     * @return
     */
    @RequestMapping(value = "/test/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResult updateArticle2(@PathVariable(value = "id") Long articleId, ArticleModel articleModel, HttpServletRequest request) {
        articleModel.setId(articleId);

        ApiResult apiResult = articleService.updateArticle(articleModel);
        if (apiResult.getResultCode() == ResultCode.SUCCESS.getResultCode()) {
            // 删除文章关联的图片
//            String[] oldImgs = articleModel.getOldImgs().split("\\|");
            String[] oldImgs = articleModel.getOldImgs();
            for (String oldImg : oldImgs) {
                String realPath = request.getSession().getServletContext().getRealPath(oldImg);
                resourceService.removeRes(realPath);
            }
        }
        Article article = (Article) apiResult.getData();
        article.setUser(null);
        apiResult.setData(article);
        return apiResult;
    }
}
