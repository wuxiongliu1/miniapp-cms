package com.huobanplus.miniapp.web.service;

import com.huobanplus.miniapp.web.common.ApiResult;
import com.huobanplus.miniapp.web.entity.User;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
public interface UserService {

    User findUser(String username, String password);

    ApiResult addUser(String username, String password);

    ApiResult updateUser(Long id, String username, String password);

    ApiResult deleteUser(User user);
}
