package com.huobanplus.miniapp.web.service;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.config.MiniAppTestBase;
import com.huobanplus.miniapp.web.entity.Article;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.repository.ArticleRepository;
import com.huobanplus.miniapp.web.repository.UserRepository;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
        User user = (User) userService.addUser("wuxiongliu", "12345").getData();

        Article article = new Article();
        article.setTitle("title");
        article.setSummary("test");
        article.setContent("this is cms");
        article.setRawContent("raw content");
        article.setCreateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUpdateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        article.setUser(user);

        ApiResult apiResult = articleService.addArticle(article);
        article = (Article) apiResult.getData();
        Assert.assertNotNull(article.getId());

        articleService.addArticle(user, "title2", "test2", "content2");
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
        article.setRawContent("raw content");
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
}