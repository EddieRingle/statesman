package io.ringle.statesman

import android.os.Bundle
import android.support.annotation.CallSuper
import android.util.SparseArray
import java.util.ArrayList

public interface StateHost {

    companion object {

        @suppress("nothing_to_inline")
        inline fun newManagedStates(): SparseArray<Bundle> = SparseArray()
    }

    val managedStates: SparseArray<Bundle>

    public fun deleteState(key: Int) {
        managedStates.delete(key)
    }

    public fun getState(key: Int): Bundle {
        var state = managedStates.get(key, null)
        if (state == null) {
            state = Bundle()
            state.putBoolean(Statesman.sKeyNewState, true)
            managedStates.put(key, state)
        }
        return state
    }

    @CallSuper
    fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val keyList = savedInstanceState.getIntegerArrayList(Statesman.sKeyKeyList)
            if (keyList != null) {
                for (k in keyList) {
                    if (savedInstanceState.containsKey(Statesman.sKeyState(k))) {
                        managedStates.put(k, savedInstanceState.getBundle(Statesman.sKeyState(k)))
                    }
                }
            }
        }
    }

    @CallSuper
    fun saveState(outState: Bundle) {
        val keyList = ArrayList<Int>(managedStates.size())
        for ((k, s) in managedStates) {
            s.remove(Statesman.sKeyNewState)
            outState.putBundle(Statesman.sKeyState(k), s)
            keyList.add(k)
        }
        outState.putIntegerArrayList(Statesman.sKeyKeyList, keyList)
    }
}
