package com.leizy.demo;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;

import cn.leizy.lib.base.BaseActivity;

/**
 * @author Created by wulei
 * @date 2020/9/4, 004
 * @description
 */
public class TestAcitvity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initViews() {
        router("main","group").navigation(this, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {

            }

            @Override
            public void onLost(Postcard postcard) {

            }

            @Override
            public void onArrival(Postcard postcard) {

            }

            @Override
            public void onInterrupt(Postcard postcard) {

            }
        });
    }
}
