package io.ringle.statesman

import android.os.Bundle
import android.util.SparseArray

public interface Stateful : StateHost {

    val key: Int

    val hasState: Boolean
        get() = !state.isNewState()

    val state: Bundle
        get() = stateHost.getState(key)

    var stateHost: StateHost

    override val managedStates: SparseArray<Bundle>
        get() = stateHost.managedStates

    override fun deleteState(key: Int) {
        stateHost.deleteState(key)
    }

    override fun getState(key: Int): Bundle = stateHost.getState(key)

    override fun restoreState(savedInstanceState: Bundle?) {
        stateHost.restoreState(savedInstanceState)
    }

    override fun saveState(outState: Bundle) {
        stateHost.saveState(outState)
    }
}
