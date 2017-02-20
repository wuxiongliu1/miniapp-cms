package com.huobanplus.miniapp.web.service;

/**
 * Created by wuxiongliu on 2017-02-20.
 */
public interface ResourceService {

    /**
     * 上传文件
     */
    void uploadRes();

    /**
     * 删除文件
     *
     * @param resPath 资源绝对路径
     */
    void removeRes(String resPath);
}
