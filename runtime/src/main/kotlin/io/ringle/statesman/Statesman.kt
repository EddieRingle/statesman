package io.ringle.statesman

import android.app.Activity
import android.app.Fragment
import android.os.Bundle

class Statesman() : Fragment(), StateHost {

    companion object {

        @JvmStatic val sKeyPrefix = "__statesman_"

        @JvmStatic val sKeyFragment = "${sKeyPrefix}fragment"

        @JvmStatic val sKeyKeyList = "${sKeyPrefix}keyList"

        @JvmStatic val sKeyNewState = "${sKeyPrefix}newState"

        @JvmStatic val sKeyState = { key: Int -> "${sKeyPrefix}state[$key]" }

        fun of(activity: Activity): Statesman {
            val fm = activity.fragmentManager
            var sm = fm.findFragmentByTag(sKeyFragment) as? Statesman
            if (sm == null) {
                sm = Statesman()
                fm.beginTransaction().add(sm, sKeyFragment).commit()
            }
            return sm
        }
    }

    override val managedStates = StateHost.newManagedStates()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveState(outState)
        super.onSaveInstanceState(outState)
    }
}
