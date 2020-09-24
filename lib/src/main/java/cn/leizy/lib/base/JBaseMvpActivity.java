package cn.leizy.lib.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import java.lang.reflect.ParameterizedType;

import cn.leizy.lib.base.mvp.IView;

/**
 * @author Created by wulei
 * @date 2019/7/31
 * @description
 */
public abstract class JBaseMvpActivity<P extends JBasePresenter> extends BaseActivity implements IView {
    protected P presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        presenter = getPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        super.onDestroy();
    }

    /**
     * 让子类自己创建对应的Presenter
     * @return 返回创建的presenter
     */
//    protected abstract P createPresenter();

    /**
     * 反射获取Presenter真实类型
     *
     * @return 返回反射得到的Presenter真实类型。
     */
    private P getPresenter() {
        P p;
        try {
            // 通过反射获取model的真实类型
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<P> clazz = (Class<P>) pt.getActualTypeArguments()[0];
            // 通过反射创建model的实例
            p = clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return p;
    }
}
