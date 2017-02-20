package com.huobanplus.miniapp.web.common;

/**
 * Created by wuxiongliu on 2017-02-20.
 */
public interface ArticleType {

    enum LayoutEnum implements ICommonEnum {
        NO_PIC(0, "无图"),
        BANNER(1, "头条"),
        ONE_PIC(2, "单图"),
        MULTI_PIC(3, "多图");

        private int code;
        private String name;

        LayoutEnum(int code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public int getCode() {
            return this.code;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }


}
