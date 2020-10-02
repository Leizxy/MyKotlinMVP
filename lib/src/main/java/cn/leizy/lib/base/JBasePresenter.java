package cn.leizy.lib.base;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;

import cn.leizy.lib.base.mvp.IModel;
import cn.leizy.lib.base.mvp.IView;

/**
 * @author Created by wulei
 * @date 2019/7/31
 * @description
 */
public abstract class JBasePresenter<V extends IView, M extends IModel> implements JPresenter<V, M> {
    protected V view;
    protected M model;
    private WeakReference<V> weakReference;

    @Override
    public void attachView(V view) {
        weakReference = new WeakReference<>(view);
        this.view = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new MvpViewHandler(weakReference.get()));
        if (this.model == null) {
            this.model = createModel();
            /*if (createModel() != null) {
            } else {
                this.model = getModel();
            }*/
        }
    }

    protected abstract M createModel();

    private M getModel() {
        M m;
        try {
            ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<M> cls = (Class<M>) type.getActualTypeArguments()[1];
            m = cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return m;
    }

    @Override
    public void detachView() {
        this.model = null;
        if (isViewAttached()) {
            weakReference.clear();
            weakReference = null;
        }
    }

    /**
     * 是否与View建立连接
     */
    protected boolean isViewAttached() {
        return weakReference != null && weakReference.get() != null;
    }

    /**
     * View代理类  防止 页面关闭P异步操作调用V 方法 空指针问题
     */
    private class MvpViewHandler implements InvocationHandler {

        private IView mvpView;

        MvpViewHandler(IView mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //如果V层没被销毁, 执行V层的方法.
            if (isViewAttached()) {
                return method.invoke(mvpView, args);
            } //P层不需要关注V层的返回值
            return null;
        }
    }
}
