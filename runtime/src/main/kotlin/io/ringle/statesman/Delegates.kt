package io.ringle.statesman

import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable
import kotlin.properties.KeyMissingException
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty

private val defaultKeyProvider: (PropertyMetadata) -> String = { it.name }
private val defaultValueProvider: (Any?, Any?) -> Nothing = { thisRef, key ->
    throw KeyMissingException("$key is missing from $thisRef")
}

public abstract class BundleVal<T, out V>() : ReadOnlyProperty<T, V> {

    abstract fun bundle(ref: T): Bundle

    abstract fun key(desc: PropertyMetadata): String

    open fun default(ref: T, desc: PropertyMetadata): V {
        throw KeyMissingException("Key $desc is missing in $ref")
    }

    [suppress("unchecked_cast")]
    public override fun get(thisRef: T, desc: PropertyMetadata): V {
        val b = bundle(thisRef)
        val k = key(desc)
        if (!b.containsKey(k)) {
            return default(thisRef, desc)
        }
        return bundle(thisRef).get(k) as V
    }
}

public abstract class BundleVar<T, V>() : BundleVal<T, V>(), ReadWriteProperty<T, V> {

    override fun set(thisRef: T, desc: PropertyMetadata, value: V) {
        val b = bundle(thisRef)
        val k = key(desc)
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
            is Array<Parcelable> -> b.putParcelableArray(k, v)
            is ShortArray -> b.putShortArray(k, v)
            is Array<CharSequence> -> b.putCharSequenceArray(k, v)
            is Array<String> -> b.putStringArray(k, v)
            is Bundle -> b.putBundle(k, v)
            else -> throw IllegalArgumentException("Unsupported bundle component (${v.javaClass})")
        }
    }
}

public open class FixedBundleVal<T, out V>(private val bundle: Bundle,
                                           private val key: (PropertyMetadata) -> String = defaultKeyProvider,
                                           private val default: (T, String) -> V = defaultValueProvider) : BundleVal<T, V>() {

    override fun bundle(ref: T): Bundle {
        return bundle
    }

    override fun key(desc: PropertyMetadata): String {
        return (key)(desc)
    }

    override fun default(ref: T, desc: PropertyMetadata): V {
        return (default)(ref, key(desc))
    }
}

public open class FixedBundleVar<T, V>(private val bundle: Bundle,
                                       private val key: (PropertyMetadata) -> String = defaultKeyProvider,
                                       private val default: (T, String) -> V = defaultValueProvider) : BundleVar<T, V>() {

    override fun bundle(ref: T): Bundle {
        return bundle
    }

    override fun key(desc: PropertyMetadata): String {
        return (key)(desc)
    }

    override fun default(ref: T, desc: PropertyMetadata): V {
        return (default)(ref, key(desc))
    }
}
