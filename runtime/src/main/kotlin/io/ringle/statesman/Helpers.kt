package io.ringle.statesman

import android.os.Bundle
import android.util.SparseArray
import kotlin.properties.ReadWriteProperty

fun <T> bundle(bundle: Bundle,
               default: (thisRef: Any?, desc: String) -> T = defaultValueProvider): ReadWriteProperty<Any?, T> {
    return FixedBundleVar(bundle, defaultKeyProvider, default)
}

fun <T> Stateful.store(default: (thisRef: Any?, desc: String) -> T = defaultValueProvider): ReadWriteProperty<Any?, T> {
    return bundle(state, default)
}

fun <T> Stateful.store(default: T): ReadWriteProperty<Any?, T> {
    return bundle(state, { r, d -> default })
}

fun Bundle.isNewState(): Boolean = getBoolean(Statesman.sKeyNewState)

class SparseEntry<E>(val k: Int, val v: E) : Map.Entry<Int, E> {
    override val key: Int = k
    override val value: E = v
}

operator fun <E> SparseArray<E>.iterator(): MutableIterator<SparseEntry<E>> {
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
