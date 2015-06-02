package io.ringle.statesman

import android.app.Activity
import android.app.Fragment
import android.os.Bundle
import kotlin.platform.platformStatic

public class Statesman() : Fragment(), StateHost {

    companion object {

        platformStatic val sKeyPrefix = "__statesman_"

        platformStatic val sKeyFragment = "${sKeyPrefix}fragment"

        platformStatic val sKeyKeyList = "${sKeyPrefix}keyList"

        platformStatic val sKeyNewState = "${sKeyPrefix}newState"

        platformStatic val sKeyState = { key: Int -> "${sKeyPrefix}state[${key}]" }

        public fun of(activity: Activity): Statesman {
            val fm = activity.getFragmentManager()
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
        super<Fragment>.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super<Fragment>.onActivityCreated(savedInstanceState)
        restoreState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        saveState(outState)
        super<Fragment>.onSaveInstanceState(outState)
    }
}
