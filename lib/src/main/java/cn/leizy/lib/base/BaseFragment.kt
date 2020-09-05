package cn.leizy.lib.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * @author Created by wulei
 * @date 2019-11-18
 * @description
 */
abstract class BaseFragment : Fragment() {
    private var isViewCreated: Boolean = false
    private var currentVisibleState: Boolean = false
    private var isFirstVisible: Boolean = true
    private var unbinder: Unbinder? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView:View = inflater.inflate(getLayoutId(), container, false)
        attachPresenter()
        isViewCreated = true
        if (!isHidden && userVisibleHint) {
            dispatchVisibleState(true)
        }
        unbinder = ButterKnife.bind(this, rootView)
        initView(rootView)
        //解决点击穿越问题
        rootView.isClickable = true
        return rootView
    }

    protected open fun attachPresenter() {}

    abstract fun getLayoutId(): Int

    abstract fun initView(view: View)

    override fun onResume() {
        super.onResume()
        if (!isFirstVisible) {
            if (!isHidden && !currentVisibleState && userVisibleHint) {
                dispatchVisibleState(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (currentVisibleState && userVisibleHint)
            dispatchVisibleState(false)
    }

    override fun onDestroyView() {
        unbinder?.unbind()
        super.onDestroyView()
        reset()
    }

    private fun reset() {
        isViewCreated = false
        isFirstVisible = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isViewCreated) {
            if (currentVisibleState && !isVisibleToUser) {
                dispatchVisibleState(false)
            } else if (!currentVisibleState && isVisibleToUser) {
                dispatchVisibleState(true)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        dispatchVisibleState(!hidden)
    }

    private fun dispatchVisibleState(isVisible: Boolean) {
        if (isVisible && isParentInvisible()) return
        if (isVisible == currentVisibleState) return
        currentVisibleState = isVisible
        if (isVisible) {
            if (isFirstVisible) {
                isFirstVisible = false
                onFragmentFirstVisible()
            }
            onFragmentResume()
            dispatchChildVisibilityState(true)
        } else {
            onFragmentPause()
            dispatchChildVisibilityState(false)
        }
    }

    private fun isParentInvisible(): Boolean {
        val parent = parentFragment
        if (parent is BaseFragment) {
            val lz: BaseFragment = parent
            return !lz.currentVisibleState
        }
        return false
    }

    private fun dispatchChildVisibilityState(isVisible: Boolean) {
        val fragmentManager = childFragmentManager
        fragmentManager.fragments.forEach {
            if (it is BaseFragment && !it.isHidden && it.userVisibleHint) {
                it.dispatchVisibleState(isVisible)
            }
        }
    }

    protected fun onFragmentResume() {
        Log.i(this.javaClass.simpleName, "onFragmentResume: ")
    }

    protected fun onFragmentPause() {
        Log.i(this.javaClass.simpleName, "onFragmentPause: ")
    }

    protected fun onFragmentFirstVisible() {
        Log.i(this.javaClass.simpleName, "onFragmentFirstVisible: ")
    }
}