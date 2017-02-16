package com.huobanplus.miniapp.web.repository;

import com.huobanplus.miniapp.web.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by wuxiongliu on 2017-02-10.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}
