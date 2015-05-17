package io.ringle.statesman

import android.content.Context
import android.os.Bundle
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

public val Context.statesman: StateManager
        get() = getSystemService(StatesmanContextWrapper.STATE_SERVICE) as StateManager
