package io.ringle.statesman

import android.os.Bundle
import android.util.SparseArray
import kotlin.properties.ReadWriteProperty

public fun bundle<T>(bundle: Bundle,
                     default: (thisRef: Any?, desc: String) -> T = defaultValueProvider): ReadWriteProperty<Any?, T> {
    return FixedBundleVar(bundle, defaultKeyProvider, default)
}

public fun Stateful.store<T>(default: (thisRef: Any?, desc: String) -> T = defaultValueProvider): ReadWriteProperty<Any?, T> {
    return bundle(state, default)
}

public fun Stateful.store<T>(default: T): ReadWriteProperty<Any?, T> {
    return bundle(state, { r, d -> default })
}

public fun Bundle.isNewState(): Boolean = getBoolean(Statesman.sKeyNewState)

private class SparseEntry<E>(val k: Int, val v: E) : Map.Entry<Int, E> {

    override fun getKey(): Int = k

    override fun getValue(): E = v
}

private fun SparseArray<E>.iterator<E>(): MutableIterator<SparseEntry<E>> {
    return object : MutableIterator<SparseEntry<E>> {
        var index = 0

        override fun hasNext(): Boolean {
            return index < size()
        }

        override fun next(): SparseEntry<E> {
            val i = index++
            return SparseEntry(keyAt(i), valueAt(i))
        }

        override fun remove() {
            index -= 1
            removeAt(index)
        }
    }
}
