package cn.leizy.lib.util

import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.lang.Exception
import kotlin.collections.HashMap

/**
 * @author Created by wulei
 * @date 2019-11-16
 * @description
 */
class MyLiveDataBus private constructor() {
    private val bus: HashMap<String, MyMutableLiveData<Any>>

    init {
        bus = hashMapOf<String, MyMutableLiveData<Any>>()
    }

    companion object {
        val instance = Holder.holder
    }

    private object Holder {
        val holder = MyLiveDataBus()
    }

    @Suppress("UNUSED_PARAMETER")
    fun <T> with(key: String, type: Class<T>): MyMutableLiveData<Any> {
        if (!bus.containsKey(key)) {
            bus[key] = MyMutableLiveData()
        }
        return bus[key] as MyMutableLiveData<Any>
    }

    fun with(target: String): MyMutableLiveData<Any> {
        return with(target, Any::class.java)
    }

    fun <T> post(key: String, t: T) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            with(key).value = t
        } else {
            with(key).postValue(t)
        }
    }

    class MyMutableLiveData<T> : MutableLiveData<T>() {
        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            try {
                hook(observer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        @Throws(Exception::class)
        private fun hook(observer: Observer<in T>) {
            val classLiveData = LiveData::class.java
            val fieldObservers = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val mObservers = fieldObservers.get(this)
            val classObservers = mObservers.javaClass

            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(mObservers, observer)
            val objectWrapper = (objectWrapperEntry as Map.Entry<*, *>).value
            val classObserverWrapper = objectWrapper?.javaClass!!.superclass

            val fieldLastVersion = classObserverWrapper!!.getDeclaredField("mLastVersion")
            fieldLastVersion.isAccessible = true
            val fieldVersion = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion.get(this)
            fieldLastVersion.set(objectWrapper, objectVersion)
        }
    }
}