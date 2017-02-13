package com.huobanplus.miniapp.web.service.impl;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.common.ResultCode;
import com.huobanplus.miniapp.web.entity.User;
import com.huobanplus.miniapp.web.repository.UserRepository;
import com.huobanplus.miniapp.web.service.UserService;
import com.huobanplus.miniapp.web.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 初始化一个管理员
     */
    @PostConstruct
    public void initAdmin(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword("12345");
        user.setEnabled(true);
        user.setCreateTime(StringUtil.DateFormat(new Date(),StringUtil.TIME_PATTERN));
        userRepository.save(user);
    }

    @Override
    public User findUser(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public ApiResult addUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setCreateTime(StringUtil.DateFormat(new Date(), StringUtil.TIME_PATTERN));
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        return ApiResult.resultWith(ResultCode.SUCCESS, savedUser);
    }

    @Override
    public ApiResult updateUser(Long id, String username, String password) {
        User user = userRepository.findOne(id);
        if (user == null) {
            return ApiResult.resultWith(ResultCode.NO_USER);
        }

        user.setUsername(username);
        user.setPassword(password);
        User updatedUser = userRepository.save(user);
        return ApiResult.resultWith(ResultCode.SUCCESS, updatedUser);
    }

    @Override
    public ApiResult deleteUser(User user) {
        userRepository.delete(user);
        return ApiResult.resultWith(ResultCode.SUCCESS);
    }
}
