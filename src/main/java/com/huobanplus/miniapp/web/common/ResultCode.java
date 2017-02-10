/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huobanplus.miniapp.web.common;

/**
 * Created by allan on 2015/8/5.
 */
public enum ResultCode {
    SUCCESS(200, "返回成功"),
    NO_PERMISSTION(500, "没有权限"),
    NO_USER(400, "没有该用户"),
    NO_ARTICLE(404, "找不到该文章"),
    ERROR(-1, "处理失败");

    private int resultCode;
    private String resultMsg;

    ResultCode(int resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
