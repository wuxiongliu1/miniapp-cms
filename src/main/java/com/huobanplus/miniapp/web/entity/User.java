package com.huobanplus.miniapp.web.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by wuxiongliu on 2017-02-10.
 */

@Entity
@Table(name = "cms_user")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String username;

    @Column(name = "pass_word")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "create_time")
    private String createTime;

    /**
     * 用户的头像url
     */
    @Column(name = "avatar")
    private String avatar;

}
