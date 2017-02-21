package com.huobanplus.miniapp.web.common;

/**
 * Created by wuxiongliu on 2017-02-20.
 */
public interface ArticleType {

    /**
     * 文章的布局类型
     */
    enum LayoutEnum implements ICommonEnum {
        NO_PIC(0, "无图"),
        SMALL_PIC(1, "小图"),
        BIG_PIC(2, "大图"),
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

    enum ArticleStatus implements ICommonEnum {
        UNRELEASE(0, "未发布"),
        RELEASE(1, "已发布"),
        OFFRELEASE(2, "已下架");

        private int code;

        private String name;

        ArticleStatus(int code, String name) {
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

    enum ArticleOperateEnum implements ICommonEnum {
        DELETE(0, "删除"),
        RELEASE(1, "发布"),
        OFFRELEASE(2, "取消发布");

        private int code;
        private String name;

        ArticleOperateEnum(int code, String name) {
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
