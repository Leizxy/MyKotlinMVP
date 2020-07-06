package cn.leizy.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import butterknife.ButterKnife
import butterknife.Unbinder
import cn.leizy.lib.R

/**
 * @author Created by wulei
 * @date 2019/11/19, 019
 * @description
 */
abstract class BaseDialogFragment : DialogFragment() {
    private var unbinder: Unbinder? = null
    private val SAVED_DIALOG_STATE_TAG = "android:savedDialogState"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.lib_default_dialog_style)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (showsDialog) {
            showsDialog = false
        }
        super.onActivityCreated(savedInstanceState)
        showsDialog = true
        if (view != null) {
            if (view!!.parent != null) {
                throw IllegalStateException("DialogFragment can not be attached to a container view")
            }
            dialog.setContentView(view!!)
        }
        if (activity != null) {
            dialog.setOwnerActivity(activity!!)
        }
        if (savedInstanceState != null) {
            val bundle: Bundle = savedInstanceState.getBundle(SAVED_DIALOG_STATE_TAG)!!
            dialog.onRestoreInstanceState(bundle)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view = inflater.inflate(getDialogLayout(), container)
        unbinder = ButterKnife.bind(this, view)
        initView(view)
        return view
    }

    abstract fun getDialogLayout(): Int

    abstract fun initView(view: View)

    override fun onDestroyView() {
        super.onDestroyView()
        unbinder?.unbind()
        unbinder = null
    }
}
