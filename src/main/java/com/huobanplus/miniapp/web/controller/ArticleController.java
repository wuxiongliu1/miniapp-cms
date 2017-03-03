package com.huobanplus.miniapp.web.controller;

import com.huobanplus.miniapp.web.annotations.UserAuthenticationPrincipal;
import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ArticleType;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleModel;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.service.ArticleService;
import com.huobanplus.miniapp.web.service.ResourceService;
import com.huobanplus.miniapp.web.util.ContentUtil;
import com.huobanplus.miniapp.web.util.EnumHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
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
        articleSearch.setEnabled(true);// 设置文章状态为未删除条件
        Page<Article> articlePage = articleService.findAll(articleSearch, pageIndex, pageSize, new Sort(Sort.Direction.DESC, "updateTime"));
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
        articleModel.setNewsFiles(article.getPreviewImage());
        articleModel.setTopHead(article.isTopHead());
        articleModel.setLayoutCode(article.getLayoutType().getCode());
        model.addAttribute("article", articleModel);
        return "editNews";
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
        model.addAttribute("user", user);
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
     * @param
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public ApiResult addArticle(@UserAuthenticationPrincipal User user, String newsTittle, String newsSummary,
                                String newsAuthor, String editorValue,
                                boolean isBanner, int newsType, @RequestParam(value = "newsFiles[]", defaultValue = "") String[] newsFiles,
                                @RequestParam(defaultValue = "0") int code) {

        ArticleModel articleModel = new ArticleModel();
        articleModel.setTitle(newsTittle);
        articleModel.setSummary(newsSummary);
        articleModel.setAuthor(newsAuthor);
        articleModel.setContent(editorValue);
        articleModel.setTopHead(isBanner);
        articleModel.setNewsFiles(newsFiles);
        articleModel.setLayoutType(EnumHelper.getEnumType(ArticleType.LayoutEnum.class, newsType));
        articleModel.setArticleStatus(EnumHelper.getEnumType(ArticleType.ArticleStatus.class, code));

        return articleService.addArticle(user, articleModel);
    }

    /**
     * 更新文章
     *
     * @param articleId
     * @param newsTittle
     * @param newsSummary
     * @param newsAuthor
     * @param editorValue
     * @param isBanner
     * @param newsType
     * @param newsFiles
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ApiResult updateArticle(@PathVariable(value = "id") Long articleId, String newsTittle, String newsSummary,
                                   String newsAuthor, String editorValue,
                                   boolean isBanner, int newsType,
                                   @RequestParam(value = "newsFiles[]", defaultValue = "") String[] newsFiles,
                                   HttpServletRequest request) {

        ArticleModel articleModel = new ArticleModel();
        articleModel.setId(articleId);
        articleModel.setTitle(newsTittle);
        articleModel.setSummary(newsSummary);
        articleModel.setAuthor(newsAuthor);
        articleModel.setContent(editorValue);
        articleModel.setTopHead(isBanner);
        articleModel.setNewsFiles(newsFiles);
        articleModel.setLayoutType(EnumHelper.getEnumType(ArticleType.LayoutEnum.class, newsType));

        ApiResult apiResult = articleService.updateArticle(articleModel);
        if (apiResult.getResultCode() == ResultCode.SUCCESS.getResultCode()) {

            ArticleModel model = (ArticleModel) apiResult.getData();
            if (model != null) {
                String[] oldPreviewImgs = model.getNewsFiles();
                String[] oldContentImgs = ContentUtil.captureImgUrls(model.getContent());
                ServletContext servletContext = request.getSession().getServletContext();

                if (oldPreviewImgs != null) {
                    for (String oldPreviewImg : oldPreviewImgs) {
                        if (!containsImg(oldPreviewImg, articleModel.getNewsFiles())) {
                            resourceService.removeRes(servletContext.getRealPath(oldPreviewImg));
                        }
                    }
                }

                if (oldContentImgs != null) {
                    for (String oldContentImg : oldContentImgs) {
                        if (!containsImg(oldContentImg, ContentUtil.captureImgUrls(articleModel.getContent()))) {
                            resourceService.removeRes(servletContext.getRealPath(oldContentImg));
                        }
                    }
                }

            }
        }
        return apiResult;
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
        return "previewPage";
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
        ApiResult apiResult = articleService.deleteArticle(user.getId(), articleId);
        // 同时删除关联的图片
        return apiResult;
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

    /**
     *  文章的操作
     *
     * @param articleId
     * @param code 0:删除 1:发布 2:取消发布
     * @return
     */
    @RequestMapping(value = "/operate/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ApiResult operateArticle(@PathVariable(value = "id") Long articleId, int code) {
        ArticleType.ArticleOperateEnum operateEnum = EnumHelper.getEnumType(ArticleType.ArticleOperateEnum.class, code);
        if (operateEnum == null) {
            return ApiResult.resultWith(ResultCode.ERROR, "不识别的操作码:" + code);
        }
        return articleService.operateArticle(articleId, operateEnum);

    }

}
