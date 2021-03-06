package kotterknife

import android.app.Activity
import android.app.Dialog
import android.app.DialogFragment
import android.app.Fragment
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
//import android.support.v4.app.DialogFragment as SupportDialogFragment
//import android.support.v4.app.Fragment as SupportFragment
import androidx.fragment.app.Fragment as SupportFragment
import androidx.fragment.app.DialogFragment as SupportDialogFragment

public fun <V : View> View.bindView(id: Int)
        : ReadOnlyProperty<View, V> = required(id, viewFinder)
public fun <V : View> Activity.bindView(id: Int)
        : ReadOnlyProperty<Activity, V> = required(id, viewFinder)
public fun <V : View> Dialog.bindView(id: Int)
        : ReadOnlyProperty<Dialog, V> = required(id, viewFinder)
public fun <V : View> DialogFragment.bindView(id: Int)
        : ReadOnlyProperty<DialogFragment, V> = required(id, viewFinder)
public fun <V : View> SupportDialogFragment.bindView(id: Int)
        : ReadOnlyProperty<SupportDialogFragment, V> = required(id, viewFinder)
public fun <V : View> Fragment.bindView(id: Int)
        : ReadOnlyProperty<Fragment, V> = required(id, viewFinder)
public fun <V : View> SupportFragment.bindView(id: Int)
        : ReadOnlyProperty<SupportFragment, V> = required(id, viewFinder)
public fun <V : View> RecyclerView.ViewHolder.bindView(id: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, V> = required(id, viewFinder)

public fun <V : View> View.bindOptionalView(id: Int)
        : ReadOnlyProperty<View, V?> = optional(id, viewFinder)
public fun <V : View> Activity.bindOptionalView(id: Int)
        : ReadOnlyProperty<Activity, V?> = optional(id, viewFinder)
public fun <V : View> Dialog.bindOptionalView(id: Int)
        : ReadOnlyProperty<Dialog, V?> = optional(id, viewFinder)
public fun <V : View> DialogFragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<DialogFragment, V?> = optional(id, viewFinder)
public fun <V : View> SupportDialogFragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<SupportDialogFragment, V?> = optional(id, viewFinder)
public fun <V : View> Fragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<Fragment, V?> = optional(id, viewFinder)
public fun <V : View> SupportFragment.bindOptionalView(id: Int)
        : ReadOnlyProperty<SupportFragment, V?> = optional(id, viewFinder)
public fun <V : View> RecyclerView.ViewHolder.bindOptionalView(id: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, V?> = optional(id, viewFinder)

public fun <V : View> View.bindViews(vararg ids: Int)
        : ReadOnlyProperty<View, List<V>> = required(ids, viewFinder)
public fun <V : View> Activity.bindViews(vararg ids: Int)
        : ReadOnlyProperty<Activity, List<V>> = required(ids, viewFinder)
public fun <V : View> Dialog.bindViews(vararg ids: Int)
        : ReadOnlyProperty<Dialog, List<V>> = required(ids, viewFinder)
public fun <V : View> DialogFragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<DialogFragment, List<V>> = required(ids, viewFinder)
public fun <V : View> SupportDialogFragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<SupportDialogFragment, List<V>> = required(ids, viewFinder)
public fun <V : View> Fragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<Fragment, List<V>> = required(ids, viewFinder)
public fun <V : View> SupportFragment.bindViews(vararg ids: Int)
        : ReadOnlyProperty<SupportFragment, List<V>> = required(ids, viewFinder)
public fun <V : View> RecyclerView.ViewHolder.bindViews(vararg ids: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, List<V>> = required(ids, viewFinder)

public fun <V : View> View.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<View, List<V>> = optional(ids, viewFinder)
public fun <V : View> Activity.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<Activity, List<V>> = optional(ids, viewFinder)
public fun <V : View> Dialog.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<Dialog, List<V>> = optional(ids, viewFinder)
public fun <V : View> DialogFragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<DialogFragment, List<V>> = optional(ids, viewFinder)
public fun <V : View> SupportDialogFragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<SupportDialogFragment, List<V>> = optional(ids, viewFinder)
public fun <V : View> Fragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<Fragment, List<V>> = optional(ids, viewFinder)
public fun <V : View> SupportFragment.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<SupportFragment, List<V>> = optional(ids, viewFinder)
public fun <V : View> RecyclerView.ViewHolder.bindOptionalViews(vararg ids: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, List<V>> = optional(ids, viewFinder)

private val View.viewFinder: Finder<View>
    get() = { findViewById(it) }
private val Activity.viewFinder: Finder<Activity>
    get() = { findViewById(it) }
private val Dialog.viewFinder: Finder<Dialog>
    get() = { findViewById(it) }
private val DialogFragment.viewFinder: Finder<DialogFragment>
    get() = { dialog?.findViewById(it) ?: view?.findViewById(it) }
private val SupportDialogFragment.viewFinder: Finder<SupportDialogFragment>
    get() = { dialog?.findViewById(it) ?: view?.findViewById(it) }
private val Fragment.viewFinder: Finder<Fragment>
    get() = { view?.findViewById(it) }
private val SupportFragment.viewFinder: Finder<SupportFragment>
    get() = { view!!.findViewById(it) }
private val RecyclerView.ViewHolder.viewFinder: Finder<RecyclerView.ViewHolder>
    get() = { itemView.findViewById(it) }

private fun viewNotFound(id:Int, desc: KProperty<*>): Nothing =
    throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: Finder<T>)
        = Lazy { t: T, desc -> t.finder(id) as V? ?: viewNotFound(id, desc) }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(id: Int, finder: Finder<T>)
        = Lazy { t: T, desc ->  t.finder(id) as V? }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(ids: IntArray, finder: Finder<T>)
        = Lazy { t: T, desc -> ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) } }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(ids: IntArray, finder: Finder<T>)
        = Lazy { t: T, desc -> ids.map { t.finder(it) as V? }.filterNotNull() }

typealias Finder<T> = T.(Int) -> View?

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY
    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}