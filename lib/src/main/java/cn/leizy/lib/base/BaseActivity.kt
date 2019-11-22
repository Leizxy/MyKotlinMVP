package cn.leizy.lib.base

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
abstract class BaseActivity : AppCompatActivity() {
    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setContentView(getLayoutId())
        unbinder = ButterKnife.bind(this)
    }

    abstract fun getLayoutId(): Int

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        initViews()
    }

    abstract fun initViews()

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

}