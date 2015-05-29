package io.ringle.statesman

import android.app.Activity
import android.os.Bundle
import android.util.SparseArray
import java.util.ArrayList
import kotlin.properties.Delegates

public open class StateManager private constructor() : LifecycleAdapter {

    override var target: Activity by Delegates.notNull()

    private val managedStates = SparseArray<Bundle>()

    companion object {

        public fun create(): StateManager = StateManager()
    }

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
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

    override fun onActivitySaveInstanceState(outState: Bundle) {
        val keyList = ArrayList<Int>(managedStates.size())
        for ((k, s) in managedStates) {
            s.remove(Statesman.sKeyNewState)
            outState.putBundle(Statesman.sKeyState(k), s)
            keyList.add(k)
        }
        outState.putIntegerArrayList(Statesman.sKeyKeyList, keyList)
    }
}

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
