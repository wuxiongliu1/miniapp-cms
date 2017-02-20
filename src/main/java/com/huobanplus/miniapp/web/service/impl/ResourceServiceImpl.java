package com.huobanplus.miniapp.web.service.impl;

import com.huobanplus.miniapp.web.service.ResourceService;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Created by wuxiongliu on 2017-02-20.
 */
@Service
public class ResourceServiceImpl implements ResourceService {

    @Override
    public void uploadRes() {

    }

    @Override
    public void removeRes(String resPath) {
        File file = new File(resPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
