package io.ringle.statesman

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal val defaultKeyProvider: (KProperty<*>) -> String = { it.name }
internal val defaultValueProvider: (Any?, Any?) -> Nothing = { thisRef, key ->
    throw NoSuchElementException("$key is missing from $thisRef")
}

abstract class BundleVal<T, V>() : ReadOnlyProperty<T, V> {

    abstract fun bundle(ref: T): Bundle

    abstract fun key(property: KProperty<*>): String

    open fun default(ref: T, property: KProperty<*>): V {
        throw NoSuchElementException("Key $property is missing in $ref")
    }

    @Suppress("unchecked_cast")
    override fun getValue(thisRef: T, property: KProperty<*>): V {
        val b = bundle(thisRef)
        val k = key(property)
        if (!b.containsKey(k)) {
            return default(thisRef, property)
        }
        return bundle(thisRef).get(k) as V
    }
}

abstract class BundleVar<T, V>() : BundleVal<T, V>(), ReadWriteProperty<T, V> {

    @Suppress("unchecked_cast")
    override fun setValue(thisRef: T, property: KProperty<*>, value: V) {
        val b = bundle(thisRef)
        val k = key(property)
        val v = value
        when (v) {
            is Boolean -> b.putBoolean(k, v)
            is Byte -> b.putByte(k, v)
            is Char -> b.putChar(k, v)
            is Short -> b.putShort(k, v)
            is Int -> b.putInt(k, v)
            is Long -> b.putLong(k, v)
            is Float -> b.putFloat(k, v)
            is Double -> b.putDouble(k, v)
            is String -> b.putString(k, v)
            is CharSequence -> b.putCharSequence(k, v)
            is Parcelable -> b.putParcelable(k, v)
            is Serializable -> b.putSerializable(k, v)
            is BooleanArray -> b.putBooleanArray(k, v)
            is ByteArray -> b.putByteArray(k, v)
            is CharArray -> b.putCharArray(k, v)
            is DoubleArray -> b.putDoubleArray(k, v)
            is FloatArray -> b.putFloatArray(k, v)
            is IntArray -> b.putIntArray(k, v)
            is LongArray -> b.putLongArray(k, v)
            is ShortArray -> b.putShortArray(k, v)
            is Bundle -> b.putBundle(k, v)
            is Array<*> -> {
                if (v.size > 0) {
                    when (v[0]) {
                        is Parcelable -> b.putParcelableArray(k, v as Array<Parcelable>)
                        is CharSequence -> b.putCharSequenceArray(k, v as Array<CharSequence>)
                        is String -> b.putStringArray(k, v as Array<String>)
                    }
                }
            }
            else -> throw IllegalArgumentException("Unsupported bundle component")
        }
    }
}

open class FixedBundleVal<T, V>(private val bundle: Bundle,
                                private val key: (KProperty<*>) -> String = defaultKeyProvider,
                                private val default: (T, String) -> V = defaultValueProvider) : BundleVal<T, V>() {

    override fun bundle(ref: T): Bundle {
        return bundle
    }

    override fun key(property: KProperty<*>): String {
        return (key)(property)
    }

    override fun default(ref: T, property: KProperty<*>): V {
        return (default)(ref, key(property))
    }
}

open class FixedBundleVar<T, V>(private val bundle: Bundle,
                                private val key: (KProperty<*>) -> String = defaultKeyProvider,
                                private val default: (T, String) -> V = defaultValueProvider) : BundleVar<T, V>() {

    override fun bundle(ref: T): Bundle {
        return bundle
    }

    override fun key(property: KProperty<*>): String {
        return (key)(property)
    }

    override fun default(ref: T, property: KProperty<*>): V {
        return (default)(ref, key(property))
    }
}
