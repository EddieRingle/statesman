package io.ringle.statesman

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

public open class StatesmanContextWrapper(base: Context) : ContextWrapper(base) {

    companion object {
        public val STATE_SERVICE: String = "io.ringle.statesman.STATE_SERVICE"
    }

    private val manager = StateManager.create()

    override fun getSystemService(name: String): Any? =
        if (!STATE_SERVICE.equals(name)) {
            super.getSystemService(name)
        } else {
            manager
        }
}
