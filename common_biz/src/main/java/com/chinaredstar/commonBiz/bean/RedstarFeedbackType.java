package com.chinaredstar.commonBiz.bean;

import com.xiwa.base.bean.Identified;

/**
 * Created by lenovo on 2016/5/18.
 */
public class RedstarFeedbackType implements Identified {
    private  int id;

    private String name;//  姓名

    private String alias;//  别名

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
