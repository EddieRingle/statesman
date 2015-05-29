package io.ringle.statesman

import android.content.Context
import android.os.Bundle

public interface Stateful : Contextual {

    public val key: Int

    public val hasState: Boolean
        get() = !state.isNewState()

    public val state: Bundle
        get() = ctx.statesman.getState(key)
}
