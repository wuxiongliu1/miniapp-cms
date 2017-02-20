/*
 * 版权所有:杭州火图科技有限公司
 * 地址:浙江省杭州市滨江区西兴街道阡陌路智慧E谷B幢4楼
 *
 * (c) Copyright Hangzhou Hot Technology Co., Ltd.
 * Floor 4,Block B,Wisdom E Valley,Qianmo Road,Binjiang District
 * 2013-2016. All rights reserved.
 */

package com.huobanplus.miniapp.web.entity.support;


import com.huobanplus.miniapp.web.common.ArticleType;
import com.huobanplus.miniapp.web.util.EnumHelper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * Created by allan on 4/15/16.
 */
@Converter(autoApply = true)
public class LayoutEnumConverter implements AttributeConverter<ArticleType.LayoutEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(ArticleType.LayoutEnum layoutEnum) {
        if (layoutEnum == null) {
            return null;
        }
        return layoutEnum.getCode();
    }

    @Override
    public ArticleType.LayoutEnum convertToEntityAttribute(Integer integer) {
        if (integer == null) {
            return null;
        }
        return EnumHelper.getEnumType(ArticleType.LayoutEnum.class, integer);
    }
}
