package com.huobanplus.miniapp.web.service;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ArticleType;
import com.huobanplus.miniapp.web.config.MiniAppTestBase;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.model.ArticleSearch;
import com.huobanplus.miniapp.web.repository.ArticleRepository;
import com.huobanplus.miniapp.web.repository.UserRepository;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.UUID;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
public class ArticleServiceTest extends MiniAppTestBase {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    private User mockUser;

    private Article mockArticle;

    @Before
    public void init() {
        mockUser = initUser();
        mockArticle = initArticle(mockUser);
    }

    @Test
    public void test() {
        Assert.assertNotNull(articleService);
    }


    @Test
    @Rollback(value = false)
    public void testAddArticle() {

        Article article = new Article();
        article.setTitle("title");
        article.setSummary("test");
        article.setContent("this is cms");
        article.setLayoutType(ArticleType.LayoutEnum.NO_PIC);
        article.setCreateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUpdateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUser(mockUser);

        article.setPreviewImage("fafasdfsadf|fasdfadsfasd");

        ApiResult apiResult = articleService.addArticle(article);
        article = (Article) apiResult.getData();
        Assert.assertNotNull(article.getId());

        articleService.addArticle(mockUser, "title2", "test2", "content2", "", "");
    }

    private User initUser() {
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        user.setPassword("12345");
        user.setCreateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        user.setEnabled(true);
        return userRepository.save(user);
    }

    private Article initArticle(User user) {
        Article article = new Article();
        article.setTitle("title");
        article.setSummary("test");
        article.setContent("this is cms");
        article.setCreateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUpdateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUser(user);

        return articleRepository.save(article);
    }

    @Test
    public void testDeleteArticle() {
        User user = initUser();
        Article article = initArticle(user);
        Assert.assertNotNull(articleRepository.findOne(article.getId()));
        articleService.deleteArticle(user.getId(), article.getId());
        Assert.assertNull(articleRepository.findOne(article.getId()));

    }

    @Test
    public void testUpdateArticle() {
        String title = "updateTitle";
        ApiResult apiResult = articleService.updateArticle(mockArticle.getId(), title, null, null, null);
        Article article = (Article) apiResult.getData();
        Assert.assertEquals(title, article.getTitle());

    }

    @Test
    public void testFindAll(){
        Page<Article> articlePage = articleService.findAll(new ArticleSearch(), 1, 3, new Sort("updateTime"));
        System.out.println("\n*******************************");
        System.out.println(articlePage.getContent());
        System.out.println("\n*******************************");
    }

    private String generateFilePath(Long userId) {
        StringBuilder sb = new StringBuilder();
        sb.append("/upload/")
                .append(userId)
                .append("/image/")
                .append(StringUtil.DateFormat(new Date(), "yyyyMMdd")).append("/");
        return sb.toString();
    }

    @Test
    public void generateFilePath() {
        Long id = 1L;
        System.out.println(generateFilePath(id));
        System.out.println(RandomStringUtils.randomNumeric(6));
    }
}
